package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.ClockInLogBean;
import com.ai.bbcpro.mvp.view.CalendarContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class CalendarModel implements CalendarContract.CalendarModel {


    @Override
    public Disposable getShareInfoShow(String uid, int appId, String time, CalendarContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getShareInfoShow(Constant.URL_GET_SHARE_INFO_SHOW, uid, appId, time)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ClockInLogBean>() {
                    @Override
                    public void accept(ClockInLogBean clockInLogBean) throws java.lang.Exception {

                        callback.success(clockInLogBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws java.lang.Exception {

                        callback.error((Exception) throwable);
                    }
                });

    }
}
