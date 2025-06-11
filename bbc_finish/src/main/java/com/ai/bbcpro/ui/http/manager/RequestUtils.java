package com.ai.bbcpro.ui.http.manager;

import android.os.Process;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.ApiService;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestUtils {

    public final static OkHttpClient client = new OkHttpClient();

    public static Call<SumBean> initHttp(int parentID, int page) {
        int pageNum = 10;
        pageNum = page * 10;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.post("android", "json", Integer.parseInt(Constant.APPID), 0, page, 10, parentID);
    }

    public static List<SumBean.DataDTO> doGet(int parentID, int page) {
        Process.setThreadPriority(Process.THREAD_PRIORITY_FOREGROUND);
        JSONObject jo = null;

//        Request request = new Request.Builder()
//                .url(urlStr)
//                .header("User-Agent", "My super agent")
//                .addHeader("Accept", "text/html")
//                .build();

        Call<SumBean> sumBeanCall = initHttp(parentID, page);
        Response<SumBean> execute = null;
        try {
            execute = sumBeanCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<SumBean.DataDTO> body = execute.body().data;


        return body;
    }

}
