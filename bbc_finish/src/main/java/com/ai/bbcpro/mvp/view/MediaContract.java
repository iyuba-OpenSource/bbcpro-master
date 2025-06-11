package com.ai.bbcpro.mvp.view;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;

public interface MediaContract {

    interface MediaView extends BaseView {

        /**
         * 村塾数据到数据库完成
         */
        void saveNewContentComplete(int logPosition);
    }

    interface MediaPresenter extends IBasePresenter<MediaView> {

        void updateStudyRecordNew(String format, String uid,
                                  String BeginTime, String EndTime,
                                  String Lesson, String TestMode,
                                  String TestWords, String platform,
                                  String appName, String DeviceId,
                                  String LessonId, String sign, int EndFlg, int TestNumber);

        void textAllApi(String format, int bbcid, int logPosition);
    }

    interface MediaModel extends BaseModel {

        Disposable updateStudyRecordNew(String format, String uid,
                                        String BeginTime, String EndTime,
                                        String Lesson, String TestMode,
                                        String TestWords, String platform,
                                        String appName, String DeviceId,
                                        String LessonId, String sign, int EndFlg, int TestNumber, Callback callback);

        Disposable textAllApi(String format, int bbcid, NewsContentCallback newsContentCallback);
    }


    interface NewsContentCallback {

        void success(BBCContentBean bbcContentBean);

        void error(Exception e);
    }


    interface Callback {

        void success(ResponseBody responseBody);

        void error(Exception e);

    }

}
