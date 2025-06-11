package com.ai.bbcpro.mvp.presenter.me;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.model.me.WordCollectionModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;
import com.ai.bbcpro.mvp.view.me.WordCollectionContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class WordCollectionPresenter extends BasePresenter<WordCollectionContract.WordCollectionView, WordCollectionContract.WordCollectionModel>
        implements WordCollectionContract.WordCollectionPresenter {


    @Override
    protected WordCollectionContract.WordCollectionModel initModel() {
        return new WordCollectionModel();
    }

    @Override
    public void updateWord(String groupName, String mod, String word, String userId, String format) {


        Disposable disposable = model.updateWord(Constant.URL_UPDATE_WORD, groupName, mod, word, userId,
                format, new WordCollectionContract.CollectCallback() {
                    @Override
                    public void success(WordCollectBean wordCollectBean) {

                        if (wordCollectBean.getResult() == 1) {

                            view.collectWord(wordCollectBean);
                        } else {

                        }
                    }

                    @Override
                    public void error(Exception e) {

                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }
}
