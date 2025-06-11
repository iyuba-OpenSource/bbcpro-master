package com.ai.bbcpro.ui.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.ai.bbcpro.ui.utils.Tasks;
import com.blankj.utilcode.util.NetworkUtils;

public class LoadingView extends LinearLayout {

    private ProgressBar progressBar;
    private TextView tv;
    private final Context context;
    private View mView;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        mView = LayoutInflater.from(context).inflate(R.layout.loading_view, this, true);
        mView.setVisibility(GONE);
        progressBar = mView.findViewById(R.id.progressBar);
        tv = findViewById(R.id.tv);
    }

    /**
     * loading
     */
    public void showLoading(String msg) {
        mView.setVisibility(VISIBLE);
        progressBar.setVisibility(VISIBLE);
        setText(msg);
    }

    /**
     * 成功
     */
    public void showSuccess(String msg) {
        if (TextUtils.isEmpty(msg)) {
            mView.setVisibility(GONE);
        } else {
            progressBar.setVisibility(GONE);
            setText(msg);
            Tasks.postDelayed2UI(() -> mView.setVisibility(GONE), 1500);
        }

    }

    /**
     *失败
     */
    public void showFail() {
        progressBar.setVisibility(GONE);
        setText(NetworkUtils.isConnected() ? "服务器错误，请稍后重试" :"网络异常，请检查网络后重试");
        Tasks.postDelayed2UI(() -> mView.setVisibility(GONE), 1500);
    }

    /**
     * 提示文字
     *
     * @param txt string
     */
    public void setText(String txt) {
        if (TextUtils.isEmpty(txt)) {
            tv.setVisibility(GONE);
        } else {
            tv.setText(txt);
            tv.setVisibility(VISIBLE);
        }

    }
}

