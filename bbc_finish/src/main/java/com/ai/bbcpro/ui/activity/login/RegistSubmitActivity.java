package com.ai.bbcpro.ui.activity.login;

/**
 * 手机注册完善内容界面
 *
 * @author czf
 * @version 1.0
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;

import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.mvp.presenter.login.RegisterSubmitPresenter;
import com.ai.bbcpro.mvp.view.login.RegisterSubmitContract;
import com.ai.bbcpro.sqlite.listener.OperateCallBack;
import com.ai.bbcpro.ui.BasisActivity;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.register.UpLoadImageActivity;
import com.ai.bbcpro.util.MD5;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;


public class RegistSubmitActivity extends BasisActivity implements RegisterSubmitContract.RegisterSubmitView {


    private Context mContext;
    private EditText userNameEditText, passWordEditText;
    private Button submitButton;
    private ImageView icon;
    private String phonenumb, userName, passWord;
    private CustomDialog wettingDialog;
    String TAG = "RegistSubmitActivity";

    private RegisterSubmitPresenter registerSubmitPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
//        CrashApplication.getInstance().addActivity(this);
        setContentView(R.layout.regist_layout_phone_regist);

        registerSubmitPresenter = new RegisterSubmitPresenter();
        registerSubmitPresenter.attchView(this);

        userNameEditText = findViewById(R.id.regist_phone_username);
        icon = findViewById(R.id.imageView1);
        passWordEditText = findViewById(R.id.regist_phone_paswd);
        submitButton = findViewById(R.id.regist_phone_submit);
        phonenumb = getIntent().getExtras().getString("phoneNumb");
        wettingDialog = WaittingDialog.showDialog(mContext);
        submitButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (verification()) {// 验证通过
                    // 开始注册
                    handler.sendEmptyMessage(0);// 在handler中注册
                }
            }

        });
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        icon.setImageResource(Constant.AppIcon);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registerSubmitPresenter != null) {

            registerSubmitPresenter.detachView();
        }
    }

    /**
     * 验证
     */
    public boolean verification() {
        userName = userNameEditText.getText().toString();
        passWord = passWordEditText.getText().toString();
        if (!checkUserId(userName)) {
            userNameEditText.setError(mContext
                    .getString(R.string.regist_check_username_1));
            return false;
        }
        if (!checkUserName(userName)) {
            userNameEditText.setError(mContext
                    .getString(R.string.regist_check_username_2));
            return false;
        }
        if (!checkUserPwd(passWord)) {
            passWordEditText.setError(mContext
                    .getString(R.string.regist_check_userpwd_1));
            return false;
        }
        return true;
    }

    /**
     * 匹配用户名1
     *
     * @param userId
     * @return
     */
    public boolean checkUserId(String userId) {
        return userId.length() >= 3 && userId.length() <= 20;
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
        return !userId.matches("^(1)\\d{10}$");
    }

    /**
     * 匹配密码
     *
     * @param userPwd
     * @return
     */
    public boolean checkUserPwd(String userPwd) {
        return userPwd.length() >= 6 && userPwd.length() <= 20;
    }

    private void regist() {

        String signStr = MD5.getMD5ofStr(11002 + userName + MD5.getMD5ofStr(passWord) + "iyubaV2");
        registerSubmitPresenter.register(11002, phonenumb, userName, MD5.getMD5ofStr(passWord), "android"
                , Integer.parseInt(Constant.APPID), Constant.APPName, "json", signStr);

/*        ExeProtocol.exe(new RequestPhoneNumRegister(userName, passWord, AccountManager.Instance(mContext).getUserId(),
                phonenumb), new ProtocolResponse() {
            @Override
            public void finish(BaseHttpResponse bhr) {

                ResponsePhoneNumRegister rr = (ResponsePhoneNumRegister) bhr;
                Log.e(TAG, "finish: " + rr.resultCode + "--" + rr.message + "-" + rr.isRegSuccess);
                if (rr.isRegSuccess) {
                    wettingDialog.dismiss();
//                    CustomToast.showToast(mContext, R.string.regist_success);
                    //handle4隐藏等待框

                    //6是保存了用户的用户名和密码,并开启完善信息界面
                    handler.sendEmptyMessage(6);
                } else if (rr.resultCode.equals("112")) {
                    // 提示用户已存在
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(4);
                    handler.sendEmptyMessage(1);// 弹出错误提示
                }
            }

            @Override
            public void error() {
            }
        });*/
    }

    private void gotoLogin() {
        AccountManager.Instance(mContext).login(userName, passWord,
                new OperateCallBack() {
                    @Override
                    public void success(String result) {


                        Intent intent = new Intent(mContext, UpLoadImageActivity.class);
                        intent.putExtra("regist", true);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void fail(String message) {
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    handler.sendEmptyMessage(5);
                    regist();
                    break;
                case 1:
                    handler.sendEmptyMessage(4);
                    CustomToast.showToast(mContext, R.string.regist_fail);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.regist_success);
                    break;
                case 3:
                    handler.sendEmptyMessage(4);
                    CustomToast.showToast(mContext, R.string.regist_userid_exist);
                    break;
                case 4:
                    wettingDialog.dismiss();
                    break;
                case 5:
                    if (!isFinishing()) {
                        wettingDialog.show();
                    }
                    break;
                case 6:
                    gotoLogin();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

        if (wettingDialog != null) {
            wettingDialog.dismiss();
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void registerComplete(UserBean userinfoDTO) {

        //存储用户数据
        ConfigManager.Instance().putBoolean("isvip", false);
        ConfigManager.Instance().putString("userId", userinfoDTO.getUid() + "");
        ConfigManager.Instance().putString("imgUrl", userinfoDTO.getImgSrc());
        ConfigManager.Instance().putString("userName", userinfoDTO.getUsername());
        ConfigManager.Instance().putString("expireTime", userinfoDTO.getExpireTime() + "");
        ConfigManager.Instance().putInt("isvip2", 0);
        //设置共同模块
        User user = new User();
        user.vipStatus = userinfoDTO.getVipStatus();
        user.uid = userinfoDTO.getUid();
        user.name = userinfoDTO.getUsername();
        IyuUserManager.getInstance().setCurrentUser(user);


        finish();
    }
}
