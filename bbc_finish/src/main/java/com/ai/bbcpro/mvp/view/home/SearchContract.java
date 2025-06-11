package com.ai.bbcpro.mvp.view.home;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.RecommendBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Query;

public interface SearchContract {


    interface SearchView extends LoadingView {

        void recommendComplete(RecommendBean recommendBean);

        void searchApiNewComplete(SearchBean searchBean);

        void testComplete(EvalBean evalBean);

        void saveNewContentComplete(int bbcid);

        /**
         * 收藏和取消收藏句子
         * type = insert 收藏   type = del 取消收藏
         */
        void updateCollect(String type, String voaId, String timing);
    }

    interface SearchPresenter extends IBasePresenter<SearchView> {

        void recommend(String newstype);

        void searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid);

        void test(RequestBody requestBody);

        void textAllApi(String format, int bbcid);


        void updateCollect(String userId, String voaId,
                           String groupName, String sentenceId, int sentenceFlg,
                           String appid, String type, String topic,
                           String format);
    }

    interface SearchModel extends BaseModel {

        Disposable recommend(String newstype, Callback callback);

        Disposable searchApiNew(String format, String key, int pages
                , int pageNum, int parentID
                , String type, int flg, String userid, String appid, SearchCallbak searchCallbak);

        Disposable test(RequestBody requestBody, EvalCallback evalCallback);


        Disposable textAllApi(String format, int bbcid, TextAllCallback textAllCallback);


        //收藏和取消收藏句子
        Disposable updateCollect(String url, String userId, String voaId,
                                 String groupName, String sentenceId, int sentenceFlg,
                                 String appid, String type, String topic,
                                 String format, EvaluateContentContract.SentenceCallback callback);
    }

    interface SentenceCallback {

        void success(ResponseBody responseBody);

        void error(Exception e);
    }


    interface TextAllCallback {

        void success(BBCContentBean bbcContentBean);

        void error(Exception e);
    }

    interface EvalCallback {

        void success(EvalBean evalBean);

        void error(Exception e);
    }

    interface SearchCallbak {

        void success(SearchBean searchBean);

        void error(Exception e);
    }

    interface Callback {

        void success(RecommendBean recommendBean);

        void error(Exception e);
    }
}
