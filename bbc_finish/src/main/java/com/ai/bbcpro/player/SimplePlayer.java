package com.ai.bbcpro.player;



import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;


import java.io.IOException;

/**
 * Author：Howard on 2016/8/19 16:09
 * Email：Howard9891@163.com
 */

public class SimplePlayer implements MediaPlayer.OnBufferingUpdateListener,
        MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener,MediaPlayer.OnErrorListener {

    private Context mContext;
    private MediaPlayer mediaPlayer;
    private String audioUrl;
    private OnPlayStateChangedListener opscl;
    private  static SimplePlayer instance;
    public  SimplePlayer(Context context){
        this.mContext = context;
        initPlayer();
    }
    public boolean isPlaying(){
        if (mediaPlayer!=null){
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public static SimplePlayer getInstance(Context context){
        if(instance==null){
            instance = new SimplePlayer(context);
        }
        return instance;
    }
    public void pause(){
        if(mediaPlayer!=null){
            mediaPlayer.pause();
        }
    }
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


    /**
     * 初始化player
     */
    private void initPlayer() {
        try {
            if(mediaPlayer!=null){
                mediaPlayer.reset();
            }else{
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
        } catch (Exception e) {
            Log.e("mediaPlayer", "error", e);
        }
    }
    public void playUrl(String audioUrl){
        PlayerUrlTask task =new PlayerUrlTask(audioUrl);
        ThreadManager.executeLong(task);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return true;
    }

    class  PlayerUrlTask implements Runnable{

        private String audioUrl;
        public PlayerUrlTask(String audioUrl){
            this.audioUrl =audioUrl;
        }
        @Override
        public void run() {
            try {
                if(mediaPlayer==null){
                    initPlayer();
                }
                if(audioUrl!=null){
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(audioUrl);
                    mediaPlayer.prepareAsync();
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    @Override
    public void onCompletion(MediaPlayer mp) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        Log.e("AudioPlayer start:","start");

    }
    public void seekTo(int progress){
        if(mediaPlayer!=null){
            mediaPlayer.seekTo(progress);
        }
    }
    public int getCurrentTime(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            return  mediaPlayer.getCurrentPosition();
        }

        return  0;
    }

    public int getTotalTime(){
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            return  mediaPlayer.getDuration();
        }
        return  0;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        // TODO Auto-generated method stub

    }

}
