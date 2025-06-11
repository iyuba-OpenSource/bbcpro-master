package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.MyTimeBean;
import com.ai.bbcpro.model.bean.ScoreBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface MyTimeContract {


    interface MyTimeView extends LoadingView {


        void getMyTimeComplete(MyTimeBean myTimeBean);

        void updateScore(ScoreBean scoreBean);
    }

    interface MyTimePresenter extends IBasePresenter<MyTimeView> {

        void getMyTime(String uid, int day, int flg);

        void updateScore(int srid, int mobile, String flag, String uid, int appid);
    }

    interface MyTimModel extends BaseModel {

        Disposable getMyTime(String uid, int day, int flg, MyTimeCallback myTimeCallback);

        Disposable updateScore(int srid, int mobile, String flag, String uid, int appid, ScoreCallback callback);
    }

    interface ScoreCallback {

        void success(ScoreBean scoreBean);

        void error(Exception e);
    }

    interface MyTimeCallback {

        void success(MyTimeBean myTimeBean);

        void error(Exception e);
    }
}
