package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface TitleContract {

    interface TitleView extends LoadingView {

        void getAdEntryAllComplete(AdEntryBean adEntryBean);
    }


    interface TitlePresenter extends IBasePresenter<TitleView> {

        void getAdEntryAll(String appId, int flag, String uid);
    }

    interface TitleModel extends BaseModel {

        Disposable getAdEntryAll(String appId, int flag, String uid, AdEntryCallback adEntryCallback);
    }

    interface AdEntryCallback {

        void success(List<AdEntryBean> adEntryBeanList);

        void error(Exception e);
    }
}
