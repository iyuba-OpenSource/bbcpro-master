package com.ai.bbcpro.model.login;

import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.mvp.view.login.RegisterByEmailContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegisterByEmailModel implements RegisterByEmailContract.RegisterByEmailModel {


    @Override
    public Disposable register(String url, int protocol, String mobile, String username, String password, String platform, int appid, String app, String format, String sign, RegisterByEmailContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .register(url, protocol, mobile, username, password, platform, appid, app, format, sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UserBean>() {
                    @Override
                    public void accept(UserBean userBean) throws Exception {

                        callback.success(userBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
