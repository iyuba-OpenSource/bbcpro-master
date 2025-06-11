package com.ai.bbcpro.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.adapter.CollectionAdapter;
import com.ai.bbcpro.ui.bean.CollectionBean;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollectionActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private String TAG = "CollectionActivity";
    private Context mContext;
    String uid;
    private HeadlinesDataManager dataManager;
    private CollectionBean bean;
    private SyncDataHelper syncDataHelper;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what){
                case 0:
                    List<CollectionBean.DataBean> list =  ((CollectionBean)message.obj).getData();
                    CollectionAdapter adapter = new CollectionAdapter(mContext, list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerView.setAdapter(adapter);
                    return true;
            }
            return false;
        }
    });


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        mContext = this;
        setTitle("收藏");
        uid = ConfigManager.Instance().loadString("userId");
        dataManager = HeadlinesDataManager.getInstance(mContext);
        syncDataHelper = SyncDataHelper.getInstance(mContext);
        initView();
        reloadData();
    }

    private void reloadData() {
        java.util.Date date = new java.util.Date();
        long unixTimestamp = date.getTime()/1000+3600*8; //东八区;
        long days = unixTimestamp/86400;
        String signStr = "iyuba + " + uid + "bbc" + "279" + days;
        String sign = MD5.getMD5ofStr(signStr);
        String url = "http://cms."+ CommonConstant.domain +"/dataapi/jsp/getCollect.jsp?" +
                "userId=" + uid +
                "&topic=bbc" +
                "&appid="+ Constant.APPID+
                "&sentenceFlg=0" +
                "&format=json" +
                "&sign=" + sign;
        System.out.println("---------------" + url);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                System.out.println("=================" + e.toString());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    bean = new Gson().fromJson(response.body().string(), CollectionBean.class);
                    List<String> list = new ArrayList<>();
                    for (int i = 0; i < bean.getData().size(); i++) {
                        list.add(bean.getData().get(i).getVoaid());
                    }
                    syncDataHelper.saveCollection(list);
//                    dataManager.saveCollection(bean.getData());
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = bean;
                    mHandler.sendMessage(message);
                    if (refreshLayout.isRefreshing()){
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });
    }

    private void initView() {
        refreshLayout = findViewById(R.id.collection_swipe);
        recyclerView = findViewById(R.id.collection_recycle);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reloadData();
            }
        });
    }


}
