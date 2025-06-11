package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface MoreEssayContract {

    interface MoreEssayView extends LoadingView {

        void searchApiNewComplete(SearchBean searchBean);

        //文章存储完成
        void saveNewContentComplete(int bbcid);

        void loadmore(SearchBean searchBean);
    }

    interface MoreEssayPresenter extends IBasePresenter<MoreEssayView> {

        void searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid);

        void textAllApi(String format, int bbcid);
    }

    interface MoreEssayModel extends BaseModel {

        Disposable searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid, SearchCallbak searchCallbak);

        Disposable textAllApi(String format, int bbcid, SearchContract.TextAllCallback textAllCallback);

    }


    interface SearchCallbak {

        void success(SearchBean searchBean);

        void error(Exception e);
    }
}
