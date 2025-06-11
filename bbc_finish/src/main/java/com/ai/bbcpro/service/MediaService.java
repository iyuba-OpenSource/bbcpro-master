package com.ai.bbcpro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.event.MediaCompleteEventbus;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.mvp.presenter.MediaPresenter;
import com.ai.bbcpro.mvp.view.MediaContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.ai.bbcpro.ui.player.eval.RecycleViewData;
import com.ai.bbcpro.util.DateUtil;
import com.ai.common.CommonConstant;
import com.iyuba.module.toolbox.MD5;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

/**
 * 新闻音频播放器
 */
public class MediaService extends Service implements MediaContract.MediaView {

    private static final String TAG = "MediaService";
    private MediaPlayer mPlayer;

    /**
     * 用来判断是不是全部内容,来实现顺序播放
     */
    private int parentId;

    private SumBean.DataDTO playDataDTO;
    /**
     * 随机播放
     */
    public final static int MODE_RANDOM = 1;

    /**
     * 列表播放
     */
    public final static int MODE_LIST = 2;

    /**
     * 单曲循环
     */
    public final static int MODE_SINGLE = 3;

    /**
     * 默认播放模式为3
     */
    private int mode = 3;

    /**
     * 格式化id
     */
    private DecimalFormat decimalFormat;


    /**
     * 通知栏上的点击事件
     */
    public static String FLAG_MUSIC_PLAY = MainApplication.getApplication().getPackageName() + ".play";

    /**
     * 启动教材首页的绑定service
     */
    public static String FLAG_MEDIA_PLAY = MainApplication.getApplication().getPackageName() + ".player";

    /**
     * 在broadcast中更改完播放转状态触发，更新教材首页播放器、详情页播放器的状态及更新
     */
    public static String FLAG_MEDIA_BROADCAST = MainApplication.getApplication().getPackageName() + ".broadcast.update_play_state";

    /**
     * 随机播放、顺数播放引起的   详情页切换数据   教材首页播放器更新title及播放状态
     */
    public static String FLAG_SWITCH_DATA = MainApplication.getApplication().getPackageName() + ".broadcast.switch_data";

    private NotificationManagerCompat nManagerCompat;

    private AudioManager audioManager;

    /**
     * android 8.0的音频焦点
     */
    private AudioFocusRequest audioFocusRequest;


    /**
     * presenter
     */
    private MediaPresenter mediaPresenter;

    /**
     * 数据库
     */
    private HeadlinesDataManager headlinesDataManager;

    /**
     * 上传听力进度的
     * 开始时间
     */
    private String beginTime;

    private MusicController musicController;

    /**
     * 格式化id，如1->001
     *
     * @param id
     * @return
     */
    private String formatId(String id) {

        return decimalFormat.format(Integer.parseInt(id));
    }

