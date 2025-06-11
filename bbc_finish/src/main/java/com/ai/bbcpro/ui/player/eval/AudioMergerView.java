package com.ai.bbcpro.ui.player.eval;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ai.bbcpro.R;
import com.iyuba.play.OnStateChangeListener;
import com.iyuba.play.Player;
import com.iyuba.play.State;

import timber.log.Timber;

public class AudioMergerView extends ConstraintLayout implements View.OnClickListener {
    private static final String DEFAULT_TIME = "00:00";
    private TextView mCurrentTv, mTotalTv, mMergeTv, mListenTv, mPublishTv, mShareTv, mScoreTv;
    private ImageButton mPauseBtn;
    private SeekBar mSeekBar;
    private Player mPlayer;
    private boolean mEnableShare = true;
    private ActionListener mActionListener;
    private String audioFilePath;
    private MergeAudioMeta mMetaData;
    private MergeAudioResponse mResponse;

    private Callback callback;

    public AudioMergerView(Context context) {
        super(context);
    }

    public AudioMergerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AudioMergerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(getContext(), R.layout.function_merge, this);
        initView();
        mCurrentTv.setText(DEFAULT_TIME);
        mTotalTv.setText(DEFAULT_TIME);
        mPublishTv.setEnabled(false);
        mSeekBar.setOnSeekBarChangeListener(mSeekListener);
        mSeekBar.setEnabled(false);

    }

    private void initView() {
        mCurrentTv = findViewById(R.id.text_current);
        mTotalTv = findViewById(R.id.text_total);
        mSeekBar = findViewById(R.id.seek_bar);
        mMergeTv = findViewById(R.id.text_merge);
        mListenTv = findViewById(R.id.text_listen);
        mPublishTv = findViewById(R.id.text_publish);
        mShareTv = findViewById(R.id.text_share);
        mPauseBtn = findViewById(R.id.image_button_pause);
        mScoreTv = findViewById(R.id.text_score);
        mCurrentTv.setOnClickListener(this);
        mTotalTv.setOnClickListener(this);
        mSeekBar.setOnClickListener(this);
        mMergeTv.setOnClickListener(this);
        mListenTv.setOnClickListener(this);
        mPublishTv.setOnClickListener(this);
        mShareTv.setOnClickListener(this);
        mPauseBtn.setOnClickListener(this);
        mScoreTv.setOnClickListener(this);
    }

    public void setEnableShare(boolean value) {
        mEnableShare = value;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_merge:
                mMergeTv.setClickable(false);
                if (mActionListener != null) mActionListener.onMerge();
                break;
            case R.id.text_listen:

                if (callback != null) {

                    callback.stopMediaPlayer();
                }
                checkPlayer();
                if (mPlayer.isPausing() || mPlayer.isCompleted() || mPlayer.isInterrupted()) {
                    mPlayer.start();
                } else {
                    if (!TextUtils.isEmpty(audioFilePath)) {
                        mPlayer.startToPlay(audioFilePath);
                    }
                }
                break;
            case R.id.image_button_pause:
                if (mPlayer == null) return;
                mPlayer.pause();
                break;
            case R.id.text_publish:
                if (mActionListener != null) mActionListener.onPublish(audioFilePath, mMetaData);
                break;
            case R.id.text_share:
                if (mActionListener != null && mResponse != null)
                    mActionListener.onShare(mResponse);
                break;
        }
    }

    public interface ActionListener {
        void onMerge();

        void onPublish(String mergeAudio, MergeAudioMeta metaData);

        void onShare(MergeAudioResponse response);
    }

    public void destroy() {
        mProgressHandler.removeCallbacksAndMessages(null);
        if (mPlayer != null) mPlayer.stopAndRelease();
    }

    public void setAudioFile(String filePath, MergeAudioMeta meta) {
        if (!TextUtils.isEmpty(filePath) && meta != null) {
            audioFilePath = filePath;
            mMetaData = meta;
            checkPlayer();
            mPlayer.reset();
            mScoreTv.setVisibility(View.VISIBLE);
            setReadScoreViewContent(mScoreTv, meta.score);
            mMergeTv.setVisibility(View.GONE);
            mListenTv.setVisibility(View.VISIBLE);
            mPublishTv.setEnabled(true);
        }
    }

    private void setReadScoreViewContent(TextView view, int score) {
        if (score < 60) {
            view.setText("");
            view.setBackgroundResource(R.drawable.sen_score_lower60);
        } else if (score > 80) {
            view.setText(String.valueOf(score));
            view.setBackgroundResource(R.drawable.merge_score_higher_80);
        } else {
            view.setText(String.valueOf(score));
            view.setBackgroundResource(R.drawable.merge_score_60_80);
        }
    }

    public void setResponse(MergeAudioResponse response) {
        if (response != null) {
            mResponse = response;
            mPublishTv.setVisibility(View.GONE);
            if (mEnableShare) mShareTv.setVisibility(View.VISIBLE);
        }
    }

    public void reset() {
        mCurrentTv.setText(DEFAULT_TIME);
        mTotalTv.setText(DEFAULT_TIME);
        audioFilePath = "";
        mMetaData = null;
        mResponse = null;
        checkPlayer();
        try {
            if (mPlayer != null)
                mPlayer.reset();//???????
        } catch (Exception e) {
            e.printStackTrace();
        }

        mScoreTv.setVisibility(View.GONE);
        mMergeTv.setVisibility(View.VISIBLE);
        mMergeTv.setClickable(true);
        mListenTv.setVisibility(View.GONE);
        mPauseBtn.setVisibility(View.GONE);
        mShareTv.setVisibility(View.GONE);
        mPublishTv.setVisibility(View.VISIBLE);
        mPublishTv.setEnabled(false);
        mSeekBar.setProgress(0);
        mSeekBar.setEnabled(false);
    }

    public void setPublish(boolean isEnable) {
        mPublishTv.setEnabled(isEnable);
    }

    public void setActionListener(ActionListener l) {
        mActionListener = l;
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                mPlayer.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    private void checkPlayer() {
        if (mPlayer == null) {
            mPlayer = new Player();
            mPlayer.setOnStateChangeListener(mListener);
        } else {
            Timber.d("player is initialized!");
        }
    }

    private OnStateChangeListener mListener = new OnStateChangeListener() {
        @Override
        public void onStateChange(int newState) {
            switch (newState) {
                case State.PREPARED:
                    mSeekBar.setEnabled(true);
                    int duration = mPlayer.getDuration();
                    mSeekBar.setMax(duration);
                    mSeekBar.setProgress(0);
                    mTotalTv.setText(msecToFormat(duration));
                    mCurrentTv.setText(msecToFormat(0));
                    break;
                case State.PLAYING:
                    mListenTv.setVisibility(View.GONE);
                    mPauseBtn.setVisibility(View.VISIBLE);
                    mProgressHandler.sendEmptyMessage(0);
                    break;
                case State.PAUSED:
                case State.INTERRUPTED:
                case State.COMPLETED:
                case State.ERROR:
                    mListenTv.setVisibility(View.VISIBLE);
                    mPauseBtn.setVisibility(View.GONE);
                    mProgressHandler.removeMessages(0);
                    break;
            }
        }
    };

    private Handler mProgressHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int progress = mPlayer.getCurrentPosition();
            mSeekBar.setProgress(progress);
            mCurrentTv.setText(msecToFormat(progress));
            mProgressHandler.sendEmptyMessageDelayed(0, 200);
            return false;
        }
    });

    private String msecToFormat(int msec) {
        StringBuilder sb = new StringBuilder();
        int minute = msec / 60000;
        int second = (msec / 1000) % 60;
        if (minute < 10)
            sb.append("0");
        sb.append(minute).append(":");
        if (second < 10)
            sb.append("0");
        sb.append(second);
        return sb.toString();
    }

    public interface Callback {

        /**
         * 停止音播放频，录音
         */
        void stopMediaPlayer();
    }

}
