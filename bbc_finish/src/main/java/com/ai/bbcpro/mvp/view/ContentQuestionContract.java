package com.ai.bbcpro.mvp.view;


import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import io.reactivex.disposables.Disposable;

public interface ContentQuestionContract {

    interface ContentQuestionView extends LoadingView {

        void updateTestRecordComplete(UpdateTestRecordBean updateTestRecordBean);
    }

    interface ContentQuestionPresenter extends IBasePresenter<ContentQuestionView> {

        void updateTestRecordNew(String format, String appName, String sign,
                                 String uid, String appId, int TestMode,
                                 String DeviceId, String jsonStr);
    }

    interface ContentQuestionModel extends BaseModel {


        Disposable updateTestRecordNew(String format, String appName, String sign,
                                       String uid, String appId, int TestMode,
                                       String DeviceId, String jsonStr, Callback callback);
    }

    interface Callback {

        void success(UpdateTestRecordBean updateTestRecordBean);

        void error(Exception e);
    }

}
