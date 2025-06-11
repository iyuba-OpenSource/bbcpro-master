package com.ai.bbcpro.ui.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.entity.OpenTextEventbus;
import com.ai.bbcpro.ui.adapter.DownloadListAdapter;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.bean.SumBean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 新闻下载页面
 */
public class DownloadListActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Context mContext;
    private HeadlinesDataManager dataManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setTitle("下载");
        setContentView(R.layout.activity_download_list);
        mRecyclerView = findViewById(R.id.download_recycler);
        dataManager = HeadlinesDataManager.getInstance(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        List<SumBean.DataDTO> list = dataManager.loadDownloadNews(SyncDataHelper.getInstance(mContext).loadDownload());
        DownloadListAdapter adapter = new DownloadListAdapter(list, mContext);
        mRecyclerView.setAdapter(adapter);
        adapter.setCallback(new DownloadListAdapter.Callback() {
            @Override
            public void jumpAudioContent(int position) {

//                AudioContentActivity.startActivity(DownloadListActivity.this, list, position, 0);

                SumBean.DataDTO dataDTO = adapter.getDataList().get(position);
                EventBus.getDefault().post(new OpenTextEventbus(dataDTO.getBbcId()));
            }
        });

    }
}
