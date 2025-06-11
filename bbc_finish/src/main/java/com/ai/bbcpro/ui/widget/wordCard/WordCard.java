package com.ai.bbcpro.ui.widget.wordCard;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ai.bbcpro.R;

import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.helper.GoToVipHelper;
import com.ai.bbcpro.ui.player.subtitle.Xml2Json;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.word.Word;
import com.ai.common.CommonConstant;
import com.alibaba.fastjson.JSONObject;
import com.iyuba.module.toolbox.EnDecodeUtils;
import com.iyuba.play.Player;

import org.dom4j.DocumentException;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WordCard extends FrameLayout {
    private String TAG = "WordCard";
    private ProgressBar mPB;
    private LinearLayout mContentContainer;
    private TextView mWordTv;
    private TextView mDefinitionTv;
    private TextView mPronTv;
    private ImageView mSpeakerIv;
    private Activity mActivity;
    private Word wordQueryResult;
    private WordCard.ActionDelegate mDelegate;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    onWordQueryResult((Word) msg.obj);
                    break;
                case 1:
                    ToastUtil.showToast(getContext(), "收藏成功");
                    break;
            }
        }
    };

    public WordCard(Context context) {
        this(context, (AttributeSet)null);
    }

    public WordCard(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public WordCard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    public void setActionDelegate(WordCard.ActionDelegate delegate) {
        this.mDelegate = delegate;
    }

    private void init() {
        inflate(this.getContext(), R.layout.headline_partial_wordcard, this);
        this.mContentContainer = (LinearLayout)this.findViewById(R.id.container);
        this.mPB = (ProgressBar)this.findViewById(R.id.progressbar);
        this.mWordTv = (TextView)this.findViewById(R.id.text_word);
        this.mDefinitionTv = (TextView)this.findViewById(R.id.text_definition);
        this.mPronTv = (TextView)this.findViewById(R.id.text_pron);
        this.mSpeakerIv = (ImageView)this.findViewById(R.id.image_speaker);
        this.mSpeakerIv.setOnClickListener(this::clickSpeaker);
        this.findViewById(R.id.text_collect).setOnClickListener(this::clickCollect);
        this.findViewById(R.id.image_close).setOnClickListener(this::clickClose);
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    public void onSearch(String selectWord) {
        Log.e(TAG, "onSearch: "+selectWord );
        this.setVisibility(View.VISIBLE);
        this.mContentContainer.setVisibility(View.GONE);
        this.mPB.setVisibility(View.VISIBLE);
        String url = "http://word."+ CommonConstant.domain +"/words/apiWord.jsp?q=" + selectWord;
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
                Word word = new Word();
                JSONObject jo = null;
                try {
                    jo = Xml2Json.xml2Json(response.body().string());
                    word.audioUrl = jo.getString("audio");
                    word.pron = jo.getString("pron");
                    word.def = jo.getString("def");
                    word.key = jo.getString("key");
                } catch (DocumentException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.what = 0;
                message.obj = word;
                mHandler.sendMessage(message);
            }
        });
    }



    public void onWordQueryResult(Word word) {
        this.wordQueryResult = word;
        this.mPB.setVisibility(View.GONE);
        this.mContentContainer.setVisibility(View.VISIBLE);
        this.mWordTv.setText(this.wordQueryResult.key);
        this.mDefinitionTv.setText(this.wordQueryResult.def);
        if (!TextUtils.isEmpty(this.wordQueryResult.pron) && !"null".equals(this.wordQueryResult.pron)) {
            this.mPronTv.setVisibility(View.VISIBLE);
            StringBuilder sb = new StringBuilder();
            sb.append('[').append(EnDecodeUtils.decode(this.wordQueryResult.pron)).append(']');
            this.mPronTv.setText(sb.toString());
        } else {
            this.mPronTv.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(this.wordQueryResult.audioUrl)) {
            this.mSpeakerIv.setVisibility(View.VISIBLE);
        } else {
            this.mSpeakerIv.setVisibility(View.GONE);
        }

    }

    private void clickSpeaker(View view) {
        if (this.mDelegate != null && this.wordQueryResult != null && !TextUtils.isEmpty(this.wordQueryResult.audioUrl)) {
            this.mDelegate.getPlayer().startToPlay(this.wordQueryResult.audioUrl);
        }

    }

    private void clickCollect(View view) {
        if ( this.wordQueryResult != null && !TextUtils.isEmpty(this.wordQueryResult.key)) {
            if(AccountManager.Instance(getContext()).checkUserLogin()){
                addWord(wordQueryResult.key);
            }else{
                GoToVipHelper.getInstance().goToLogin(mActivity);
            }
        }

    }

    void addWord(String word) {
        String userId = ConfigManager.Instance().loadString("userId");
        String url = "http://word."+ CommonConstant.domain +"/words/updateWord.jsp?userId="+userId+"&mod=insert&groupName=Iyuba&word="+word;
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
                        msg.what = 1;
                        mHandler.sendMessage(msg);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void clickClose(View view) {
        this.animateHide();
    }

    public void animateHide() {
        TranslateAnimation animation = new TranslateAnimation(1, 0.0F, 1, 1.0F, 1, 0.0F, 1, 0.0F);
        animation.setDuration(200L);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                WordCard.this.setVisibility(View.GONE);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.startAnimation(animation);
    }

    public interface ActionDelegate {
        Player getPlayer();

        void collectWord(String var1);
    }
}
