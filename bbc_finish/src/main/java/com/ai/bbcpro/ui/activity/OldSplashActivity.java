package com.ai.bbcpro.ui.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.manager.RuntimeManager;
//import com.ai.bbcpro.ui.ad.AdSplashUtil;
import com.ai.bbcpro.ui.ad.AdSplashUtil;
import com.ai.bbcpro.ui.helper.ReadBitmap;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.ApiService;
import com.ai.bbcpro.ui.register.Web;
import com.ai.bbcpro.ui.widget.IyuButton;
import com.ai.bbcpro.util.IyubaADBean;
import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdSpread;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 启动界面
 */
public class OldSplashActivity extends AppCompatActivity {

    private IyuButton btn_skip;
    private Activity mContext;
    private int cout_down = 5;
    private boolean isClick = false;
    private Timer timer;
    private TimerTask timerTask;
    private ImageView ivBase, ivAd;
    public boolean canJump;
    private boolean resume;
    private AdSplashUtil adSplashUtil;
    private String TAG = "SplashActivity";

    private RelativeLayout splash_rl_ad;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case 0:

                    break;
                case 1:
                    Intent intent3 = new Intent(OldSplashActivity.this,
                            MainActivity.class);
                    intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent3.putExtra("isFirstInfo", 0);
                    startActivity(intent3);
                    finish();

                    break;
                case 2:
                    break;

                case 3:
                    btn_skip.setText("跳过(" + cout_down + "s)");
                    if (cout_down < 1) {
                        timer.cancel();
                        timerTask.cancel();
                        handler.removeCallbacksAndMessages(null);
                        handler.sendEmptyMessage(1);
                    }
                default:
                    break;
            }
            return false;
        }
    });
