package com.ai.bbcpro.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

public class HeadlineDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "custom_headlines.db";
    private static final int DB_VERSION = 7;
    private static final String CREATE_HEADLINE_TABLE = "create table if not exists HeadlinesList (BbcId text,Category text,Url text,CreateTime text,DescCn text,HotFlg text,Pic text,ReadCount text,Title text,Sound text,Texts integer,PublishTime text,Title_cn text,primary key(BbcId))";
    private static final String CREATE_DETAIL_TABLE = "create table if not exists HeadlinesDetail (Headlinesid integer,Type text,ParaId integer,IdIndex integer,Sentence text,Sentence_cn text,Timing float default 0,EndTiming float,wordScores text,totalScore text,soundPath text,soundUrl text,words text,userId integer,primary key(Headlinesid,Type,ParaId,IdIndex))";
    private static final String CREATE_UPVOTE_TABLE = "create table if not exists CommentAgreeCount (userid text,cid text)";
    private static final String CREATE_RECORD_TABLE = "create table if not exists HeadlineRecord (userId integer,voaId integer,headline_type text,total int(10) not null,listener_num int(10) not null,endFlag_listen integer,primary key(voaId,userId))";

    public HeadlineDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        //endFlag表示阅读完成100% TestNumber表示读到第几句了
        db.execSQL("CREATE TABLE \"HeadlinesList\" (" +
                "  \"BbcId\" text," +
                "  \"Category\" text," +
                "  \"Url\" text," +
                "  \"CreateTime\" text," +
                "  \"DescCn\" text," +
                "  \"HotFlg\" text," +
                "  \"Pic\" text," +
                "  \"ReadCount\" text," +
                "  \"Title\" text," +
                "  \"Sound\" text," +
                "  \"Texts\" integer," +
                "  \"PublishTime\" text," +
                "  \"Title_cn\" text," +
                "  \"FreeTimes\" integer," +
                "  \"is_download\" integer," +
                "  \"endFlag\" integer DEFAULT 0," +
                "  \"TestNumber\" integer DEFAULT 0," +
                "  \"collect\" integer NOT NULL DEFAULT 0," +
                "  PRIMARY KEY (\"BbcId\")" +
                ")");
        db.execSQL("create table if not exists CollectionList (BbcId text,Category text,Url text,CreateTime text,DescCn text,HotFlg text,Pic text,ReadCount text,Title text,Sound text,Texts integer,PublishTime text,Title_cn text,FreeTimes integer,primary key(BbcId))");
        db.execSQL("create table if not exists \"HeadlinesDetail\" (" +
                "  \"sen_key\" text," +
                "  \"BbcId\" integer," +
                "  \"ImgPath\" text," +
                "  \"EndTiming\" text," +
                "  \"ParaId\" text," +
                "  \"IdIndex\" text," +
                "  \"Announcer\" text," +
                "  \"sentence_cn\" text," +
                "  \"ImgWords\" text," +
                "  \"Timing\" text," +
                "  \"Sentence\" text," +
                "  \"is_eval\" integer," +
                "  \"collect\" integer NOT NULL DEFAULT 0," +
                "  PRIMARY KEY (\"sen_key\")" +
                ")");
        db.execSQL("create table if not exists CommentAgreeCount (userid text,cid text)");
        db.execSQL("create table if not exists HeadlineRecord (userId integer,voaId integer,headline_type text,total int(10) not null,listener_num int(10) not null,endFlag_listen integer,primary key(voaId,userId))");
        db.execSQL("create table if not exists CorrectPronData ( BBCID text,POSITION text,CONTENT text,PRON text,USERPORN text,DEF text,AUDIO text,SEQUENCE integer)");
        db.execSQL("create table if not exists EvalSentenceCache (sen_key text,bbc_id integer,position integer,sentence text,sentence_cn text,timing text,endtiming text,para_id text,id_index text,score float,audio_url text,is_correct boolean,primary key(sen_key))");
        db.execSQL("create table if not exists EvalWordCache (word_key text,bbc_id integer,position integer,content text,score float,word_index integer,pron text,user_pron text,pron2 text,user_pron2 text,user_delete text,user_insert text,id_index text,para_id text,sen_key text,primary key(word_key))");
        db.execSQL("create table if not exists MixRecCache (bbc_id text,url text,avrScore integer,primary key(bbc_id))");


        //问题表  upload是否上传1是0没有
        //userAnswerPosition   用户选择选项的位置
        db.execSQL("CREATE TABLE \"question\" (" +
                "  \"Answer4\" TEXT," +
                "  \"Answer3\" TEXT," +
                "  \"Answer2\" TEXT," +
                "  \"Answer\" TEXT," +
                "  \"IndexId\" INTEGER," +
                "  \"Answer1\" TEXT," +
                "  \"Question\" TEXT," +
                "  \"BbcId\" integer," +
                "  \"upload\" integer NOT NULL DEFAULT 0," +
                "  \"userAnswerPosition\" integer DEFAULT -1" +
                ");");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        label23:
        {
            switch (oldVersion) {
                case 1:
                    String[] alertDetails = new String[]{"alter table HeadlinesDetail add wordScores text", "alter table HeadlinesDetail add totalScore text", "alter table HeadlinesDetail add soundPath text", "alter table HeadlinesDetail add soundUrl text"};
                    String[] var5 = alertDetails;
                    int var6 = alertDetails.length;

                    for (int var7 = 0; var7 < var6; ++var7) {
                        String alertDetail = var5[var7];
                        db.execSQL(alertDetail);
                    }
                case 2:
                    String sql = "alter table HeadlinesDetail add words text";
                    db.execSQL(sql);
                case 3:
                    break;
                case 4:
                    db.execSQL("CREATE TABLE \"question\" (" +
                            "  \"Answer4\" TEXT," +
                            "  \"Answer3\" TEXT," +
                            "  \"Answer2\" TEXT," +
                            "  \"Answer\" TEXT," +
                            "  \"IndexId\" INTEGER," +
                            "  \"Answer1\" TEXT," +
                            "  \"Question\" TEXT," +
                            "  \"BbcId\" integer," +
                            "  \"upload\" integer NOT NULL DEFAULT 0," +
                            "  \"userAnswerPosition\" integer DEFAULT -1" +
                            ");");
                case 5:

                    db.execSQL("alter table HeadlinesList add column endFlag integer  DEFAULT 0");
                    db.execSQL("alter table HeadlinesList add column TestNumber integer  DEFAULT 0");
                    break;
                case 6:

                    db.execSQL("alter table HeadlinesDetail add column collect integer  DEFAULT 0");
                    db.execSQL("alter table HeadlinesList add column collect integer  DEFAULT 0");
                    break;
                default:
                    break label23;
            }
        }
    }

}
