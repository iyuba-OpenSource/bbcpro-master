package com.ai.bbcpro.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.activity.login.LoginActivity;
import com.ai.bbcpro.ui.activity.login.RegisterForSecVerifyActivity;
import com.ai.bbcpro.ui.bean.SecondCheckBean;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.event.SecVerifyLoginSuccessEvent;
import com.ai.bbcpro.ui.http.HttpHelper;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;
import com.mob.secverify.SecVerify;
import com.mob.secverify.VerifyCallback;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.UiSettings;
import com.mob.secverify.datatype.VerifyResult;
import com.mob.secverify.ui.component.CommonProgressDialog;
import com.sd.iyu.training_camp.TCApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.net.URLEncoder;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SecVerifyManager {

    private static boolean isFail = false;

    public static SecVerifyManager getInstance() {
        return InnerClass.sInstance;
    }

    private SecVerifyManager() {
    }

    private static class InnerClass {
        private static SecVerifyManager sInstance = new SecVerifyManager();
    }

    private static final String TAG = SecVerifyManager.class.getSimpleName();

    public void verify(Context context) {

        if (!AccountManager.Instance(context).checkUserLogin()) {

            isFail = false;

            UiSettings.Builder builder = new UiSettings.Builder();
            builder.setSwitchAccText("其他方式登录");
            builder.setLoginBtnTextSize(14);

            builder.setImmersiveTheme(true);
            builder.setImmersiveStatusTextColorBlack(true);
            builder.setCusAgreementNameId1("《用户协议》");
            String url_Agreement = BuildConfig.PROTOCOL_USE;
            builder.setCusAgreementUrl1(url_Agreement);
            builder.setCusAgreementNameId2("《隐私协议》");
            String url_PrivacyPolicy = BuildConfig.PROTOCOL_PRI;
            builder.setCusAgreementUrl2(url_PrivacyPolicy);

            SecVerify.setUiSettings(builder.build());

            SecVerify.verify(new VerifyCallback() {
                @Override
                public void onOtherLogin() {
                    CommonProgressDialog.dismissProgressDialog();

                    SecVerify.finishOAuthPage();
                    Intent intent = new Intent();
                    intent.setClass(context, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                @Override
                public void onUserCanceled() {
                    CommonProgressDialog.dismissProgressDialog();
                    // 用户点击“关闭按钮”或“物理返回键”取消登录，处理自己的逻辑
                    SecVerify.finishOAuthPage();
                }

                @Override
                public void onComplete(VerifyResult verifyResult) {
                    SettingConfig.Instance().setAutoLogin(true);
                    CommonProgressDialog.dismissProgressDialog();

                    String url = "http://api." + CommonConstant.domainLong + "/v2/api.iyuba?protocol=10010";
                    RequestBody body = new FormBody.Builder()
                            .add("appkey", Constant.SMSAPPID)
                            .add("token", URLEncoder.encode(verifyResult.getToken()))
                            .add("opToken", verifyResult.getOpToken())
                            .add("operator", verifyResult.getOperator())
                            .add("appId", Constant.APPID)
                            .build();
                    Request request = new Request.Builder().post(body).url(url).build();
                    HttpHelper.getClient().newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(okhttp3.Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                            Looper.prepare();
//                        JSONObject jo = JSONObject.parseObject(response.body().string());
//                        Log.d(TAG, "onResponse: -------秒验 iyuba服务器返回 " + jo.toString());
                            if (response.isSuccessful()) {

                                SecondCheckBean checkBean = new Gson().fromJson(response.body().string(), SecondCheckBean.class);
                                if (checkBean.getIsLogin().equals("1")) {
                                    if (checkBean.getUserinfo().getResult().equals("101")) {
                                        Log.d(TAG, "onResponse: " + checkBean.getUserinfo().toString());
                                        com.ai.bbcpro.http.protocol.LoginResponse tmpResp = new com.ai.bbcpro.http.protocol.LoginResponse();
                                        SecondCheckBean.UserinfoBean userMsg = checkBean.getUserinfo();

                                        tmpResp.amount = userMsg.getAmount();
                                        tmpResp.imgsrc = userMsg.getImgSrc();
                                        tmpResp.isteacher = userMsg.getIsteacher();
                                        tmpResp.money = userMsg.getMoney();
                                        tmpResp.result = userMsg.getResult();
                                        tmpResp.uid = String.valueOf(userMsg.getUid());
                                        tmpResp.username = userMsg.getUsername();
                                        tmpResp.vipStatus = userMsg.getVipStatus();
                                        tmpResp.validity = String.valueOf(userMsg.getExpireTime());
                                        tmpResp.expireTime = userMsg.getExpireTime() + "";
                                        tmpResp.mobile = userMsg.getMobile();

                                        //训练营
                                        TCApplication.setUserid(userMsg.getUid() + "");
                                        TCApplication.setVipStatus(userMsg.getVipStatus());

                                        HeadlinesDataManager.getInstance(MainApplication.getApplication()).resetProgress();
                                        AccountManager.Instance(context).Refresh(tmpResp);
                                        EventBus.getDefault().post(new SecVerifyLoginSuccessEvent());
                                        ToastUtil.showToast(context, "登录成功");
                                        SecVerify.finishOAuthPage();
                                    }

                                } else if (checkBean.getIsLogin().equals("0")) {
                                    String phone = checkBean.getRes().getPhone();
                                    RegisterForSecVerifyActivity.start("iyuba"
                                            + (int) (Math.random() * 9000 + 1000)
                                            + phone.substring(phone.length() - 4), phone.substring(phone.length() - 6), phone, context);
                                    ToastUtil.showToast(context, "当前手机号尚未注册！");
                                    SecVerify.finishOAuthPage();
                                }
                            } else {
                                ToastUtil.showToast(context, response.message());
                            }
                            Looper.loop();
                        }
                    });


                }

                @Override
                public void onFailure(VerifyException e) {
                    SecVerify.finishOAuthPage();
//                    ToastUtil.showToast(context, "运营商网络连接失败，请稍后重试或者手动登录");
                    CommonProgressDialog.dismissProgressDialog();
                    if (!isFail) {

                        Intent intent = new Intent(MainApplication.getApplication(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        isFail = true;
                    }
                }
            });
        }

    }

}
