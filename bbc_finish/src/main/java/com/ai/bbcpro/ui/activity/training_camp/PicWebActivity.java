package com.ai.bbcpro.ui.activity.training_camp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.databinding.ActivityPicWebBinding;

/**
 * 网页页面
 */
public class PicWebActivity extends AppCompatActivity {

    private ActivityPicWebBinding activityMyWebBinding;

    private String url;

    private String title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyWebBinding = ActivityPicWebBinding.inflate(getLayoutInflater());
        setContentView(activityMyWebBinding.getRoot());


        getBundle();


        activityMyWebBinding.toolbar.toolbarTvTitle.setText(title);
        activityMyWebBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        WebSettings webSettings = activityMyWebBinding.webWvWeb.getSettings();
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);

        activityMyWebBinding.webWvWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

        String htmlStr = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset=\"utf-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;\"/>" +
                "<title></title>" +
                "<style>img{width: 100%;height:auto;}" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div id=\"div\" >" +
                "<img src=\"" + url + "\" />" +
                "</div>" +
                "</body>" +
                "</html>";

        activityMyWebBinding.webWvWeb.loadDataWithBaseURL(null, htmlStr, "text/html", "utf-8", null);
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            url = bundle.getString("URL");
            title = bundle.getString("TITLE");
        }
    }

    public static void startActivity(Activity activity, String url, String title) {

        Intent intent = new Intent(activity, PicWebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("TITLE", title);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }
}