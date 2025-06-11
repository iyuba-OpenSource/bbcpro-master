package com.ai.bbcpro.ui.adapter;

public interface DownloadNewsCallback {
    void download(String bbcId,String imageUrl,String audioUrl,int position);
}
