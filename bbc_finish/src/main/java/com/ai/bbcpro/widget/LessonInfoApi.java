package com.ai.bbcpro.widget;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LessonInfoApi {
    @GET("jlpt1/getExamList.jsp")
    Observable<TestListInfoBean> getTestListInfo(@Query("bookname") String book);
}
