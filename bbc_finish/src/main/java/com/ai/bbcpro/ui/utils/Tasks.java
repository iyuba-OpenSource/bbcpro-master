package com.ai.bbcpro.ui.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public final class Tasks {

    private static Handler sMainHandler;

    private static final Object S_LOCK = new Object();

    private static Handler sThreadHandler;

    /**
     * @param r
     * @return
     */
    public static boolean post2UI(Runnable r) {
        return sMainHandler.post(r);
    }

    /**
     * @param r
     * @param delayMillis
     * @return
     */
    public static boolean postDelayed2UI(Runnable r, long delayMillis) {
        return sMainHandler.postDelayed(r, delayMillis);
    }

    /**
     * 取消UI线程任务
     * @param r
     */
    public static final void cancelUITask(Runnable r) {
        initThread();
        sMainHandler.removeCallbacks(r);
    }

    /**
     * @param r
     * @return
     */
    public static boolean post2Thread(Runnable r) {
        initThread();
        return sThreadHandler.post(r);
    }

    /**
     * @param r
     * @param delayMillis
     * @return
     */
    public static boolean postDelayed2Thread(Runnable r, long delayMillis) {
        initThread();
        return sThreadHandler.postDelayed(r, delayMillis);
    }

    /**
     * 取消后台线程任务
     * @param r
     */
    public static void cancelThreadTask(Runnable r) {
        initThread();
        sThreadHandler.removeCallbacks(r);
    }

    /**
     * @hide 内部接口
     */
    public static void init() {
        sMainHandler = new Handler(Looper.getMainLooper());
    }

    private static void initThread() {
        synchronized (S_LOCK) {
            if (sThreadHandler == null) {
                HandlerThread t = new HandlerThread("daemon-handler-thread");
                t.start();
                sThreadHandler = new Handler(t.getLooper());
            }
        }
    }
}

