package com.ai.bbcpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ai.bbcpro.databinding.ActivitySplashBinding;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.mvp.activity.BaseActivity;
import com.ai.bbcpro.mvp.presenter.SplashPresenter;
import com.ai.bbcpro.mvp.view.SplashContract;
import com.ai.bbcpro.ui.activity.MainActivity;
import com.ai.bbcpro.ui.activity.MyWebActivity;
import com.ai.bbcpro.util.popup.PrivacyPopup;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.util.Log;
import com.sd.iyu.training_camp.TCApplication;
import com.yd.saas.base.interfaces.AdViewSpreadListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdSpread;

public class SplashActivity extends BaseActivity<SplashContract.SplashView, SplashContract.SplashPresenter>
        implements SplashContract.SplashView, AdViewSpreadListener {


    private PrivacyPopup privacyPopup;

    private ActivitySplashBinding binding;

    private AdEntryBean.DataDTO dataDTO;

    private boolean isAdCLick = false;

    private YdSpread mSplashAd;


    private CountDownTimer webTimer;

    /**
     * 是否请求了title的广告
     */
    private boolean isRequestTitle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dealWindow();
        initView();

        boolean isFirstEnter = ConfigManager.Instance().loadBoolean("initPermission");
        if (!isFirstEnter) {

            initPrivacyPopup();
        } else {

            SharedPreferences userInfoSP = getSharedPreferences("userInfo", MODE_PRIVATE);
            String uid = userInfoSP.getString("uid", "0");
            String vipStatus = userInfoSP.getInt("vipStatus", 0) + "";
            //传值给训练营
            TCApplication.setUserid(uid);
            TCApplication.setVipStatus(vipStatus);

            presenter.getAdEntryAll(Constant.ADAPPID + "", 1, uid + "");
            countDownTimer.start();
        }
    }

    private void initView() {

    }

    @Override
    public View initLayout() {
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public SplashContract.SplashPresenter initPresenter() {
        return new SplashPresenter();
    }

    private void initPrivacyPopup() {

        if (privacyPopup == null) {

            privacyPopup = new PrivacyPopup(this);
            privacyPopup.setCallback(new PrivacyPopup.Callback() {
                @Override
                public void yes() {

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcastSync(new Intent(MainApplication.ACTION_INIT));
                    ConfigManager.Instance().putBoolean("initPermission", true);
                    privacyPopup.dismiss();

                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void no() {

                    privacyPopup.dismiss();
                    finish();
                }

                @Override
                public void user() {

                    MyWebActivity.startActivity(SplashActivity.this, Constant.PROTOCOLUSE, "用户协议");
                }

                @Override
                public void privacy() {

                    MyWebActivity.startActivity(SplashActivity.this, Constant.PROTOCOLPRI, "隐私政策");
                }
            });
        }
        privacyPopup.showPopupWindow();
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

    @Override
    protected void onResume() {
        super.onResume();

        if (isAdCLick) {//点击了就直接跳转mainactivity

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {

            countDownTimer.cancel();
        }
    }

    /**
     * 计时器
     */
    CountDownTimer countDownTimer = new CountDownTimer(2000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {


            if (dataDTO == null) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }
    };


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
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        dataDTO = adEntryBean.getData();
        String type = dataDTO.getType();
        if (type.equals("web")) {

            dealAdWeb();
        } else if (type.startsWith("ads")) {

            dealAds(type);
        } else {

            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }


    /**
     * 获取开屏广告
     *
     * @param type
     */
    private void dealAds(String type) {

        String adKey = null;
        if (type.equals(Constant.AD_ADS1)) {

            adKey = BuildConfig.SPLASH_AD_KEY_BZ;
        } else if (type.equals(Constant.AD_ADS2)) {

            adKey = BuildConfig.SPLASH_AD_KEY_CSJ;
        } else if (type.equals(Constant.AD_ADS3)) {

            adKey = BuildConfig.SPLASH_AD_KEY_BD;
        } else if (type.equals(Constant.AD_ADS4)) {

            adKey = BuildConfig.SPLASH_AD_KEY_YLH;
        } else if (type.equals(Constant.AD_ADS5)) {

            adKey = BuildConfig.SPLASH_AD_KEY_KS;
        }
        if (adKey != null) {

            mSplashAd = new YdSpread.Builder(SplashActivity.this)
                    .setKey(adKey)
                    .setContainer(binding.splashFlContent)
                    .setSpreadListener(this)
                    .setCountdownSeconds(4)
                    .setSkipViewVisibility(true)
                    .build();

            mSplashAd.requestSpread();
            android.util.Log.d("adadad", "name:" + adKey);
        } else {//没有key的时候使用默认的广告

            dealAdWeb();
        }
    }

    /**
     * 处理web
     */
    private void dealAdWeb() {

        binding.splashFlContent.removeAllViews();
        View adView = LayoutInflater.from(SplashActivity.this).inflate(R.layout.splash_web, null);
        ImageView sw_iv_pic = adView.findViewById(R.id.sw_iv_pic);
        TextView sw_tv_jump = adView.findViewById(R.id.sw_tv_jump);
        binding.splashFlContent.addView(adView);

        Glide.with(adView.getContext())
                .load("http://dev.iyuba.cn/" + dataDTO.getStartuppic())
                .into(sw_iv_pic);
        sw_tv_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (webTimer != null) {

                    webTimer.cancel();
                }

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
        adView.setOnClickListener(view -> {

            if (dataDTO == null) {
                return;
            }
            if (dataDTO.getType().equals("web")) {

                if (!dataDTO.getStartuppicUrl().trim().equals("")) {

                    MyWebActivity.startActivity(SplashActivity.this, dataDTO.getStartuppicUrl(), "详情");
                }
            }
        });
        webTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

                sw_tv_jump.setText("跳转(" + (l / 1000) + "s)");
            }

            @Override
            public void onFinish() {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }.start();
    }

    @Override
    public void onAdDisplay() {
        Log.d("adadad", "onAdDisplay");

    }

    @Override
    public void onAdClose() {

        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onAdClick(String s) {

        isAdCLick = true;
        Log.d("adadad", "onAdClick");
    }

    @Override
    public void onAdFailed(YdError ydError) {

        if (!isRequestTitle) {

            isRequestTitle = true;
            if (dataDTO.getTitle() == null || dataDTO.getTitle().isEmpty()) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            } else {

                dealAds(dataDTO.getTitle());
            }
        } else {

            dealAdWeb();
        }
        Log.d("adadad", "onAdFailed:" + ydError.toString());
    }
}