package com.ai.bbcpro.mvp.presenter.me;


import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.home.UpdateCollectBean;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.ai.bbcpro.model.me.SentenceCollectModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.util.xml.PullXmlUtil;

import java.io.IOException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SentenceCollectPresenter extends BasePresenter<SentenceCollectContract.SentenceCollectView, SentenceCollectContract.SentenceCollectModel>
        implements SentenceCollectContract.SentenceCollectPresenter {

    @Override
    protected SentenceCollectContract.SentenceCollectModel initModel() {
        return new SentenceCollectModel();
    }

    @Override
    public void getCollect(String sign, String topic, String appid, String sentenceFlg, String userId, String format) {

        view.showLoading(null);
        Disposable disposable = model.getCollect(Constant.URL_COLLECT, sign, topic, appid, sentenceFlg,
                userId, format, new SentenceCollectContract.Callback() {
                    @Override
                    public void success(SentenceCollectBean sentenceCollectBean) {

                        view.hideLoading();
                        view.getCollect(sentenceCollectBean);
                    }

                    @Override
                    public void error(Exception e) {

                        view.hideLoading();
                        if (e instanceof UnknownHostException) {

                            view.toast("请求超时");
                        }
                    }
                });
        addSubscribe(disposable);
    }

    @Override
    public void textAllApi(String format, String bbcid, int position) {

        Disposable disposable = model.textAllApi(format, bbcid, new SentenceCollectContract.NewsTextCallback() {

            @Override
            public void success(BBCContentBean newsTextBean) {

                view.textAllApi(newsTextBean, position);
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

    @Override
    public void test(RequestBody requestBody, String voaid, String timing) {

        Disposable disposable = model.test(null, requestBody, new SentenceCollectContract.EvalCallback() {
            @Override
            public void success(EvalBean evalBean) {

                if (evalBean.getResult().equals("1")) {

                    view.test(evalBean, voaid, timing);
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

    @Override
    public void updateCollect(String userId, String voaId, String groupName, String sentenceId, int sentenceFlg, String appid, String type, String topic, String format) {

        Disposable disposable = model.updateCollect(Constant.URL_UPDATE_COLLECT, userId, voaId,
                groupName, sentenceId, sentenceFlg, appid, type, topic, format, new SentenceCollectContract.SentenceCallback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        try {
                            String s = responseBody.string().trim();
                            if (s != null && !s.equals("")) {

                                UpdateCollectBean bean = PullXmlUtil.parseXMLWithPull(s);

                                if (bean.getResult().equals("1")) {
                                    //sentenceId = timing
                                    view.updateCollect(bean.getType(), voaId, sentenceId);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
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
