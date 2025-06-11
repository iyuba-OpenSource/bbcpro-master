package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.disposables.Disposable;

public interface DropdownTitleContract {

    interface DropdownTitleView extends LoadingView {

        void doCheckIPBBC(CheckBBCBean checkBBCBean);
    }

    interface DropdownTitlePresenter extends IBasePresenter<DropdownTitleView> {

        void doCheckIPBBC(String uid, String appid, String platform, String vip);
    }

    interface DropdownTitleModel extends BaseModel {

        Disposable doCheckIPBBC(String uid, String appid, String platform, String vip, CheckIPCallback callback);
    }

    interface CheckIPCallback {

        void success(CheckBBCBean checkBBCBean);

        void error(Exception e);
    }
}
