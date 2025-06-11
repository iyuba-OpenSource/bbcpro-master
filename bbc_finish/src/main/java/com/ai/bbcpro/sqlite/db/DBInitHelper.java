package com.ai.bbcpro.sqlite.db;

import android.content.Context;

import com.ai.bbcpro.sqlite.dbCategory.download.DownloadDBOpenHelper;

public class DBInitHelper {
    public static void init(Context mContext) {
        initLibDB(mContext);
        try {
            DownloadDBOpenHelper downloadHelper = new DownloadDBOpenHelper(mContext);
//            downloadHelper.createHeadline();
            downloadHelper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static void initLibDB(Context mContext) {
        ImportLibDatabase libDatabase = new ImportLibDatabase(mContext.getApplicationContext());
        libDatabase.setPackageName(mContext.getPackageName());
        libDatabase.setVersion(4, 5);
        libDatabase.openDatabase(libDatabase.getDBPath());
    }
}
