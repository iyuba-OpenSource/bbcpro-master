package com.ai.bbcpro.ui.http.net;

import com.ai.bbcpro.ui.bean.UpdateServiceResp;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UpdateServiceApi {

    @POST("api/getDomain.jsp")
    Call<UpdateServiceResp> requestNewService(@Query("appId") String appId, @Query("short1") String short1, @Query("short2") String short2);
}
