package com.ai.bbcpro.mvp.presenter.home;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.home.UpdateCollectBean;
import com.ai.bbcpro.model.home.AudioContentModel;
import com.ai.bbcpro.mvp.presenter.BasePresenter;
import com.ai.bbcpro.mvp.view.home.AudioContentContract;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.util.xml.PullXmlUtil;

import java.io.IOException;
import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class AudioContentPresenter extends BasePresenter<AudioContentContract.AudioContentView, AudioContentContract.AudioContentModel>
        implements AudioContentContract.AudioContentPresenter {


    @Override
    protected AudioContentContract.AudioContentModel initModel() {
        return new AudioContentModel();
    }

    @Override
    public void textAllApi(String format, String bbcid) {

        Disposable disposable = model.textAllApi(format, bbcid, new SentenceCollectContract.NewsTextCallback() {

            @Override
            public void success(BBCContentBean newsTextBean) {

                view.textAllApi(newsTextBean);
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
    public void updateCollect(String userId, String voaId, String groupName, String sentenceId, int sentenceFlg,
                              String appid, String type, String topic, String format) {

        Disposable disposable = model.updateCollect(Constant.URL_UPDATE_COLLECT, userId, voaId,
                groupName, sentenceId, sentenceFlg, appid, type, topic, format, new AudioContentContract.TextCallback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        try {
                            String s = responseBody.string().trim();
                            if (s != null && !s.equals("")) {

                                UpdateCollectBean bean = PullXmlUtil.parseXMLWithPull(s);

                                if (bean.getResult().equals("1")) {
                                    //sentenceId = timing
                                    view.updateCollect(bean.getType(), voaId);
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
