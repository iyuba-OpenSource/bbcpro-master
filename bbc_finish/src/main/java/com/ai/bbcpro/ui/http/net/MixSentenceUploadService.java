package com.ai.bbcpro.ui.http.net;

import com.ai.bbcpro.ui.player.rank.MixSentenceResponse;
import com.ai.common.CommonConstant;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MixSentenceUploadService {

    String BASE_URL = "http://voa." + CommonConstant.domain + "/voa/";

    @GET("UnicomApi?")
    Call<MixSentenceResponse> post(@Query("platform") String platform, @Query("format") String format, @Query("protocol") int protocol,
                                   @Query("topic") String topic, @Query("shuoshuotype") int shuoshuotype, @Query("userid") String userid,
                                   @Query("voaid") int voaid, @Query("score") int score, @Query("content") String content,
                                   @Query("rewardVersion") int rewardVersion, @Query("appid") int appid);
}
