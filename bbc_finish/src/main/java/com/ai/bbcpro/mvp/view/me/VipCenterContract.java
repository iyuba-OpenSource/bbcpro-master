package com.ai.bbcpro.mvp.view.me;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.mvp.view.login.WxLoginContract;

import io.reactivex.disposables.Disposable;

public interface VipCenterContract {

    interface VipCenterView extends LoadingView {

        void getUserInfo(MoreInfoBean moreInfoBean, String uid);
    }

    interface VipCenterPresenter extends IBasePresenter<VipCenterView> {

        void getUserInfo(String platform, String format, String appid,
                         String protocol, String id, String myid, String sign);
    }


    interface VipCenterModel extends BaseModel {

        Disposable getUserInfo(String url, String platform, String format, String appid,
                               String protocol, String id, String myid, String sign, UserCallback callback);
    }

    interface UserCallback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }
}
