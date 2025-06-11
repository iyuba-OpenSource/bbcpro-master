package com.ai.bbcpro.mvp.view.me;


import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public interface SentenceCollectContract {


    interface SentenceCollectView extends LoadingView {

        void getCollect(SentenceCollectBean sentenceCollectBean);

        void textAllApi(BBCContentBean newsTextBean, int position);

        void test(EvalBean evalBean, String voaid, String timing);


        /**
         * 收藏和取消收藏句子
         * type = insert 收藏   type = del 取消收藏
         */
        void updateCollect(String type, String voaId, String timing);
    }

    interface SentenceCollectPresenter extends IBasePresenter<SentenceCollectView> {

        void getCollect(String sign, String topic,
                        String appid, String sentenceFlg, String userId,
                        String format);

        void textAllApi(String format, String bbcid, int position);

        void test(RequestBody requestBody, String voaid, String timing);

        void updateCollect(String userId, String voaId,
                           String groupName, String sentenceId, int sentenceFlg,
                           String appid, String type, String topic,
                           String format);
    }

    interface SentenceCollectModel extends BaseModel {

        Disposable getCollect(String url, String sign, String topic,
                              String appid, String sentenceFlg, String userId,
                              String format, Callback callback);


        Disposable textAllApi(String format, String bbcid, NewsTextCallback callback);

        Disposable test(String url, RequestBody requestBody, EvalCallback callback);

        //收藏和取消收藏句子
        Disposable updateCollect(String url, String userId, String voaId,
                                 String groupName, String sentenceId, int sentenceFlg,
                                 String appid, String type, String topic,
                                 String format, SentenceCallback callback);
    }


    interface SentenceCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }

    interface EvalCallback {

        void success(EvalBean evalBean);

        void error(Exception e);
    }

    interface NewsTextCallback {

        void success(BBCContentBean newsTextBean);

        void error(Exception e);
    }

    interface Callback {

        void success(SentenceCollectBean sentenceCollectBean);

        void error(Exception e);
    }
}
