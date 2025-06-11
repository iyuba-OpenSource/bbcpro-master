package com.ai.bbcpro.ui.utils;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String text) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Looper.prepare();
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }).start();
    }

    public static void showLongToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();

//        if (toast == null) {
//            //如果等于null，则创建
//        } else {
//            //如果不等于空，则直接将text设置给toast
//            toast.setText(text);
//        }
//        toast.show();
    }


}
