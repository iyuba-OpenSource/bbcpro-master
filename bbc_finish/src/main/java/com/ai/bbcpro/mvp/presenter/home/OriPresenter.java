package com.ai.bbcpro.mvp.presenter.home;

import android.util.Log;

import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.model.home.OriModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.home.OriContract;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class OriPresenter extends BasePresenter<OriContract.OriView, OriContract.OriModel>
        implements OriContract.OriPresenter {


    @Override
    protected OriContract.OriModel initModel() {
        return new OriModel();
    }

    @Override
    public void getAdEntryAll(String appId, int flag, String uid) {

        Disposable disposable = model.getAdEntryAll(appId, flag, uid, new OriContract.Callback() {
            @Override
            public void success(List<AdEntryBean> adEntryBeans) {

                if (adEntryBeans.size() != 0) {

                    AdEntryBean adEntryBean = adEntryBeans.get(0);
                    if (!adEntryBean.getResult().equals("-1")) {

                        view.getAdEntryAll(adEntryBeans);
                    }
                }
            }

            @Override
            public void error(Exception e) {

            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void announcer(String speaker, String prompt, String newsid, String newstype, int paraid, int idindex) {

        Disposable disposable = model.announcer(speaker, prompt, newsid, newstype, paraid, idindex, new OriContract.AnnouncerCallback() {
            @Override
            public void success(AnnouncerBean announcerBean) {

                announcerBean.setSpeaker(speaker);
                view.announcer(announcerBean);
                Log.d("announcer", announcerBean.toString());
            }

            @Override
            public void error(Exception e) {

                view.announcerFail(e);
                Log.d("announcer", e.toString());
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void getBroadcastOptions(String uid) {

        Disposable disposable = model.getBroadcastOptions(uid, new OriContract.FamousCallback() {
            @Override
            public void success(FamousPersonBean famousPersonBean) {

                view.getBroadcastOptions(famousPersonBean);
            }

            @Override
            public void error(Exception e) {

                Log.d("BroadcastOptions", e.toString());
            }
        });
        addSubscribe(disposable);
    }
}
