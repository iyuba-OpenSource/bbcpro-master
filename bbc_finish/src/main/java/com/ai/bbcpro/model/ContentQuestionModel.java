package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.mvp.view.ContentQuestionContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ContentQuestionModel implements ContentQuestionContract.ContentQuestionModel {


    @Override
    public Disposable updateTestRecordNew(String format, String appName, String sign, String uid,
                                          String appId, int TestMode, String DeviceId, String jsonStr, ContentQuestionContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .updateTestRecordNew(Constant.URL_UPDATE_TEST_RECORD_NEW, format, appName, sign, uid, appId, TestMode, DeviceId, jsonStr)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateTestRecordBean>() {
                    @Override
                    public void accept(UpdateTestRecordBean updateTestRecordBean) throws Exception {

                        callback.success(updateTestRecordBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
