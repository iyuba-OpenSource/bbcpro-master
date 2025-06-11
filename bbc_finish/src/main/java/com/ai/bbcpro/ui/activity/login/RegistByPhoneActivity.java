package com.ai.bbcpro.ui.activity.login;


/**
 * 手机注册界面,在注册成功之后就对密码和用户名进行保存,
 *
 * @author czf
 * @version 1.0
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.http.main.Http;
import com.ai.bbcpro.http.main.HttpCallback;
import com.ai.bbcpro.ui.BasisActivity;
import com.ai.bbcpro.ui.register.RegisterMessageBean;
import com.ai.bbcpro.ui.register.SmsContent;
import com.ai.bbcpro.ui.register.TelNumMatch;
import com.ai.bbcpro.ui.register.Web;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import okhttp3.Call;


public class RegistByPhoneActivity extends BasisActivity {
    private Context mContext;
    private ImageView icon;
    private EditText phoneNum, messageCode;
    private Button getCodeButton;
    private TextView toEmailButton;
    private String phoneNumString = "", messageCodeString = "";
    private Timer timer;
    private TextView protocol;
    private EventHandler eh;
    private CheckBox checkBox;
    private TimerTask timerTask;
    private SmsContent smsContent;
    private CustomDialog waittingDialog;
    private EditTextWatch editTextWatch;
    private String TAG = "RegistByPhoneActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        editTextWatch = new EditTextWatch();
        setContentView(R.layout.regist_layout_phone);
//        CrashApplication.getInstance().addActivity(this);
        waittingDialog = WaittingDialog.showDialog(mContext);
        messageCode = findViewById(R.id.regist_phone_code);
        messageCode.addTextChangedListener(editTextWatch);
        phoneNum = findViewById(R.id.regist_phone_numb);
        phoneNum.addTextChangedListener(editTextWatch);
        getCodeButton = findViewById(R.id.regist_getcode);
        nextstep_unfocus = findViewById(R.id.nextstep_unfocus);
        nextstep_unfocus.setEnabled(false);
        nextstep_focus = findViewById(R.id.nextstep_focus);
        checkBox = findViewById(R.id.register_checkbox);
        icon = findViewById(R.id.imageView1);
        icon.setImageResource(Constant.AppIcon);
        //短信验证码接受
        MobSDK.init(this, Constant.SMSAPPID, Constant.SMSAPPSECRET);

        eh = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                handlerSms.sendMessage(msg);

            }
        };
        SMSSDK.registerEventHandler(eh);
        smsContent = new SmsContent(RegistByPhoneActivity.this, handler_verify);
        protocol = findViewById(R.id.protocol);
        String remindString = "我已阅读并同意使用协议和隐私政策";

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(remindString);
        ClickableSpan secretString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Web.start(mContext, Constant.BaseUrl1 + Constant.AppName + "&company="+Constant.Company, "用户隐私政策");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(RegistByPhoneActivity.this, R.color.app_color));
            }
        };

        ClickableSpan policyString = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Web.start(mContext, Constant.BaseUrl2 + Constant.AppName + "&company="+Constant.Company, "用户使用协议");
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(RegistByPhoneActivity.this, R.color.app_color));
            }
        };
        spannableStringBuilder.setSpan(secretString, remindString.indexOf("隐私政策"), remindString.indexOf("隐私政策") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(policyString, remindString.indexOf("使用协议"), remindString.indexOf("使用协议") + 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        protocol.setText(spannableStringBuilder);
        protocol.setMovementMethod(LinkMovementMethod.getInstance());
        findViewById(R.id.button_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
/*        toEmailButton = findViewById(R.id.regist_email);
        toEmailButton.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
//        toEmailButton.getPaint().setTextAlign();
//        toEmailButton.setTextAlignment();
        toEmailButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, RegisterByEmailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });*/
        //设置获取验证码的监听
        getCodeButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO 自动生成的方法存根
                if (phoneNumString.length()<11){
                    phoneNum.setError("手机号输入错误");
                }
                {
                    if (verificationNum()) {
                        if (timer != null) {
                            timer.cancel();
                        }
                        handler_waitting.sendEmptyMessage(1);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0,
                                InputMethodManager.HIDE_NOT_ALWAYS);
                        phoneNumString = phoneNum.getText().toString();
                        String path = "http://api."+ CommonConstant.domainLong +"/sendMessage3.jsp?format=json"
                                + "&userphone=" + phoneNumString;
                        Http.get(path, new HttpCallback() {
                            @Override
                            public void onSucceed(Call call, String response) {
                                Log.e(TAG, "onSucceed: ");
                                RegisterMessageBean bean;
                                try {
                                    bean = new Gson().fromJson(response, RegisterMessageBean.class);
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                    handler_waitting.sendEmptyMessage(2);
                                    return;
                                }
                                try {
                                    if ("1".equals(bean.getResult())) {
                                        handler_verify.sendEmptyMessage(1);
                                        RegistByPhoneActivity.this
                                                .getContentResolver()
                                                .registerContentObserver(
                                                        Uri.parse("content://sms/"),
                                                        true, smsContent);
                                    } else if ("-1".equals(bean.getResult())) {
                                        handler_waitting.sendEmptyMessage(3);
                                    }
                                    handler_waitting.sendEmptyMessage(2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Call call, Exception e) {
                                Log.e("===okhttp==error==", e.getMessage());
                                handler_waitting.sendEmptyMessage(2);
                            }
                        });
                    } else {
                        handler_waitting.sendEmptyMessage(4);
                    }
                }
            }
        });
        nextstep_focus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果什么都不为空的话
                if (verification()) {

                    SMSSDK.submitVerificationCode("86", phoneNumString,
                            messageCode.getText().toString());
                } else {
//                    CustomToast.showToast(mContext, "验证码不能为空");
                }

            }
        });
    }

    public class EditTextWatch implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

        }

        //监听文本框内容的变化如果输入了内容,判计时器是否及时作出相应的判断并改变下一步的图片
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (( verificationNum()) && (verificationNum() && messageCode.getText().toString().length() == 6)) {
                if (timer != null) {
                    timer.cancel();
                }
                nextstep_focus.setVisibility(View.VISIBLE);
                nextstep_focus.setEnabled(true);
            } else {
                nextstep_focus.setVisibility(View.GONE);
                nextstep_focus.setEnabled(false);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

    //分别判断手机号是否输入手机号是否正确验证码是否为空
    public boolean verification() {
        phoneNumString = phoneNum.getText().toString();
        messageCodeString = messageCode.getText().toString();
        if (!checkBox.isChecked()) {
            CustomToast.showToast(mContext, "必须要先同意使用条款与隐私协议");
            return false;
        }
        if (phoneNumString.length() == 0) {
            phoneNum.setError("手机号不能为空");
            return false;
        }
        if (!checkPhoneNum(phoneNumString)) {
            phoneNum.setError("手机号输入错误");
            return false;
        }
        if (messageCodeString.length() == 0) {
            messageCode.setError("验证码不能为空");
            return false;
        }
        return true;
    }

    /**
     * 验证
     */
    //判断手机号码是否为空以及手机号是否正确
    public boolean verificationNum() {
        //获取到电话框里面的内容,以及获取到验证框里面内容
        phoneNumString = phoneNum.getText().toString();
        messageCodeString = messageCode.getText().toString();
        if (phoneNumString.length() == 0) {
            phoneNum.setError("手机号不能为空");
            return false;
        }
//        if (!checkPhoneNum(phoneNumString)) {
//            phoneNum.setError("手机号输入错误");
//            return false;
//        }

        return true;
    }

    //判断手机号是否正确
    public boolean checkPhoneNum(String userId) {
//        if (userId.length() < 11)
//            return false;
        TelNumMatch match = new TelNumMatch(userId);
        int flag = match.matchNum();
        /*不check 号码的正确性，只check 号码的长度*/
        if (flag == 1 || flag == 2 || flag == 3) {
            return true;
        } else {
            return false;
        }

    }

    @SuppressLint("HandlerLeak")
    Handler handlerSms = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 短信注册成功后，返回MainActivity,然后提示新好友
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    CustomToast.showToast(mContext, "验证成功");
                    Intent intent = new Intent();
                    intent.setClass(mContext, RegistSubmitActivity.class);
                    intent.putExtra("phoneNumb", phoneNumString);
                    startActivity(intent);
                    finish();
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    boolean smart = (Boolean) msg.obj;
                    if (smart) {
                        //通过智能验证
                        CustomToast.showToast(mContext, "已通过短信智能验证，请完成注册");
                        Intent intent = new Intent();
                        intent.setClass(mContext, RegistSubmitActivity.class);
                        intent.putExtra("phoneNumb", phoneNumString);
                        startActivity(intent);
                        finish();
                    } else {
                        //依然走短信验证
                        CustomToast.showToast(mContext, "验证码已经发送，请等待接收");
                    }
                }
            } else {
                Throwable data = (Throwable) msg.obj;
                data.getMessage();
                Log.e("RegistByPhoneActivity", "" + result+"  "+data.getMessage());

                //CustomToast.showToast(mContext, "验证失败，请输入正确的验证码！");
                getCodeButton.setText("获取验证码");
                getCodeButton.setEnabled(true);
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_time = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Handler处理消息
            if (msg.what > 0) {
                getCodeButton.setText("重新发送(" + msg.what + "s)");
            } else {
                timer.cancel();
                getCodeButton.setEnabled(true);
                getCodeButton.setText("获取验证码");
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_waitting = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    waittingDialog.show();
                    break;
                case 2:
                    waittingDialog.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, "手机号已注册，请换一个号码试试~", 2000);
                    break;
                case 4:
                    CustomToast.showToast(mContext, "电话不能为空");
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler_verify = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // Handler处理消息
            if (msg.what == 0) {
                timer.cancel();
                /*
                 * getCodeButton.setText("下一步"); getCodeButton.setEnabled(true);
                 */
                String verifyCode = (String) msg.obj;
                messageCode.setText(verifyCode);
                nextstep_focus.setVisibility(View.VISIBLE);
                nextstep_focus.setEnabled(true);
            } else if (msg.what == 1) {
                Log.e("send", "onClick: "+phoneNum.getText().toString());
                SMSSDK.getVerificationCode("86", phoneNum.getText().toString());
                timer = new Timer();
                timerTask = new TimerTask() {
                    int i = 60;

                    @Override
                    public void run() {
                        Message msg = new Message();
                        msg.what = i--;
                        handler_time.sendMessage(msg);
                    }
                };
                timer.schedule(timerTask, 1000, 1000);
                getCodeButton.setTextColor(Color.WHITE);
                /*getCodeButton.setEnabled(false);*/
            }
        }
    };
    private Button nextstep_unfocus;
    private Button nextstep_focus;
}

