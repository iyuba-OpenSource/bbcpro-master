package com.ai.bbcpro.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.newSection.T;
import com.jaeger.library.StatusBarUtil;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;

public abstract class DeepBaseActivity extends AppCompatActivity {
    protected Activity mContext;
//    protected SwipeBackHelper swipeBackHelper;
//    protected SimpleNightMode simpleNightMode;
    public ProgressDialog dialog;
    private Boolean isActive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = this;

        if (isSwipeBackEnable()) {
//            swipeBackHelper = new SwipeBackHelper(this);
        }

        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Class decorViewClazz = Class.forName("com.android.internal.policy.DecorView");
                Field field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor");
                field.setAccessible(true);
                field.setInt(getWindow().getDecorView(), Color.TRANSPARENT);  //改为透明
            } catch (Exception e) {
            }
        }
        setAndroidNativeLightStatusBar(this, true);
//        simpleNightMode = new SimpleNightMode(this);
        initDialog();

    }

    private void initDialog() {
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.setIndeterminate(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        simpleNightMode.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
//        simpleNightMode.close();
        super.onDestroy();
    }

    protected <T extends View> T findView(@IdRes int id) {
        return (T) findViewById(id);
    }

    public void showShort(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showShort(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void showLong(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showLong(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void e(String msg) {
        L.e(msg);
    }

    public void w(String msg) {
        L.w(msg);
    }

    public void d(String msg) {
        L.d(msg);
    }

    protected boolean isSwipeBackEnable() {
        return true;
    }
    

    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColor(this, color);
    }

    protected void setSwipeBStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColorForSwipeBack(this, color);
    }

    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
