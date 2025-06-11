package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface OriContract {

    interface OriView extends LoadingView {


        void getAdEntryAll(List<AdEntryBean> adEntryBeans);

        void getBroadcastOptions(FamousPersonBean famousPersonBean);

        void announcer(AnnouncerBean announcerBean);

        void announcerFail(Exception e);
    }

    interface OriPresenter extends IBasePresenter<OriView> {


        void getAdEntryAll(String appId, int flag, String uid);

        void announcer(String speaker, String prompt, String newsid, String newstype, int paraid,
                       int idindex);

        void getBroadcastOptions(String uid);
    }


    interface OriModel extends BaseModel {

        Disposable getAdEntryAll(String appId, int flag, String uid, Callback callback);

        Disposable getBroadcastOptions(String uid, FamousCallback callback);

        Disposable announcer(String speaker, String prompt, String newsid, String newstype, int paraid,
                             int idindex, AnnouncerCallback announcerCallback);
    }


    interface FamousCallback {

        void success(FamousPersonBean famousPersonBean);

        void error(Exception e);
    }

    interface AnnouncerCallback {

        void success(AnnouncerBean announcerBean);

        void error(Exception e);
    }

    interface Callback {

        void success(List<AdEntryBean> adEntryBeans);

        void error(Exception e);
    }
}
