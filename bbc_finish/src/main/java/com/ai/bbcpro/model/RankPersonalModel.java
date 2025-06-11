package com.ai.bbcpro.model;

import com.ai.bbcpro.model.bean.StudyRankingBean;
import com.ai.bbcpro.model.bean.ZanBean;
import com.ai.bbcpro.mvp.view.RankPersonalContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankPersonalModel implements RankPersonalContract.RankPersonalModel {


    @Override
    public Disposable zan(String url, String id, String protocol, RankPersonalContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .zan(url, id, protocol)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ZanBean>() {
                    @Override
                    public void accept(ZanBean zanBean) throws Exception {

                        callback.success(zanBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
