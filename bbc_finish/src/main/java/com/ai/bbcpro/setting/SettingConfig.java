package com.ai.bbcpro.setting;

import android.content.Context;
import android.util.Log;

import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.manager.ConfigManager;

public class SettingConfig {
    private volatile static SettingConfig instance;
    private boolean highspeed;
    private boolean syncho;
    private boolean autoLogin;
    private boolean autoPlay;
    private boolean autoStop;
    private boolean light;
    private boolean push;
    private boolean night;
    private boolean auto_download;
    private final static String LIGHT_TAG = "light";
    private final static String PUSH_TAG = "push";
    private final static String NIGHT_TAG = "night";
    private final static String HIGH_SPEED_LIT_TAG = "highspeed";
    private final static String SYNCHO_TAG = "syncho";
    private final static String AUTO_LOGIN_TAG = "autoLogin";
    private final static String AUTO_PLAY_TAG = "autoplay";
    private final static String AUTO_STOP_TAG = "autostop";
    private final static String AUTO_DOWNLOAD_TAG = "autoDownload";

    private SettingConfig() {

    }

    public static SettingConfig Instance() {

        if (instance == null) {
            synchronized (SettingConfig.class) {
                if (instance == null) {
                    instance = new SettingConfig();
                }
            }

        }
        return instance;
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isLight() {
        try {
            light = ConfigManager.Instance().loadBoolean(LIGHT_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            light = true;
        }
        return light;
    }

    /**
     * 设置屏幕常亮
     */
    public void setLight(boolean light) {
        ConfigManager.Instance().putBoolean(LIGHT_TAG, light);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isPush() {
        try {
            push = ConfigManager.Instance().loadBoolean(PUSH_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            push = true;
        }
        return push;
    }

    /**
     * 设置屏幕常亮
     */
    public void setPush(boolean push) {
        ConfigManager.Instance().putBoolean(PUSH_TAG, push);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isNight() {
        try {
            night = ConfigManager.Instance().loadBoolean(NIGHT_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            night = true;
        }
        return night;
    }

    /**
     * 设置屏幕常亮
     */
    public void setNight(boolean night) {
        ConfigManager.Instance().putBoolean(NIGHT_TAG, night);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isDownload() {
        try {
            auto_download = ConfigManager.Instance().loadBoolean(
                    AUTO_DOWNLOAD_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            auto_download = true;
        }
        return auto_download;
    }

    /**
     * 设置屏幕常亮
     */
    public void setAutoDownload(boolean download) {
        ConfigManager.Instance().putBoolean(AUTO_DOWNLOAD_TAG, download);
    }

    /**
     * 获取是否播放时屏幕常量
     *
     * @return
     */
    public boolean isHighSpeed() {
        try {
            highspeed = ConfigManager.Instance()
                    .loadBoolean(HIGH_SPEED_LIT_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            highspeed = true;
        }
        return highspeed;
    }

    /**
     * 设置屏幕常亮
     */
    public void setHighSpeed(boolean screenLit) {
        ConfigManager.Instance().putBoolean(HIGH_SPEED_LIT_TAG, screenLit);
    }

    /**
     * 获取是否播放时同步
     *
     * @return
     */
    public boolean isSyncho() {
        try {
            syncho = ConfigManager.Instance().loadBoolean(SYNCHO_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            syncho = true;
        }
        return syncho;
    }

    /**
     * 设置屏幕常亮
     */
    public void setSyncho(boolean syncho) {
        ConfigManager.Instance().putBoolean(SYNCHO_TAG, syncho);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoLogin() {
        autoLogin = ConfigManager.Instance().loadBoolean(AUTO_LOGIN_TAG);
        Log.d("测试", "isAutoLogin:用户登录取出表示 " + autoLogin);
        return autoLogin;
    }

    /**
     * 设置是否自动登录
     */
    public void setAutoLogin(boolean AutoLogin) {
        ConfigManager.Instance().putBoolean(AUTO_LOGIN_TAG, AutoLogin);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoPlay() {
        try {
            autoPlay = ConfigManager.Instance().loadBoolean(AUTO_PLAY_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            autoPlay = false;
        }
        return autoPlay;
    }

    /**
     * 设置是否自动登录
     */
    public void setAutoPlay(boolean AutoPlay) {
        ConfigManager.Instance().putBoolean(AUTO_PLAY_TAG, AutoPlay);
    }

    /**
     * 获取是否自动登录
     *
     * @return
     */
    public boolean isAutoStop() {
        try {
            autoStop = ConfigManager.Instance().loadBoolean(AUTO_STOP_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            autoStop = false;
        }
        return autoStop;
    }

    /**
     * 设置是否自动登录
     */
    public void setAutoStop(boolean AutoStop) {
        ConfigManager.Instance().putBoolean(AUTO_STOP_TAG, AutoStop);
    }

    /**
     * 设置临时登录的状态
     */
    public void setIstour(Boolean isTourist) {
        setTourist(isTourist);
    }

    //获取到临时登录的状态
    public Boolean getIstour() {
        return isTourist();
    }

    public static void setTourist(Boolean isTourist) {
        ConfigManager.Instance().putBoolean("Istour", isTourist);
    }

    public static boolean isTourist() {
        return ConfigManager.Instance().loadBoolean("Istour");
    }

    public void setTouristLogout(boolean value) {
        ConfigManager.Instance().putBoolean("touristLogout", value);
    }

    public boolean isTouristLogout() {
        return ConfigManager.Instance().loadBoolean("touristLogout");
    }

    public static void showTouristHint(Context context) {
        CustomToast.showToast(context, "请注册正式账户后再进行操作");
    }


    /**
     * 设置临时用户是否有缓存
     */
    public void setIsCaChe(Boolean b) {
        ConfigManager.Instance().putBoolean("IsCaChe", b);
    }

    public Boolean getIsCaChe() {
        return ConfigManager.Instance().loadBoolean("IsCaChe");
    }

    //设置两个Fragement之间互不显示的标识
    public void setIsHint(boolean b) {
        ConfigManager.Instance().putBoolean("F", b);
    }

    public Boolean getIsHint() {
        return ConfigManager.Instance().loadBoolean("F");
    }
}
