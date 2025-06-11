package com.ai.bbcpro.mvp.presenter;

import android.util.Log;

import com.ai.bbcpro.model.TitleModel;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.mvp.view.TitleContract;

import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class TitlePresenter extends BasePresenter<TitleContract.TitleView, TitleContract.TitleModel>
        implements TitleContract.TitlePresenter {

    @Override
    protected TitleContract.TitleModel initModel() {
        return new TitleModel();
    }


    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new TitleContract.AdEntryCallback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeanList) {

                if (adEntryBeanList.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeanList.get(0);
                    if (adEntryBean.getResult().equals("1") || adEntryBean.getResult().equals("-1")) {

                        view.getAdEntryAllComplete(adEntryBean);
                    }
                }

            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    Log.d("TitlePresenter", "请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }


}
