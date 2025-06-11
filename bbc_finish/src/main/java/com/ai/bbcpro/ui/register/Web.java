package com.ai.bbcpro.ui.register;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import com.ai.bbcpro.R;
import com.jaeger.library.StatusBarUtil;
import com.nostra13.universalimageloader.utils.L;

/**
 * 网页显示
 *
 * @author chentong
 * @version 1.0
 * @para 传入"url" 网址；"title"标题显示
 */
public class Web extends AppCompatActivity {
    private View backButton;
    private WebView web;
    private TextView textView;
    private String titleStr;
    private String curUrl;

    public static void start(Context context , String url , String title ){
        Intent intent  = new Intent(context , Web.class);
        intent.putExtra("url",url);
        intent.putExtra("title",title);
        context.startActivity(intent);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.lib_web);
        StatusBarUtil.setColor(this, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()), 0);

        setProgressBarVisibility(true);

        backButton = findViewById(R.id.lib_button_back);
        textView = (TextView) findViewById(R.id.web_buyiyubi_title);
        web = (WebView) findViewById(R.id.webView);
        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });
        web.loadUrl(this.getIntent().getStringExtra("url"));
        L.e("web:::  " + this.getIntent().getStringExtra("url"));
        web.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null) return false;

                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
            }
        });


        titleStr = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(titleStr)) {
            textView.setText(titleStr);
        }

        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setMediaPlaybackRequiresUserGesture(false);
        web.loadUrl(this.getIntent().getStringExtra("url"));
        web.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        web.getSettings().setUseWideViewPort(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        web.destroy();
    }

}

