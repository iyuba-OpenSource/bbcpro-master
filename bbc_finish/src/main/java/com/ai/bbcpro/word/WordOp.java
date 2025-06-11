package com.ai.bbcpro.word;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.Cursor;

import com.ai.bbcpro.sqlite.db.DatabaseService;
import com.ai.bbcpro.sqlite.db.ImportLibDatabase;


/**
 * 获取单词表数据库
 *
 * @author ct
 * @time 12.9.27
 */
public class WordOp extends DatabaseService {
    public static final String TABLE_NAME_WORD = "word";
    public static final String ID = "user";
    public static final String KEY = "key";
    public static final String LANG = "lang";
    public static final String AUDIOURL = "audiourl";
    public static final String PRON = "pron";
    public static final String DEF = "def";
    public static final String EXAMPLES = "examples";
    public static final String CREATEDATE = "createdate";
    public static final String ISDELETE = "isdelete";

    public WordOp(Context context) {
        super(context);
    }

    public synchronized void saveData(Word word) {
        String dateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        dateTime = sdf.format(new Date());
        importDatabase.openDatabase().execSQL(
                "insert or replace into " + TABLE_NAME_WORD + " (" + ID + ","
                        + KEY + "," + LANG + "," + AUDIOURL + "," + PRON + ","
                        + DEF + "," + EXAMPLES + "," + CREATEDATE + ","
                        + ISDELETE + ") values(?,?,?,?,?,?,?,?,?)",
                new Object[]{word.userid, word.key, word.lang, word.audioUrl,
                        word.pron, word.def, word.examples, dateTime, "0"});
        closeDatabase(null);

    }

    public synchronized void saveData(ArrayList<Word> words) {

        if (words != null && words.size() != 0) {

            int size = words.size();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

            for (int i = 0; i < size; i++) {

                Word word = words.get(i);
                String dateTime = sdf.format(new Date());
                ImportLibDatabase.openDatabase().execSQL(
                        "insert or replace into " + TABLE_NAME_WORD + " (" + ID
                                + "," + KEY + "," + LANG + "," + AUDIOURL + ","
                                + PRON + "," + DEF + "," + EXAMPLES + ","
                                + CREATEDATE + "," + ISDELETE
                                + ") values(?,?,?,?,?,?,?,?,?)",
                        new Object[]{word.userid, word.key, word.lang,
                                word.audioUrl, word.pron, word.def,
                                word.examples, dateTime, "0"});
                closeDatabase(null);
            }
        }
    }

    /**
     * @return
     */
    public synchronized List<Word> findDataByAll(String userid) {
        List<Word> words = new ArrayList<Word>();
        Cursor cursor = importDatabase.openDatabase()
                .rawQuery(
                        "select " + ID + "," + KEY + "," + LANG + ","
                                + AUDIOURL + "," + PRON + "," + DEF + ","
                                + EXAMPLES + "," + CREATEDATE + "," + ISDELETE
                                + " from " + TABLE_NAME_WORD + " where user='"
                                + userid + "' AND " + ISDELETE + "='0'"
                                + " ORDER BY " + ID + " DESC", new String[]{});
        Word word;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            word = new Word();
            word.userid = cursor.getString(0);
            word.key = cursor.getString(1);
            word.lang = cursor.getString(2);
            word.audioUrl = cursor.getString(3);
            word.pron = cursor.getString(4);
            word.def = cursor.getString(5);
            word.examples = cursor.getString(6);
            word.createDate = cursor.getString(7);
            word.delete = cursor.getString(8);
            words.add(word);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    public synchronized Word findDataByName(String wordKey) {
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + KEY + "," + LANG + "," + AUDIOURL + ","
                        + PRON + "," + DEF + "," + EXAMPLES + "," + CREATEDATE
                        + "," + ISDELETE + " from " + TABLE_NAME_WORD
                        + " where key='" + wordKey + "' AND " + ISDELETE + "='0'"
                        + " ORDER BY " + ID + " DESC", new String[]{});
        if (cursor.moveToFirst()) {
            Word word = new Word();
            word.userid = cursor.getString(0);
            word.key = cursor.getString(1);
            word.lang = cursor.getString(2);
            word.audioUrl = cursor.getString(3);
            word.pron = cursor.getString(4);
            word.def = cursor.getString(5);
            word.examples = cursor.getString(6);
            word.createDate = cursor.getString(7);
            word.delete = cursor.getString(8);
            cursor.close();
            closeDatabase(null);
            return word;
        } else {
            cursor.close();
            closeDatabase(null);
            return null;
        }
    }

    /**
     * @return
     */
    public synchronized List<Word> findDataByDelete(String userid) {
        List<Word> words = new ArrayList<Word>();
        Cursor cursor = importDatabase.openDatabase().rawQuery(
                "select " + ID + "," + KEY + "," + LANG + "," + AUDIOURL + ","
                        + PRON + "," + DEF + "," + EXAMPLES + "," + CREATEDATE
                        + "," + ISDELETE + " from " + TABLE_NAME_WORD
                        + " where user='" + userid + "' AND " + ISDELETE
                        + "='1'", new String[]{});
        Word word;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            word = new Word();
            word.userid = cursor.getString(0);
            word.key = cursor.getString(1);
            word.lang = cursor.getString(2);
            word.audioUrl = cursor.getString(3);
            word.pron = cursor.getString(4);
            word.def = cursor.getString(5);
            word.examples = cursor.getString(6);
            word.createDate = cursor.getString(7);
            word.delete = cursor.getString(8);
            words.add(word);
        }
        if (cursor != null) {
            cursor.close();
        }
        closeDatabase(null);
        if (words.size() != 0) {
            return words;
        }
        return null;
    }

    /**
     * 删除
     *
     * @param ids ID集合，以�?”分�?每项加上""
     * @return
     */
    public synchronized boolean deleteItemWord(String userid) {
        try {
            importDatabase.openDatabase().execSQL(
                    "delete from " + TABLE_NAME_WORD + " where " + ID + "='"
                            + userid + "' AND " + ISDELETE + "='1'");
            closeDatabase(null);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public synchronized boolean tryToDeleteItemWord(String keys, String userid) {
        try {
            importDatabase.openDatabase().execSQL(
                    "update " + TABLE_NAME_WORD + " set " + ISDELETE
                            + "='1' where " + ID + "='" + userid + "' AND "
                            + KEY + " in (" + keys + ")");
            closeDatabase(null);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取一个随机单词
     *
     * @return
     */
    public synchronized Word getRandom() {

        Cursor cursor = importDatabase.openDatabase().rawQuery("SELECT * FROM word ORDER BY RANDOM() limit 1", null);
        Word word = null;
        if (cursor.moveToNext()) {

            String key = cursor.getString(cursor.getColumnIndexOrThrow("key"));
            String audiourl = cursor.getString(cursor.getColumnIndexOrThrow("audiourl"));
            String pron = cursor.getString(cursor.getColumnIndexOrThrow("pron"));
            String def = cursor.getString(cursor.getColumnIndexOrThrow("def"));
            word = new Word();
            word.setKey(key);
            word.setAudioUrl(audiourl);
            word.setPron(pron);
            word.setDef(def);
        }
        closeDatabase(null);
        return word;
    }
}

