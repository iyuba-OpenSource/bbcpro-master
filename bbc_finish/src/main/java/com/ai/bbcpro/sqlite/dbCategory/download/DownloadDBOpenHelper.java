package com.ai.bbcpro.sqlite.dbCategory.download;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by aspsine on 15-4-19.
 */
public class DownloadDBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "downloadcopy.db";
    private static final int DB_VERSION = 3;

    public DownloadDBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    void createTable(SQLiteDatabase db) {
        ThreadInfoDao.createTable(db);
    }

    void dropTable(SQLiteDatabase db) {
        ThreadInfoDao.dropTable(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    public void createHeadline() {
        StringBuilder sql = new StringBuilder();
        sql.append("create table task_info (id integer primary key autoincrement, tag text,")
                .append("original_url text, real_url text, filepath text, filename text, state integer,")
                .append("current_bytes integer, total_bytes integer, mime_type text, e_tag text,")
                .append("disposition text, location text, category text)");

        getWritableDatabase().execSQL(sql.toString());

        sql.setLength(0);
        sql.append("create table thread_info (id integer primary key autoincrement,")
                .append("tag text, start integer, end integer, uuid text)");
        getWritableDatabase().execSQL(sql.toString());

        sql.setLength(0);
        sql.append("create unique index task_index on task_info (tag)");
        getWritableDatabase().execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTable(db);
        createTable(db);
    }
}
