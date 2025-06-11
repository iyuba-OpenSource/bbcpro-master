package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.ZanBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RankPersonalContract {


    interface RankPersonalView extends LoadingView {

        void zan(ZanBean zanBean,String id);

    }


    interface RankPersonalPresenter extends IBasePresenter<RankPersonalView> {

        void zan(String url, String id, String protocol);
    }

    interface RankPersonalModel extends BaseModel {

        Disposable zan(String url, String id, String protocol, Callback callback);
    }

    interface Callback {

        void success(ZanBean zanBean);

        void error(Exception e);
    }
}
