package com.ai.bbcpro.widget;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 获取用户练习测试结果  做过的每一个题目的详情
 * Created by liuzhenli on 2017/5/16.
 */

public interface ExamScoreApi {
    //http://daxue."+WebConstant.WEB_SUFFIX+"ecollege/getExamScore.jsp?
    // appId=248
    // &uid=3212989
    // &lesson=Toefl
    // &testMode=
    // &flg=1
    // &sign=6e3a91f8d6df8e551b4d055a538e2253
    // &format=json
    @GET("ecollege/getExamScore.jsp?")
    Call<ExamScore> exampleScore(@Query("appId") String appId,
                                 @Query("uid") String uid,
                                 @Query("lesson") String lesson,
                                 @Query("testMode") String TestMode,
                                 @Query("flg") int flg,
                                 @Query("sign") String sign,
                                 @Query("format") String format
    );
//  http://daxue."+WebConstant.WEB_SUFFIX+"ecollege/getExamDetail.jsp?format=json&appId=104&uid=611179&lesson=voa&TestMode=A&mode=3&sign=77617f096e34bcccc7c718fe4fe66135&testids=33374,33375,33376,33377,33378,33379,33380,33381,33382,33383,33384,33385,33386,33387,33388,33389,34143,34144,34145,34146,34147,34148,34162,34163,34164,34165,34166,34167,34181,34182,34183,34184,34185,34186,34200,34201,34202,34203,34204,34205,34219,34220,34221,34222,34223,34224,34238,34239,34240,34241,34242,34243,34257,34258,34259,34260,34261,34262,34694,34695,34696,34697,34698,34699,34732,34733,34734,34735,34736,34737,34941,34942,34943,34944,34945,34946,35701,35702,35703,35704,35705,35706,35707,35708,35709,35710,35711,35712,35713,35714,35715,35716

//  @GET("ecollege/getExamScore.jsp?")
//  Call<ExamScore> getExamScore(@Query("appId") String appId,
//                               @Query("uid") String uid,
//                               @Query("lesson") String lesson,
//                               @Query("testMode") String TestMode,
//                               @Query("flg") int flg,
//                               @Query("sign") String sign,
//                               @Query("format") String format
//  );

}