    @Override
    public void saveNewContentComplete(int logPosition) {

        String soundUrl = null;
        if (playDataDTO.getSound().equals("http://")) {

            soundUrl = playDataDTO.getSound();
        } else {
            soundUrl = CommonConstant.URL_STATICVIP + "/sounds/minutes/" + playDataDTO.getSound();
        }
        try {
            mPlayer.reset();
            mPlayer.setDataSource(soundUrl);
            mPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //通知
        SumBean.DataDTO dataDTO = playDataDTO;
        initNotification(dataDTO.getTitle(), R.mipmap.icon_home_pause);

        //切换音频数据源
        LocalBroadcastManager.getInstance(MainApplication.getApplication()).sendBroadcast(new Intent(FLAG_SWITCH_DATA));
    }

    public class MusicController extends Binder {

        public void setParentId(int parentId) {

            MediaService.this.parentId = parentId;
        }

        /**
         * 获取数据源
         *
         * @return
         */
        public void setData(SumBean.DataDTO dataDTO) {

            MediaService.this.playDataDTO = dataDTO;
        }

        /**
         * 课文
         *
         * @return
         */
        public SumBean.DataDTO getCurData() {

            if (playDataDTO != null) {

                return playDataDTO;
            } else {
                return null;
            }
        }

        public void play() {

            //"http://staticvip." + WebConstant.IYBHttpHead + "/sounds/minutes/" + sound

            SumBean.DataDTO dataDTO = playDataDTO;
            String soundUrl;
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), dataDTO.getBbcId() + ".mp3");
            if (file.exists()) {

                soundUrl = file.getAbsolutePath();
            } else {

                if (dataDTO.getSound().startsWith("http://")) {

                    soundUrl = dataDTO.getSound();
                } else {
                    soundUrl = CommonConstant.URL_STATICVIP + "/sounds/minutes/" + dataDTO.getSound();
                }
            }
            try {
                mPlayer.reset();
                if (soundUrl.startsWith("http://")) {

                    mPlayer.setDataSource(soundUrl);
                } else {

                    mPlayer.setDataSource(getApplicationContext(), Uri.parse(soundUrl));
                }

                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

            initNotification(dataDTO.getTitle(), R.mipmap.icon_home_pause);
        }


        /**
         * 播放
         */
        public void playFamous() {

            if (playDataDTO != null && playDataDTO.getAnnouncerBean() != null) {

                String famousUrl = playDataDTO.getAnnouncerBean().getPath().replace("/data/projects/", "http://iuserspeech.iyuba.cn:9001/");
                try {
                    mPlayer.reset();
                    mPlayer.setDataSource(famousUrl);
                    mPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public boolean isPlaying() {

            return mPlayer.isPlaying();
        }

        public void pause() {

            mPlayer.pause();  //暂停音乐
            sendPlayCurrent(0);
            //通知
            SumBean.DataDTO dataDTO = playDataDTO;
            initNotification(dataDTO.getTitle(), R.mipmap.icon_home_play);

            LocalBroadcastManager.getInstance(MainApplication.getApplication()).sendBroadcast(new Intent(MediaService.FLAG_MEDIA_BROADCAST));
        }

        public long getMusicDuration() {
            return mPlayer.getDuration();  //获取文件的总长度
        }

        public long getPosition() {
            return mPlayer.getCurrentPosition();  //获取当前播放进度
        }

        public void seek(int position) {
            mPlayer.seekTo(position);  //重新设定播放进度
        }

        public void start() {

            mPlayer.start();
            initAudioManager();
            beginTime = DateUtil.getCurTime();

            //更新播放按钮的状态
            LocalBroadcastManager
                    .getInstance(MainApplication.getApplication())
                    .sendBroadcast(new Intent(FLAG_MEDIA_BROADCAST));

            SumBean.DataDTO dataDTO = playDataDTO;
            initNotification(dataDTO.getTitle(), R.mipmap.icon_home_pause);
        }

        public void setPlaySpeed(float speed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PlaybackParams params = mPlayer.getPlaybackParams();
                params.setSpeed(speed);
//                mPlayer.setPlaybackParams(params);
            }
        }


        /**
         * 设置播放模式
         *
         * @param mode
         */
        public void setPlayMode(int mode) {

            MediaService.this.mode = mode;
        }

        /**
         * 获取模式
         *
         * @return
         */
        public int getMode() {

            return mode;
        }
    }

    /**
     * 当绑定服务的时候，自动回调这个方法
     * 返回的对象可以直接操作Service内部的内容
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        if (musicController == null) {

            musicController = new MusicController();
        }
        return musicController;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        position = intent.getIntExtra("POSITION", 0);
//        itemList = (List<SumBean.DataDTO>) intent.getSerializableExtra("DATA");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mode = MODE_SINGLE;
        decimalFormat = new DecimalFormat("#000");

        mediaPresenter = new MediaPresenter();
        mediaPresenter.attchView(this);
        headlinesDataManager = HeadlinesDataManager.getInstance(getApplicationContext());

        initMediaPlayer();
//        initNotification();
        //广播接收器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MUSIC_PLAY);
        registerReceiver(new MusicBroadcast(), intentFilter);
    }


    /**
     * 将播放进度发送到服务器
     * endflag 0暂停  1 播放完成
     */
    private void sendPlayCurrent(int endflag) {

        String uid = ConfigManager.Instance().loadString("userId", "0");
        if (!uid.equals("0")) {

            int currentPosition;
            if (endflag == 0) {//是否播放完成

                currentPosition = mPlayer.getCurrentPosition();
            } else {

                currentPosition = mPlayer.getDuration();
            }
            int id = Integer.parseInt(playDataDTO.getBbcId());
            String sign = MD5.getMD5ofStr(uid + beginTime + DateUtil.getCurTime());

            //计算当前播放到第几句
            int index = 0;
            List<RecycleViewData> viewDataList = headlinesDataManager.getEvalSentenceList(id);
            for (int i = 0; i < viewDataList.size(); i++) {

                RecycleViewData recycleViewData = viewDataList.get(i);
                int sTime = (int) (Double.parseDouble(recycleViewData.getTiming()) * 1000);
                int eTime = (int) (Double.parseDouble(recycleViewData.getEndTiming()) * 1000);
                if (currentPosition >= sTime && currentPosition <= eTime) {

                    index = i;
                    break;
                }
            }
            String endTime = DateUtil.getCurTime();
            mediaPresenter.updateStudyRecordNew("json", uid, beginTime, endTime, "BBC",
                    "1", 0 + "", "android", Constant.APPID, "",
                    id + "", sign, endflag, index);
        }
    }


    /**
     * 初始化播放器
     */
    private void initMediaPlayer() {

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setLooping(false);
        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mPlayer.start();
                initAudioManager();
                beginTime = DateUtil.getCurTime();

                //更新播放按钮的状态
                LocalBroadcastManager
                        .getInstance(MainApplication.getApplication())
                        .sendBroadcast(new Intent(FLAG_MEDIA_BROADCAST));
            }
        });
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                EventBus.getDefault().post(new MediaCompleteEventbus());

