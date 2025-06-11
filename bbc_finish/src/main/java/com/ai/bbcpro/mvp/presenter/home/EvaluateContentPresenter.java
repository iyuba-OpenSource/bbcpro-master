package com.ai.bbcpro.mvp.presenter.home;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.home.ApiWordBean;
import com.ai.bbcpro.model.bean.home.UpdateCollectBean;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.model.home.EvaluateContentModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;
import com.ai.bbcpro.util.xml.ApiWordXmlToBean;
import com.ai.bbcpro.util.xml.PullXmlUtil;

import java.io.IOException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class EvaluateContentPresenter extends BasePresenter<EvaluateContentContract.EvaluateContentView, EvaluateContentContract.EvaluateContentModel>
        implements EvaluateContentContract.EvaluateContentPresenter {

    @Override
    protected EvaluateContentContract.EvaluateContentModel initModel() {
        return new EvaluateContentModel();
    }

    @Override
    public void apiWord(String q) {

        Disposable disposable = model.apiWord(Constant.URL_API_WORD, q, new EvaluateContentContract.WordCallback() {
            @Override
            public void success(ResponseBody responseBody) {

                try {
                    ApiWordBean apiWordBean = ApiWordXmlToBean.parseXMLWithPull(responseBody.string());
                    if (apiWordBean.getResult() == 1) {

                        view.getWord(apiWordBean);
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

    @Override
    public void updateWord(String groupName, String mod, String word, String userId, String format) {

        Disposable disposable = model.updateWord(Constant.URL_UPDATE_WORD, groupName, mod, word, userId,
                format, new EvaluateContentContract.CollectCallback() {
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

    @Override
    public void updateCollect(String userId, String voaId, String groupName, String sentenceId, int sentenceFlg, String appid, String type, String topic, String format) {

        Disposable disposable = model.updateCollect(Constant.URL_UPDATE_COLLECT, userId, voaId,
                groupName, sentenceId, sentenceFlg, appid, type, topic, format, new EvaluateContentContract.SentenceCallback() {
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
