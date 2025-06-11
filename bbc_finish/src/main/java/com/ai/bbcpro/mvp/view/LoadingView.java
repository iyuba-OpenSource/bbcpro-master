package com.ai.bbcpro.mvp.view;

public interface LoadingView extends BaseView {


    void showLoading(String msg);

    void hideLoading();

    void toast(String msg);
}
