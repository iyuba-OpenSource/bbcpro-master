package com.ai.bbcpro.ui.http.manager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ai.bbcpro.ui.adapter.ReturnData;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.ApiService;

import java.io.IOException;

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

public class DataControl {
    ReturnData returnData;
    int num,parentID;
    String TAG = "DataControl";
    public void setListener(ReturnData returnData){
        this.returnData = returnData;
    }


    public void setNum(int num) {
        this.num = num;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public Call<SumBean> initHttp(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.post("android","json", 221,0,10,10,1);
    }

    public void getData(){
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws IOException {
                Response<SumBean> execute = initHttp().execute();
                SumBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean SumBean) {
                 returnData.loadSuccess(SumBean.data);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: "+e.getMessage() );
            }

            @Override
            public void onComplete() {

            }
        });
    }

}
