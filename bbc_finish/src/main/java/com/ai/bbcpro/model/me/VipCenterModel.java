package com.ai.bbcpro.model.me;

import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.mvp.view.me.VipCenterContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class VipCenterModel implements VipCenterContract.VipCenterModel {
    @Override
    public Disposable getUserInfo(String url, String platform, String format, String appid, String protocol, String id, String myid, String sign, VipCenterContract.UserCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getMoreInfo(url, platform, Integer.parseInt(protocol), Integer.parseInt(id), Integer.parseInt(myid)
                        , Integer.parseInt(appid), sign)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MoreInfoBean>() {
                    @Override
                    public void accept(MoreInfoBean userInfoResponse) throws Exception {

                        callback.success(userInfoResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
