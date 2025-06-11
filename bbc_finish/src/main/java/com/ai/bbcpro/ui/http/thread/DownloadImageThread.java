package com.ai.bbcpro.ui.http.thread;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ai.bbcpro.ui.utils.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadImageThread extends Thread{
    private String url;
    private String bbcId;
    private Context mContext;

    public DownloadImageThread(String url, String bbcId,Context context) {
        this.url = url;
        this.bbcId = bbcId;
        this.mContext = context;
    }

    @Override
    public void run() {
        super.run();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        byte[] bytes = Objects.requireNonNull(response.body()).bytes();
                        String filePath = mContext.getFilesDir() + "/downloadimg";
                        Log.e("thread", "run: "+filePath );
                        String fileName = bbcId+".jpg";
                        if (FileUtils.fileIsExist(filePath)){
                            File file = new File(filePath, fileName);
                            try {
                                OutputStream os = new FileOutputStream(file);
                                InputStream is = new ByteArrayInputStream(bytes);
                                byte[] buff = new byte[2048];
                                int len = 0;
                                while ((len = is.read(buff)) != -1){
                                    os.write(buff,0,len);
                                }
                                is.close();
                                os.close();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }


                    }
                }
            }
        });
    }
}
