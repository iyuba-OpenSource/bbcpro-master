package com.ai.bbcpro.widget;



import android.content.Context;
import android.database.Cursor;

import com.ai.bbcpro.sqlite.db.DBOpenHelper;

public class DatabaseService {
    protected DBOpenHelper dbOpenHelper;

    protected DatabaseService(Context context) {
        dbOpenHelper = new DBOpenHelper(context);
    }

    /**
     * 删除表
     *
     * @param taleName
     */
    public void dropTable(String taleName) {
        dbOpenHelper.getWritableDatabase().execSQL(
                "DROP TABLE IF EXISTS " + taleName);

    }

    /**
     * 关闭数据库
     *
     * @param DatabaseName
     */
    public void closeDatabase(String DatabaseName) {
        dbOpenHelper.getWritableDatabase().close();

    }

    /**
     * 删除数据库表数据
     *
     * @param tableName
     * @param id
     */
    public void deleteItemData(String tableName, Integer id) {
        dbOpenHelper.getWritableDatabase().execSQL(
                "delete from " + tableName + " where _id=?",
                new Object[] { id });
        closeDatabase(null);
    }

    /**
     * 删除数据库表数据
     *
     * @param tableName
     * @param ids
     *            ids格式为"","","",""
     */
    public void deleteItemsData(String tableName, String ids) {
        dbOpenHelper.getWritableDatabase().execSQL(
                "delete from " + tableName + " where voaid in (" + ids + ")",
                new Object[] {});
        closeDatabase(null);
    }

    /**
     * 获取数据库表项数
     *
     * @param tableName
     * @return
     */
    public long getDataCount(String tableName) {
        Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery(
                "select count(*) from " + tableName, null);
        cursor.moveToFirst();
        closeDatabase(null);
        return cursor.getLong(0);
    }

    /**
     * 关闭数据库服务
     */
    public void close() {
        dbOpenHelper.close();
    }

}

