package com.ai.bbcpro.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageUtil {

    /**
     * 获取app的名称
     *
     * @param context
     * @return
     */
    public static String getAppName(Context context) {
        String name = "";
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
            name = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }
}
