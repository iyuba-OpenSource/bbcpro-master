package com.ai.bbcpro.mvp.presenter;

import com.ai.bbcpro.model.RankPersonalModel;
import com.ai.bbcpro.model.bean.ZanBean;
import com.ai.bbcpro.mvp.view.RankPersonalContract;

import io.reactivex.disposables.Disposable;

public class RankPersonalPresenter extends BasePresenter<RankPersonalContract.RankPersonalView, RankPersonalContract.RankPersonalModel>
        implements RankPersonalContract.RankPersonalPresenter {
    @Override
    protected RankPersonalContract.RankPersonalModel initModel() {
        return new RankPersonalModel();
    }

    @Override
    public void zan(String url, String id, String protocol) {

        Disposable disposable = model.zan(url, id, protocol, new RankPersonalContract.Callback() {
            @Override
            public void success(ZanBean zanBean) {

                view.zan(zanBean, id);
            }

            @Override
            public void error(Exception e) {

                view.toast("请求超时");
            }
        });
        addSubscribe(disposable);
    }
}
