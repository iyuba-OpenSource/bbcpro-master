package com.ai.bbcpro.mvp.view.me;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.mvp.view.login.WxLoginContract;

import io.reactivex.disposables.Disposable;

public interface PersonalCenterContract {

    interface PersonalCenterView extends LoadingView {

        void getUserInfo(MoreInfoBean moreInfoBean, String uid);
    }

    interface PersonalCenterPresenter extends IBasePresenter<PersonalCenterView> {

        void getUserInfo(String platform, String format, String appid,
                         String protocol, String id, String myid, String sign);
    }

    interface PersonalCenterModel extends BaseModel {

        Disposable getUserInfo(String url, String platform, String format, String appid,
                               String protocol, String id, String myid, String sign, WxLoginContract.UserCallback callback);
    }
}
