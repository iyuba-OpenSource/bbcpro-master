package com.ai.bbcpro.newSection;


import android.content.Context;
import androidx.annotation.StringRes;
import android.widget.Toast;

/**
 * Toast
 *
 * @author wayne
 * @date 2017/11/18
 */
public class T {
    public static void showShort(Context context, @StringRes int msgId) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, @StringRes int msgId) {
        if (context == null) {
            return;
        }
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String msg) {
        if (msg == null || context == null) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String msg) {
        if (msg == null || context == null) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
