package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.view.MainContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainModel implements MainContract.MainModel {


    @Override
    public Disposable textAllApi(String format, int bbcid, MainContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .textAllApi(Constant.URL_TEXT_ALL_API, format, bbcid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BBCContentBean>() {
                    @Override
                    public void accept(BBCContentBean bbcContentBean) throws Exception {

                        callback.success(bbcContentBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable doCheckIPBBC(String uid, String appid, String platform, String vip, MainContract.CheckIPCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .doCheckIPBBC(Constant.URL_DO_CHECK_IPBBC, uid, appid, platform, vip)
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
