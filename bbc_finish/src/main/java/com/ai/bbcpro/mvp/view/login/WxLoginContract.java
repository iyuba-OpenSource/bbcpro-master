package com.ai.bbcpro.mvp.view.login;


import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.bean.WxLoginBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface WxLoginContract {

    interface WxLoginView extends LoadingView {


        void getWxAppletToken(WxLoginBean wxLoginBean);

        void getUidByToken(WxLoginBean wxLoginBean);

        void getUserInfo(MoreInfoBean moreInfoBean, String uid);
    }


    interface WxLoginPresenter extends IBasePresenter<WxLoginView> {

        void getWxAppletToken(String platform, String format, String protocol, String appid, String sign);

        void getUidByToken(String platform, String format, String protocol, String token, String sign, String appid);

        void getUserInfo(String platform, String format, String appid,
                         String protocol, String id, String myid, String sign);
    }

    interface WxLoginModel extends BaseModel {

        Disposable getWxAppletToken(String url, String platform, String format, String protocol, String appid, String sign, Callback callback);

        Disposable getUidByToken(String url, String platform, String format, String protocol, String token, String sign, String appid, Callback callback);


        Disposable getUserInfo(String url, String platform, String format, String appid,
                               String protocol, String id, String myid, String sign, UserCallback callback);
    }

    interface UserCallback {

        void success(MoreInfoBean moreInfoBean);

        void error(Exception e);
    }

    interface Callback {

        void success(WxLoginBean wxLoginBean);

        void error(Exception e);
    }
}
