package com.ai.bbcpro.mvp.presenter.home;

import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.model.home.DropdownTitleModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.DropdownTitleContract;

import io.reactivex.disposables.Disposable;

public class DropdownTitlePresenter extends BasePresenter<DropdownTitleContract.DropdownTitleView, DropdownTitleContract.DropdownTitleModel>
        implements DropdownTitleContract.DropdownTitlePresenter {
    @Override
    protected DropdownTitleContract.DropdownTitleModel initModel() {
        return new DropdownTitleModel();
    }

    @Override
    public void doCheckIPBBC(String uid, String appid, String platform, String vip) {

        Disposable disposable = model.doCheckIPBBC(uid, appid, platform, vip, new DropdownTitleContract.CheckIPCallback() {
            @Override
            public void success(CheckBBCBean checkBBCBean) {

                view.doCheckIPBBC(checkBBCBean);
            }

            @Override
            public void error(Exception e) {

                view.doCheckIPBBC(null);
            }
        });
        addSubscribe(disposable);
    }
}
