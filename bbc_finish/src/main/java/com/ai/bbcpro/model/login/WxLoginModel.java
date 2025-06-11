package com.ai.bbcpro.model.login;


import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.bean.WxLoginBean;
import com.ai.bbcpro.mvp.view.login.WxLoginContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WxLoginModel implements WxLoginContract.WxLoginModel {

    @Override
    public Disposable getWxAppletToken(String url, String platform, String format, String protocol, String appid, String sign,
                                       WxLoginContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getWxAppletToken(url, platform, format, protocol, appid, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WxLoginBean>() {
                    @Override
                    public void accept(WxLoginBean wxLoginBean) throws Exception {

                        callback.success(wxLoginBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getUidByToken(String url, String platform, String format, String protocol, String token, String sign, String appid, WxLoginContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getUidByToken(url, platform, format, protocol, token, sign, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WxLoginBean>() {
                    @Override
                    public void accept(WxLoginBean wxLoginBean) throws Exception {

                        callback.success(wxLoginBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }


    @Override
    public Disposable getUserInfo(String url, String platform, String format, String appid, String protocol, String id,
                                  String myid, String sign, WxLoginContract.UserCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getMoreInfo(url, platform, Integer.parseInt(protocol), Integer.parseInt(id), Integer.parseInt(myid)
                        , Integer.parseInt(appid), sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean userInfoResponse) throws Exception {

                        callback.success(userInfoResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
