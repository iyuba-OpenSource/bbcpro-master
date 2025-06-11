package com.ai.bbcpro.widget;



import com.ai.bbcpro.ui.utils.LogUtils;
import com.ai.common.CommonConstant;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 工厂
 * Created by liuzhenli on 2017/5/16.
 */

public class HttpRequestFactory {
    private static final String TAG = "HttpRequestFactory";
    private static OkHttpClient okHttpClient;
    private static GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            LogUtils.e(TAG, message);
        }
    });


    private static void initHttpClient() {
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (BuildConfig.log){
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .readTimeout(8, TimeUnit.SECONDS)
                    .writeTimeout(8, TimeUnit.SECONDS)
                    .build();
        }else {
            okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(8, TimeUnit.SECONDS)
                    .writeTimeout(8, TimeUnit.SECONDS)
                    .build();
        }

    }

    private static ExamDetailApi examDetailApi;

    public static ExamDetailApi getExamDetailApi() {
        if (examDetailApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue."+ CommonConstant.domain+"")
                    .build();
            examDetailApi = retrofit.create(ExamDetailApi.class);
        }
        return examDetailApi;
    }


    private static TestQuestionApi testQuestionApi;

    public static TestQuestionApi getTestQuestionApi() {
        if (testQuestionApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://class."+CommonConstant.domain+"")
                    .build();
            testQuestionApi = retrofit.create(TestQuestionApi.class);
        }
        return testQuestionApi;
    }

    static EvaluateApi evaluateApi;
    public static EvaluateApi getEvaluateApi(){
        if (evaluateApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(CommonConstant.HTTP_SPEECH_ALL+ "/test/")
                    .build();
            evaluateApi = retrofit.create(EvaluateApi.class);
        }
        return evaluateApi;
    }

    static LessonInfoApi lessonInfoApi;
    public static LessonInfoApi getLessonInfApi(){
        if (evaluateApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://m."+ CommonConstant.domain +"/")
                    .build();
            lessonInfoApi = retrofit.create(LessonInfoApi.class);
        }
        return lessonInfoApi;
    }

    private static ExamScoreApi examScoreApi;

    public static ExamScoreApi getExamScoreApi() {
        if (examScoreApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://daxue."+CommonConstant.domain+"")
                    .build();
            examScoreApi = retrofit.create(ExamScoreApi.class);
        }
        return examScoreApi;
    }

    private static AddCreditApi addCreditApi;

    public static AddCreditApi addCredit() {
        if (addCreditApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://api."+ CommonConstant.domain)
                    .build();
            addCreditApi = retrofit.create(AddCreditApi.class);
        }
        return addCreditApi;
    }


    private static SearchApi searchApi;

    public static SearchApi getSearchApi() {
        if (searchApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("http://apps."+ CommonConstant.domain +"/")
                    .build();
            searchApi = retrofit.create(SearchApi.class);
        }
        return searchApi;
    }

    private static PublishApi publishApi;

    public static PublishApi getPublishApi() {
        if (addCreditApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .baseUrl("http://voa."+ CommonConstant.domain +"/voa/")
                    .build();
            publishApi = retrofit.create(PublishApi.class);
        }
        return publishApi;
    }

    public static RequestBody fromString(String text) {
        return RequestBody.create(MediaType.parse("text/plain"), text);
    }

    public static MultipartBody.Part fromFile(File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
    }

    static WordPdfApi wordPdfApi;

    public static  WordPdfApi getWordPdfApi(){
        if (wordPdfApi == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(CommonConstant.HTTP_SPEECH_ALL+"/")
                    .build();
            wordPdfApi = retrofit.create(WordPdfApi.class);
        }
        return wordPdfApi;

    }
}
