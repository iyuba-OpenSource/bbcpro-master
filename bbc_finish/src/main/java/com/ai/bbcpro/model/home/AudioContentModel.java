package com.ai.bbcpro.model.home;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.mvp.view.home.AudioContentContract;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class AudioContentModel implements AudioContentContract.AudioContentModel {


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
    public Disposable updateCollect(String url, String userId, String voaId, String groupName, String sentenceId, int sentenceFlg, String appid, String type, String topic, String format, AudioContentContract.TextCallback callback) {

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
