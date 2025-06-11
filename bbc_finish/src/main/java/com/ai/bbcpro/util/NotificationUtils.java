package com.ai.bbcpro.util;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ai.bbcpro.R;
import com.ai.bbcpro.download.Constants;
//import com.ai.bbcpro.word.NewSectionAActivity;


public class NotificationUtils {
    private static final String CHANNEL_ID = "music";
    private static final int CODE_CLOSE = 1;
    private static final int CODE_PAUSE = 2;
    private static final int CODE_NEXT = 3 ;
    private static final int CODE_PALY= 4 ;
    //    private MusicInfo musicInfo ;
    private Boolean isPlay ;
    private PlayBean musicInfo;
    private int  playPosition;

    public NotificationUtils(NotificationManager manager, Context context, PlayBean musicInfo, boolean isPlay , int playPosition ) {
        this.manager = manager;
        this.context = context;
        this.musicInfo = musicInfo;
        this.isPlay = isPlay;
        this.playPosition = playPosition;
    }

    NotificationManager manager ;
    Context context ;
    public Notification createNotification(){
        NotificationCompat.Builder  builder = new NotificationCompat.Builder(context);
//        Intent intent1 = new Intent(context, NewSectionAActivity.class);
//        intent1.putExtra("examtime", musicInfo.examTime);
//        intent1.putExtra("section", musicInfo.section);
//        intent1.putExtra("testFlag", "test");
//        intent1.putExtra("subtitle", musicInfo.subTitle);
//        intent1.putExtra("isNewType", true);
//        intent1.putExtra("playPosition", playPosition);
//        PendingIntent intent = PendingIntent.getActivity(context,0,intent1 , PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setOnlyAlertOnce(true)
//                .setContentIntent(intent)
//                .setWhen(System.currentTimeMillis())
//                .setChannelId(CHANNEL_ID)
//                .setPriority(NotificationCompat.PRIORITY_MAX)
//                .setSmallIcon(R.drawable.ic_launcher)
//                .setCustomContentView(createContentView());

        return builder.build() ;
    }

    public void setCommonClickPendings(RemoteViews view ){
        Intent playOrPause = new Intent(Constants.ACTION_MUSIC);
        playOrPause.putExtra(Constants.NOTIFY_BUTTON_ID, Constants.PLAY);
        Intent close = new Intent(Constants.ACTION_MUSIC);
        close.putExtra(Constants.NOTIFY_BUTTON_ID, Constants.CLOSE);
        Intent next = new Intent(Constants.ACTION_MUSIC);
        next.putExtra(Constants.NOTIFY_BUTTON_ID, Constants.NEXT);
        PendingIntent intentClose = PendingIntent.getBroadcast(context,CODE_CLOSE, close , PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intentPause = PendingIntent.getBroadcast(context,CODE_PAUSE, playOrPause , PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent intentNext = PendingIntent.getBroadcast(context,CODE_NEXT, next , PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.close,intentClose);
        view.setOnClickPendingIntent(R.id.next,intentNext);
        view.setOnClickPendingIntent(R.id.pause,intentPause);
    }

    // 在show 的时候
    public void show() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"music",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(false);
            manager.createNotificationChannel(channel);
        }
        Notification nf = createNotification();
        manager.notify(Constants.CHANNELID, nf);
    }

    private RemoteViews createContentView() {
        // 构造方法传报名和布局
        RemoteViews view  = new RemoteViews(context.getPackageName() , R.layout.remote_view);
        setContents(view);
        setCommonClickPendings(view);
        return view ;
    }

    // TODO  点击事件
    private void setContents(RemoteViews view) {
//        view.setImageViewBitmap(R.id.image , getBitmapFromUrl(musicInfo.alubumurl));
        view.setTextViewText(R.id.name , musicInfo.name);
        view.setTextViewText(R.id.singer , musicInfo.author);
        if (isPlay){
            view.setImageViewResource(R.id.pause , R.drawable.btn_playing_pause);
        }else {
            view.setImageViewResource(R.id.pause , R.drawable.btn_playing_play);
        }
    }

    public void cancel() {
        manager.cancel(1);
    }
}
