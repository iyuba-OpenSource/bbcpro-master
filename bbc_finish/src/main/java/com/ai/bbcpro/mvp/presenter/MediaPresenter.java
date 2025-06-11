package com.ai.bbcpro.mvp.presenter;

import android.widget.Toast;

import com.ai.bbcpro.entity.RewardEventbus;
import com.ai.bbcpro.model.MediaModel;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.model.bean.TestRecordBean;
import com.ai.bbcpro.mvp.view.MediaContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import okhttp3.ResponseBody;

public class MediaPresenter extends BasePresenter<MediaContract.MediaView, MediaContract.MediaModel>
        implements MediaContract.MediaPresenter {

    /**
     * 数据库
     */
    private HeadlinesDataManager headlinesDataManager;

    public MediaPresenter() {

        headlinesDataManager = HeadlinesDataManager.getInstance(MainApplication.getApplication());
    }

    @Override
    protected MediaContract.MediaModel initModel() {
        return new MediaModel();
    }

    @Override
    public void updateStudyRecordNew(String format, String uid, String BeginTime, String EndTime,
                                     String Lesson, String TestMode, String TestWords,
                                     String platform, String appName, String DeviceId, String LessonId,
                                     String sign, int EndFlg, int TestNumber) {

        model.updateStudyRecordNew(format, uid, BeginTime, EndTime, Lesson, TestMode, TestWords, platform,
                appName, DeviceId, LessonId, sign, EndFlg, TestNumber, new MediaContract.Callback() {
                    @Override
                    public void success(ResponseBody responseBody) {

                        TestRecordBean testRecordBean = null;
                        try {
                            testRecordBean = new Gson().fromJson(responseBody.string().trim(), TestRecordBean.class);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if (testRecordBean.getResult().equals("1")) {

                            //todo  保存播放进度到数据库
                            headlinesDataManager.updateListenProgress(Integer.parseInt(LessonId), EndFlg, TestNumber);
                        }


                        if (!testRecordBean.getReward().equals("0")) {

                            EventBus.getDefault().post(new RewardEventbus(Integer.parseInt(testRecordBean.getReward())));
                        } else if (testRecordBean.getJiFen() != null) {

                            Toast.makeText(MainApplication.getApplication(), "获得" + testRecordBean.getJiFen() + "积分", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void error(Exception e) {

                    }
                });
    }

    @Override
    public void textAllApi(String format, int bbcid, int logPosition) {


        model.textAllApi(format, bbcid, new MediaContract.NewsContentCallback() {
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
                    view.saveNewContentComplete(logPosition);
                } else {

                    Toast.makeText(MainApplication.getApplication(), "暂无数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    Toast.makeText(MainApplication.getApplication(), "请求超时", Toast.LENGTH_SHORT).show();
                }

                if (e instanceof RuntimeException) {

                    Toast.makeText(MainApplication.getApplication(), "请求超时", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
