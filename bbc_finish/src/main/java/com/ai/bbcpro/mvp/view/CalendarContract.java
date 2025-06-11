package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.ClockInLogBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import retrofit2.http.Query;

public interface CalendarContract {


    interface CalendarView extends LoadingView {

        void getLogComplete(ClockInLogBean clockInLogBean);
    }

    interface CalendarPresenter extends IBasePresenter<CalendarView> {

        void getShareInfoShow(String uid, int appId, String time);
    }

    interface CalendarModel extends BaseModel {

        Disposable getShareInfoShow(String uid, int appId, String time, Callback callback);
    }

    interface Callback {

        void success(ClockInLogBean clockInLogBean);

        void error(Exception e);
    }
}
