package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public interface AudioContentContract {

    interface AudioContentView extends LoadingView {


        void textAllApi(BBCContentBean newsTextBean);

        void updateCollect(String type, String voaId);
    }

    interface AudioContentPresenter extends IBasePresenter<AudioContentContract.AudioContentView> {

        void textAllApi(String format, String bbcid);

        void updateCollect(String userId, String voaId,
                           String groupName, String sentenceId, int sentenceFlg,
                           String appid, String type, String topic,
                           String format);
    }

    interface AudioContentModel extends BaseModel {

        Disposable textAllApi(String format, String bbcid, SentenceCollectContract.NewsTextCallback callback);

        /**
         * 收藏文章
         *
         * @param url
         * @param userId
         * @param voaId
         * @param groupName
         * @param sentenceId
         * @param sentenceFlg
         * @param appid
         * @param type
         * @param topic
         * @param format
         * @param callback
         * @return
         */
        Disposable updateCollect(String url, String userId, String voaId,
                                 String groupName, String sentenceId, int sentenceFlg,
                                 String appid, String type, String topic,
                                 String format, TextCallback callback);
    }

    interface TextCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }
}
