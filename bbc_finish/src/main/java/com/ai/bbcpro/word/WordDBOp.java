package com.ai.bbcpro.word;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.ai.bbcpro.sqlite.db.DatabaseService;


/**
 * 获取单词表数据库
 *
 * @author ct
 * @time 12.9.27
 */
public class WordDBOp extends DatabaseService {
    public static final String TABLE_NAME_WORD = "worddb";
    public static final String ID = "id";
    public static final String KEY = "word";
    public static final String AUDIOURL = "audio";
    public static final String PRON = "pron";
    public static final String DEF = "def";
    public static final String VIEWCOUNT = "viewCount";

    public WordDBOp(Context context) {
        super(context);
    }

    /**
     * @return
     */
    public synchronized ArrayList<Word> findDataByFuzzy(String word) {
        if (!TextUtils.isEmpty(word)) {
            word = word.replace("'", "");
        }
        ArrayList<Word> words = new ArrayList<Word>();
        Cursor cursor = importDatabase.openDatabase()
                .rawQuery(
                        "select " + ID + "," + KEY + "," + AUDIOURL + ","
                                + PRON + "," + DEF + "," + VIEWCOUNT + " from "
                                + TABLE_NAME_WORD + " WHERE " + KEY + " LIKE '"
                                + word + "%' limit 0,60;", new String[]{});
        Word temp;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            temp = new Word();
            temp.key = cursor.getString(1);
            temp.audioUrl = cursor.getString(2);
            temp.pron = cursor.getString(3);
            temp.def = cursor.getString(4);
            words.add(temp);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return words;
    }

    /**
     * @return
     */
    public synchronized ArrayList<Word> findDataByView() {
        ArrayList<Word> words = new ArrayList<Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + KEY + "," + AUDIOURL + "," + PRON + ","
                        + DEF + "," + VIEWCOUNT + " from " + TABLE_NAME_WORD
                        + " WHERE " + VIEWCOUNT + " = '1' limit 0,60;",
                new String[]{});
        Word temp;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            temp = new Word();
            temp.key = cursor.getString(1);
            temp.audioUrl = cursor.getString(2);
            temp.pron = cursor.getString(3);
            temp.def = cursor.getString(4);
            words.add(temp);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        return words;
    }

    public synchronized boolean updateWord(String key) {
        try {
            if (!TextUtils.isEmpty(key)) {
                key = key.replace("'", "");
            }
            importDatabase.openDatabase().execSQL(
                    "update " + TABLE_NAME_WORD + " SET " + VIEWCOUNT
                            + " = '1'" + " where " + KEY + "='" + key + "'");
            closeDatabase(null);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public synchronized Word findDataByKey(String key) {
        if (!TextUtils.isEmpty(key)) {
            key = key.replace("'", "");
        }
        Cursor cursor = importDatabase.openDatabase()
                .rawQuery(
                        "select " + ID + "," + KEY + "," + AUDIOURL + ","
                                + PRON + "," + DEF + "," + VIEWCOUNT + " from "
                                + TABLE_NAME_WORD + " WHERE " + KEY + " = '"
                                + key + "'", new String[]{});
        if (cursor.moveToFirst()) {
            Word temp = new Word();
            temp.key = cursor.getString(1);
            temp.audioUrl = cursor.getString(2);
            temp.pron = cursor.getString(3);
            temp.def = cursor.getString(4);
            cursor.close();
            closeDatabase(null);
            return temp;
        } else {
            cursor.close();
            closeDatabase(null);
            return null;
        }

    }
}

