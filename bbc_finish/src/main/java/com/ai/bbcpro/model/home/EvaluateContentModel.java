package com.ai.bbcpro.model.home;

import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class EvaluateContentModel implements EvaluateContentContract.EvaluateContentModel {


    @Override
    public Disposable apiWord(String url, String q, EvaluateContentContract.WordCallback wordCallback) {

        return NetWorkManager
                .getRequestForApi()
                .apiWord(url, q)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        wordCallback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        wordCallback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateWord(String url, String groupName, String mod, String word, String userId, String format, EvaluateContentContract.CollectCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .updateWord(url, groupName, mod, word, userId, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordCollectBean>() {
                    @Override
                    public void accept(WordCollectBean wordCollectBean) throws Exception {

                        callback.success(wordCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
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
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {

                        callback.success(responseBody);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
