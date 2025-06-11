package com.ai.bbcpro.ui.http.net;





import com.ai.bbcpro.ui.http.bean.PDFExportBean;
import com.ai.common.CommonConstant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PdfService {
    String BASE_URL = "http://apps."+ CommonConstant.domain +"/minutes/";

    @GET("getBbcpdfFile_new.jsp")
    Call<PDFExportBean> post(@Query("type") String format, @Query("bbcid")String bbcid, @Query("isenglish")int isenglish);
}
