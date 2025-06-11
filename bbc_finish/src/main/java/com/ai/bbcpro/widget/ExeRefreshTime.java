package com.ai.bbcpro.widget;

import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.manager.RuntimeManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExeRefreshTime {
    public static String lastRefreshTime(String source) {
        String lastTime = ConfigManager.Instance().loadString(source);
        if (lastTime == null || lastTime.equals("")) {
            lastTime = RuntimeManager.getContext().getString(
                    R.string.never_update);
        }
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        ConfigManager.Instance().putString(source,
                formatter.format(currentTime));
        return lastTime;
    }
}
