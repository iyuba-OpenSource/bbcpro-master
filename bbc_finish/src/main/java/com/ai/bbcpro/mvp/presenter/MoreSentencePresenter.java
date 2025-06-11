package com.ai.bbcpro.mvp.presenter;

import com.ai.bbcpro.model.MoreSentenceModel;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.home.MoreEssayContract;
import com.ai.bbcpro.mvp.view.home.MoreSentenceContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

public class MoreSentencePresenter extends BasePresenter<MoreSentenceContract.MoreSentenceView, MoreSentenceContract.MoreSentenceModel>
        implements MoreSentenceContract.MoreSentencePresenter {


    @Override
    protected MoreSentenceContract.MoreSentenceModel initModel() {
        return new MoreSentenceModel();
    }

    @Override
    public void searchApiNew(String format, String key, int pages, int pageNum, int parentID, String type, int flg, String userid, String appid) {

        Disposable disposable = model.searchApiNew(format, key, pages, pageNum, parentID,
                type, flg, userid, appid, new MoreSentenceContract.SearchCallbak() {
                    @Override
                    public void success(SearchBean searchBean) {

                        if (pages == 1) {

                            view.searchApiNewComplete(searchBean);
                        } else {

                            view.loadmore(searchBean);
                        }
                    }

                    @Override
                    public void error(Exception e) {

                        view.loadmore(null);
                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void test(RequestBody requestBody) {

        Disposable disposable = model.test(requestBody, new SearchContract.EvalCallback() {
            @Override
            public void success(EvalBean evalBean) {

                if (evalBean.getResult().equals("1")) {

                    view.testComplete(evalBean);
                } else {

                    view.toast(evalBean.getMessage());
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
