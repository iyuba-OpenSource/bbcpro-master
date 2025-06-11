package com.ai.bbcpro.word.cetDB;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;


import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import timber.log.Timber;

/**
 * 数据库管理
 *
 * @author chentong
 */
@SuppressLint("StaticFieldLeak")
public class ImportCetDatabase {
    private final int BUFFER_SIZE = 400000;
    private String PACKAGE_NAME;
    private String DB_PATH;
    private Cet4DBHelper mdbhelper;
    private Context mContext;
    private int lastVersion, currentVersion;

    SQLiteDatabase mDB ;
    public ImportCetDatabase(Context context) {
        mContext = context;
        mdbhelper = new Cet4DBHelper(mContext);
//        mDB = SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/" + Constant.DB_NAME_CET,null);
    }

    public String getDBPath() {
        return Constant.DB_PATH + "/" + Constant.DB_NAME_CET;
    }

    /*
     * 传入报名 导入数据库
     */
    public void setPackageName(String packageName) {
        PACKAGE_NAME = packageName;
        DB_PATH = Constant.DB_PATH + "/" + Constant.PACKAGE_NAME +"/databases";
        Timber.tag("InitActivity").e("db>>>path%s", DB_PATH);
    }

    public void setVersion(int lastVeision, int curVersion) {
        this.lastVersion = lastVeision;
        this.currentVersion = curVersion;
    }

    public SQLiteDatabase openDatabase() {
        mDB = mdbhelper.openDatabase();
        return mDB;
    }

    private boolean checkColumnExists2(SQLiteDatabase db, String tableName
            , String columnName) {
        boolean result = false;
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from sqlite_master where name = ? and sql like ?"
                    , new String[]{tableName, "%" + columnName + "%"});
            result = null != cursor && cursor.moveToFirst();
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return result;
    }

    public void addFollowingColumn() {
        //todo todo
        if (!checkColumnExists2(mDB, "newtype_texta4", "score")) {
            try {
                mDB.execSQL("alter table newtype_texta4 add score text ;");
                mDB.execSQL("alter table newtype_textb4 add score text ;");
                mDB.execSQL("alter table newtype_textc4 add score text ;");
                mDB.execSQL("alter table newtype_texta4 add record_url text ;");
                mDB.execSQL("alter table newtype_textb4 add record_url text ;");
                mDB.execSQL("alter table newtype_textc4 add record_url text ;");
                mDB.execSQL("alter table newtype_texta4 add bad text ;");
                mDB.execSQL("alter table newtype_textb4 add bad text ;");
                mDB.execSQL("alter table newtype_textc4 add bad text ;");
                mDB.execSQL("alter table newtype_texta4 add good text ;");
                mDB.execSQL("alter table newtype_textb4 add good text ;");
                mDB.execSQL("alter table newtype_textc4 add good text ;");

            } catch (SQLiteException e) {
                e.printStackTrace();
            } finally {
            }
        }
        if (!checkColumnExists2(mDB, "newtype_texta6", "score")) {
            try {
                mDB.execSQL("alter table newtype_texta6 add score text ;");
                mDB.execSQL("alter table newtype_textb6 add score text ;");
                mDB.execSQL("alter table newtype_textc6 add score text ;");
                mDB.execSQL("alter table newtype_texta6 add record_url text ;");
                mDB.execSQL("alter table newtype_textb6 add record_url text ;");
                mDB.execSQL("alter table newtype_textc6 add record_url text ;");
                mDB.execSQL("alter table newtype_texta6 add good text ;");
                mDB.execSQL("alter table newtype_textb6 add good text ;");
                mDB.execSQL("alter table newtype_textc6 add good text ;");
                mDB.execSQL("alter table newtype_texta6 add bad text ;");
                mDB.execSQL("alter table newtype_textb6 add bad text ;");
                mDB.execSQL("alter table newtype_textc6 add bad text ;");
            } catch (SQLiteException e) {
                e.printStackTrace();
            }finally {
                mDB.close();
            }
        }
    }

    /**
     * 打开数据库 根据版本判断是否需要更新
     *
     * @param dbfile
     */
    public void openDatabase(String dbfile) {
        lastVersion = ConfigManager.Instance().loadInt("cet_database_version");
        File database = new File(dbfile);
        if (currentVersion > lastVersion) {
            if (database.exists()) {
                database.delete();
            }
            loadDataBase(dbfile);
            ConfigManager.Instance().putInt("cet_database_version",
                    currentVersion);
        } else {
            ConfigManager.Instance().putInt("cet_database_version",
                    currentVersion);
        }
    }


    /**
     * 更换数据库
     *
     * @param dbfile
     */
    private void loadDataBase(String dbfile) {
        try {
            Timber.d("loadDataBase is begin");
            InputStream is = mContext.getResources().openRawResource(
                    R.raw.cet_13);
            BufferedInputStream bis = new BufferedInputStream(is);
            if (!(new File(DB_PATH).exists())) {
                new File(DB_PATH).mkdir();
            }
            FileOutputStream fos = new FileOutputStream(dbfile);
            BufferedOutputStream bfos = new BufferedOutputStream(fos);
            byte[] buffer = new byte[BUFFER_SIZE];
            int count = 0;
            while ((count = bis.read(buffer)) > 0) {
                bfos.write(buffer, 0, count);
            }
            bfos.flush();
            bfos.close();
            fos.close();
            bis.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
