package com.ai.bbcpro.ui.http.thread;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.ApiService;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsCacheThread extends Thread{
    private String TAG = "NewsCacheThread";
    int parentID;
    int page;
    private Context mContext;
    private HeadlinesDataManager dataManager;

    public NewsCacheThread(int parentID, int page, Context mContext) {
        this.parentID = parentID;
        this.page = page;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        super.run();
    }

    public Call<SumBean> initHttp(int parentID, int page) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.post("android", "json", Integer.parseInt(Constant.APPID), 0, page, 10, parentID);
    }

    public void getData(int parentID, int page) {
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws IOException {
                Response<SumBean> execute = initHttp(parentID, page).execute();
                SumBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean SumBean) {
                List<SumBean.DataDTO> netData = SumBean.data;
                dataManager = HeadlinesDataManager.getInstance(mContext);
                dataManager.saveHeadlines(netData);
                Log.e(TAG, "onNext: "+parentID );
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
