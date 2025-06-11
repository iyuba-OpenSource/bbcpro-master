package com.ai.bbcpro.mvp.presenter.login;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.model.login.RegisterByEmailModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.login.RegisterByEmailContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class RegisterByEmailPresenter extends BasePresenter<RegisterByEmailContract.RegisterByEmailView, RegisterByEmailContract.RegisterByEmailModel>
        implements RegisterByEmailContract.RegisterByEmailPresenter {


    @Override
    protected RegisterByEmailContract.RegisterByEmailModel initModel() {
        return new RegisterByEmailModel();
    }

    @Override
    public void register(int protocol, String mobile, String username, String password, String platform, int appid, String app, String format, String sign) {

        Disposable disposable = model.register(Constant.URL_GET_WX_APPLET_TOKEN, protocol, mobile,
                username, password, platform, appid, app, format, sign, new RegisterByEmailContract.Callback() {
                    @Override
                    public void success(UserBean userBean) {

                        view.hideLoading();
                        if (userBean.getResult().equals("111")) {

                            view.registerComplete(userBean);
                        } else if (userBean.getResult().equals("115")) {

                            view.toast("此号码已注册");
                        } else {

                            view.toast(userBean.getMessage());
                        }
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
