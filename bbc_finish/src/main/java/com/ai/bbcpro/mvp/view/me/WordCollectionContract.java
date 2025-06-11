package com.ai.bbcpro.mvp.view.me;

import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;

import io.reactivex.disposables.Disposable;

public interface WordCollectionContract {

    interface WordCollectionView extends LoadingView {

        void collectWord(WordCollectBean wordCollectBean);
    }

    interface WordCollectionPresenter extends IBasePresenter<WordCollectionView> {

        void updateWord(String groupName, String mod, String word, String userId, String format);
    }

    interface WordCollectionModel extends BaseModel {


        Disposable updateWord(String url, String groupName, String mod, String word, String userId, String format,
                               CollectCallback callback);
    }

    interface CollectCallback {

        void success(WordCollectBean wordCollectBean);

        void error(Exception e);
    }


}
