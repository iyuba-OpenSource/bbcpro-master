package com.ai.bbcpro.widget;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.ai.bbcpro.word.BlogContent;

public class BlogOp extends DatabaseService {
    private static final String TABLE_NAME_BLOG = "blog";
    private static final String _ID = "_id";
    private static final String BLOGID = "blogid";
    private static final String PIC = "pic";
    private static final String SUBJECT = "subject";
    private static final String VIEWNUM = "viewnum";
    private static final String REPLYNUM = "replynum";
    private static final String DATELINE = "dateline";
    private static final String NOREPLY = "noreply";
    private static final String FRIEND = "friend";
    private static final String PASSWORD = "password";
    private static final String FAVTIMES = "favtimes";
    private static final String SHARETIMES = "sharetimes";
    private static final String MESSAGE = "message";
    private static final String IDS = "ids";
    private static final String USERNAME = "username";
    private static final String UID = "uid";

    public BlogOp(Context context) {
        super(context);

        CreateTabSql();
    }

    public void CreateTabSql() {

        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_BLOG + " ("
                + _ID + " integer primary key autoincrement, " + BLOGID
                + " text, " + PIC + " text, " + SUBJECT + " text, " + VIEWNUM + " text, "
                + REPLYNUM + " text," + DATELINE + " text," + NOREPLY
                + " text," + FRIEND + " text," + PASSWORD + " text," + FAVTIMES
                + " text," + SHARETIMES + " text," + MESSAGE + " text," + IDS
                + " text," + USERNAME + " text," + UID + " text)";
        dbOpenHelper.getWritableDatabase().execSQL(sql);
    }

