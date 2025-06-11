package com.ai.bbcpro.mvp.presenter.me;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.me.VipCenterModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.me.VipCenterContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class VipCenterPresenter extends BasePresenter<VipCenterContract.VipCenterView, VipCenterContract.VipCenterModel>
        implements VipCenterContract.VipCenterPresenter {

    @Override
    protected VipCenterContract.VipCenterModel initModel() {
        return new VipCenterModel();
    }

    @Override
    public void getUserInfo(String platform, String format, String appid, String protocol, String id, String myid, String sign) {

        Disposable disposable = model.getUserInfo(Constant.URL_GET_WX_APPLET_TOKEN, platform, format, appid, protocol, id, myid, sign, new VipCenterContract.UserCallback() {

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
