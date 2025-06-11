package com.ai.bbcpro.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.bean.CollectionBean;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.net.ContentService;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.ColorDialog;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionAdapter.CollectionViewHolder> {
    private Context mContext;
    private List<CollectionBean.DataBean> dataList = new ArrayList<>();
    private final HeadlinesDataManager dataManager;
    private Handler mHadnler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    notifyDataSetChanged();
                    break;
            }
            return false;
        }
    });

    public CollectionAdapter(Context mContext, List<CollectionBean.DataBean> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
        dataManager = HeadlinesDataManager.getInstance(mContext);
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_article_item, null);
        return new CollectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionViewHolder holder, int position) {
        CollectionBean.DataBean bean = dataList.get(position);
        String bbcid = bean.getVoaid();
        holder.tvTitle.setText(bean.getTitleCn());
        holder.tvCate.setText(getCateName(Integer.parseInt(bean.getCategory())));
        holder.tvTime.setText(bean.getCreateTime().substring(0,10));
        Glide.with(mContext).load(bean.getPic()).into(holder.iv);
        holder.ly.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, AudioContentActivity.class);
            intent.putExtra("sound", bean.getSound());
            intent.putExtra("bbcid", bbcid);
            intent.putExtra("title_cn", bean.getTitle());
            intent.putExtra("image",bean.getPic());
            if (dataManager.loadDetail(Integer.parseInt(bbcid)).isEmpty()) {
                getDetailData(intent, bbcid);
            } else {
                mContext.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(view -> {
            showTip(position);
        });
        holder.tvTitle.setOnClickListener(view -> {
            List<String> list = SyncDataHelper.getInstance(mContext).loadCollection();
            String s = list.toString();

        });
    }

    private void showTip(int position) {
        ColorDialog colorDialog = new ColorDialog(mContext);
        colorDialog.setTitle("删除收藏");
        colorDialog.setContentText("是否删除该收藏?");
        colorDialog.setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        colorDialog.setPositiveListener("删除", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
               delCollection(position);
               dialog.dismiss();
            }
        });
        colorDialog.show();
    } ;

    private String getCateName(int cateNum) {
        String[] array = mContext.getResources().getStringArray(R.array.bbc_category);
        int[] numbers = mContext.getResources().getIntArray(R.array.bbc_category_number);
        Map<Integer,String> map = new HashMap<>();
            for (int i = 0; i < array.length; i++) {
                map.put(numbers[i],array[i]);
            }
        return map.get(cateNum);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout ly;
        ImageView iv,delete;
        TextView tvTitle;
        TextView tvTime;
        TextView tvCate;

        public CollectionViewHolder(@NonNull View view) {
            super(view);
            iv = view.findViewById(R.id.iv_collection);
            tvTitle = view.findViewById(R.id.tv_collection_title);
            tvTime = view.findViewById(R.id.tv_collection_time);
            tvCate = view.findViewById(R.id.tv_collection_cate);
            ly = view.findViewById(R.id.ly_collection);
            delete = view.findViewById(R.id.delete_collection);
        }
    }

    public Call<BBCContentBean> initDetailHttp(String bbcid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ContentService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ContentService contentService = retrofit.create(ContentService.class);
        return contentService.post("json", bbcid);
    }

    public void getDetailData(Intent intent, String bbcid) {
        Observable.create(new ObservableOnSubscribe<BBCContentBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<BBCContentBean> emitter) throws Exception {
                Response<BBCContentBean> execute = initDetailHttp(bbcid).execute();
                BBCContentBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BBCContentBean>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {


            }

            @Override
            public void onNext(@NonNull BBCContentBean dataBean) {
                dataManager.saveDetail(dataBean.getData());
                mContext.startActivity(intent);

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    private void delCollection(int position) {
        String uid = ConfigManager.Instance().loadString("userId");
        CollectionBean.DataBean data = dataList.get(position);
        String url = "http://apps."+ CommonConstant.domain +"/iyuba/updateCollect.jsp?" +
                "userId=" + uid +
                "&voaId=" + data.getVoaid() +
                "&groupName=Iyuba" +
                "&sentenceId=0" +
                "&sentenceFlg=0" +
                "&appid=" + Constant.APPID+
                "&type=del" +
                "&topic=bbc" +
                "&format=json";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        okhttp3.Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                dataList.remove(position);
                mHadnler.sendEmptyMessage(0);
            }
        });
    }


}
