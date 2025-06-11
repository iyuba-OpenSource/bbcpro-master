package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.home.HomePageContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.sd.iyu.training_camp.model.bean.CourseTitleBean;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MainContract {

    interface MainView extends LoadingView {

        void textAllApi(BBCContentBean bbcContentBean, CourseTitleBean.DataDTO dataDTO);

        void doCheckIPBBC(CheckBBCBean checkBBCBean);
    }


    interface MainPresenter extends IBasePresenter<MainView> {

        void textAllApi(String format, int bbcid, CourseTitleBean.DataDTO dataDTO);

        void doCheckIPBBC(String uid, String appid, String platform, String vip);
    }

    interface MainModel extends BaseModel {

        Disposable textAllApi(String format, int bbcid, Callback callback);

        Disposable doCheckIPBBC(String uid, String appid, String platform, String vip, CheckIPCallback callback);
    }

    interface CheckIPCallback {

        void success(CheckBBCBean checkBBCBean);

        void error(Exception e);
    }

    interface Callback {

        void success(BBCContentBean bbcContentBean);

        void error(Exception e);
    }

}
