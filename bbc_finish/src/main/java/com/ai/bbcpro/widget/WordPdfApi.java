package com.ai.bbcpro.widget;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WordPdfApi {
    @GET("/management/getWordToPDF.jsp?")
    Observable<WordPdfResponse> getPdf(@Query("u") String u ,
                                       @Query("pageNumber")String pageNumber ,
                                       @Query("pageCounts") String pageCount);
}
