package com.ai.bbcpro.manager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

public class RuntimeManager {
    private static Application mApplication;
    private static DisplayMetrics displayMetrics;
    private static Context mContext;

    public static void setApplicationContext(Context context){
        mContext = context;
    }

    public static DisplayMetrics getDisplayMetrics(){
        return displayMetrics;
    }

    public static Application getApplication() {
        return mApplication;
    }

    public static void setApplication(Application application){
        mApplication = application;
        mContext =application.getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }

    public static String getString(int resourcesID){
        return mApplication.getResources().getString(resourcesID);
    }

    public static Object getSystemService(String ServiceName){
        return mApplication.getSystemService(ServiceName);
    }

    public static void setDisplayMetrics(Activity activity){
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    }

    public static void setDisplayMetrics(){
        displayMetrics = mContext.getResources().getDisplayMetrics();
    }

    public static int getWindowWidth(){
        return displayMetrics.widthPixels;
    }

    public static int getWindowHeight(){
        return displayMetrics.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        float scale = displayMetrics.density;
        return (int) (dpValue * scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        float scale = displayMetrics.density;
        return (int) (pxValue / scale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 sp
     */
    public static int px2sp(float pxValue) {
        float fontScale = displayMetrics.scaledDensity;
        return (int) (pxValue / fontScale + 0.5F);
    }

    /**
     * 根据手机的分辨率从 sp 的单位 转成为 px(像素)
     */
    public static int sp2px(float spValue) {
        float fontScale = displayMetrics.scaledDensity;
        return (int) (spValue * fontScale + 0.5F);
    }

}
