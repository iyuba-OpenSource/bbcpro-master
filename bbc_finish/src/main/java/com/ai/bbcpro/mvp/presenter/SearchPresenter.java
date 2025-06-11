package com.ai.bbcpro.mvp.presenter;

import android.widget.Toast;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.SearchModel;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.model.bean.RecommendBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.model.bean.home.UpdateCollectBean;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.util.xml.PullXmlUtil;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SearchPresenter extends BasePresenter<SearchContract.SearchView, SearchContract.SearchModel>
        implements SearchContract.SearchPresenter {

    private HeadlinesDataManager headlinesDataManager;

    public SearchPresenter() {

        headlinesDataManager = HeadlinesDataManager.getInstance(MainApplication.getApplication());
    }

    @Override
    protected SearchContract.SearchModel initModel() {
        return new SearchModel();
    }

    @Override
    public void recommend(String newstype) {

        Disposable disposable = model.recommend(newstype, new SearchContract.Callback() {
            @Override
            public void success(RecommendBean recommendBean) {

                if (recommendBean.getResult().equals("200")) {

                    view.recommendComplete(recommendBean);
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
    public void searchApiNew(String format, String key, int pages, int pageNum, int parentID, String type,
                             int flg, String userid, String appid) {

        Disposable disposable = model.searchApiNew(format, key, pages, pageNum, parentID,
                type, flg, userid, appid, new SearchContract.SearchCallbak() {
                    @Override
                    public void success(SearchBean searchBean) {

                        view.searchApiNewComplete(searchBean);
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

    @Override
    public void textAllApi(String format, int bbcid) {

        Disposable disposable = model.textAllApi(format, bbcid, new SearchContract.TextAllCallback() {
            @Override
            public void success(BBCContentBean bbcContentBean) {

                if (!bbcContentBean.getTotal().equals("0")) {

                    headlinesDataManager.saveDetail(bbcContentBean.getData());

                    //存储问题到数据库
                    List<QuestionBean> questionBeanList = bbcContentBean.getDataQuestion();
                    for (int i = 0; i < questionBeanList.size(); i++) {

                        QuestionBean questionBean = questionBeanList.get(i);
                        long count = headlinesDataManager.hasQuestionBean(questionBean.getBbcId(), questionBean.getIndexId());
                        if (count > 0) {//有数据

                            headlinesDataManager.updateQuestion(questionBean);
                        } else {

                            headlinesDataManager.addQuestion(questionBean);
                        }
                    }
                    view.saveNewContentComplete(bbcid);
                } else {

                    Toast.makeText(MainApplication.getApplication(), "暂无数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Exception e) {

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