                //上传播放进度
                sendPlayCurrent(1);
                //处理要播放的位置
                if (mode == MODE_LIST) {

                    SumBean.DataDTO dataDTO;
                    if (parentId == 0) {

                        dataDTO = headlinesDataManager.getNextNewsByParent0(playDataDTO.getCreatTime());
                    } else {

                        dataDTO = headlinesDataManager.getNextNewsByCate(Integer.parseInt(playDataDTO.getBbcId()), Integer.parseInt(playDataDTO.getCategory()));
                    }
                    if (dataDTO == null) {

                        dataDTO = headlinesDataManager.getMaxNewsByCate(Integer.parseInt(playDataDTO.getCategory()));
                        if (dataDTO != null) {

                            playDataDTO = dataDTO;
                        }
                    } else {

                        playDataDTO = dataDTO;
                    }
                } else if (mode == MODE_RANDOM) {//随机播放

                    SumBean.DataDTO dataDTO;
                    if (parentId == 0) {

                        dataDTO = headlinesDataManager.getRandomNewsByParent0(Integer.parseInt(playDataDTO.getBbcId()));
                    } else {

                        dataDTO = headlinesDataManager.getRandomNewsByCate(Integer.parseInt(playDataDTO.getBbcId()), Integer.parseInt(playDataDTO.getCategory()));
                    }
                    if (dataDTO != null) {

                        playDataDTO = dataDTO;
                    }
                } else {
                    //什么都不做
                }
                long count = headlinesDataManager.hasQuestionBean(Integer.parseInt(playDataDTO.getBbcId()));
                if (count > 0) {//含有数据

                    saveNewContentComplete(-1);
                } else {//没有数据

                    mediaPresenter.textAllApi("json", Integer.parseInt(playDataDTO.getBbcId()), -1);
                }
            }
        });
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
    }


    /**
     * 初始化通知
     */
    private void initNotification(String title, int imgres) {


        Intent intent = new Intent(MediaService.FLAG_MUSIC_PLAY);

        PendingIntent playOrPausependingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

            playOrPausependingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            playOrPausependingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {

            playOrPausependingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        //点击通知打开app
        Intent openIntent = new Intent(getApplicationContext(), AudioContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("MY_ACTION", "OPEN");
        bundle.putSerializable("DATA", playDataDTO);
        openIntent.putExtras(bundle);
        PendingIntent openPendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            openPendingIntent = PendingIntent.getActivity(getApplicationContext(), 200, openIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } else {

            openPendingIntent = PendingIntent.getActivity(getApplicationContext(), 200, openIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification_music);
        remoteViews.setOnClickPendingIntent(R.id.music_iv_but, playOrPausependingIntent);
        remoteViews.setOnClickPendingIntent(R.id.music_fl_notifi, openPendingIntent);
        //设置播放按钮
        remoteViews.setImageViewResource(R.id.music_iv_but, imgres);
        //设置标题
        remoteViews.setTextViewText(R.id.music_tv_title, title);
        //设置背景
        remoteViews.setImageViewResource(R.id.music_iv_bg, R.drawable.bbe);

        nManagerCompat = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {//android8.0及以上的通道

            NotificationChannel notificationChannel = new NotificationChannel("MediaServiceChannel", "MediaServiceChannel", NotificationManager.IMPORTANCE_DEFAULT);
            nManagerCompat.createNotificationChannel(notificationChannel);
        }

        Notification notification;
        if (imgres == R.mipmap.icon_home_play) {

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.drawable.bbce)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(false)
                    .build();
        } else {

            notification = new NotificationCompat.Builder(getApplicationContext(), "MediaServiceChannel")
                    .setCustomContentView(remoteViews)
                    .setSmallIcon(R.drawable.bbce)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setOngoing(true)
                    .build();
        }
        nManagerCompat.notify(1, notification);
    }

    /**
     * 初始化音频焦点，用来处理串音的问题
     */
    private void initAudioManager() {

        if (audioManager == null) {

            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }

        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上处理

            if (audioFocusRequest == null) {

                audioFocusRequest
                        = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                        .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                        .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                        .build();
            }
            flag = audioManager.requestAudioFocus(audioFocusRequest);
        } else {

            flag = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        }
        if (flag == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            Log.d(TAG, "音频焦点获取成功");
        } else {

            Log.d(TAG, "音频焦点获取失败");
        }
    }

    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            Log.d(TAG, focusChange + "");
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    //当其他应用申请焦点之后又释放焦点会触发此回调
                    //可重新播放音乐
                    Log.d(TAG, "AUDIOFOCUS_GAIN");

                    /*if (mPlayer != null) {

                        mPlayer.start();
                        remoteViews.setImageViewResource(R.id.music_iv_but, R.mipmap.icon_home_pause);
                        startForeground(1, notification);
                    }*/
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点,当其他应用申请的焦点为 AUDIOFOCUS_GAIN 时，
                    //会触发此回调事件，例如播放 QQ 音乐，网易云音乐等
                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                    Log.d(TAG, "AUDIOFOCUS_LOSS");
                    if (mPlayer != null) {

                        mPlayer.pause();
                        sendPlayCurrent(0);


                        SumBean.DataDTO dataDTO = playDataDTO;
                        initNotification(dataDTO.getTitle(), R.mipmap.icon_home_play);
                    }
                    //释放焦点，该方法可根据需要来决定是否调用
                    //若焦点释放掉之后，将不会再自动获得
//                        mAudioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，当其他应用申请 AUDIOFOCUS_GAIN_TRANSIENT 或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE 时，
                    //会触发此回调事件，例如播放短视频，拨打电话等。
                    //通常需要暂停音乐播放
                    if (mPlayer != null) {

                        mPlayer.pause();
                        sendPlayCurrent(0);

                        SumBean.DataDTO dataDTO = playDataDTO;
                        initNotification(dataDTO.getTitle(), R.mipmap.icon_home_play);
                    }
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂性丢失焦点并作降音处理
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
            }
        }
    };


    /**
     * 任意一次unbindService()方法，都会触发这个方法
     * 用于释放一些绑定时使用的资源
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {


        if (mediaPresenter != null) {

            mediaPresenter.detachView();
        }
        /**
         * 释放音频焦点
         */
        if (audioManager != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                audioManager.abandonAudioFocusRequest(audioFocusRequest);
            } else {

                audioManager.abandonAudioFocus(onAudioFocusChangeListener);
            }
        }

        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
        }
        mPlayer = null;
        super.onDestroy();
    }

    /**
     * 用来接收通知的点击事件
     */
    class MusicBroadcast extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action != null && action.equals(MediaService.FLAG_MUSIC_PLAY)) {

                if (mPlayer == null) {

                    return;
                }
                if (mPlayer.isPlaying()) {

                    mPlayer.pause();
                    sendPlayCurrent(0);

                    SumBean.DataDTO dataDTO = playDataDTO;
                    initNotification(dataDTO.getTitle(), R.mipmap.icon_home_play);
                } else {

                    mPlayer.start();
                    initAudioManager();
                    beginTime = DateUtil.getCurTime();

                    SumBean.DataDTO dataDTO = playDataDTO;
                    initNotification(dataDTO.getTitle(), R.mipmap.icon_home_pause);
                }
                //更新播放按钮的状态
                LocalBroadcastManager
                        .getInstance(MainApplication.getApplication())
                        .sendBroadcast(new Intent(FLAG_MEDIA_BROADCAST));
            }
        }
    }
}