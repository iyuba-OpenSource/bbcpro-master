package com.ai.bbcpro.model.api;

import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.model.bean.ClockInLogBean;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.bean.MyTimeBean;
import com.ai.bbcpro.model.bean.RecommendBean;
import com.ai.bbcpro.model.bean.ScoreBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.model.bean.StudyRankingBean;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.model.bean.WxLoginBean;
import com.ai.bbcpro.model.bean.ZanBean;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.model.bean.home.ReadSubmitBean;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.model.bean.me.RewardBean;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiServer {


    /**
     * 获取积分
     *
     * @param srid
     * @param mobile
     * @param flag
     * @param uid
     * @param appid
     * @return
     */
    @GET
    Observable<ScoreBean> updateScore(@Query("srid") int srid, @Query("mobile") int mobile, @Query("flag") String flag,
                                      @Query("uid") String uid, @Query("appid") int appid);


    /**
     * 获取更多的用户数据
     *
     * @param platform
     * @param protocol
     * @param id
     * @param myid
     * @param appid
     * @param sign
     * @return
     */
    @GET
    Observable<MoreInfoBean> getMoreInfo(@Url String url, @Query("platform") String platform, @Query("protocol") int protocol,
                                         @Query("id") int id, @Query("myid") int myid,
                                         @Query("appid") int appid, @Query("sign") String sign);


    /**
     * 获取微信小程序的token
     *
     * @param platform
     * @param format
     * @param protocol
     * @param appid
     * @param sign
     * @return
     */
    @GET
    Observable<WxLoginBean> getWxAppletToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                             @Query("protocol") String protocol, @Query("appid") String appid,
                                             @Query("sign") String sign);


    /**
     * 通过获取token获取uid
     *
     * @param platform
     * @param format
     * @param protocol
     * @param token
     * @return
     */
    @GET
    Observable<WxLoginBean> getUidByToken(@Url String url, @Query("platform") String platform, @Query("format") String format,
                                          @Query("protocol") String protocol, @Query("token") String token, @Query("sign") String sign,
                                          @Query("appid") String appid);


    /**
     * 获取单词信息
     *
     * @param q
     * @return
     */
    @GET
    Observable<ResponseBody> apiWord(@Url String url, @Query("q") String q);


    /**
     * 收藏单词
     *
     * @param groupName Iyuba
     * @param mod       insert
     * @param word
     * @param userId
     * @param format    Iyuba
     * @return
     */
    @GET
    Observable<WordCollectBean> updateWord(@Url String url, @Query("groupName") String groupName, @Query("mod") String mod,
                                           @Query("word") String word, @Query("userId") String userId, @Query("format") String format);


    /**
     * 获取收藏的句子
     *
     * @param url
     * @param sign
     * @param topic
     * @param appid
     * @param sentenceFlg
     * @param userId
     * @param format
     * @return
     */
    @GET
    Observable<SentenceCollectBean> getCollect(@Url String url, @Query("sign") String sign, @Query("topic") String topic,
                                               @Query("appid") String appid, @Query("sentenceFlg") String sentenceFlg, @Query("userId") String userId,
                                               @Query("format") String format);


    /**
     * 注册
     *
     * @param protocol
     * @param mobile
     * @param username
     * @param password
     * @param platform
     * @param appid
     * @param app
     * @param format
     * @param sign
     * @return
     */
    @GET
    Observable<UserBean> register(@Url String url, @Query("protocol") int protocol, @Query("mobile") String mobile, @Query("username") String username,
                                  @Query("password") String password, @Query("platform") String platform, @Query("appid") int appid,
                                  @Query("app") String app, @Query("format") String format, @Query("sign") String sign);


    /**
     * 点赞
     *
     * @param url      http://voa.iyuba.cn/voa/UnicomApi
     * @param id
     * @param protocol 61001
     * @return
     */
    @GET
    Observable<ZanBean> zan(@Url String url, @Query("id") String id, @Query("protocol") String protocol);


    /**
     * 获取名人读音的列表
     * @param url
     * @param uid
     * @return
     */
    @GET
    Observable<FamousPersonBean> getBroadcastOptions(@Url String url, @Query("uid") String uid);

    /**
     *
     * @param url
     * @param speaker
     * @param prompt
     * @param newsid
     * @param newstype
     * @param paraid
     * @param idindex
     * @return
     */
    @GET
    Observable<AnnouncerBean> announcer(@Url String url, @Query("speaker") String speaker, @Query("prompt") String prompt,
                                        @Query("newsid") String newsid, @Query("newstype") String newstype, @Query("paraid") int paraid,
                                        @Query("idindex") int idindex);


    /**
     * 获取打卡记录
     *
     * @param uid
     * @param appId
     * @param time
     * @return
     */
    @GET
    Observable<ClockInLogBean> getShareInfoShow(@Url String url, @Query("uid") String uid, @Query("appId") int appId, @Query("time") String time);


    /**
     *
     * @param url
     * @param requestBody
     * @return
     */
    @POST
    Observable<EvalBean> test(@Url String url, @Body RequestBody requestBody);

    /**
     * 获取广告
     *
     * @param appId
     * @param flag  2 广告顺序  5自家广告内容
     * @param uid
     * @return
     */
    @GET
    Observable<List<AdEntryBean>> getAdEntryAll(@Url String url, @Query("appId") String appId, @Query("flag") int flag, @Query("uid") String uid);


    /**
     * 获取新闻的详情
     *
     * @param format
     * @param bbcid
     * @return
     */
    @GET
    Observable<BBCContentBean> textAllApi(@Url String url, @Query("format") String format, @Query("bbcid") int bbcid);

    /**
     * 获取热搜
     *
     * @param newstype
     * @return
     */
    @GET
    Observable<RecommendBean> recommend(@Url String url, @Query("newstype") String newstype);


    /**
     * 搜索
     *
     * @param format
     * @param key
     * @param pages
     * @param pageNum
     * @param parentID
     * @param type
     * @param flg
     * @param userid
     * @return
     */
    @GET
    Observable<SearchBean> searchApiNew(@Url String url, @Query("format") String format, @Query("key") String key, @Query("pages") int pages
            , @Query("pageNum") int pageNum, @Query("parentID") int parentID
            , @Query("type") String type, @Query("flg") int flg, @Query("userid") String userid
            , @Query("appid") String appid);


    /**
     * 收藏和取消收藏
     *
     * @param url
     * @param userId
     * @param voaId
     * @param groupName
     * @param sentenceId
     * @param sentenceFlg
     * @param appid
     * @param type
     * @param topic
     * @param format
     * @return
     */
    @GET
    Observable<ResponseBody> updateCollect(@Url String url, @Query("userId") String userId, @Query("voaId") String voaId,
                                           @Query("groupName") String groupName, @Query("sentenceId") String sentenceId, @Query("sentenceFlg") int sentenceFlg,
                                           @Query("appid") String appid, @Query("type") String type, @Query("topic") String topic,
                                           @Query("format") String format);


    /**
     * 获取是否要隐藏新闻类型
     *
     * @param uid
     * @param appid
     * @param platform
     * @return
     */
    @GET
    Observable<CheckBBCBean> doCheckIPBBC(@Url String url, @Query("uid") String uid,
                                          @Query("appid") String appid,
                                          @Query("platform") String platform,
                                          @Query("vip") String vip);




    /**
     * 练习题提交接口
     *
     * @param format
     * @param appName
     * @param sign
     * @param uid
     * @param appId
     * @param TestMode
     * @param DeviceId
     * @return
     */
    @GET
    Observable<UpdateTestRecordBean> updateTestRecordNew(@Url String url, @Query("format") String format, @Query("appName") String appName, @Query("sign") String sign,
                                                         @Query("uid") String uid, @Query("appId") String appId, @Query("TestMode") int TestMode,
                                                         @Query("DeviceId") String DeviceId, @Query("jsonStr") String jsonStr);


    /**
     * 上传学习记录（语音）
     *
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param Lesson
     * @param TestMode
     * @param TestWords
     * @param platform
     * @param appId
     * @param DeviceId
     * @param LessonId
     * @param sign
     * @return
     */
    @GET
    Observable<ResponseBody> updateStudyRecordNew(@Url String url, @Query("format") String format, @Query("uid") String uid,
                                                  @Query("BeginTime") String BeginTime, @Query("EndTime") String EndTime,
                                                  @Query("Lesson") String Lesson, @Query("TestMode") String TestMode,
                                                  @Query("TestWords") String TestWords, @Query("platform") String platform,
                                                  @Query("appId") String appId, @Query("DeviceId") String DeviceId,
                                                  @Query("LessonId") String LessonId, @Query("sign") String sign,
                                                  @Query("EndFlg") int EndFlg, @Query("TestNumber") int TestNumber,
                                                  @Query("rewardVersion") int rewardVersion);


    /**
     * 获取打卡界面的信息
     *
     * @param uid
     * @param day
     * @param flg
     * @return
     */
    @GET
    Observable<MyTimeBean> getMyTime(@Url String url, @Query("uid") String uid, @Query("day") int day, @Query("flg") int flg);


    /**
     * 获取学习排行榜 同听力排行
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @param mode
     * @return
     */
    @GET
    Observable<StudyRankingBean> getStudyRanking(@Url String url, @Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                 @Query("sign") String sign, @Query("start") int start, @Query("mode") String mode);


    /**
     * 获取口语排行榜
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @param topic
     * @param topicid
     * @param shuoshuotype
     * @return
     */
    @GET
    Observable<StudyRankingBean> getTopicRanking(@Url String url, @Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                 @Query("sign") String sign, @Query("start") int start, @Query("topic") String topic,
                                                 @Query("topicid") int topicid, @Query("shuoshuotype") int shuoshuotype);

    /**
     * 获取测试排行榜
     *
     * @param uid
     * @param type
     * @param total
     * @param sign
     * @param start
     * @return
     */
    @GET
    Observable<StudyRankingBean> getTestRanking(@Url String url, @Query("uid") String uid, @Query("type") String type, @Query("total") int total,
                                                @Query("sign") String sign, @Query("start") int start);


    /**
     * 提交阅读记录
     *
     * @param url
     * @param format
     * @param uid
     * @param BeginTime
     * @param EndTime
     * @param appName
     * @param Lesson
     * @param LessonId
     * @param appId
     * @param Device
     * @param DeviceId
     * @param EndFlg
     * @param wordcount
     * @param categoryid
     * @param platform
     * @return
     */
    @GET
    Observable<ReadSubmitBean> updateNewsStudyRecord(@Url String url, @Query("format") String format, @Query("uid") int uid, @Query("BeginTime") String BeginTime,
                                                     @Query("EndTime") String EndTime, @Query("appName") String appName, @Query("Lesson") String Lesson, @Query("LessonId") int LessonId,
                                                     @Query("appId") int appId, @Query("Device") String Device, @Query("DeviceId") String DeviceId, @Query("EndFlg") int EndFlg,
                                                     @Query("wordcount") int wordcount, @Query("categoryid") int categoryid, @Query("platform") String platform,
                                                     @Query("rewardVersion") int rewardVersion);


    /**
     * 我的钱包
     *
     * @param uid
     * @param pages
     * @param pageCount
     * @param sign
     * @return
     */
    @GET
    Observable<RewardBean> getUserActionRecord(@Url String url, @Query("uid") int uid, @Query("pages") int pages,
                                               @Query("pageCount") int pageCount, @Query("sign") String sign);


}
