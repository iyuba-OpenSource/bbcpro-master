package com.ai.bbcpro.model.me;

import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.view.me.WordCollectionContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class WordCollectionModel implements WordCollectionContract.WordCollectionModel{


    @Override
    public Disposable updateWord(String url, String groupName, String mod, String word, String userId, String format, WordCollectionContract.CollectCallback callback) {

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
}
