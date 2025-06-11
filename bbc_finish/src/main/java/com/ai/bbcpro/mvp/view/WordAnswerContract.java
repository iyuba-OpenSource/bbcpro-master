package com.ai.bbcpro.mvp.view;


import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;

public interface WordAnswerContract {


    interface WordAnswerView extends LoadingView {

    }

    interface WordAnswerPresenter extends IBasePresenter<WordAnswerView> {


    }


    interface WordAnswerModel extends BaseModel {

    }
}

