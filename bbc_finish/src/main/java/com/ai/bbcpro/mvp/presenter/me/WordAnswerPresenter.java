package com.ai.bbcpro.mvp.presenter.me;


import com.ai.bbcpro.model.me.WordAnswerModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.WordAnswerContract;

public class WordAnswerPresenter extends BasePresenter<WordAnswerContract.WordAnswerView, WordAnswerContract.WordAnswerModel>
        implements WordAnswerContract.WordAnswerPresenter {

    @Override
    protected WordAnswerContract.WordAnswerModel initModel() {
        return new WordAnswerModel();
    }
}
