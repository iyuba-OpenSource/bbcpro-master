package com.ai.bbcpro.model.home;



import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.home.ReadSubmitBean;
import com.ai.bbcpro.mvp.view.home.ParaContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ParaModel implements ParaContract.ParaModel {


    @Override
    public Disposable updateNewsStudyRecord(String format, int uid, String BeginTime, String EndTime, String appName,
                                            String Lesson, int LessonId, int appId, String Device, String DeviceId,
                                            int EndFlg, int wordcount, int categoryid, String platform, int rewardVersion, ParaContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .updateNewsStudyRecord(Constant.UPDATE_NEWS_STUDY_RECORD, format, uid,
                        BeginTime, EndTime, appName, Lesson, LessonId, appId, Device, DeviceId, EndFlg,
                        wordcount, categoryid, platform, rewardVersion)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReadSubmitBean>() {
                    @Override
                    public void accept(ReadSubmitBean readSubmitBean) throws Exception {

                        callback.success(readSubmitBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateScore20230814(int srid, int uid, int appid, int voaid, String sign, ParaContract.UpdateCallback updateCallback) {

        return null;
    }
}
