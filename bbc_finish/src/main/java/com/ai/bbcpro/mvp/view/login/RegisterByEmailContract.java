package com.ai.bbcpro.mvp.view.login;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface RegisterByEmailContract {

    interface RegisterByEmailView extends LoadingView {

        void registerComplete(UserBean userinfoDTO);
    }

    interface RegisterByEmailPresenter extends IBasePresenter<RegisterByEmailView> {

        void register(int protocol, String mobile, String username,
                      String password, String platform, int appid,
                      String app, String format, String sign);
    }

    interface RegisterByEmailModel extends BaseModel {

        Disposable register(String url, int protocol, String mobile, String username,
                            String password, String platform, int appid,
                            String app, String format, String sign, Callback callback);
    }

    interface Callback {

        void success(UserBean userBean);

        void error(Exception e);
    }
}
