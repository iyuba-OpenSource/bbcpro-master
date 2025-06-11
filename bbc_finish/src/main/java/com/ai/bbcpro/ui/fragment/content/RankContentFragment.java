package com.ai.bbcpro.ui.fragment.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.adapter.RankAdapter;
import com.ai.bbcpro.ui.event.RefreshRankEvent;
import com.ai.bbcpro.ui.player.rank.RankContentResponse;
import com.ai.bbcpro.ui.player.rank.RankPersonalActivity;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;

import org.apache.commons.logging.LogFactory;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 排行榜
 */
public class RankContentFragment extends Fragment {
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(RankContentFragment.class);
    private Context mContext;
    private int start = 0; // 从低几个开始请求，用于分页
    private String uid;
    private AccountManager accountManager;
    private int bbcId;
    private String TAG = "RankFragment";
    private RankContentResponse data;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private RankAdapter adapter;
    private TextView tvRank;
    private TextView tvName;
    private TextView tvCount;
    private TextView tvAvr;
    private TextView tvScore;
    private ImageView ivHead, noRanking;
    private View mySelfView, normalView;

    private static final int INIT_VIEW = 0;

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (msg.what == INIT_VIEW) {

                if (data.getData() == null || data.getData().isEmpty()) {

                    normalView.setVisibility(View.GONE);
                    noRanking.setVisibility(View.VISIBLE);
                } else {

                    normalView.setVisibility(View.VISIBLE);
                    noRanking.setVisibility(View.GONE);

                    recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                    recyclerView.setAdapter(adapter);
                    refreshLayout.setRefreshing(false);
                    tvRank.setText(String.valueOf(data.getMyranking()));
                    tvName.setText(data.getMyname());
                    tvCount.setText("句子数: " + data.getMycount());
                    tvScore.setText(data.getMyscores() + "分");
                    if (data.getMyid() == 0) {
                        mySelfView.setVisibility(View.GONE);
                    } else {
                        mySelfView.setVisibility(View.VISIBLE);
                    }
                    if (data.getMycount() == 0) {
                        tvAvr.setText("平均分: 0");
                    } else {
                        tvAvr.setText("平均分: " + (int) Math.ceil(data.getMyscores() / data.getMycount()));
                    }
//                    Glide.with(mContext).load(data.getMyimgSrc()).into(ivHead);
                    Bitmap bmp = (Bitmap) msg.obj;
                    ivHead.setImageBitmap(bmp);
                }
            }
            return false;
        }
    });

    public Bitmap getURLImage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            bbcId = Integer.parseInt(getArguments().getString("bbcid"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_content_rank, container, false);
        recyclerView = root.findViewById(R.id.rank_list);
        refreshLayout = root.findViewById(R.id.rank_swipe);
        tvRank = root.findViewById(R.id.rank_index_me);
        tvName = root.findViewById(R.id.rank_name_me);
        tvCount = root.findViewById(R.id.rank_count_me);
        tvAvr = root.findViewById(R.id.rank_avr_me);
        tvScore = root.findViewById(R.id.rank_score_me);
        ivHead = root.findViewById(R.id.rank_head_me);
        mySelfView = root.findViewById(R.id.rank_me_ly);
        normalView = root.findViewById(R.id.normal_view);
        noRanking = root.findViewById(R.id.no_ranking);
        EventBus.getDefault().register(RankContentFragment.this);
        mySelfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (data == null) {

                    return;
                }

                if (data.getMycount() == 0) {
                    ToastUtil.showToast(mContext, "您尚未上传评测结果");
                } else {
                    int userId = data.getMyid();
                    String name = data.getMyname();
                    String head = data.getMyimgSrc();
                    Intent intent = new Intent(mContext, RankPersonalActivity.class);
                    intent.putExtra("bbcId", bbcId);
                    intent.putExtra("uid", userId);
                    intent.putExtra("name", name);
                    intent.putExtra("head", head);
                    mContext.startActivity(intent);
                }
            }
        });
        refreshLayout.setOnRefreshListener(() -> getRankData());

//        getRankData();
        return root;

    }

    @Override
    public void onResume() {
        super.onResume();

        getRankData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRank(RefreshRankEvent rankEvent) {
        getRankData();
    }


    public void getRankData() {

        uid = ConfigManager.Instance().loadString("userId", "0");
        Log.e(TAG, "userid: " + uid);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sign = MD5.getMD5ofStr(uid + "bbc" + bbcId + start + "20" + sdf.format(System.currentTimeMillis()));
        String url = "http://daxue." + CommonConstant.domain + "/ecollege/getTopicRanking.jsp?" +
                "type=D" +
                "&uid=" + uid +
                "&topic=bbc" +
                "&topicid=" + bbcId +
                "&start=" + start +
                "&total=20" +
                "&sign=" + sign;

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Log.d("onFailure", "11111");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {


                if (response.isSuccessful()) {

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {

                        String content = responseBody.string();
                        data = new Gson().fromJson(content, RankContentResponse.class);
                        Log.e(TAG, "onResponse: " + data.toString());
                        adapter = new RankAdapter(mContext, data, bbcId);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                Bitmap bitmapImage = getURLImage(data.getMyimgSrc());
                                Message msg = Message.obtain();
                                msg.what = 0;
                                msg.obj = bitmapImage;
                                mHandler.sendMessage(msg);
                            }
                        }).start();
                    }
                }
            }
        });
    }


    /**
     * 切换数据
     */
    public void switchData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            bbcId = Integer.parseInt(getArguments().getString("bbcid"));
        }
        //获取数据
        getRankData();
    }
}
