package com.ai.bbcpro.mvp.presenter.home;

import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.model.home.HomePageModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.home.HomePageContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class HomePagePresenter extends BasePresenter<HomePageContract.HomePageView, HomePageContract.HomePageModel>
        implements HomePageContract.HomePagePresenter {


    @Override
    protected HomePageContract.HomePageModel initModel() {
        return new HomePageModel();
    }


    @Override
    public void doCheckIPBBC(String uid, String appid, String platform, String vip) {

        Disposable disposable = model.doCheckIPBBC(uid, appid, platform, vip, new HomePageContract.Callback() {
            @Override
            public void success(CheckBBCBean checkBBCBean) {

                view.doCheckIPBBC(checkBBCBean);
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }
}
