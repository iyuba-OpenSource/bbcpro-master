package com.ai.bbcpro.sqlite.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "lib_database.sqlite";

    public static final int VERSION = 1;

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS voa");
        db.execSQL("DROP TABLE IF EXISTS voadetail");
        db.execSQL("DROP TABLE IF EXISTS collection");
        db.execSQL("DROP TABLE IF EXISTS user");
        db.execSQL("DROP TABLE IF EXISTS word");
        onCreate(db);
    }
}
