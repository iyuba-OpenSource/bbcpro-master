package com.ai.bbcpro.util;


import com.ai.bbcpro.R;
import com.ai.bbcpro.player.SimplePlayer;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 取消和确认的对话框
 *
 * @author Administrator
 */
public class ConfirmDialog extends BaseDialog {

    private Button dialog_btn_cancel, dialog_btn_addword;
    private String keyWord, explanation, pron, audioUrl;
    private Context mContext;
    private Typeface mFace;
    private OnConfirmDialogClickListener listener;

    private TextView tv_key;
    private TextView tv_pron;
    private TextView tv_def;
    //private Player mediaPlayer;
    private SimplePlayer mPlayer;
    private ImageView iv_audio;

    public ConfirmDialog(Context context, String keyWord, String explanation,
                         String pron, String audioUrl, SimplePlayer mediaPlayer, OnConfirmDialogClickListener listener) {
        super(context);
        this.listener = listener;
        this.keyWord = keyWord;
        this.explanation = explanation;
        this.mContext = context;
        this.pron = pron;
        this.audioUrl = audioUrl;
        this.mPlayer = mediaPlayer;
    }

    public void setValue(String keyWord, String explanation, String pron, String audioUrl) {
        this.audioUrl = audioUrl;
        tv_key.setText(keyWord);
        tv_def.setText(explanation);
        tv_pron.setText(Html.fromHtml("[ " + pron + " ]"));
        tv_pron.setTypeface(mFace);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.dialog_confirm);

        tv_key = findViewById(R.id.word_key);
        tv_pron = findViewById(R.id.word_pron);
        tv_def = findViewById(R.id.word_def);
        iv_audio = findViewById(R.id.iv_audio);
        iv_audio.setOnClickListener(this);
        mFace = Typeface.createFromAsset(mContext.getAssets(),
                "font/SEGOEUI.TTF");
        dialog_btn_cancel = findViewById(R.id.dialog_btn_cancel);
        dialog_btn_addword = findViewById(R.id.dialog_btn_addword);
        this.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                //释放播放器资源
                if (mPlayer != null) {
                    mPlayer.release();
                }
            }
        });
    }

    @Override
    protected void initListener() {
        dialog_btn_cancel.setOnClickListener(this);
        dialog_btn_addword.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tv_key.setText(keyWord);

        tv_def.setText(explanation);
        tv_pron.setText(Html.fromHtml("[ " + pron + " ]"));
        tv_pron.setTypeface(mFace);

    }

    @Override
    protected void processClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_btn_cancel:
                if (listener != null) {
                    listener.onCancel();
                    dismiss();
                }
                break;
            case R.id.dialog_btn_addword:
                if (listener != null) {
                    listener.onSave();
                    dismiss();
                }
                break;
            case R.id.iv_audio:
                playerAudio();
                break;
        }

    }

    private void playerAudio() {
        //mPlayer =new AudioPlayer(mContext,new Opscl());
        Log.e("ConfirmDialog", "" + audioUrl);
        if (TextUtils.isEmpty(audioUrl)) {
            return;
        }
        mPlayer.playUrl(audioUrl);
    }


    public interface OnConfirmDialogClickListener {
        void onCancel();

        void onSave();
    }


}

