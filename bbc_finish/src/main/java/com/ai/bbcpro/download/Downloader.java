package com.ai.bbcpro.download;

public interface Downloader {
    public interface OnDownloaderDestroyedListener {
        void onDestroyed(String key, Downloader downloader);
    }

    boolean isRunning();

    void start();

    void pause();

    void cancel();

    void onDestroy();
}
