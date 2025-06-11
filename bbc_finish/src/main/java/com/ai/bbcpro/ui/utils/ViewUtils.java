package com.ai.bbcpro.ui.utils;

import android.view.View;

public class ViewUtils {
    public static void bindOnClickListener(View.OnClickListener listener, View... views) {
        for (View view : views) {
            view.setOnClickListener(listener);
        }
    }

    public static void gone(View... views) {
        for (View view : views) {
            view.setVisibility(View.GONE);
        }
    }

    public static void visible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }
}
