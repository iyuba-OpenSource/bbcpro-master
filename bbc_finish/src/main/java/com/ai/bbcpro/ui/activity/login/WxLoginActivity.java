package com.ai.bbcpro.ui.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.model.bean.WxLoginBean;
import com.ai.bbcpro.mvp.presenter.login.WxLoginPresenter;
import com.ai.bbcpro.mvp.view.login.WxLoginContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.util.PackageUtil;
import com.iyuba.imooclib.ui.web.Web;
import com.iyuba.module.toolbox.MD5;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.sd.iyu.training_camp.TCApplication;
import com.sd.iyu.training_camp.util.popup.LoadingPopup;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * 微信一键登录
 */
public class WxLoginActivity extends AppCompatActivity implements WxLoginContract.WxLoginView {

    private LinearLayout wxlogin_ll_login;

    private TextView wxlogin_but_pswlogin;

    //隐私
    private RadioButton wxlogin_rb_pri;
    private LinearLayout wxlogin_ll_pri;
    private TextView wxlogin_tv_pri;

    private WxLoginPresenter wxLoginPresenter;

    private WxLoginBean wxLoginBeanToken;

    private LoadingPopup loadingPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login);

        View decor = getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        wxLoginPresenter = new WxLoginPresenter();
        wxLoginPresenter.attchView(this);

        //显示app 的名称
        TextView wxlogin_tv_name = findViewById(R.id.wxlogin_tv_name);
        wxlogin_tv_name.setText(PackageUtil.getAppName(WxLoginActivity.this));

        initOperation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (wxLoginPresenter != null) {

            wxLoginPresenter.detachView();
        }
    }

    private void initOperation() {

        wxlogin_ll_login = findViewById(R.id.wxlogin_ll_login);
        wxlogin_ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!wxlogin_rb_pri.isChecked()) {

                    toast("请勾选服务协议和隐私协议");
                    return;
                }

                showLoading("正在登录...");
                String sign = MD5.getMD5ofStr(10011 + Constant.APPID + "iyubaV2");
                wxLoginPresenter.getWxAppletToken("android", "json", "10011", Constant.APPID, sign);
            }
        });
        //密码登录
        wxlogin_but_pswlogin = findViewById(R.id.wxlogin_but_pswlogin);
        wxlogin_but_pswlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(WxLoginActivity.this, LoginActivity.class));
                finish();
            }
        });
        //隐私协议
        wxlogin_rb_pri = findViewById(R.id.wxlogin_rb_pri);
        wxlogin_ll_pri = findViewById(R.id.wxlogin_ll_pri);
        wxlogin_ll_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (wxlogin_rb_pri.isChecked()) {
                    wxlogin_rb_pri.setChecked(false);
                } else {

                    wxlogin_rb_pri.setChecked(true);
                }
            }
        });
        wxlogin_tv_pri = findViewById(R.id.wxlogin_tv_pri);
        wxlogin_tv_pri.setMovementMethod(LinkMovementMethod.getInstance());

        String priStr = "登录即代表同意爱语吧的服务协议和隐私政策";
        SpannableString spannableString = new SpannableString(priStr);

        String url_Agreement = Constant.PROTOCOLUSE;
        ClickableSpan spanAgreement = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorPrimary));//设置超链接的颜色
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                // 单击事件处理
                startActivity(Web.buildIntent(WxLoginActivity.this, url_Agreement, "用户协议"));
            }
        };

        String url_PrivacyPolicy = Constant.PROTOCOLPRI;
        ClickableSpan spanPrivacyPolicy = new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.colorPrimary));//设置超链接的颜色
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                // 单击事件处理
                startActivity(Web.buildIntent(WxLoginActivity.this, url_PrivacyPolicy, "隐私政策"));
            }
        };
        spannableString.setSpan(spanAgreement, priStr.indexOf("服务协议"),
                priStr.indexOf("服务协议") + "服务协议".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(spanPrivacyPolicy, priStr.indexOf("隐私政策"),
                priStr.indexOf("隐私政策") + "隐私政策".length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        wxlogin_tv_pri.setText(spannableString);

    }

    @Override
    public void showLoading(String msg) {

        if (loadingPopup == null) {

            loadingPopup = new LoadingPopup(WxLoginActivity.this);
        }
        loadingPopup.setContent(msg);
        loadingPopup.showPopupWindow();
    }

    @Override
    public void hideLoading() {

        if (loadingPopup != null) {

            loadingPopup.dismiss();
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getWxAppletToken(WxLoginBean wxLoginBean) {

        IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WECHAT_APP_KEY);
        if (!api.isWXAppInstalled()) {
            toast("您还未安装微信客户端");
            return;
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_a8c91d1317ce"; // 填小程序原始id
        req.path = "/template/subpage/getphone/getphone?token=" + wxLoginBean.getToken() + "&appid=" + Constant.APPID;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
        this.wxLoginBeanToken = wxLoginBean;
    }

    @Override
    public void getUidByToken(WxLoginBean wxLoginBean) {


        String sign = MD5.getMD5ofStr("20001" + wxLoginBean.getUid() + "iyubaV2");
        wxLoginPresenter.getUserInfo("android", "json", Constant.APPID,
                "20001", wxLoginBean.getUid() + "", wxLoginBean.getUid() + "", sign);
    }

    @Override
    public void getUserInfo(MoreInfoBean infobean, String uid) {


        ConfigManager.Instance().putString("userId", uid);
        ConfigManager.Instance().putString("expireTime", infobean.getExpireTime() + "");
        ConfigManager.Instance().putString("userName", infobean.getUsername());
        if (infobean.getVipStatus().equals("0")) {

            ConfigManager.Instance().putBoolean("isvip", false);
        } else {
            ConfigManager.Instance().putBoolean("isvip", true);
        }
        ConfigManager.Instance().putInt("isvip2", Integer.parseInt(infobean.getVipStatus()));


        IyuUserManager iyuUserManager = IyuUserManager.getInstance();
        User user = new User();
        user.uid = Integer.parseInt(uid);
        user.name = infobean.getUsername();
        user.nickname = infobean.getNickname();
        user.vipStatus = infobean.getVipStatus();
        iyuUserManager.setCurrentUser(user);

        //传值给训练营
        TCApplication.setUserid(uid);
        TCApplication.setVipStatus(infobean.getVipStatus() + "");

        //EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (wxLoginBeanToken != null) {

                    String sign = MD5.getMD5ofStr("10016" + Constant.APPID + wxLoginBeanToken.getToken() + "iyubaV2");
                    wxLoginPresenter.getUidByToken("android", "json", "10016", wxLoginBeanToken.getToken(), sign, Constant.APPID);
                }
            }
        }, 800);
    }
}