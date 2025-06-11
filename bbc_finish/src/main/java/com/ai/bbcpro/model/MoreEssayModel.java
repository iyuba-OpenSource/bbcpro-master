package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.home.MoreEssayContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MoreEssayModel implements MoreEssayContract.MoreEssayModel {


    @Override
    public Disposable searchApiNew(String format, String key, int pages, int pageNum, int parentID, String type, int flg, String userid, String appid, MoreEssayContract.SearchCallbak searchCallbak) {

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
}
