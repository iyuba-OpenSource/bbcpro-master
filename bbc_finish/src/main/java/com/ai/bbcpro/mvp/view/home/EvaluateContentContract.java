package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.ApiWordBean;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface EvaluateContentContract {

    interface EvaluateContentView extends LoadingView {

        void getWord(ApiWordBean apiWordBean);

        void collectWord(WordCollectBean wordCollectBean);

        /**
         * 收藏和取消收藏句子
         * type = insert 收藏   type = del 取消收藏
         */
        void updateCollect(String type, String voaId, String timing);
    }

    interface EvaluateContentPresenter extends IBasePresenter<EvaluateContentView> {

        void apiWord(String q);

        void updateWord(String groupName, String mod, String word, String userId, String format);

        void updateCollect(String userId, String voaId,
                           String groupName, String sentenceId, int sentenceFlg,
                           String appid, String type, String topic,
                           String format);
    }

    interface EvaluateContentModel extends BaseModel {

        Disposable apiWord(String url, String q, WordCallback wordCallback);

        Disposable updateWord(String url, String groupName, String mod, String word, String userId, String format,
                              CollectCallback callback);

        //收藏和取消收藏句子
        Disposable updateCollect(String url, String userId, String voaId,
                                 String groupName, String sentenceId, int sentenceFlg,
                                 String appid, String type, String topic,
                                 String format, SentenceCallback callback);
    }

    interface WordCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface CollectCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);
    }

    interface SentenceCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }
}
