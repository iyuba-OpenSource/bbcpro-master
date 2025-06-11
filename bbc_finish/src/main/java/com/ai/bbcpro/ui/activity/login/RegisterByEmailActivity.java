package com.ai.bbcpro.ui.activity.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.mvp.presenter.login.RegisterByEmailPresenter;
import com.ai.bbcpro.mvp.view.login.RegisterByEmailContract;
import com.ai.bbcpro.ui.BasisActivity;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.register.Web;


/**
 * 注册界面
 *
 * @author chentong
 * @version 1.1 修改内容 更新API
 */
public class RegisterByEmailActivity extends BasisActivity implements RegisterByEmailContract.RegisterByEmailView {

    private Context mContext;
    private EditText userName, userPwd, reUserPwd, email;
    private CheckBox checkBox;
    private Button regBtn;
    private String userNameString;
    private String userPwdString;
    private String reUserPwdString;
    private String emailString;
    private ImageView icon;
    private boolean send = false;
    private CustomDialog wettingDialog;
    private TextView protocol;

    private RegisterByEmailPresenter registerByEmailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.regist_layout);


        registerByEmailPresenter = new RegisterByEmailPresenter();
        registerByEmailPresenter.attchView(this);

//        CrashApplication.getInstance().addActivity(this);
        wettingDialog = WaittingDialog.showDialog(mContext);
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        userName = (EditText) findViewById(R.id.editText_userId);
        userPwd = (EditText) findViewById(R.id.editText_userPwd);
        reUserPwd = (EditText) findViewById(R.id.editText_reUserPwd);
        email = (EditText) findViewById(R.id.editText_email);
        checkBox = findViewById(R.id.register_checkbox);
        regBtn = (Button) findViewById(R.id.button_regist);
        icon = findViewById(R.id.imageView1);
        icon.setImageResource(Constant.AppIcon);
        regBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (verification()) { // 验证通过
                        // 开始注册
                        if (!send) {
                            send = true;
                            handler.sendEmptyMessage(5);
                            regist();
                        } else {
                            handler.sendEmptyMessage(7);
                        }
                    }
                } else {
                    Toast.makeText(mContext, "必须要先同意使用条款与隐私协议", Toast.LENGTH_SHORT).show();
                }
            }
        });
        protocol = (TextView) findViewById(R.id.protocol);
        String remindString = "我已阅读并同意使用协议和隐私政策";

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(remindString);
        ClickableSpan secretString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Web.start(mContext, Constant.BaseUrl1 + Constant.AppName + "&company=" + Constant.Company, "用户隐私政策");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(RegisterByEmailActivity.this, R.color.app_color));
            }
        };

        ClickableSpan policyString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Web.start(mContext, Constant.BaseUrl2 + Constant.AppName + "&company=" + Constant.Company, "用户使用协议");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(RegisterByEmailActivity.this, R.color.app_color));
            }
        };
        spannableStringBuilder.setSpan(secretString, remindString.indexOf("隐私政策"), remindString.indexOf("隐私政策") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(policyString, remindString.indexOf("使用协议"), remindString.indexOf("使用协议") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocol.setText(spannableStringBuilder);
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (registerByEmailPresenter != null) {

            registerByEmailPresenter.detachView();
        }
    }

    /**
     * 验证
     */
    public boolean verification() {
        userNameString = userName.getText().toString();
        userPwdString = userPwd.getText().toString();
        reUserPwdString = reUserPwd.getText().toString();
        emailString = email.getText().toString();
        if (!checkBox.isChecked()) {
            Toast.makeText(mContext, "必须要先同意使用条款与隐私协议", Toast.LENGTH_SHORT).show();
        }
        if (userNameString.length() == 0) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_1));
            return false;
        }
        if (!checkUserId(userNameString)) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_1));
            return false;
        }
        if (!checkUserName(userNameString)) {
            userName.setError(mContext
                    .getString(R.string.regist_check_username_2));
            return false;
        }
        if (userPwdString.length() == 0) {
            userPwd.setError(mContext
                    .getString(R.string.regist_check_userpwd_1));
            return false;
        }
        if (!checkUserPwd(userPwdString)) {
            userPwd.setError(mContext
                    .getString(R.string.regist_check_userpwd_1));
            return false;
        }
        if (!reUserPwdString.equals(userPwdString)) {
            reUserPwd.setError(mContext
                    .getString(R.string.regist_check_reuserpwd));
            return false;
        }
        if (emailString.length() == 0) {
            email.setError(getResources().getString(
                    R.string.regist_check_email_1));
            return false;
        }
        if (!emailCheck(emailString)) {
            email.setError(mContext.getString(R.string.regist_check_email_2));
            return false;
        }
        return true;
    }

    /**
     * 匹配用户名
     *
     * @param userId
     * @return
     */
    public boolean checkUserId(String userId) {
        if (userId.length() < 3 || userId.length() > 20)
            return false;
        return true;
    }

    /**
     * 匹配用户名2 验证非手机号 邮箱号
     *
     * @param userId
     * @return
     */
    public boolean checkUserName(String userId) {
        if (userId
                .matches("^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
            return false;
        }
        if (userId.matches("^(1)\\d{10}$")) {
            return false;
        }

        return true;
    }

    /**
     * 匹配密码
     *
     * @param userPwd
     * @return
     */
    public boolean checkUserPwd(String userPwd) {
        if (userPwd.length() < 6 || userPwd.length() > 20)
            return false;
        return true;
    }

    /**
     * email格式匹配
     *
     * @param email
     * @return
     */
    public boolean emailCheck(String email) {
        return email
                .matches("^([a-z0-ArrayA-Z]+[-_|\\.]?)+[a-z0-ArrayA-Z]@([a-z0-ArrayA-Z]+(-[a-z0-ArrayA-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    finish();
                    break;
                case 1: // 弹出错误信息
                    CustomToast.showToast(mContext, R.string.regist_email_used);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.regist_userid_exist);
                    break;
                case 4:
                    CustomToast.showToast(mContext, msg.obj.toString());
                    break;
                case 5:
                    wettingDialog.show();
                    break;
                case 6:
                    wettingDialog.dismiss();
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.regist_operating);
                    break;
            }
        }
    };

    private void regist() {

       /* String signStr = MD5.getMD5ofStr(10002 + userName.getText().toString() + MD5.getMD5ofStr(userPwd.getText().toString()) + email.getText().toString() + "iyubaV2");
        registerByEmailPresenter.register(10002, "", userName.getText().toString(), MD5.getMD5ofStr(userPwd.getText().toString()), "android"
                , Integer.parseInt(Constant.APPID), Constant.APPName, "json", signStr);


        ExeProtocol.exe(new RegistRequest(userName.getText().toString(),
                        userPwd.getText().toString(), email.getText().toString(),
                        AccountManager.Instance(mContext).getUserId()),
                new ProtocolResponse() {

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        RegistResponse rr = (RegistResponse) bhr;
                        send = false;
                        handler.sendEmptyMessage(6);
                        if (rr.result.equals("111")) {


                        } else if (rr.result.equals("112")) {
                            handler.sendEmptyMessage(3);
                        } else if (rr.result.equals("114")) {
                            handler.obtainMessage(4, rr.message).sendToTarget();
                        } else {
                            handler.sendEmptyMessage(1);
                        }
                    }

                    @Override
                    public void error() {
                        send = false;
                        handler.sendEmptyMessage(2);
                        handler.sendEmptyMessage(6);
                    }
                });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.button_regist_phone).setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(mContext,
                                RegistByPhoneActivity.class));
                        finish();
                    }
                });
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void registerComplete(UserBean userinfoDTO) {

    }
}
