package com.ai.bbcpro.ui.http.thread;

import android.content.Context;

import com.ai.bbcpro.ui.utils.DownloadUtil;
import com.ai.bbcpro.ui.utils.FileUtils;
import com.ai.common.CommonConstant;

public class DownloadAudioThread extends Thread{
    String audioUrl;
    Context mContext;
    DownloadUtil.OnDownloadListener listener;

    public DownloadAudioThread(String audioUrl, Context mContext, DownloadUtil.OnDownloadListener listener) {
        this.audioUrl = audioUrl;
        this.mContext = mContext;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        String path = mContext.getFilesDir() + "/downloadaudio";
        DownloadUtil instance = DownloadUtil.getInstance();
        if (FileUtils.fileIsExist(path)){
            instance.download("http://staticvip."+ CommonConstant.domain +"/sounds/minutes/"+audioUrl,path,listener);
        }

    }
}
