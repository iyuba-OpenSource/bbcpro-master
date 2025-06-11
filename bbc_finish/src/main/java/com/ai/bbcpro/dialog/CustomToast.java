package com.ai.bbcpro.dialog;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

//    private static Toast mToast;
//    private static Handler mHandler = new Handler();
//    private static Runnable r = new Runnable() {
//        @Override
//        public void run() {
//            mToast.cancel();
//        }
//    };

    public static void showToast(Context mContext, String text, int duration) {
        int time = Toast.LENGTH_SHORT;
        if (duration > 1000) {
            time = Toast.LENGTH_LONG;
        }
        Toast.makeText(mContext, text, time).show();

//        mHandler.removeCallbacks(r);
//        if (mToast != null) {
//            mToast.setText(text);
//        } else {
//            mToast = Toast.makeText(mContext, text, duration);
//        }
//        mHandler.postDelayed(r, duration);
//
//        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        int time = Toast.LENGTH_SHORT;
        if (duration > 1000) {
            time = Toast.LENGTH_LONG;
        }
        Toast.makeText(mContext, resId, time).show();
//        showToast(mContext, mContext.getResources().getString(resId), duration);
    }

    public static void showToast(Context mContext, int resId) {
        Toast.makeText(mContext, resId, Toast.LENGTH_SHORT).show();
//        showToast(mContext, mContext.getResources().getString(resId), Toast.LENGTH_SHORT);
    }

    public static void showToast(Context mContext, String text) {
        Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
//        showToast(mContext, text, Toast.LENGTH_SHORT);
    }

}

