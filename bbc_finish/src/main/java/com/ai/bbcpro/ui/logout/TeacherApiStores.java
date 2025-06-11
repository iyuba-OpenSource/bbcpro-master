package com.ai.bbcpro.ui.logout;


import com.ai.bbcpro.model.UserBean;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TeacherApiStores {

    @GET("question/getQuestionList.jsp")
    Call<QuestionListBean> getQuesList(@Query("format") String format, @Query("type") int type,
                                       @Query("category1") int category1, @Query("category2") int category2,
                                       @Query("pageNum") int pageNum, @Query("isanswered") int isanswered);


    // 登录
    @GET("v2/api.iyuba")
    @ResponseFormat("json")
    Call<LoginResponse> login(@Query("protocol") String protocol,
                              @Query("username") String username,
                              @Query("password") String password,
                              @Query("x") String x,
                              @Query("y") String y,
                              @Query("appid") String appid,
                              @Query("sign") String sign,
                              @Query("format") String format);


    /**
     * 注销使用
     * @param protocol
     * @param username
     * @param password
     * @param sign
     * @param format
     * @return
     */
    @POST("v2/api.iyuba")
    @FormUrlEncoded
    Call<UserBean> login(@Field("protocol") String protocol,
                         @Field("username") String username,
                         @Field("password") String password,
                         @Field("sign") String sign,
                         @Field("format") String format);
}

