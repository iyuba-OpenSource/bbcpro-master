package com.ai.bbcpro.ui.http.net;





import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.common.CommonConstant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ContentService {
    String BASE_URL = "http://apps."+ CommonConstant.domain +"/minutes/";

    @GET("textAllApi.jsp")
    Call<BBCContentBean> post(@Query("format") String format, @Query("bbcid")String bbcid);
}
