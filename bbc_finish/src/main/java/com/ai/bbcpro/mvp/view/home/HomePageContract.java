package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface HomePageContract {


    interface HomePageView extends LoadingView {


        void doCheckIPBBC(CheckBBCBean checkBBCBean);
    }

    interface HomePagePresenter extends IBasePresenter<HomePageView> {

        void doCheckIPBBC(String uid, String appid, String platform, String vip);
    }

    interface HomePageModel extends BaseModel {

        Disposable doCheckIPBBC(String uid, String appid, String platform, String vip, Callback callback);
    }

    interface Callback {

        void success(CheckBBCBean checkBBCBean);

        void error(Exception e);
    }
}
