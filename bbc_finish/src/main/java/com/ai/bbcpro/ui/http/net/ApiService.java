package com.ai.bbcpro.ui.http.net;





import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.common.CommonConstant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    String BASE_URL = "http://apps."+ CommonConstant.domain +"/minutes/";


    @GET("titleApi.jsp")
    Call<SumBean> post(@Query("type") String type, @Query("format") String format, @Query("appId") int appId,
                       @Query("maxid") int maxid, @Query("pages") int pages, @Query("pageNum") int pageNum, @Query("parentID") int parentID);

}
