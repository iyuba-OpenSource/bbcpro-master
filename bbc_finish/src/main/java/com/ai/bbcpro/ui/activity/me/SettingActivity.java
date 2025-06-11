package com.ai.bbcpro.ui.activity.me;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.R;
import com.ai.bbcpro.databinding.ActivitySettingBinding;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.UserBean;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.ui.bean.UpdateServiceResp;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.fragment.PersonalCenterFragment;
import com.ai.bbcpro.ui.http.net.UpdateServiceApi;
import com.ai.bbcpro.ui.logout.ClearUserResponse;
import com.ai.bbcpro.ui.logout.JsonOrXmlConverterFactory;
import com.ai.bbcpro.ui.logout.PwdInputDialog;
import com.ai.bbcpro.ui.logout.TeacherApiStores;
import com.ai.bbcpro.ui.register.Web;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

/**
 * 我的-设置页面
 */
public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;

    private PwdInputDialog dialogTip;

    private PersonalCenterFragment.ApiComService mApiComService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.toolbar.toolbarIvBack.setOnClickListener(v -> finish());
        binding.toolbar.toolbarTvTitle.setText("设置");

        binding.settingTvVersion.setText("版本：" + BuildConfig.VERSION_NAME);
        binding.settingTvIpc.setText(BuildConfig.ICP);
        initOperation();
    }


    private void initOperation() {

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AccountManager.Instance(SettingActivity.this).checkUserLogin()) {
                    new AlertDialog.Builder(SettingActivity.this)
                            .setTitle("提示")
                            .setMessage("注销账号后将无法登录，并且将不再会保存个人信息，账户信息将会被清除，确定要注销账号吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showContinueWrittenOffDialog();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                } else {
                    Toast.makeText(SettingActivity.this, "未登录，请先登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.updateService.setOnClickListener(v -> {
            updateService();
        });
        binding.consult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog alertDialog = new AlertDialog.Builder(SettingActivity.this).create();
                View inflate = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_privacy, null);
                TextView textView = inflate.findViewById(R.id.text_link);
                TextView title = inflate.findViewById(R.id.text_description);
                title.setText("咨询电话");
                textView.setText("400-888-1905");
                textView.setGravity(Gravity.CENTER);
                textView.setTextIsSelectable(true);
                textView.setTextColor(getResources().getColor(R.color.black));
                textView.setTextSize(20);
                alertDialog.setView(inflate);
                TextView agreeNo = inflate.findViewById(R.id.text_no_agree);
                TextView agree = inflate.findViewById(R.id.text_agree);
                agreeNo.setText("返回");
                agree.setText("拨打");
                agreeNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            if (ActivityCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
                            } else {
                                call();
                            }

                        } else {
                            call();
                        }
                        alertDialog.dismiss();

                    }
                });
                alertDialog.show();
            }
        });

        binding.QQgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog qqGroupDialog = new AlertDialog.Builder(SettingActivity.this).create();
                View qqGroupView = LayoutInflater.from(SettingActivity.this).inflate(R.layout.dialog_privacy, null);
                TextView qqGroupTitle = qqGroupView.findViewById(R.id.text_description);
                qqGroupTitle.setText("QQ群号");
                TextView qqGroupContent = qqGroupView.findViewById(R.id.text_link);
                qqGroupContent.setText(ConfigManager.Instance().loadString("qq_group_number"));
                qqGroupContent.setGravity(Gravity.CENTER);
                qqGroupContent.setTextSize(20);
                qqGroupContent.setTextColor(getResources().getColor(R.color.black));
                qqGroupContent.setTextIsSelectable(true);
                TextView qqGroupAgree = qqGroupView.findViewById(R.id.text_agree);
                qqGroupAgree.setText("加入");
                TextView qqGroupNoAgree = qqGroupView.findViewById(R.id.text_no_agree);
                qqGroupNoAgree.setText("返回");
                qqGroupDialog.setView(qqGroupView);
                qqGroupAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startQQGroup(SettingActivity.this, ConfigManager.Instance().loadString("qq_group_key"));
                        qqGroupDialog.dismiss();
                    }
                });
                qqGroupNoAgree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qqGroupDialog.dismiss();
                    }
                });
                qqGroupDialog.show();

            }
        });
        binding.secrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web.start(SettingActivity.this, Constant.PROTOCOLPRI, "用户隐私政策");
            }
        });
        binding.protocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Web.start(SettingActivity.this, Constant.PROTOCOLUSE, "用户使用协议");
            }
        });

    }

    public static void startQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?" +
                "url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:4008881905");
        intent.setData(data);
        startActivity(intent);
    }


    private void updateService() {
        UpdateServiceApi updateServiceApi = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://111.198.52.105:8085").client(new OkHttpClient()).build()
                .create(UpdateServiceApi.class);
        updateServiceApi.requestNewService(String.valueOf(Constant.APPID), CommonConstant.domain, CommonConstant.domainLong).enqueue(new Callback<UpdateServiceResp>() {
            @Override
            public void onResponse(retrofit2.Call<UpdateServiceResp> call, Response<UpdateServiceResp> response) {

                UpdateServiceResp body = response.body();
                Log.d("TAG", "onResponse: " + body.getUpdateflg());
                if (body.getUpdateflg() == 1) {
                    ConfigManager.Instance().putString("short1", body.getShort1());
                    ConfigManager.Instance().putString("short2", body.getShort2());
                    ToastUtil.showToast(SettingActivity.this, "已更新至最新服务");
                } else {
                    ToastUtil.showToast(SettingActivity.this, "当前已是最新服务");
                }
            }

            @Override
            public void onFailure(retrofit2.Call<UpdateServiceResp> call, Throwable t) {

            }
        });
    }


    private void showContinueWrittenOffDialog() {

        dialogTip = new PwdInputDialog(SettingActivity.this);
        dialogTip.setTitleStr("输入密码注销账号");
        dialogTip.setCancelStr("取消");
        dialogTip.setConfirmStr("确认注销");
        dialogTip.setShowInputPwd(true);
        dialogTip.setListener(new PwdInputDialog.OnBtnClickListener() {
            @Override
            public void onCancelClick() {
                dialogTip.dismiss();
            }

            @Override
            public void onConfirmClick() {
                dialogTip.dismiss();
            }

            @Override
            public void onCheckInputPassWord(final String password) {

                if (password.equals("")) {

                    return;
                }

//                String[] nameAndPwd = AccountManager.Instance(mContext).getUserNameAndPwd();
                final String userName = ConfigManager.Instance().loadString("userName", "");
//                final String userPwd = nameAndPwd[1];
//                if (TextUtils.equals(password, userPwd)) {
//                    clearUser(userName, userPwd);
//                    ToastUtil.showToast(mContext, "frist vertify");
//                } else {
                // 用 输入 的密码登录验证 输入的密码是否正确
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .build();

                Retrofit retrofit = new Retrofit.Builder()
                        //这里建议：- Base URL: 总是以/结尾；- @Url: 不要以/开头
                        .baseUrl("http://api." + CommonConstant.domainLong + "/")
                        .client(client)
                        .addConverterFactory(JsonOrXmlConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                TeacherApiStores apiService = retrofit.create(TeacherApiStores.class);
                try {
                    String name = URLEncoder.encode(userName, "UTF-8");
                    String pwd = MD5.getMD5ofStr(password);
                    String sign = MD5.getMD5ofStr("11005" + userName + MD5.getMD5ofStr(password) + "iyubaV2");

                    apiService.login("11005", URLEncoder.encode(userName, "UTF-8"), MD5.getMD5ofStr(password),
                                    MD5.getMD5ofStr("11005" + userName + MD5.getMD5ofStr(password) + "iyubaV2"), "json")
                            .enqueue(new Callback<UserBean>() {
                                @Override
                                public void onResponse(retrofit2.Call<UserBean> call, Response<UserBean> response) {

                                    if (response.isSuccessful() && response.body() != null) {
                                        UserBean userMsg = response.body();
                                        if (TextUtils.equals("101", userMsg.getResult())) {

                                            // 登陆成功，证明输入的密码正确
                                            clearUser(userName, password);
                                        } else if (TextUtils.equals("102", userMsg.getResult())) {
                                            ToastUtil.showToast(SettingActivity.this, "验证失败，请稍后重试！");
                                        } else if (TextUtils.equals("103", userMsg.getResult())) {
                                            ToastUtil.showToast(SettingActivity.this, "输入的密码有误，请重试！");
                                        } else {
                                            ToastUtil.showToast(SettingActivity.this, getString(R.string.login_fail));
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(retrofit2.Call<UserBean> call, Throwable t) {
                                    ToastUtil.showToast(SettingActivity.this, "验证失败，请稍后重试！");
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                }
            }
        });
        dialogTip.show();
    }


    private void clearUser(String username, String password) {

        AccountManager.Instance(SettingActivity.this).loginOut();
        EventBus.getDefault().post(new RefreshListEvent("refresh"));
        viewChange();

        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<Boolean> observableEmitter) throws Exception {
                Response<ClearUserResponse> execute = null;
                execute = clearDataUser(username, password).execute();
                observableEmitter.onNext(execute != null);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable disposable) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Boolean aBoolean) {
                if (aBoolean) {
                    clearSuccess();
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


    private void clearSuccess() {
        SettingConfig.Instance().setHighSpeed(false);
        ConfigManager.Instance().putString("userId", "");
        ConfigManager.Instance().putString("userName", "");
        ConfigManager.Instance().putBoolean("isvip", false);
        viewChange();

        CustomToast.showToast(SettingActivity.this, R.string.loginout_success);
        new AlertDialog.Builder(SettingActivity.this)
                .setTitle("提示")
                .setMessage("账户被注销！账户信息清除")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        dialogTip.dismiss();

                    }
                })
                .show();

    }


    public retrofit2.Call<ClearUserResponse> clearDataUser(String username, String password) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(interceptor);

        OkHttpClient client = builder.build();
        SimpleXmlConverterFactory xmlFactory = SimpleXmlConverterFactory.create();
        GsonConverterFactory gsonFactory = GsonConverterFactory.create();
        RxJava2CallAdapterFactory rxJavaFactory = RxJava2CallAdapterFactory.create();

        mApiComService = PersonalCenterFragment.ApiComService.Creator.createService(client, gsonFactory, rxJavaFactory);

        String protocol = "11005";
        String format = "json";
        String passwordMD5 = com.iyuba.module.toolbox.MD5.getMD5ofStr(password);
        String sign = buildV2Sign(protocol, username, passwordMD5, "iyubaV2");
        return mApiComService.clearUserInfo(protocol, username, passwordMD5, sign, format);

    }


    private String buildV2Sign(String... stuffs) {
        StringBuilder sb = new StringBuilder();
        for (String stuff : stuffs) {
            sb.append(stuff);
        }
        return com.iyuba.module.toolbox.MD5.getMD5ofStr(sb.toString());
    }


    private void viewChange() {

        String userId = ConfigManager.Instance().loadString("userId", "");
        if (!userId.equals("")) {

            binding.logout.setVisibility(View.VISIBLE);
        } else {

            binding.logout.setVisibility(View.GONE);
        }
    }
}