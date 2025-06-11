package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.RecommendBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SearchModel implements SearchContract.SearchModel {

    @Override
    public Disposable recommend(String newstype, SearchContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .recommend(Constant.URL_RECOMMEND, newstype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RecommendBean>() {
                    @Override
                    public void accept(RecommendBean recommendBean) throws Exception {

                        callback.success(recommendBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable searchApiNew(String format, String key, int pages, int pageNum, int parentID,
                                   String type, int flg, String userid, String appid, SearchContract.SearchCallbak searchCallbak) {

        return NetWorkManager
                .getRequestForApi()
                .searchApiNew(Constant.URL_SEARCH_BBC_API, format, key, pages, pageNum, parentID, type, flg, userid, appid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SearchBean>() {
                    @Override
                    public void accept(SearchBean searchBean) throws Exception {

                        searchCallbak.success(searchBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        searchCallbak.error((Exception) throwable);
                    }
                });
    }


    @Override
    public Disposable test(RequestBody requestBody, SearchContract.EvalCallback evalCallback) {

        return NetWorkManager
                .getRequestForApi()
                .test(Constant.URL_EVAL, requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvalBean>() {
                    @Override
                    public void accept(EvalBean evalBean) throws Exception {

                        evalCallback.success(evalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        evalCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable textAllApi(String format, int bbcid, SearchContract.TextAllCallback textAllCallback) {

        return NetWorkManager
                .getRequestForApi()
                .textAllApi(Constant.URL_TEXT_ALL_API, format, bbcid)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BBCContentBean>() {
                    @Override
                    public void accept(BBCContentBean bbcContentBean) throws Exception {

                        textAllCallback.success(bbcContentBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        textAllCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateCollect(String url, String userId, String voaId, String groupName, String sentenceId, int sentenceFlg, String appid, String type, String topic, String format, EvaluateContentContract.SentenceCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .updateCollect(url, userId, voaId, groupName, sentenceId, sentenceFlg, appid, type, topic, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback::success, throwable -> callback.error((Exception) throwable));
    }
}
