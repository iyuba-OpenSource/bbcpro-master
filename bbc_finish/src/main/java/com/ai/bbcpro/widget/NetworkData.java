package com.ai.bbcpro.widget;



import android.app.Application;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ai.bbcpro.manager.RuntimeManager;


public class NetworkData {


    public static String sessionId;

    public static String accessPoint = BaseProtocolDef.getXmlAbsoluteURI();

    public static String mapDownloadUrl;
    public static String cellIDUrl;
    public static String tempImageUploadUrl;
    public static String offsetLocationUrl;
    public static String besideIconDownloadUrl;
    public static String pulseCheckUrl;
    public static String trafficUrl;

    public static String handpodHeart = "www.surfingo.net";
    public static String googleHost = "www.google.com.hk";

    public static String SMD = "";
    public static String PhoneNum = "";
    public static String IMEI = "";
    public static String IMSI = "";

    public static String removeString = "_60x46_86x66_120x92";
    public static String replaceString = "_120x92";

    // public static String userName = BaseProtocolDef.defGuestName;
    // public static String passWord = BaseProtocolDef.defGuestPassword;

    // public static int Width;
    // public static int Height;

    // public static Application application;

    // public static void Init() {
    // TelephonyManager telMgr = (TelephonyManager)
    // RuntimeManager.getApplication()
    // .getSystemService(Application.TELEPHONY_SERVICE);
    // if (telMgr.getDeviceId() != null) {
    // IMEI = telMgr.getDeviceId();
    // }
    // if (telMgr.getSubscriberId() != null) {
    // IMSI = telMgr.getSubscriberId();
    // }
    //
    // if (telMgr.getSimSerialNumber() != null) {
    // SMD = telMgr.getSimSerialNumber();
    // } else {
    // SMD = telMgr.getDeviceId();
    // }
    //
    // PhoneNum = telMgr.getLine1Number();
    // }

    public static NetworkInfo getNetworkInfo() {
        if(RuntimeManager.getApplication()!=null){
            return ((ConnectivityManager) RuntimeManager.getApplication()
                    .getSystemService(Application.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
        }
        return null;
    }
}
