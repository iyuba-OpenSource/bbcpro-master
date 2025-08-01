package com.ai.bbcpro.mvp.view.home;



import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.ReadSubmitBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.disposables.Disposable;

public interface ParaContract {


    interface ParaView extends LoadingView {


        /**
         * 上传阅读
         */
        void submitRead(ReadSubmitBean readSubmitBean);
    }

    interface ParaPresenter extends IBasePresenter<ParaView> {


        void updateNewsStudyRecord(String format, int uid, String BeginTime,
                                   String EndTime, String appName, String Lesson, int LessonId,
                                   int appId, String Device, String DeviceId, int EndFlg,
                                   int wordcount, int categoryid, String platform, int rewardVersion);

        void updateScore20230814(int srid, int uid, int appid, int voaid, String sign);
    }

    interface ParaModel extends BaseModel {


        Disposable updateNewsStudyRecord(String format, int uid, String BeginTime,
                                         String EndTime, String appName, String Lesson, int LessonId,
                                         int appId, String Device, String DeviceId, int EndFlg,
                                         int wordcount, int categoryid, String platform, int rewardVersion, Callback callback);


        Disposable updateScore20230814(int srid, int uid, int appid, int voaid, String sign,
                                       UpdateCallback updateCallback);
    }

    interface UpdateCallback {

        void success(String s);

        void error(Exception e);

    }


    interface Callback {

        void success(ReadSubmitBean readSubmitBean);

        void error(Exception e);
    }
}
