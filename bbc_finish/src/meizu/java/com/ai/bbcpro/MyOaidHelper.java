package com.ai.bbcpro;

import android.content.Context;
import android.util.Log;

import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
* Date: 16:27 2021/2/26 0026
 * Version: 1.0.3
**/
public class MyOaidHelper implements IIdentifierListener {

    public static final String TAG = "DemoHelper";
    public static final int HELPER_VERSION_CODE = 20220815; // DemoHelper版本号
    private final AppIdsUpdater appIdsUpdater;
    private boolean isCertInit = false;

    public boolean isSDKLogOn = true;          // TODO （1）设置 是否开启sdk日志
    public static final String ASSET_FILE_NAME_CERT = "com.ai.bbepro.cert.pem";  // TODO （2）设置 asset证书文件名

    public MyOaidHelper(AppIdsUpdater appIdsUpdater){
        // TODO （3）加固版本在调用前必须载入SDK安全库,因为加载有延迟，推荐在application中调用loadLibrary方法
        //        System.loadLibrary("msaoaidsec");
        // DemoHelper版本建议与SDK版本一致
        if(MdidSdkHelper.SDK_VERSION_CODE != HELPER_VERSION_CODE){
            Log.w(TAG,"SDK version not match.");
        }
        this.appIdsUpdater = appIdsUpdater;
    }

    public void getDeviceIds(Context cxt){
        getDeviceIds(cxt, true, false, false);
    }



    /**
     * 获取OAID
     * @param cxt
     */
    public void getDeviceIds(Context cxt,boolean isGetOAID,boolean isGetVAID,boolean isGetAAID){
        // TODO （4）初始化SDK证书
        if(!isCertInit){ // 证书只需初始化一次
            // 证书为PEM文件中的所有文本内容（包括首尾行、换行符）
            try {
                isCertInit = MdidSdkHelper.InitCert(cxt, loadPemFromAssetFile(cxt, ASSET_FILE_NAME_CERT));
            } catch (Error e) {
                e.printStackTrace();
            }
            if(!isCertInit){
                Log.w(TAG, "getDeviceIds: cert init failed");
            }
        }

        //（可选）设置InitSDK接口回调超时时间(仅适用于接口为异步)，默认值为5000ms.
        // 注：请在调用前设置一次后就不再更改，否则可能导致回调丢失、重复等问题
        try {
            MdidSdkHelper.setGlobalTimeout(5000);
        } catch (Error error) {
            error.printStackTrace();
        }
        int code = 0;
        // TODO （5）调用SDK获取ID
        try {
            code = MdidSdkHelper.InitSdk(cxt, isSDKLogOn, isGetOAID, isGetVAID, isGetAAID, this);
        } catch (Error error) {
            error.printStackTrace();
        }

        // TODO （6）根据SDK返回的code进行不同处理
/*        IdSupplierImpl unsupportedIdSupplier = new IdSupplierImpl();
        if(code == InfoCode.INIT_ERROR_CERT_ERROR){                         // 证书未初始化或证书无效，SDK内部不会回调onSupport
            // APP自定义逻辑
            Log.w(TAG,"cert not init or check not pass");
            onSupport(unsupportedIdSupplier);
        }else if(code == InfoCode.INIT_ERROR_DEVICE_NOSUPPORT){             // 不支持的设备, SDK内部不会回调onSupport
            // APP自定义逻辑
            Log.w(TAG,"device not supported");
            onSupport(unsupportedIdSupplier);
        }else if( code == InfoCode.INIT_ERROR_LOAD_CONFIGFILE){            // 加载配置文件出错, SDK内部不会回调onSupport
            // APP自定义逻辑
            Log.w(TAG,"failed to load config file");
            onSupport(unsupportedIdSupplier);
        }else if(code == InfoCode.INIT_ERROR_MANUFACTURER_NOSUPPORT){      // 不支持的设备厂商, SDK内部不会回调onSupport
            // APP自定义逻辑
            Log.w(TAG,"manufacturer not supported");
            onSupport(unsupportedIdSupplier);
        }else if(code == InfoCode.INIT_ERROR_SDK_CALL_ERROR){             // sdk调用出错, SSDK内部不会回调onSupport
            // APP自定义逻辑
            Log.w(TAG,"sdk call error");
            onSupport(unsupportedIdSupplier);
        } else if(code == InfoCode.INIT_INFO_RESULT_DELAY) {             // 获取接口是异步的，SDK内部会回调onSupport
            Log.i(TAG, "result delay (async)");
        }else if(code == InfoCode.INIT_INFO_RESULT_OK){                  // 获取接口是同步的，SDK内部会回调onSupport
            Log.i(TAG, "result ok (sync)");
        }else {
            // sdk版本高于DemoHelper代码版本可能出现的情况，无法确定是否调用onSupport
            // 不影响成功的OAID获取
            Log.w(TAG,"getDeviceIds: unknown code: " + code);
        }*/
    }

    /**
     * APP自定义的getDeviceIds(Context cxt)的接口回调
     * @param supplier
     */
    @Override
    public void onSupport(IdSupplier supplier) {
        if(supplier==null) {
            Log.w(TAG, "onSupport: supplier is null");
            return;
        }
        if(appIdsUpdater ==null) {
            Log.w(TAG, "onSupport: callbackListener is null");
            return;
        }
        // 获取Id信息
        // 注：IdSupplier中的内容为本次调用MdidSdkHelper.InitSdk()的结果，不会实时更新。 如需更新，需调用MdidSdkHelper.InitSdk()
        boolean isSupported = supplier.isSupported();
        boolean isLimited  = supplier.isLimited();
        String oaid=supplier.getOAID();
        String vaid=supplier.getVAID();
        String aaid=supplier.getAAID();

        //TODO (7) 自定义后续流程，以下显示到UI的示例
        String idsText= "support: " + (isSupported ? "true" : "false") +
                "\nlimit: " + (isLimited ? "true" : "false") +
                "\nOAID: " + oaid +
                "\nVAID: " + vaid +
                "\nAAID: " + aaid + "\n";
        Log.d(TAG, "onSupport: ids: \n" + idsText);
        appIdsUpdater.onIdsValid(oaid);
    }

    public interface AppIdsUpdater{
        void onIdsValid(String ids);
    }

    /**
     * 从asset文件读取证书内容
     * @param context
     * @param assetFileName
     * @return 证书字符串
     */
    public static String loadPemFromAssetFile(Context context, String assetFileName){
        try {
            InputStream is = context.getAssets().open(assetFileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null){
                builder.append(line);
                builder.append('\n');
            }
            return builder.toString();
        } catch (IOException e) {
            Log.e(TAG, "loadPemFromAssetFile failed");
            return "";
        }
    }
}

