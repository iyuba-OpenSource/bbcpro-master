package com.ai.bbcpro.ui.activity.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.home.SearchAdapter;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.ActivityMoreEssayBinding;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.MoreEssayPresenter;
import com.ai.bbcpro.mvp.view.home.MoreEssayContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索-更多文章
 */
public class MoreEssayActivity extends AppCompatActivity implements MoreEssayContract.MoreEssayView {

    private ActivityMoreEssayBinding activityMoreEssayBinding;

    private SearchAdapter searchAdapter;

    private MoreEssayPresenter moreEssayPresenter;

    private HeadlinesDataManager headlinesDataManager;

    private String key;

    private int page = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoreEssayBinding = ActivityMoreEssayBinding.inflate(getLayoutInflater());
        setContentView(activityMoreEssayBinding.getRoot());

        getBundle();
        activityMoreEssayBinding.toolbarTvTitle.setText("更多文章");
        activityMoreEssayBinding.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headlinesDataManager = HeadlinesDataManager.getInstance(this);

        activityMoreEssayBinding.meRv.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(R.layout.item_search, new ArrayList<>());
        activityMoreEssayBinding.meRv.setAdapter(searchAdapter);
        searchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                String userId = ConfigManager.Instance().loadString("userId");
                moreEssayPresenter.searchApiNew("json", key, page, 10, 0,
                        "bbc", 1, userId, Constant.APPID);
            }
        }, activityMoreEssayBinding.meRv);
        searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                if(view.getId() == R.id.search_ll_essay){
                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);

                    long count = headlinesDataManager.hasQuestionBean(dataDTO.getBbcId());
                    if (count > 0) {//含有数据

                        saveNewContentComplete(dataDTO.getBbcId());
                    } else {//没有数据

                        moreEssayPresenter.textAllApi("json", dataDTO.getBbcId());
                    }
                }
            }
        });
        moreEssayPresenter = new MoreEssayPresenter();
        moreEssayPresenter.attchView(this);

        String userId = ConfigManager.Instance().loadString("userId");
        moreEssayPresenter.searchApiNew("json", key, page, 10, 0,
                "bbc", 1, userId, Constant.APPID);
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            key = bundle.getString("KEY", "");
        }
    }

    public static void startActivity(Activity activity, String key) {

        Intent intent = new Intent(activity, MoreEssayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("KEY", key);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (moreEssayPresenter != null) {

            moreEssayPresenter.detachView();
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
    public void searchApiNewComplete(SearchBean searchBean) {

        List<SearchBean.TitleDataDTO> essayDataDTOList = searchBean.getTitleData();
        for (int i = 0; i < essayDataDTOList.size(); i++) {

            SearchBean.TitleDataDTO titleDataDTO = essayDataDTOList.get(i);
            titleDataDTO.setFlag(3);
        }

        searchAdapter.setNewData(essayDataDTOList);
    }

    @Override
    public void saveNewContentComplete(int bbcid) {

        List<SearchBean.TitleDataDTO> dataDTOList = searchAdapter.getData();
        SearchBean.TitleDataDTO dataDTO = null;//获取bbcid的对象
        for (int i = 0; i < dataDTOList.size(); i++) {

            SearchBean.TitleDataDTO titleDataDTO = dataDTOList.get(i);
            if (titleDataDTO.getFlag() == 3 && titleDataDTO.getBbcId() == bbcid) {
                dataDTO = titleDataDTO;
                break;
            }
        }
        //跳转
        if (dataDTO != null) {

            SumBean.DataDTO sDto = new SumBean.DataDTO();
            sDto.setSound(dataDTO.getSound());
            sDto.setBbcId(dataDTO.getBbcId() + "");
            sDto.setPic(dataDTO.getPic());
            sDto.setTitle(dataDTO.getTitle());
            AudioContentActivity.startActivity(MoreEssayActivity.this, sDto, 0,0);
        }
    }

    @Override
    public void loadmore(SearchBean searchBean) {

        if (searchBean == null) {

            searchAdapter.loadMoreFail();
        } else {

            List<SearchBean.TitleDataDTO> essayDataDTOList = searchBean.getTitleData();
            for (int i = 0; i < essayDataDTOList.size(); i++) {

                SearchBean.TitleDataDTO titleDataDTO = essayDataDTOList.get(i);
                titleDataDTO.setFlag(3);
            }

            if (searchBean.getTitleData().size() == 10) {

                searchAdapter.addData(searchBean.getTitleData());
                searchAdapter.loadMoreComplete();
            } else {

                searchAdapter.addData(searchBean.getTitleData());
                searchAdapter.loadMoreEnd();
            }
        }
    }
}