package com.ai.bbcpro.mvp.view;



import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface SplashContract {


    interface SplashView extends LoadingView {

        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }

    interface SplashPresenter extends IBasePresenter<SplashView> {

        void getAdEntryAll(String appId, int flag, String uid);
    }

    interface SplashModel extends BaseModel {

        Disposable getAdEntryAll(String appId, int flag, String uid, Callback callback);
    }

    interface Callback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }
}
