package com.ai.bbcpro.ui.player.rank;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.model.bean.ZanBean;
import com.ai.bbcpro.mvp.presenter.RankPersonalPresenter;
import com.ai.bbcpro.mvp.view.RankPersonalContract;
import com.ai.bbcpro.ui.adapter.RankDetailAdapter;
import com.ai.bbcpro.ui.adapter.RankPersonalItem;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 文章主页面 - 排行榜 - 排行榜详情页面
 */
public class RankPersonalActivity extends AppCompatActivity implements RankPersonalContract.RankPersonalView {

    private int bbcId;
    private int uid;
    private String name;
    private String head;
    private Context mContext;
    private String TAG = "RankPersonalActivity";
    private List<RankPersonalItem> itemList;

    private final int REQUEST_FINISH = 0;  // 数据请求完毕

    private RecyclerView recyclerView;
    private RankDetailAdapter adapter;
    private MediaPlayer player;
    private AccountManager accountManager;

    private RankPersonalPresenter rankPersonalPresenter;

    private SharedPreferences zanSp;


    private final RankDetailAdapter.OnItemClickListener mOnItemClickListener = (v, viewName, position) -> {
        RankPersonalItem data = itemList.get(position);
        if ("0".equals(viewName)) {
            if (data.isPlaying()) {
                data.setPlaying(false);
                player.stop();
            } else {
                String audioUrl = CommonConstant.HTTP_SPEECH_ALL + "/voa/" + data.getShuoShuo();
                System.out.println("--------->点击播放音频" + audioUrl);
                setAllImgStatic();
                data.setPlaying(true);
                if (player != null) {
                    player.release();
                    player = null;
                }
                player = new MediaPlayer();
                player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        data.setPlaying(false);
                        adapter.setDataList(itemList, position);
                    }
                });
                try {
                    player.setDataSource(audioUrl);
                    player.prepare();
                    player.start();
                    EventBus.getDefault().post(new MediaPause());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if ("1".equals(viewName)) {//点赞
            if (accountManager.checkUserLogin()) {

                int id = adapter.getDataList().get(position).getId();
                boolean zan = zanSp.getBoolean("zan_" + id, false);

                if (zan) {

                    toast("你已经赞过该条评测了");
                } else {

                    //记录点赞
                    rankPersonalPresenter.zan("http://voa.iyuba.cn/voa/UnicomApi", id + "", "61001");
                }
            } else {
                showLoginDialog();
            }
        }
        adapter.setDataList(itemList, position);
    };

    // 每一个item播放ImageView设置为静态图片
    void setAllImgStatic() {
        if (player != null)
            player.stop();
        for (RankPersonalItem item : itemList) {
            item.setPlaying(false);
        }
    }

    private final Handler handler = new Handler(message -> {
        int what = message.what;
        if (what == REQUEST_FINISH) {
            adapter = new RankDetailAdapter(mContext, itemList, name, head);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter.setOnItemClickListener(mOnItemClickListener);
            recyclerView.setAdapter(adapter);
            accountManager = AccountManager.Instance(this);
            return true;
        }
        return false;
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_detail);
        mContext = this;
        recyclerView = findViewById(R.id.rank_detail_list);


        zanSp = getSharedPreferences("ZAN", MODE_PRIVATE);

        rankPersonalPresenter = new RankPersonalPresenter();
        rankPersonalPresenter.attchView(this);

        Intent intent = getIntent();
        bbcId = intent.getIntExtra("bbcId", 0);
        uid = intent.getIntExtra("uid", 0);
        name = intent.getStringExtra("name");
        head = intent.getStringExtra("head");


        ImageView toolbar_iv_back = findViewById(R.id.toolbar_iv_back);
        toolbar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView toolbar_tv_title = findViewById(R.id.toolbar_tv_title);
        toolbar_tv_title.setText("\"" + name + "\"" + "的评测");

        requestDetail();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (rankPersonalPresenter != null) {
            rankPersonalPresenter.detachView();
        }
    }

    void requestDetail() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sign = MD5.getMD5ofStr(uid + "getWorksByUserId" + sdf.format(System.currentTimeMillis()));
        String url = "http://voa." + CommonConstant.domain + "/voa/getWorksByUserId.jsp?" +
                "uid=" + uid +
                "&topic=bbc" +
                "&shuoshuoType=2%2C4" +
                "&topicId=" + bbcId +
                "&sign=" + sign;
        Log.e(TAG, "requestDetail: url:" + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        Observable.create(new ObservableOnSubscribe<RankPersonalTotal>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<RankPersonalTotal> emitter) throws Exception {
                        ResponseBody body = call.execute().body();
                        RankPersonalTotal total = new Gson().fromJson(body.string(), RankPersonalTotal.class);
                        emitter.onNext(total);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RankPersonalTotal>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull RankPersonalTotal rankPersonalTotal) {
                        itemList = rankPersonalTotal.getData();

                        for (RankPersonalItem personalItem : itemList) {

                            boolean zan = zanSp.getBoolean("zan_" + personalItem.getId(), false);
                            if (zan) {
                                personalItem.setHadZan(true);
                            }
                        }

                        Log.e(TAG, "onNext: " + itemList.size());
                        Message msg = new Message();
                        msg.what = REQUEST_FINISH;
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
        }
    }

    public void showLoginDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_diy);
            window.setGravity(Gravity.CENTER);

            TextView tvContent = window.findViewById(R.id.dialog_content);
            tvContent.setText("您还未登录，是否跳转登录界面？");

            TextView tvCancel = window.findViewById(R.id.dialog_cancel);
            TextView tvAgree = window.findViewById(R.id.dialog_agree);
            tvAgree.setText("跳转登录");

            tvCancel.setOnClickListener(v -> {
                alertDialog.cancel();
            });

            tvAgree.setOnClickListener(v -> {
                // 跳转登录界面
                EventBus.getDefault().post(new LoginEventbus());;
                alertDialog.dismiss();
            });
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void zan(ZanBean zanBean, String id) {

        if (zanBean.getResultCode().equals("001")) {


            RankPersonalItem personalItem = null;

            List<RankPersonalItem> rankPersonalItems = adapter.getDataList();
            for (RankPersonalItem item : rankPersonalItems) {

                if (id.equals(item.getId() + "")) {
                    personalItem = item;
                    break;
                }
            }

            zanSp.edit().putBoolean("zan_" + id, true).apply();

            if (personalItem.isHadZan()) {
                ToastUtil.showToast(this, "你已经赞过该条评测了。");
            } else {
                personalItem.setHadZan(true);
                personalItem.setAgreeCount(personalItem.getAgreeCount() + 1);
                ToastUtil.showToast(this, "点赞成功！");
                adapter.notifyDataSetChanged();
            }

        }
    }
}