package com.ai.bbcpro.newSection;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.util.MD5;
import com.ai.bbcpro.widget.GetUserWorks;
import com.ai.bbcpro.widget.HttpRequestFactory;
import com.ai.bbcpro.widget.SpeakRankWork;
import com.jaeger.library.StatusBarUtil;
import com.sina.weibo.sdk.share.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.cet4.activity
 * @class describe
 * @time 2019/1/23 13:51
 * @change
 * @chang time
 * @class describe
 */
public class DubbingPlayListActivity extends BaseActivity {


    RecyclerView mRecycler;
    SwipeRefreshLayout swipeRefreshLayout;
    ReadItemAdapter mAdapter;

    ImageView buttonTitleBack;

    int userId;
    String userName;
    int voaId;
    String other;
    String headUrl;
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);


    public static Bundle buildArguments(int userid, String username, int voaid, String other, String topicId, String type, String url) {
        Bundle bundle = new Bundle();
        bundle.putInt("userId", userid);
        bundle.putString("userName", username);
        bundle.putInt("voaId", voaid);
        bundle.putString("userid", other);
        bundle.putString("userHeader", url);
//        bundle.putString("topicId", topicId);
//        bundle.putString("type", type);

        return bundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dubbing_list_activity);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);

        initView();
        getIntents();
        getData();
    }

    private void initView() {

        mRecycler = findViewById(R.id.recycler);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        buttonTitleBack = findViewById(R.id.buttonTitleBack);

        buttonTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onViewClicked();
            }
        });
    }

    private void getIntents() {
        userId = getIntent().getBundleExtra("ARGUMENTS").getInt("userId");
        userName = getIntent().getBundleExtra("ARGUMENTS").getString("userName");
        voaId = getIntent().getBundleExtra("ARGUMENTS").getInt("voaId");
        other = getIntent().getBundleExtra("ARGUMENTS").getString("other");
        headUrl = getIntent().getBundleExtra("ARGUMENTS").getString("userHeader");
    }

    private void getData() {

        String sign = buildWorkSign(userId);
        Map<String, String> extra = new HashMap<>();
        if (voaId != 0) {
            extra.put("topicId", String.valueOf(voaId));
        }
        Call<GetUserWorks> call = HttpRequestFactory.getPublishApi().getUserWorks(userId, Constant.mListen, "2,4", sign, extra);
        call.enqueue(new Callback<GetUserWorks>() {
            @Override
            public void onResponse(Call<GetUserWorks> call, Response<GetUserWorks> response) {
                if (response.body().data != null) {
                    setAdapter(response.body().data);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<GetUserWorks> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(onRefreshListener);

    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getData();
        }
    };

    public void setAdapter(List<SpeakRankWork> list) {
        mAdapter = new ReadItemAdapter(headUrl, userName);
        mAdapter.setData(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mRecycler.setAdapter(mAdapter);
    }

    public void onViewClicked() {
        finish();
    }

    private String buildWorkSign(int uid) {
        StringBuilder sb = new StringBuilder();
        sb.append(uid).append("getWorksByUserId").append(SDF.format(new Date()));
        return MD5.getMD5ofStr(sb.toString());
    }

    @Override
    protected void onDestroy() {
        if (mAdapter != null) {
            mAdapter.releasePlayer();
        }
        super.onDestroy();
    }
}

