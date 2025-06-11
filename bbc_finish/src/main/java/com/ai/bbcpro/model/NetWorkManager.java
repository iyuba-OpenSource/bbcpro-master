package com.ai.bbcpro.model;

import com.ai.bbcpro.model.api.ApiServer;
import com.ai.bbcpro.util.SSLSocketFactoryUtils;
import com.ai.common.CommonConstant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit的框架
 * 网络请求管理者
 */
public class NetWorkManager {

    private static NetWorkManager mInstance;
    private static Retrofit retrofitApi;
    private static volatile ApiServer apiServer = null;


    public static NetWorkManager getInstance() {
        if (mInstance == null) {
            synchronized (NetWorkManager.class) {
                if (mInstance == null) {
                    mInstance = new NetWorkManager();
                }
            }
        }
        return mInstance;
    }



    public void initApi() {
        // 初始化okhttp
        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(SSLSocketFactoryUtils.createSSLSocketFactory(), SSLSocketFactoryUtils.createTrustAllManager())
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        // 初始化Retrofit
        retrofitApi = new Retrofit.Builder()
                .client(client)
                .baseUrl(CommonConstant.URL_API_CN)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiServer getRequestForApi() {

        if (apiServer == null) {
            synchronized (ApiServer.class) {
                apiServer = retrofitApi.create(ApiServer.class);
            }
        }
        return apiServer;
    }
}
