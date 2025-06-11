package com.ai.bbcpro.widget;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TestQuestionApi {
    //http://class."+WebConstant.WEB_SUFFIX+"getClass.iyuba?&protocol=20000&lesson=Toefl&category=W&sign=6d2f6c0396e4799027e2cb2ceb226d5c&format=json&mode=2&uid=2561832
    @GET("getClass.iyuba?")
    Call<AbilityQuestion> testQuestionApi(@Query("protocol") String protocol,
                                          @Query("lesson") String lesson,
                                          @Query("category") String category,
                                          @Query("sign") String sign,
                                          @Query("format") String format,
                                          @Query("mode") int mode,
                                          @Query("uid") String uid,
                                          @Query("lessonid") int lessonid
    );
}
