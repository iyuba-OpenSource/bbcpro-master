package com.ai.bbcpro.ui.utils;

import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {
    public static final int DOWNLOAD_FAIL = 0;
    public static final int DOWNLOAD_PROGRESS = 1;
    public static final int DOWNLOAD_SUCCESS = 2;

    private static DownloadUtil downloadUtil;
    private final OkHttpClient okHttpClient;
    private int fileName;  // 下载文件之前需要先setName

    public static DownloadUtil getInstance() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtil();
        }
        return downloadUtil;
    }

    private DownloadUtil() {
        okHttpClient = new OkHttpClient();
    }

    public void download(final String url, final String saveDir, final OnDownloadListener listener){
        this.listener=listener;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.what = DOWNLOAD_FAIL;
                message.arg1 = fileName;
                mHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                //储存下载文件的目录
                String savePath = isExistDir(saveDir);
                try{
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath, fileName + ".mp3");
                    fos = new FileOutputStream(file);
                    long sum=  0;
                    while((len = is.read(buf)) != -1){
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int)(sum * 1.0f / total * 100);
                        //下载中
                        Message message = Message.obtain();
                        message.what = DOWNLOAD_PROGRESS;
                        message.obj = progress;
                        mHandler.sendMessage(message);
                    }
                    fos.flush();
                    //下载完成
                    Message message = Message.obtain();
                    message.what = DOWNLOAD_SUCCESS;
                    message.arg1 = fileName;
                    message.obj = file.getAbsolutePath();
                    mHandler.sendMessage(message);
                }catch (Exception e){
                    Message message = Message.obtain();
                    message.what = DOWNLOAD_FAIL;
                    message.arg1 = fileName;
                    mHandler.sendMessage(message);
                }finally{
                    try{
                        if(is!=null)
                            is.close();
                    }catch (IOException e){

                    }
                    try {
                        if(fos!=null){
                            fos.close();
                        }
                    }catch (IOException e){

                    }
                }
            }
        });
    }

    private String isExistDir(String saveDir) throws IOException {
        File downloadFile = new File(saveDir);
        if(!downloadFile.mkdirs()){
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }




    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DOWNLOAD_PROGRESS:
                    listener.onDownloading((Integer) msg.obj);
                    break;
                case DOWNLOAD_FAIL:
                    listener.onDownloadFailed(msg.arg1);
                    break;
                case DOWNLOAD_SUCCESS:
                    listener.onDownloadSuccess((String) msg.obj, msg.arg1);
                    break;
            }
        }
    };


    OnDownloadListener listener;

    // fileName传的是bbcId, 拼接.mp3使用
    public void setFileName(int fileName) {
        this.fileName = fileName;
    }

    public interface OnDownloadListener{
        /**
         * 下载成功
         */
        void onDownloadSuccess(String path, int bbcId);
        /**
         * 下载进度
         * @param progress
         */
        void onDownloading(int progress);
        /**
         * 下载失败
         */
        void onDownloadFailed(int bbcId);
    }
}
