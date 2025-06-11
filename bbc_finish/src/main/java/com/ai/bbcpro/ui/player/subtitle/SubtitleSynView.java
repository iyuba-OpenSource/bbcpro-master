package com.ai.bbcpro.ui.player.subtitle;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.utils.ToastUtil;

import com.ai.common.CommonConstant;
import com.alibaba.fastjson.JSONObject;

import org.dom4j.DocumentException;
import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 字幕同步View
 *
 * @author lijingwei
 */
public class SubtitleSynView extends ScrollView implements
        TextPageSelectCallBack {
    public boolean syncho = true;  // 是否把当前播放歌词滚动到屏幕中间
    private Context context;
    private LinearLayout subtitleLayout;
    private SubtitleSum subtitleSum;//
    private List<View> subtitleViews;
    private int currParagraph;
    private TextPageSelectCallBack tpstcb;
    private boolean enableSelectText = false;

    String userId = ConfigManager.Instance().loadString("userId");

    private TextPage tp;

    private int currColor;

    private int defColor;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case 0:
                    if (subtitleViews != null && subtitleViews.size() != 0) {
                        int center = 0;
                        for (int i = 0; i < subtitleViews.size(); i++) {
                            TextView textView = (TextView) subtitleViews.get(i);
                            if (currParagraph == i + 1) {
                                textView.setTextColor(currColor);
                                center = textView.getTop() + textView.getHeight() / 2;
                            } else {
                                textView.setTextColor(defColor);
                            }
                        }

                        if (syncho) {
                            center -= getHeight() / 2;
                            if (center > 0) {
                                smoothScrollTo(0, center);
                            } else {
                                smoothScrollTo(0, 0);
                            }
                        }
                    }
                    break;
                case 1:
                    break;
                case 2:
                    Word word = (Word) msg.obj;
                    showWordDialog(word);
                    break;
                case 3:
                    ToastUtil.showToast(getContext(), "收藏成功");
                    break;
                case 4:
                    showLoginDialog();
                    // ToastUtil.showToast(getContext(), "请先登录");
                    break;
            }

            return false;
        }
    });

    public void setTpstcb(TextPageSelectCallBack tpstcb) {
        if (enableSelectText)
            this.tpstcb = tpstcb;
    }

    public SubtitleSynView(Context context) {
        super(context);
        initWidget(context);
    }

    public SubtitleSynView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setVerticalScrollBarEnabled(false);
        initWidget(context);
    }

    public SubtitleSynView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setVerticalScrollBarEnabled(false);
        initWidget(context);
    }

    private void initWidget(Context context) {
        this.context = context;
        subtitleLayout = new LinearLayout(this.context);
        subtitleLayout.setOrientation(LinearLayout.VERTICAL);
        currColor = getResources().getColor(R.color.app_color);
        defColor = getResources().getColor(R.color.gray);
    }

    public void setTextSize(int textSize) {
        if (subtitleSum != null && subtitleSum.subtitles.size() != 0) {
            for (int i = 0; i < subtitleSum.subtitles.size(); i++) {
                TextView textView = (TextView) subtitleViews.get(i);
                textView.setTextSize(textSize);
            }
        }
    }

    public void setSubtitleSum(SubtitleSum subtitleSum) {
        this.subtitleSum = subtitleSum;
        subtitleLayout.removeAllViews();
        removeAllViews();
        initSubtitleSum();

    }

    @SuppressLint("ResourceAsColor")
    public void initSubtitleSum() {
        if (subtitleSum != null && subtitleSum.subtitles.size() != 0) {
            subtitleViews = new ArrayList<View>();
            for (int i = 0; i < subtitleSum.subtitles.size(); i++) {
                tp = new TextPage(this.context);
                tp.setBackgroundColor(Color.WHITE);
                tp.setTextColor(defColor);
                // tp.setTextSize(16 + ConfigManager.Instance().getfontSizeLevel() * 2);
                tp.setLineSpacing(3f, 1.3f);
                if (currParagraph == i) {
                    tp.setTextColor(currColor);
                }
                tp.setText(subtitleSum.subtitles.get(i).content);
                tp.setTextpageSelectCallBack(this);
                final int current = i;
                tp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // tpstcb.selectParagraph(current);
                    }
                });

                subtitleViews.add(tp);
                subtitleLayout.addView(tp);
            }
        }
        addView(subtitleLayout);
    }

    public void showWordDialog(Word word) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_word_trans);
            window.setGravity(Gravity.CENTER);

            TextView wordContent = window.findViewById(R.id.word_content);
            TextView wordTrans = window.findViewById(R.id.word_trans);
            TextView wordCancel = window.findViewById(R.id.word_cancel);
            TextView wordAdd = window.findViewById(R.id.word_add);

            String temp1 = word.getContent() + "[" + word.getPhonetic() + "]";
            String temp2 = word.getExplanation();

            wordContent.setText(temp1);
            wordTrans.setText(temp2);

            wordCancel.setOnClickListener(v -> {
                alertDialog.cancel();
            });

            wordAdd.setOnClickListener(v -> {
                if (!AccountManager.Instance(context).checkUserLogin()) {
                    alertDialog.cancel();
                    Message msg = new Message();
                    msg.what = 4;
                    handler.sendMessage(msg);
                } else {
                    addWord(word.getContent());
                    alertDialog.cancel();
                }
            });
        }
    }

    // 收藏单词
    void addWord(String word) {
        String url = "http://word." + CommonConstant.domain + "/words/updateWord.jsp?userId=" + userId + "&mod=insert&groupName=Iyuba&word=" + word;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ToastUtil.showToast(getContext(), "网络不佳，请稍后重试。");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                JSONObject jo = null;
                try {
                    jo = Xml2Json.xml2Json(Objects.requireNonNull(response.body()).string());
                    String result = jo.getString("result");
                    if ("1".equals(result)) {
                        Message msg = new Message();
                        msg.what = 3;
                        handler.sendMessage(msg);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @SuppressLint("ResourceAsColor")
    public void changeToEn() {
        subtitleLayout.removeAllViews();
        removeAllViews();
        if (subtitleSum != null && subtitleSum.subtitles.size() != 0) {
            subtitleViews = new ArrayList<View>();
            for (int i = 0; i < subtitleSum.subtitles.size(); i++) {
                tp = new TextPage(this.context);
                tp.setBackgroundColor(Color.WHITE);
                tp.setTextColor(defColor);
                // tp.setTextSize(16 + ConfigManager.Instance().getfontSizeLevel() * 2);
                tp.setLineSpacing(3f, 1.3f);
                if (currParagraph == i) {
                    tp.setTextColor(currColor);
                }
                tp.setText(subtitleSum.subtitles.get(i).content);
                tp.setTextpageSelectCallBack(this);
                final int current = i;
                tp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // tpstcb.selectParagraph(current);
                    }
                });

                subtitleViews.add(tp);
                subtitleLayout.addView(tp);
            }
        }
        addView(subtitleLayout);
    }

    @SuppressLint("ResourceAsColor")
    public void changeToCN() {
        subtitleLayout.removeAllViews();
        removeAllViews();
        if (subtitleSum != null && subtitleSum.subtitles.size() != 0) {
            subtitleViews = new ArrayList<View>();
            for (int i = 0; i < subtitleSum.subtitles.size(); i++) {
                tp = new TextPage(this.context);
                tp.setBackgroundColor(Color.WHITE);
                tp.setTextColor(defColor);
                // tp.setTextSize(16 + ConfigManager.Instance().getfontSizeLevel() * 2);
                tp.setLineSpacing(3f, 1.3f);
                if (currParagraph == i) {
                    tp.setTextColor(currColor);
                }
                tp.setText(subtitleSum.subtitles.get(i).content + "\n" + subtitleSum.subtitles.get(i).contentCn);
                tp.setTextpageSelectCallBack(this);
                final int current = i;
                tp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // tpstcb.selectParagraph(current);
                    }
                });

                subtitleViews.add(tp);
                subtitleLayout.addView(tp);
            }
        }
        addView(subtitleLayout);
    }

    @Override
    public void selectTextEvent(String selectText) {
        System.out.println("选中的单词是=================>" + selectText);
        String url = "http://word." + CommonConstant.domain + "/words/apiWord.jsp?q=" + selectText;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                ToastUtil.showToast(getContext(), "网络不佳，请稍后重试。");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // System.out.println("==================查询单词" + response.body().string());
                JSONObject jo = null;
                try {
                    jo = Xml2Json.xml2Json(response.body().string());
                    String result = jo.getString("result");
                    if (!result.equals("0")) {

                        String part1 = jo.getString("key");
                        String part2 = jo.getString("pron");
                        String part3 = jo.getString("def");
                        Message msg = new Message();
                        msg.what = 2;
                        msg.obj = new Word(part1, part2, part3);
                        handler.sendMessage(msg);
                    } else {

                        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(MainApplication.getApplication(), "未查询到信息", Toast.LENGTH_SHORT).show());
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
            }
        });


        // tpstcb.selectTextEvent(selectText);
    }


    /**
     * 段落高亮跳转
     *
     * @param paragraph
     */
    public void snyParagraph(int paragraph) {
        currParagraph = paragraph;
        handler.sendEmptyMessage(0);
    }

    public void unsnyParagraph() {
        handler.removeMessages(0);
    }

    public void updateSubtitleView() {
        if (subtitleSum != null && subtitleSum.subtitles.size() != 0 && subtitleViews != null && subtitleViews.size() != 0) {
            for (int i = 0; i < subtitleSum.subtitles.size(); i++) {
                TextPage tp = (TextPage) subtitleViews.get(i);
                tp.setText(subtitleSum.subtitles.get(i).content);
            }
            snyParagraph(currParagraph);
        }
    }

    public void setCurrParagraph(int currParagraph) {
        this.currParagraph = currParagraph;
    }

    public int getCurrParagraph() {
        return currParagraph;
    }

    @Override
    public void selectParagraph(int paragraph) {
        tpstcb.selectParagraph(paragraph);
    }

    public void showLoginDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_diy);
            window.setGravity(Gravity.CENTER);

            TextView tvContent = window.findViewById(R.id.dialog_content);
            tvContent.setText("您还未登录，是否跳转登录界面？");

            TextView tvCancel = window.findViewById(R.id.dialog_cancel);
            TextView tvAgree = window.findViewById(R.id.dialog_agree);
            tvAgree.setText("跳转登录");

            tvCancel.setOnClickListener(v -> {
                alertDialog.cancel();
            });

            tvAgree.setOnClickListener(v -> {
                alertDialog.cancel();
                // 跳转登录界面
                EventBus.getDefault().post(new LoginEventbus());;
            });
        }
    }
}

