package com.ai.bbcpro.ui.helper;

import android.content.Context;
import android.util.Log;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.Constant;
import com.ai.common.CommonConstant;
import com.iyuba.share.ShareExecutor;
import com.iyuba.share.mob.MobShareExecutor;
import com.mob.MobSDK;
import com.mob.secverify.PreVerifyCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.common.exception.VerifyException;

import java.util.HashMap;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.favorite.WechatFavorite;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class ShareHelper {
    public static void init(Context appContext) {
        MobSDK.init(appContext, Constant.SMSAPPID, Constant.SMSAPPSECRET);

        MobSDK.submitPolicyGrantResult(true);
        preVerify();
        Log.e("MobSDK", "init: ");
        initPlatforms();

        MobShareExecutor executor = new MobShareExecutor();
        ShareExecutor.getInstance().setRealExecutor(executor);
    }

    public static void preVerify() {
        SecVerify.setTimeOut(10000);
        SecVerify.preVerify(new PreVerifyCallback() {
            @Override
            public void onComplete(Void unused) {
                Constant.sPreVerifySuccess = true;
            }

            @Override
            public void onFailure(VerifyException e) {
                int errCode = e.getCode();     //获取错误码
                String errMsg = e.getMessage();     //获取SDK返回的错误信息
                // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
                Throwable t = e.getCause();  //获取运营商返回的错误信息
                Log.e("pre", "onFailure: " + errCode + "-" + errMsg + "-");
                Constant.sPreVerifySuccess = false;

            }
        });
    }

    private static void initPlatforms() {

        String wechatAppId = Constant.WECHAT_APP_KEY;
        String wechatAppSecret = Constant.WECHAT_APP_SECRET;
        String qqId = BuildConfig.QQ_APP_KEY;
        String qqKey = BuildConfig.QQ_APP_SECRET;
        String sinaKey = Constant.SINA_APP_KEY;
        String sinaSecret = Constant.SINA_APP_SECRET;

        if (!BuildConfig.FLAVOR.equals("meizu") && !BuildConfig.APPLICATION_ID.equals("com.ai.bbcpro")) {

            setDevInfo(QQ.NAME, qqId, qqKey);
            setDevInfo(QZone.NAME, qqId, qqKey);
        }
        setDevInfo(SinaWeibo.NAME, sinaKey, sinaSecret);
        setDevInfo(Wechat.NAME, wechatAppId, wechatAppSecret);
        setDevInfo(WechatMoments.NAME, wechatAppId, wechatAppSecret);
        setDevInfo(WechatFavorite.NAME, wechatAppId, wechatAppSecret);
    }

    private static void setDevInfo(String platform, String str1, String str2) {
        HashMap<String, Object> devInfo = new HashMap<>();
        if (SinaWeibo.NAME.equals(platform)) {
            devInfo.put("Id", "1");
            devInfo.put("SortId", "1");
            devInfo.put("AppKey", str1);
            devInfo.put("AppSecret", str2);
            devInfo.put("Enable", "true");
            devInfo.put("RedirectUrl", "http://" + CommonConstant.domain);
            devInfo.put("ShareByAppClient", "true");
        } else if (QQ.NAME.equals(platform)) {

            devInfo.put("Id", "2");
            devInfo.put("SortId", "2");
            devInfo.put("AppId", str1);
            devInfo.put("AppKey", str2);
            devInfo.put("Enable", "true");
            devInfo.put("ShareByAppClient", "true");
        } else if (QZone.NAME.equals(platform)) {

            devInfo.put("Id", "3");
            devInfo.put("SortId", "3");
            devInfo.put("AppId", str1);
            devInfo.put("AppKey", str2);
            devInfo.put("Enable", "true");
            devInfo.put("ShareByAppClient", "true");
        } else if (Wechat.NAME.equals(platform)) {
            devInfo.put("Id", "4");
            devInfo.put("SortId", "4");
            devInfo.put("AppId", str1);
            devInfo.put("AppSecret", str2);
            devInfo.put("Enable", "true");
            devInfo.put("BypassApproval", "false");
        } else if (WechatMoments.NAME.equals(platform)) {
            devInfo.put("Id", "5");
            devInfo.put("SortId", "5");
            devInfo.put("AppId", str1);
            devInfo.put("AppSecret", str2);
            devInfo.put("Enable", "true");
            devInfo.put("BypassApproval", "false");
        } else if (WechatFavorite.NAME.equals(platform)) {
            devInfo.put("Id", "6");
            devInfo.put("SortId", "6");
            devInfo.put("AppId", str1);
            devInfo.put("AppSecret", str2);
            devInfo.put("Enable", "true");
        }
        ShareSDK.setPlatformDevInfo(platform, devInfo);
    }
}
