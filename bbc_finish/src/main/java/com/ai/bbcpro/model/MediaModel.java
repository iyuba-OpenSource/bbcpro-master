package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.mvp.view.MediaContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MediaModel implements MediaContract.MediaModel {

    @Override
    public Disposable updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                           String Lesson, String TestMode, String TestWords, String platform,
                                           String appName, String DeviceId, String LessonId, String sign, int EndFlg,
                                           int TestNumber, MediaContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .updateStudyRecordNew(Constant.URL_UPDATE_STUDY_RECORD_NEW, format, uid, BeginTime, EndTime, Lesson, TestMode, TestWords, platform,
                        appName, DeviceId, LessonId, sign, EndFlg, TestNumber, 1)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        callback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable textAllApi(String format, int bbcid, MediaContract.NewsContentCallback newsContentCallback) {

        return NetWorkManager
                .getRequestForApi()
                .textAllApi(Constant.URL_TEXT_ALL_API, format, bbcid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BBCContentBean>() {
                    @Override
                    public void accept(BBCContentBean bbcContentBean) throws Exception {

                        newsContentCallback.success(bbcContentBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        newsContentCallback.error((Exception) throwable);
                    }
                });
    }
}
