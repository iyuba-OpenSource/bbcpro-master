package com.ai.bbcpro.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;

import com.ai.bbcpro.SplashActivity;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.sqlite.listener.OperateCallBack;
import com.ai.bbcpro.ui.activity.MyWebActivity;
import com.ai.bbcpro.ui.event.RefreshPersonalEvent;
import com.ai.bbcpro.ui.event.SecVerifyLoginSuccessEvent;
import com.ai.bbcpro.ui.helper.SecVerifyManager;
import com.ai.common.CommonConstant;
import com.iyuba.module.user.IyuUserManager;
import com.mob.secverify.GetTokenCallback;
import com.mob.secverify.PageCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.common.exception.VerifyException;
import com.mob.secverify.datatype.VerifyResult;
import com.mob.secverify.ui.component.CommonProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginActivity extends AppCompatActivity {

    protected Context mContext;
    private Button registBtn, loginBtn;
    private String userName, userPwd;
    private EditText userNameET, userPwdET;
    private CustomDialog cd;
    //    private CheckBox autoLogin;
    public TextView findPassword;
    private ImageView icon;
    public static final String TAG = LoginActivity.class.getSimpleName();

    private ProgressBar login_pb_progress;
    private LinearLayout login_ll_content;

    private RadioButton login_rb_pri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        EventBus.getDefault().register(LoginActivity.this);


        login_pb_progress = findViewById(R.id.login_pb_progress);
        login_ll_content = findViewById(R.id.login_ll_content);


        login_pb_progress.setVisibility(View.GONE);
        login_ll_content.setVisibility(View.VISIBLE);
        init();
    }


    private void init() {

        initView();
        Log.e(TAG, "success1: " + IyuUserManager.getInstance().isVip());
        mContext = getApplicationContext();
        cd = WaittingDialog.showDialog(mContext);
        findViewById(R.id.button_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, RegistByPhoneActivity.class));
                finish();
            }
        });
        findPassword.setText(Html.fromHtml(
                "<a href=\"http://m." + CommonConstant.domain + "/m_login/inputPhonefp.jsp?\">" + getResources().getString(
                        R.string.login_find_password) + "</a>"));
        findPassword.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SecVerifyLoginSuccessEvent event) {

        SettingConfig.Instance().setAutoLogin(true);
        EventBus.getDefault().post(new RefreshPersonalEvent());
        LoginActivity.this.finish();
    }


    private void initView() {

        registBtn = findViewById(R.id.button_regist);
        loginBtn = findViewById(R.id.button_login);
        userNameET = findViewById(R.id.editText_userId);
        userPwdET = findViewById(R.id.editText_userPwd);
//        autoLogin = findViewById(R.id.checkBox_remember_pwd);
        findPassword = findViewById(R.id.find_password);
        icon = findViewById(R.id.icon);
        icon.setImageResource(Constant.AppIcon);


        TextView login_tv_pri = findViewById(R.id.login_tv_pri);
        LinearLayout login_ll_pri = findViewById(R.id.login_ll_pri);
        login_rb_pri = findViewById(R.id.login_rb_pri);


        String priStr = "我已阅读并同意使用条款和隐私政策";
        SpannableString spannableString = new SpannableString(priStr);
        spannableString.setSpan(new PriSpan(Constant.PROTOCOLUSE), priStr.indexOf("使用条款"), priStr.indexOf("使用条款") + "使用条款".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new PriSpan(Constant.PROTOCOLPRI), priStr.indexOf("隐私政策"), priStr.indexOf("隐私政策") + "隐私政策".length(), SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        //协议
        login_tv_pri.setText(spannableString);
        login_tv_pri.setMovementMethod(LinkMovementMethod.getInstance());
/*        login_tv_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login_rb_pri.isChecked()) {

                    login_rb_pri.setChecked(false);
                } else {

                    login_rb_pri.setChecked(true);
                }
            }
        });*/
        login_ll_pri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (login_rb_pri.isChecked()) {

                    login_rb_pri.setChecked(false);
                } else {

                    login_rb_pri.setChecked(true);
                }
            }
        });
    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case 0:
                    userNameET.setText(userName);
                    userPwdET.setText(userPwd);
                    break;
                case 1:
//                    cd.show();
                    break;
                case 2:
                    cd.dismiss();
                    break;
                case 3:
                    finish();
                    break;
                case 10:
                    cd.dismiss();
                    CustomToast.showToast(mContext, "密码错误");
                    break;
            }
            return false;
        }
    });

    public boolean verification() {

        userName = userNameET.getText().toString();
        userPwd = userPwdET.getText().toString();
        if (userName.length() < 3) {
            userNameET.setError(getResources().getString(
                    R.string.login_check_effective_user_id));
            return false;
        }

        if (userPwd.length() == 0) {
            userPwdET.setError(getResources().getString(
                    R.string.login_check_user_pwd_null));
            return false;
        }
        if (!checkUserPwd(userPwd)) {
            userPwdET.setError(getResources().getString(
                    R.string.login_check_user_pwd_constraint));
            return false;
        }

        if (!login_rb_pri.isChecked()) {

            Toast.makeText(MainApplication.getApplication(), "请勾选协议", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkUserPwd(String userPwd) {
        return userPwd.length() >= 3;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e(TAG, "onBackPressed: ");
        Intent intent = new Intent();
        String data = "我是返回的数据";
        intent.putExtra("data", data);
        setResult(2, intent);
        finish();
    }

    public void login() {
        if (verification()) {
            handler.sendEmptyMessage(1);
            AccountManager.Instance(mContext).login(userName, userPwd, new OperateCallBack() {
                @Override
                public void success(String message) {
                    if (!AccountManager.isVip()) {
                        Intent intent = new Intent();
                        String data = "我是返回的数据";
                        intent.putExtra("data", data);
                        setResult(2, intent);
                    }
                    Log.e(TAG, "success2: " + IyuUserManager.getInstance().isVip());
                    handler.sendEmptyMessage(2);
                    handler.sendEmptyMessage(3);
                }

                @Override
                public void fail(String message) {
                    Log.d(TAG, "success: 登录失败" + message);
                    handler.sendEmptyMessage(10);
                }
            });
        }
    }

    class PriSpan extends ClickableSpan {

        private String url;

        public PriSpan(String url) {
            this.url = url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#3b76f4"));
        }

        @Override
        public void onClick(@NonNull View widget) {

            MyWebActivity.startActivity(LoginActivity.this, url, "用户协议");
        }
    }
}
