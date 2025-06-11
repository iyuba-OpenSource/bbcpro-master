package com.ai.bbcpro.mvp.presenter;


import com.ai.bbcpro.model.ContentQuestionModel;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.mvp.view.ContentQuestionContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class ContentQuestionPresenter extends BasePresenter<ContentQuestionContract.ContentQuestionView
        , ContentQuestionContract.ContentQuestionModel> implements ContentQuestionContract.ContentQuestionPresenter {


    @Override
    protected ContentQuestionContract.ContentQuestionModel initModel() {
        return new ContentQuestionModel();
    }

    @Override
    public void updateTestRecordNew(String format, String appName, String sign, String uid,
                                    String appId, int TestMode, String DeviceId, String jsonStr) {

        Disposable disposable = model.updateTestRecordNew(format, appName, sign, uid, appId, TestMode, DeviceId, jsonStr, new ContentQuestionContract.Callback() {
            @Override
            public void success(UpdateTestRecordBean updateTestRecordBean) {

                if(updateTestRecordBean.getResult().equals("1")){

                    view.updateTestRecordComplete(updateTestRecordBean);
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
                if (e instanceof RuntimeException) {

                    view.toast("请求超时");
                }

            }
        });
        addSubscribe(disposable);
    }
}
