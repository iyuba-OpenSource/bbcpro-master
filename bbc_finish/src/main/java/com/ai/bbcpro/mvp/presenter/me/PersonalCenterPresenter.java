package com.ai.bbcpro.mvp.presenter.me;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.me.PersonalCenterModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.login.WxLoginContract;
import com.ai.bbcpro.mvp.view.me.PersonalCenterContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class PersonalCenterPresenter extends BasePresenter<PersonalCenterContract.PersonalCenterView, PersonalCenterContract.PersonalCenterModel>
        implements PersonalCenterContract.PersonalCenterPresenter {


    @Override
    protected PersonalCenterContract.PersonalCenterModel initModel() {
        return new PersonalCenterModel();
    }

    @Override
    public void getUserInfo(String platform, String format, String appid, String protocol, String id, String myid, String sign) {

        Disposable disposable = model.getUserInfo(Constant.URL_GET_WX_APPLET_TOKEN, platform, format, appid, protocol, id, myid, sign, new WxLoginContract.UserCallback() {

            @Override
            public void success(MoreInfoBean moreInfoBean) {

                view.getUserInfo(moreInfoBean, myid);
                view.hideLoading();
            }

            @Override
            public void error(Exception e) {

                view.hideLoading();
                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