    /**
     * 批量插入数据
     */
    public void saveData(List<BlogContent> blogs) {
        // deleteBlogData();
        String dateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = sdf.format(new Date());
        if (blogs != null && blogs.size() != 0) {
            for (int i = 0; i < blogs.size(); i++) {
                BlogContent blog = blogs.get(i);
                Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                        "select * from " + TABLE_NAME_BLOG + " where blogid='"
                                + blog.blogid + "'", new String[]{});
                int databaseHasNum = cursor.getCount();
                if (databaseHasNum == 0) {
                    dbOpenHelper.getWritableDatabase().execSQL(
                            "insert into " + TABLE_NAME_BLOG + " (" + BLOGID
                                    + "," + PIC + "," + SUBJECT + "," + VIEWNUM + ","
                                    + REPLYNUM + "," + DATELINE + "," + NOREPLY
                                    + "," + FRIEND + "," + PASSWORD + ","
                                    + FAVTIMES + "," + SHARETIMES + ","
                                    + MESSAGE + "," + IDS + "," + USERNAME
                                    + "," + UID
                                    + ") values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                            new Object[]{blog.blogid, blog.pic, blog.subject,
                                    blog.viewnum, blog.replynum, blog.dateline,
                                    blog.noreply, blog.friend, blog.password,
                                    blog.favtimes, blog.sharetimes,
                                    blog.message, blog.ids, blog.username,
                                    blog.uid});

                }
                cursor.close();
            }
        }
        closeDatabase(null);
    }

    /*
     * 批量插入刷新列表时所必需的信息（包括日志ID、日志的标题、发布时间和浏览量）
     */
    public void saveNecessaryData(List<BlogContent> blogs) {
        // deleteBlogData();
        String dateTime = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = sdf.format(new Date());
        if (blogs != null && blogs.size() != 0) {
            for (int i = 0; i < blogs.size(); i++) {
                BlogContent blog = blogs.get(i);
                Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                        "select * from " + TABLE_NAME_BLOG + " where blogid='"
                                + blog.blogid + "'", new String[]{});
                int databaseHasNum = cursor.getCount();
                if (databaseHasNum == 0) {
                    dbOpenHelper.getWritableDatabase().execSQL(
                            "insert into " + TABLE_NAME_BLOG + " (" + BLOGID
                                    + "," + SUBJECT + "," + VIEWNUM + ","
                                    + DATELINE + ") values(?,?,?,?)",
                            new Object[]{blog.blogid, blog.subject,
                                    blog.viewnum, blog.dateline});
                }
                cursor.close();
            }
        }
        closeDatabase(null);
    }

    /*
     * 根据日志的ID单条获取日志的内容
     */
    public void saveBlogContentData(BlogContent blogs) {
        // deleteBlogData();

        if (blogs != null) {
            // Log.d("insert", "111111111111");
            try {
                dbOpenHelper.getWritableDatabase().execSQL(
                        "update " + TABLE_NAME_BLOG + " set MESSAGE='"
                                + blogs.message + "'" + " where blogid='"
                                + blogs.blogid + "'");
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        closeDatabase(null);
    }

    /*
     * 收藏blog
     */
    public void collectBlog(BlogContent blogs) {
        if (blogs != null) {
            try {
                dbOpenHelper.getWritableDatabase().execSQL(
                        "update " + TABLE_NAME_BLOG + " set MESSAGE='"
                                + blogs.message + "'" + " where blogid='"
                                + blogs.blogid + "'");
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        closeDatabase(null);
    }

    /*
     * 更新单条日志的评论数
     */
    public void saveBlogReplyNumData(BlogContent blog) {
        // deleteBlogData();

        if (blog != null) {
            // Log.d("insert", "111111111111");
            try {
                dbOpenHelper.getWritableDatabase().execSQL(
                        "update " + TABLE_NAME_BLOG + " set replynum='"
                                + blog.replynum + "'" + " where blogid='"
                                + blog.blogid + "'");
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        closeDatabase(null);
    }

    /*
     * 更新单条日志的评论数
     */
    public void saveBlogViewNumData(BlogContent blog) {
        // deleteBlogData();

        if (blog != null) {
            // Log.d("insert", "111111111111");
            try {
                dbOpenHelper.getWritableDatabase().execSQL(
                        "update " + TABLE_NAME_BLOG + " set viewnum='"
                                + blog.viewnum + "'" + " where blogid='"
                                + blog.blogid + "'");
            } catch (Exception e) {

                e.printStackTrace();
            }

        }
        closeDatabase(null);
    }

    public ArrayList<BlogContent> findDataByAll() {
        ArrayList<BlogContent> blogs = new ArrayList<>();
        Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                "select *" + " from " + TABLE_NAME_BLOG + " ORDER BY " + BLOGID
                        + " desc", new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BlogContent blog = getBlogContent(cursor);
            blogs.add(blog);
        }
        cursor.close();
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return null;
    }

    public ArrayList<BlogContent> selectData(int count, int offset) {
        ArrayList<BlogContent> blogs = new ArrayList<>();
        Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                "select *" + " from " + TABLE_NAME_BLOG + " ORDER BY " + BLOGID
                        + " desc Limit ? Offset ?", new String[]{String.valueOf(count), String.valueOf(offset)});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BlogContent blog = getBlogContent(cursor);
            blogs.add(blog);
        }
        cursor.close();
        closeDatabase(null);
        return blogs;
    }

    public ArrayList<BlogContent> findDataByIds(String ids) {
        ArrayList<BlogContent> blogs = new ArrayList<>();
        Cursor cursor = dbOpenHelper.getWritableDatabase().query(TABLE_NAME_BLOG,
                new String[]{"*"}, BLOGID + " in (?)", new String[]{ids},
                null, null, BLOGID + " desc");

//        Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
//                "select *" + " from " + TABLE_NAME_BLOG + " where " + BLOGID + " in  (?)" + " ORDER BY " + BLOGID
//                        + " desc", new String[]{ids});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BlogContent blog = getBlogContent(cursor);
            blogs.add(blog);
        }
        cursor.close();
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return new ArrayList<>();
    }

    private BlogContent getBlogContent(Cursor cursor) {
        BlogContent blog = new BlogContent();
        blog._id = cursor.getInt(0);
        blog.blogid = cursor.getString(1);
        blog.pic = cursor.getString(2);
        blog.subject = cursor.getString(3);
        blog.viewnum = cursor.getString(4);
        blog.replynum = cursor.getString(5);
        blog.dateline = cursor.getString(6);
        blog.noreply = cursor.getString(7);
        blog.friend = cursor.getString(8);
        blog.password = cursor.getString(9);
        blog.favtimes = cursor.getString(10);
        blog.sharetimes = cursor.getString(11);
        blog.message = cursor.getString(12);
        blog.ids = cursor.getString(13);
        blog.username = cursor.getString(14);
        blog.uid = cursor.getString(15);
        return blog;
    }

    /*
     * 查找日志列表所必需的信息（包括日志ID、日志的标题、发布时间和浏览量）
     */

    public ArrayList<BlogContent> findNecessaryDataByAll() {
        ArrayList<BlogContent> blogs = new ArrayList<BlogContent>();
        Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                "select *" + " from " + TABLE_NAME_BLOG + " ORDER BY " + _ID,
                new String[]{});
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            BlogContent blog = new BlogContent();
            blog.blogid = cursor.getString(1);
            blog.subject = cursor.getString(2);
            blog.viewnum = cursor.getString(3);
            blog.dateline = cursor.getString(5);
            blogs.add(blog);
        }
        cursor.close();
        closeDatabase(null);
        if (blogs.size() != 0) {
            return blogs;
        }
        return null;
    }

    /**
     * 删除
     *
     * @param ids ID集合，以“,”分割,每项加上""
     * @return
     */
    public boolean deleteItemblog(String ids) {
        try {
            dbOpenHelper.getWritableDatabase().execSQL(
                    "delete from " + TABLE_NAME_BLOG + " where " + _ID
                            + " in (" + ids + ")", new Object[]{});
            closeDatabase(null);
            return true;
        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
    /*
     * public boolean deleteBlogData(){ try {
     * dbOpenHelper.getWritableDatabase().execSQL( "delete from " +
     * TABLE_NAME_BLOG ); Log.e("","删除日志成功"); return true; } catch (Exception e)
     * {
     *
     * }
     */

    /*
     * findMaxBlogId():查找本地的blogId的最大值
     */
    public int findMaxBlogId() {
        int maxBlogId = 0;

        try {
            Cursor cursor = dbOpenHelper.getWritableDatabase().rawQuery(
                    "select max(" + BLOGID + ") from " + TABLE_NAME_BLOG,
                    new String[]{});
            cursor.moveToFirst();
            maxBlogId = cursor.getInt(0);
            Log.e("Test findMaxBlogId", maxBlogId + "");
            cursor.close();
            return maxBlogId;
            /*
             * dbOpenHelper.getWritableDatabase().execSQL( "select max(" +
             * BLOGID + ") from TABLE_NAME_BLOG");
             */
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }

    }

}