//    private HeadlinesDataManager dataManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSqlite();
        RuntimeManager.setDisplayMetrics(this);
        setContentView(R.layout.activity_old_splash);

        dealWindow();

        initView();


        mContext = this;
        if (ConfigManager.Instance().loadBoolean("initPermission")) {
            Log.e(TAG, "splash");
            initSplash();
        } else {
            Log.e(TAG, "dialog");
            showDialog();
        }
    }

    /**
     * 处理状态栏和虚拟返回键
     */
    private void dealWindow() {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {

            WindowInsetsController windowInsetsController = getWindow().getInsetsController();
            windowInsetsController.hide(WindowInsets.Type.systemBars());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }

    private void initSqlite() {

        int[] array = getResources().getIntArray(R.array.bbc_category_number);
        for (int i = 0; i < array.length; i++) {
            getData(array[i], 1);
        }
    }

    public Call<SumBean> initHttp(int parentID, int page) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.post("android", "json", Integer.parseInt(Constant.APPID), 0, page, 10, parentID);
    }

    public void getData(int parentID, int page) {
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws IOException {
                Response<SumBean> execute = initHttp(parentID, page).execute();
                SumBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean SumBean) {
                List<SumBean.DataDTO> netData = SumBean.data;
//                dataManager = HeadlinesDataManager.getInstance(mContext);
//                dataManager.saveHeadlines(netData);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @SuppressLint("MissingPermission")
    private void initSplash() {
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (!isClick)
                    cout_down--;
                if (cout_down >= 0)
                    handler.sendEmptyMessage(3);
            }
        };
        timer.schedule(timerTask, 1000, 1000);

        adSplashUtil = new AdSplashUtil(OldSplashActivity.this, ivAd, (ViewGroup) ivAd.getParent(), ivBase);
        adSplashUtil.requestAd();
        adSplashUtil.setCallback(callback);


//        ivBase.setImageBitmap(ReadBitmap.readBitmap(mContext, Constant.AppIcon));
        if (!NetworkUtils.isConnected()) {
            LoadAdImage();
        }
    }

    private void initView() {

        splash_rl_ad = findViewById(R.id.splash_rl_ad);
        ivBase = findViewById(R.id.base);
        ivAd = findViewById(R.id.ad);
        btn_skip = findViewById(R.id.btn_skip);
        ivAd.setOnClickListener(v -> {

            //小于限制时长则
            if (System.currentTimeMillis() < BuildConfig.examineTime) {

                return;
            }

            isClick = true;
            if (adSplashUtil != null && adSplashUtil.isRequestEnd() && adSplashUtil.getWebUrl() != null) {

                Web.start(OldSplashActivity.this, adSplashUtil.getWebUrl(), "推广");
            } else {

                Web.start(OldSplashActivity.this, "http://app.iyuba.cn", "推广");
            }
            handler.removeCallbacksAndMessages(null);
        });

        btn_skip.setOnClickListener(view -> {
            isClick = true;
            startActivity(new Intent(mContext, MainActivity.class));
            //开启主界面,并把当前界面给杀死
            finish();
        });
    }


    public void startMainActivity() {
        if (resume) {
            finish();
            return;
        }
        ConfigManager.Instance().putInt("SectionFirstRun", 0);
        ConfigManager.Instance().putInt("NewSectionFirstRun", 0);
        ConfigManager.Instance().putInt("isFirstInfo", 1);
        startActivity(new Intent(mContext, MainActivity.class));
        finish();
    }

    AdSplashUtil.SCallback callback = new AdSplashUtil.SCallback() {
        @Override
        public void loadLocal() {
            LoadWebImage(adSplashUtil.getWebPicUrl());

            ivBase.setVisibility(View.VISIBLE);
            ivBase.setImageResource(R.mipmap.splash_bottom);
            ivAd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isClick = true;
                    Web.start(OldSplashActivity.this, adSplashUtil.getWebUrl(), "推广");
                }
            });
        }

        @Override
        public void onAdClick() {
            isClick = true;
            handler.removeCallbacksAndMessages(null);
        }

        @Override
        public void startTimer() {
            handler.sendEmptyMessage(4);
        }

        @Override
        public void dismissTimer() {
            btn_skip.setVisibility(View.INVISIBLE);
        }

        @Override
        public void showOtherAD(IyubaADBean adBean) {

            String key;
            if (BuildConfig.APPLICATION_ID.equals("com.ai.bbcpro")) {

                key = "0050";
            } else {
                key = "0052";
            }

            YdSpread mSplashAd = new YdSpread.Builder(OldSplashActivity.this)
                    .setKey(key)
                    .setContainer(splash_rl_ad)
                    .setSpreadListener(adViewSpreadListener)
                    .setCountdownSeconds(4)
                    .setSkipViewVisibility(true)
                    .build();

            mSplashAd.requestSpread();
        }
    };


    /**
     * 其他广告的回调
     */
    AdViewSpreadListener adViewSpreadListener = new AdViewSpreadListener() {
        @Override
        public void onAdDisplay() {

        }

        @Override
        public void onAdClose() {

        }

        @Override
        public void onAdClick(String s) {

        }

        @Override
        public void onAdFailed(YdError ydError) {

        }
    };

    private void LoadWebImage(String url) {
        if (!isDestroyed()) {
            Glide.with(mContext).load(url).dontAnimate().into(ivAd);
        }
    }

    private void LoadAdImage() {
        try {
            File adPic = new File(Constant.APP_DATA_PATH + "/ad/ad.jpg");
            if (adPic.exists()) {
                ivAd.setImageBitmap(ReadBitmap.readBitmap(mContext, new FileInputStream(adPic)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public void showDialog() {
        String privacy1 = "1.为了更方便您使用我们的软件，我们回根据您使用的具体功能时申请必要的权限，如摄像头，存储权限，录音权限等。\n";
        String privacy2 = "2.使用本app需要您了解并同意";
        String privacy3 = "用户协议及隐私政策";
        String privacy4 = "，点击同意即代表您已阅读并同意该协议";

        ClickableSpan secretString = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {

                Web.start(mContext, Constant.PROTOCOLPRI, "用户隐私政策");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(ContextCompat.getColor(mContext, R.color.app_color));
                ds.setUnderlineText(true);
            }
        };

        ClickableSpan policyString = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {


                Web.start(mContext, Constant.PROTOCOLUSE, "用户使用协议");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(mContext, R.color.app_color));
            }
        };

        int start = privacy1.length() + privacy2.length();
        int end = start + privacy3.length();

        SpannableStringBuilder strBuilder = new SpannableStringBuilder();
        strBuilder.append(privacy1);
        strBuilder.append(privacy2);
        strBuilder.append(privacy3);
        strBuilder.append(privacy4);
        strBuilder.setSpan(policyString, start, start + 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        strBuilder.setSpan(secretString, start + 5, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .create();

        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_privacy, null);
        dialog.setView(view);
        dialog.show();
        TextView textView = view.findViewById(R.id.text_link);
        textView.setText(strBuilder);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        TextView agreeNo = view.findViewById(R.id.text_no_agree);
        TextView agree = view.findViewById(R.id.text_agree);
        agreeNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                ConfigManager.Instance().putBoolean("initPermission", true);
                //初始化
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcastSync(new Intent(MainApplication.ACTION_INIT));
                initSplash();

            }
        });

    }


    private void jump() {
        startActivity(new Intent(OldSplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isClick) {

            jump();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {

            timer.cancel();
        }
    }
}
