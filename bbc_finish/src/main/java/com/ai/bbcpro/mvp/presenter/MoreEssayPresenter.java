package com.ai.bbcpro.mvp.presenter;

import android.widget.Toast;

import com.ai.bbcpro.model.MoreEssayModel;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.home.MoreEssayContract;
import com.ai.bbcpro.mvp.view.home.SearchContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import java.net.UnknownHostException;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class MoreEssayPresenter extends BasePresenter<MoreEssayContract.MoreEssayView, MoreEssayContract.MoreEssayModel>
        implements MoreEssayContract.MoreEssayPresenter {

    private HeadlinesDataManager headlinesDataManager;

    public MoreEssayPresenter() {

        headlinesDataManager = HeadlinesDataManager.getInstance(MainApplication.getApplication());
    }

    @Override
    protected MoreEssayContract.MoreEssayModel initModel() {
        return new MoreEssayModel();
    }

    @Override
    public void searchApiNew(String format, String key, int pages, int pageNum, int parentID, String type, int flg, String userid, String appid) {

        Disposable disposable = model.searchApiNew(format, key, pages, pageNum, parentID,
                type, flg, userid, appid, new MoreEssayContract.SearchCallbak() {
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
}
