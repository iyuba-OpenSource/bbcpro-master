package com.ai.bbcpro.word;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.ai.bbcpro.manager.RuntimeManager;

public class BackgroundManager {
    private static BackgroundManager instance;
    public static Context mContext;
    public Background bindService;
    public ServiceConnection conn;

    private BackgroundManager() {
        Log.e("BackgroundManager", "BackgroundManager: init" );
        conn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                // TODO Auto-generated method stub
                Log.e("BackgroundManager", "BackgroundManager: failed" );
            }
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                // TODO Auto-generated method stub
                Log.e("BackgroundManager", "BackgroundManager: connected" );
                Background.MyBinder binder = (Background.MyBinder) service;
                bindService = binder.getService();
                bindService.init(mContext);
            }
        };
    };

    public static synchronized BackgroundManager Instace() {
        mContext = RuntimeManager.getContext();
        if (instance == null) {
            instance = new BackgroundManager();
        }
        return instance;
    }
}
