package com.ai.bbcpro.mvp.view.home;

import android.os.IInterface;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public interface MoreSentenceContract {

    interface MoreSentenceView extends LoadingView {

        void searchApiNewComplete(SearchBean searchBean);

        void loadmore(SearchBean searchBean);

        void testComplete(EvalBean evalBean);
    }

    interface MoreSentencePresenter extends IBasePresenter<MoreSentenceView> {

        void searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid);

        void test(RequestBody requestBody);
    }

    interface MoreSentenceModel extends BaseModel {

        Disposable test(RequestBody requestBody, SearchContract.EvalCallback evalCallback);

        Disposable searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid, SearchCallbak searchCallbak);
    }

    interface SearchCallbak {

        void success(SearchBean searchBean);

        void error(Exception e);
    }
}
