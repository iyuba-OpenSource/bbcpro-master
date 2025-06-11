package com.ai.bbcpro.model.home;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.mvp.view.home.OriContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class OriModel implements OriContract.OriModel {
    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, OriContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getAdEntryAll(Constant.URL_GET_AD_ENTRY_ALL, appId, flag, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AdEntryBean>>() {
                    @Override
                    public void accept(List<AdEntryBean> adEntryBeans) throws Exception {

                        callback.success(adEntryBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getBroadcastOptions(String uid, OriContract.FamousCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getBroadcastOptions(Constant.URL_GET_BROADCAST_OPTIONS, uid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FamousPersonBean>() {
                    @Override
                    public void accept(FamousPersonBean famousPersonBean) throws Exception {

                        callback.success(famousPersonBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable announcer(String speaker, String prompt, String newsid, String newstype, int paraid, int idindex, OriContract.AnnouncerCallback announcerCallback) {

        return NetWorkManager
                .getRequestForApi()
                .announcer(Constant.URL_ANNOUNCER, speaker, prompt, newsid, newstype, paraid, idindex)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AnnouncerBean>() {
                    @Override
                    public void accept(AnnouncerBean announcerBean) throws Exception {

                        announcerCallback.success(announcerBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        announcerCallback.error((Exception) throwable);
                    }
                });
    }
}
