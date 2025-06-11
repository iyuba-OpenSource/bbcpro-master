package com.ai.bbcpro.model.home;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.view.DropdownTitleContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DropdownTitleModel implements DropdownTitleContract.DropdownTitleModel {
    @Override
    public Disposable doCheckIPBBC(String uid, String appid, String platform, String vip, DropdownTitleContract.CheckIPCallback callback) {


        return NetWorkManager
                .getRequestForApi()
                .doCheckIPBBC(Constant.URL_DO_CHECK_IPBBC,uid, appid, platform, vip)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CheckBBCBean>() {
                    @Override
                    public void accept(CheckBBCBean checkBBCBean) throws Exception {

                        callback.success(checkBBCBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
