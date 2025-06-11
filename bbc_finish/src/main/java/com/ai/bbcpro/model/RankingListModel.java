package com.ai.bbcpro.model;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.model.bean.StudyRankingBean;
import com.ai.bbcpro.mvp.view.RankingListContract;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RankingListModel implements RankingListContract.RankingListModel {
    @Override
    public Disposable getStudyRanking(String uid, String type, int total, String sign, int start,
                                      String mode, RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getStudyRanking(Constant.URL_GET_STUDY_RANKING, uid, type, total, sign, start, mode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTopicRanking(String uid, String type, int total, String sign, int start,
                                      String topic, int topicid, int shuoshuotype, RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getTopicRanking(Constant.URL_GET_TOPIC_RANKING, uid, type, total, sign, start, topic, topicid, shuoshuotype)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }

    @Override
    public Disposable getTestRanking(String uid, String type, int total, String sign, int start,
                                     RankingListContract.Callback callback) {

        return NetWorkManager
                .getRequestForApi()
                .getTestRanking(Constant.URL_GET_TEST_RANKING, uid, type, total, sign, start)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StudyRankingBean>() {
                    @Override
                    public void accept(StudyRankingBean studyRankingBean) throws Exception {

                        callback.success(studyRankingBean);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        callback.error((Exception) throwable);
                    }
                });
    }
}
