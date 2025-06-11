package com.ai.bbcpro.model.me;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SentenceCollectModel implements SentenceCollectContract.SentenceCollectModel {


    @Override
    public Disposable getCollect(String url, String sign, String topic, String appid, String sentenceFlg,
                                 String userId, String format, SentenceCollectContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getCollect(url, sign, topic, appid, sentenceFlg, userId, format)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SentenceCollectBean>() {
                    @Override
                    public void accept(SentenceCollectBean sentenceCollectBean) throws Exception {

                        callback.success(sentenceCollectBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable textAllApi(String format, String bbcid, SentenceCollectContract.NewsTextCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .textAllApi(Constant.URL_TEXT_ALL_API, format, Integer.parseInt(bbcid))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BBCContentBean>() {
                    @Override
                    public void accept(BBCContentBean newsTextBean) throws Exception {

                        callback.success(newsTextBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable test(String url, RequestBody requestBody, SentenceCollectContract.EvalCallback callback) {

        return NetWorkManager
                .getRequestForApi()
                .test(Constant.URL_EVAL, requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<EvalBean>() {
                    @Override
                    public void accept(EvalBean evalBean) throws Exception {

                        callback.success(evalBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable updateCollect(String url, String userId, String voaId, String groupName, String sentenceId, int sentenceFlg, String appid, String type, String topic, String format, SentenceCollectContract.SentenceCallback callback) {

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
