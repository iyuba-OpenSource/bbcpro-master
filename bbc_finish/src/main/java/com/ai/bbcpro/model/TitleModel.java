package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.mvp.view.TitleContract;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class TitleModel implements TitleContract.TitleModel {


    @Override
    public Disposable getAdEntryAll(String appId, int flag, String uid, TitleContract.AdEntryCallback adEntryCallback) {

        return NetWorkManager
                .getRequestForApi()
                .getAdEntryAll(Constant.URL_GET_AD_ENTRY_ALL, appId, flag, uid)
                .subscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<AdEntryBean>>() {
                    @Override
                    public void accept(List<AdEntryBean> adEntryBeans) throws Exception {

                        adEntryCallback.success(adEntryBeans);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        adEntryCallback.error((Exception) throwable);
                    }
                });
    }
}
