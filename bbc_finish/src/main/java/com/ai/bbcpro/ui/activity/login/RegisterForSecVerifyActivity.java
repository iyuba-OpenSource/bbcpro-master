package com.ai.bbcpro.ui.activity.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.ActivityRegisterForSecVerifyBinding;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.sqlite.listener.OperateCallBack;
import com.ai.bbcpro.ui.activity.BaseNeedEditActivity;
import com.ai.bbcpro.ui.activity.MainActivity;
import com.ai.bbcpro.ui.bean.RegisterBean;
import com.ai.bbcpro.ui.http.HttpHelper;
import com.ai.bbcpro.ui.http.net.CheckLoginService;
import com.ai.bbcpro.ui.register.UpLoadImageActivity;
import com.ai.bbcpro.ui.utils.SpannableStringBuilderUtil;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.ui.utils.ViewUtils;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterForSecVerifyActivity extends BaseNeedEditActivity implements View.OnClickListener {

    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String PHONE = "phone";
    private ActivityRegisterForSecVerifyBinding b;
    private String userName;
    private String password;
    private String phone;

    public static void start(String userName, String pwd, String phone, Context context) {
        Intent intent = new Intent(context, RegisterForSecVerifyActivity.class);
        intent.putExtra(USER_NAME, userName);
        intent.putExtra(PASSWORD, pwd);
        intent.putExtra(PHONE, phone);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_register_for_sec_verify);
        userName = getIntent().getStringExtra(USER_NAME);
        password = getIntent().getStringExtra(PASSWORD);
        phone = getIntent().getStringExtra(PHONE);
        initView();
    }

    private void initView() {
        ViewUtils.bindOnClickListener(this, b.ivBack, b.tvSubmit);
        b.etUserName.setText(userName);
        b.etPassword.setText(password);
        CharSequence text = new SpannableStringBuilderUtil()
                .append(getString(R.string.user_name_pwd_desc))
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.gray)))
                .append("为了账号安全，建议您修改密码后再提交。")
                .setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.coral)))
                .build();
        b.tvDesc.setText(text);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_submit:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        SettingConfig.Instance().setAutoLogin(true);
        String userNameInput = b.etUserName.getText().toString().trim();
        String pwdInput = b.etPassword.getText().toString().trim();
        if (TextUtils.isEmpty(userNameInput) || userNameInput.length() < 3 || userNameInput.length() > 15) {
            ToastUtil.showToast(this, "请输入合法的用户名！");
            return;
        }
        if (TextUtils.isEmpty(pwdInput) || pwdInput.length() < 6 || pwdInput.length() > 18) {
            ToastUtil.showToast(this, "请输入有效的密码！");
            return;
        }
        b.loadingView.showLoading(null);
        CheckLoginService apiService = new Retrofit.Builder()
                .baseUrl("http://api." + CommonConstant.domainLong + "/")
                .client(HttpHelper.getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CheckLoginService.class);
        String protocol = "11002";
        apiService.registerNoMail(userNameInput, protocol, MD5.getMD5ofStr(pwdInput), MD5.getMD5ofStr(protocol + userNameInput + MD5.getMD5ofStr(pwdInput) + "iyubaV2"),
                        phone, Constant.APPName)
                .enqueue(new Callback<RegisterBean>() {
                    @Override
                    public void onResponse(Call<RegisterBean> call, Response<RegisterBean> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            RegisterBean res = response.body();
                            if (TextUtils.equals("111", res.getResult())) {
                                b.loadingView.showSuccess(null);
                                ToastUtil.showToast(RegisterForSecVerifyActivity.this, "注册成功");

                                AccountManager.Instance(RegisterForSecVerifyActivity.this).login(
                                        userNameInput,
                                        pwdInput,
                                        new OperateCallBack() {
                                            @Override
                                            public void success(String result) {

                                              /*  Intent intent = new Intent(RegisterForSecVerifyActivity.this, UpLoadImageActivity.class);
                                                intent.putExtra("regist", true);
                                                startActivity(intent);*/
                                                Intent intent = new Intent(RegisterForSecVerifyActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }

                                            @Override
                                            public void fail(String message) {
                                            }
                                        });
                                finish();
                            } else if (TextUtils.equals("112", res.getResult())) {
                                b.loadingView.setVisibility(View.GONE);
                                ToastUtil.showToast(RegisterForSecVerifyActivity.this, "用户名已存在");
                            } else if (TextUtils.equals("113", res.getResult())) {
                                b.loadingView.setVisibility(View.GONE);
                                ToastUtil.showToast(RegisterForSecVerifyActivity.this, "邮箱已经被注册");
                            } else {
                                b.loadingView.setVisibility(View.GONE);
                                ToastUtil.showToast(RegisterForSecVerifyActivity.this, res.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterBean> call, Throwable t) {
                        b.loadingView.setVisibility(View.GONE);
                        ToastUtil.showToast(RegisterForSecVerifyActivity.this, "请检查网络");
                    }
                });
    }
}
