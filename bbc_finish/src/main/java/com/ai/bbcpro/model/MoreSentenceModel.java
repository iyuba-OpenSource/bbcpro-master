package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.home.MoreSentenceContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;

public class MoreSentenceModel implements MoreSentenceContract.MoreSentenceModel {


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
    public Disposable searchApiNew(String format, String key, int pages, int pageNum, int parentID, String type, int flg, String userid, String appid, MoreSentenceContract.SearchCallbak searchCallbak) {

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
}
