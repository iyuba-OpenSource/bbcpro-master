package com.ai.bbcpro.word;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zqq
 * <p>
 * 功能：对已有数据库文件进行操作
 */
public class CetDBHelper {
    public String TABLE_TEXTA = "texta4";// SectionA原文
    public String TABLE_TEXTB = "textb4";// SectionB原文
    public String TABLE_TEXTC = "textc4";// SectionC原文
    public String TABLE_ANSWERA = "answera4";// SectionA题目及答案
    public String TABLE_ANSWERB = "answerb4";// SectionB题目及答案
    public String TABLE_ANSWERC = "answerc4";// SectionC题目及答案
    public String TABLE_EXPLAIN = "explain4";// 所有解析
    public String NEWTYPE_TABLE_TEXTA = "newtype_texta4";// 新题型SectionA原文
    public String NEWTYPE_TABLE_TEXTB = "newtype_textb4";// 新题型SectionB原文
    public String NEWTYPE_TABLE_TEXTC = "newtype_textc4";// 新题型SectionC原文
    public String NEWTYPE_TABLE_ANSWERA = "newtype_answera4";//  新题型SectionA题目及答案新题型SectionC原文
    public String NEWTYPE_TABLE_ANSWERB = "newtype_answerb4";//  新题型SectionB题目及答案
    public String NEWTYPE_TABLE_ANSWERC = "newtype_answerc4";//  新题型SectionC题目及答案
    public String NEWTYPE_TABLE_EXPLAIN = "newtype_explain4";//  新题型解析
    public String TABLE_ROLLVIEWINFO = "RollViewInfo";
    // 数据表共有字段
    public String FIELD_TESTTIME = "TestTime";// 真题年份
    public String FIELD_NUMBER = "Number";// 题号int
    public String FIELD_TIMING = "Timing";// 本句原文的时间 秒 20
    public String FIELD_NUMBERINDEX = "NumberIndex";// 本题对应的句子 2
    public String FIELD_SENTENCE = "Sentence";// 对应原文的句子
    public String FIELD_SOUND = "Sound";// 对应mp3文件名
    public String FIELD_VIPFLG = "VipFlg";// 对应mp3文件名
    public String FIELD_GOOD = "good";
    public String FIELD_BAD = "bad";
    public String FIELD_SCORE = "score";
    public String FIELD_record_url = "record_url";
    public String FIELD_SEX = "Sex";// 性别 M，W，SectionA

    public String FIELD_TIMING1 = "Timing1";// 第一遍音频的句子时间 SectionC
    public String FIELD_TIMING2 = "Timing2";// 第一遍音频的句子时间 SectionC
    public String FIELD_TIMING3 = "Timing3";// 第一遍音频的句子时间 SectionC
    public String FIELD_QWORDS = "QWords";// 本句中要填的词数，若大于0则用表answera中对应句子替换
    // SectionC
    // answera4、answerb4字段
    public String FIELD_QUESTION = "Question";// 题目问题
    public String FIELD_ANSWERA = "AnswerA";// 选项A内容
    public String FIELD_ANSWERB = "AnswerB";// 选项B内容
    public String FIELD_ANSWERC = "AnswerC";// 选项C内容
    public String FIELD_ANSWERD = "AnswerD";// 选项D内容
    public String FIELD_ANSWER = "Answer";// 答案

    public String FIELD_KEYWORD1 = "KeyWord1";// 长句子中答案关键词1 SectionC
    public String FIELD_KEYWORD2 = "KeyWord2";// 长句子中答案关键词2 SectionC
    public String FIELD_KEYWORD3 = "KeyWord3";// 长句子中答案关键词3 SectionC

    // explain4字段
    public String FIELD_TESTTYPE = "TestType";// Section类型，1为SectionA，2为SectionB，3为SectionC
    public String FIELD_KEYS = "Keyss";// 关键
    public String FIELD_EXPLAIN = "Explains";// 解释
    public String FIELD_KNOWLEDGE = "Knowledge";// 知识
    //StudyRecord 表的字段
    public static final String TABLE_NAME_STUDYRECORD = "StudyRecord";
    //	public static final String _ID = "_id";
    public static final String UID = "uid";
    public static final String APPID = "appId";
    public static final String APPNAME = "appName";
    public static final String BEGINTIME = "BeginTime";
    public static final String ENDTIME = "EndTime";
    public static final String LESSON = "Lesson";
    public static final String LESSONID = "LessonId";
    public static final String TESTNUMBER = "TestNumber";
    public static final String ENDFLG = "EndFlg";
    public static final String DEVICE = "Device";
    public static final String DEVICEID = "DeviceId";
    public static final String IP = "IP";
    public static final String UPDATETIME = "updateTime";
    public static final String ISUPLOAD = "IsUpload";


    //TestRecord 表的字段
    public static final String TABLE_NAME_TESTRECORD = "TestRecord";
    //	public static final String _ID = "_id";
//	public static final String UID = "uid";
//	public static final String APPID = "appId";
//	public static final String APPNAME = "appName";
//	public static final String BEGINTIME = "BeginTime";
    public static final String TESTTIME = "TestTime";
    //	public static final String LESSONID = "LessonId";
//	public static final String TESTNUMBER = "TestNumber";
//	public static final String DEVICEID = "DeviceId";
//	public static final String UPDATETIME = "updateTime";
    public static final String USERANSWER = "UserAnswer";
    public static final String RIGHTANSWER = "RightAnswer";
    public static final String ANSWERRESULT = "AnswerResult";
//	public static final String ISUPLOAD = "IsUpload";

    //CoursePack表
    public static final String TABLE_NAME_COURSEPACK = "CoursePack";
    public static final String ID = "id";
    public static final String PRICE = "price";
    public static final String DESC = "desc";
    public static final String NAME = "name";
    public static final String OWNERID = "ownerid";
    public static final String PICURL = "picUrl";
    public static final String CLASSNUM = "classNum";

    //CourseContent表
    public static final String TABLE_NAME_COURSECONTENT = "CourseContent";
    //	public static final String ID = "id";
    public static final String COST = "cost";
    public static final String TITLENAME = "titleName";

    public static final String ISDOWNLOAD = "IsDownload";
    public static final String PROGRESS = "Progress";
    public static final String ISFREE = "IsFree";
    public static final String PACKID = "PackId";
//	public static final String LESSON = "lesson";

    public static final String BTID = "btid";
    public static final String BTNAME = "btname";

//	public static final String TID = "tid";

    //MobClassRes表
    public static final String TABLE_NAME_MOBCLASSRES = "MobClassRes";
    //	public static final String ID = "id";
    public static final String IMAGENAME = "imageName";
    public static final String SECONDS = "seconds";
    public static final String ANSWER = "answer";
    public static final String NUMBER = "number";
    public static final String TYPE = "type";
    public static final String TITLEID = "TitleId";
//	public static final String PACKID = "PackId";


    //CoursePackDescInfo表
    public static final String TABLE_NAME_COURSEPACKDESCINFO = "CoursePackDescInfo";
    //	public static final String ID = "id";
    public static final String DETAIL = "detail";
    public static final String CONDITION = "condition";
    public static final String TID = "tid";
    public static final String RECOMMENDID = "recommendId";
    public static final String VIEWCOUNT = "viewCount";
    //CoursePackType表
    public static final String TABLE_NAME_COURSEPACKTYPE = "CoursePackType";
    //	public static final String ID = "id";
//	public static final String DESC = "desc";
//	public static final String CONDITION = "condition";
//	public static final String NAME = "name";
//	public static final String TYPE = "type";
    public static final String DESTINATION = "destination";

    //CoursePack表
    public static final String TABLE_NAME_TEACHERINFO = "TeacherInfo";
    //	public static final String TID = "tid";
    public static final String TIMG = "timg";
    public static final String TDES = "tdes";
    public static final String TNAME = "tname";

    //RollViewINfo 表
    public static final String ROWVIEW_PICURL = "picurl";

    SQLiteDatabase mDB;

    private Context mContext;

    /**
     * 打开已经存到sd卡上的数据库文件，若没有则重新复制
     *
     * @param context
     */
    public CetDBHelper(Context context) {// 如果数据库文件不存在则重新拷贝
        mContext = context;

        mDB = SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/"
                + Constant.DB_NAME_CET, null);

    }

    /**
     * @return 功能：打开数据库
     */
    private SQLiteDatabase openDatabase() {
        Log.d("打开Cet4DBHelper帮助类", "110110110110110110110110110110110110");
        return SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/"
                + Constant.DB_NAME_CET, null);
//        return SQLiteDatabase.openOrCreateDatabase(ImportCetDatabase.filePath,null);


    }


    public SubTextAB getRecordingResult(SubTextAB subTextAB, String type) {
        String tablename1 = NEWTYPE_TABLE_TEXTA;
        if ("A".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTA;
        } else if ("B".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTB;
        } else if ("C".equals(type)) {
            tablename1 = NEWTYPE_TABLE_TEXTC;
        }
        String[] goodsString = new String[]{};
        String[] badsString = new String[]{};
        if (!mDB.isOpen()) {
            Log.d("not open,", "dddddd");
            mDB = openDatabase();
        }
        String querySQL = "select * from " + tablename1 + " where " + FIELD_NUMBERINDEX + " = " + subTextAB.NumberIndex +
                " and " + FIELD_NUMBER + " = " + subTextAB.Number +
                " and " + FIELD_TESTTIME + " = " + subTextAB.TestTime;

        Cursor cur = mDB.rawQuery(querySQL, null);

        if (cur.getCount() > 0) {
            try {
                cur.moveToFirst();
                subTextAB.goodList = new ArrayList<>();
                subTextAB.badList = new ArrayList<>();

                subTextAB.record_url = cur.getString(cur.getColumnIndex(FIELD_record_url));
                if (!TextUtils.isEmpty(cur.getString(cur.getColumnIndex(FIELD_SCORE)))) {
                    subTextAB.readScore = Integer.parseInt(cur.getString(cur.getColumnIndex(FIELD_SCORE)));
                    subTextAB.isRead = true;
                }
                String goods = cur.getString(cur.getColumnIndex(FIELD_GOOD));
                String bads = cur.getString(cur.getColumnIndex(FIELD_BAD));
                // 转义字符
                if (!TextUtils.isEmpty(goods))
                    goodsString = goods.split("\\+");
                if (!TextUtils.isEmpty(bads))
                    badsString = bads.split("\\+");
                try {
                    for (int i = 0; i < goodsString.length; i++) {
                        subTextAB.goodList.add(Integer.valueOf(goodsString[i]));
                    }
                    for (int i = 0; i < badsString.length; i++) {
                        subTextAB.badList.add(Integer.valueOf(badsString[i]));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return subTextAB;
    }


    /**
     * 更新ContentCourse中的下载进度
     *
     * @param packName
     * @param progress
     * @return
     */
    public void setProgress(String titleId, float progress) {

        Log.d("更新当前进度：", progress + "");

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }

        String sqlString = "update " + TABLE_NAME_COURSECONTENT
                + " set Progress=" + progress
                + " where id='" + titleId + "'";
        mDB.execSQL(sqlString);
        //mDB.close();
    }


    //**************************移动课堂相关的数据库操作(CourseContent)*****************************
    //****************************************************************************************

    //****************************************************************************************
    //**************************移动课堂相关的数据库操作(MobClassRes)*******************************


    public int getRecordedNums(String year, String tablename, int iSectionType) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        int iCount = 0;
        Cursor cur = null;
        String sql = "select * from " + tablename + " where TestTime = " + year;
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {

                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    String score = "";
                    score = cur.getString(cur
                            .getColumnIndex(FIELD_SCORE));
                    if (!TextUtils.isEmpty(score)) {
                        iCount++;
                    }
                    cur.moveToNext();
                }
            }
            cur.close();
            //mDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            //mDB.close();
        }
        return iCount;
    }

    public void writeRecordingData(SubTextAB subTextAB, String type) {
        String tablename1 = NEWTYPE_TABLE_TEXTA;
        String tablename2 = NEWTYPE_TABLE_TEXTB;
        String tablename3 = NEWTYPE_TABLE_TEXTC;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String goodString = "";
        for (int i = 0; i < subTextAB.goodList.size(); i++) {
            goodString += subTextAB.goodList.get(i);
            if (i + 1 != subTextAB.goodList.size()) {
                goodString += "+";
            }
        }
        String badString = "";
        for (int i = 0; i < subTextAB.badList.size(); i++) {
            badString += subTextAB.badList.get(i);
            if (i + 1 != subTextAB.badList.size()) {
                badString += "+";
            }
        }

        String tablename = tablename1;
        if (TextUtils.equals(type, "A")) {
            tablename = tablename1;
        } else if (TextUtils.equals(type, "B")) {
            tablename = tablename2;
        } else if (TextUtils.equals(type, "C")) {
            tablename = tablename3;
        }
        String sql = "UPDATE " + tablename + " SET " + FIELD_BAD + " = \"" + badString + "\"," +
                FIELD_GOOD + " = \"" + goodString + "\" ," +
                FIELD_record_url + " = \"" + subTextAB.record_url + "\" ," +
                FIELD_SCORE + " = \"" + subTextAB.readScore + "\"" +
                " WHERE " + FIELD_NUMBER + " = " + subTextAB.Number +
                " AND " + FIELD_TESTTIME + " = " + subTextAB.TestTime +
                " AND " + FIELD_NUMBERINDEX + " = " + subTextAB.NumberIndex;
        mDB.execSQL(sql);

    }


    //**************************移动课堂相关的数据库操作(TeacherInfo)**********************************
    //**********************************************************************************************


    /**
     * @return 功能：从指定表中查询某年份的所有试题结果，并得到是否加入收藏 : Section A ,B
     */
    public ArrayList<AnswerAB> getSectionAByYear(String year, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<AnswerAB> answerABs = new ArrayList<>();
        Cursor cur = null;
        String sql = "select *  from " + tablename + " where TestTime = "
                + year + " order by " + FIELD_NUMBER + " asc";
        try {
            cur = mDB.rawQuery(sql, null);
            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0; i < cur.getCount(); i++) {
                    AnswerAB temp = new AnswerAB();
                    int it = cur.getInt(cur.getColumnIndex("TestTime"));// TestTime
                    temp.TestTime = Integer.toString(cur.getInt(cur
                            .getColumnIndex("TestTime")));
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));
                    temp.Question = cur.getString(cur
                            .getColumnIndex("Question"));
                    temp.AnswerA = cur.getString(cur.getColumnIndex("AnswerA"));
                    temp.AnswerB = cur.getString(cur.getColumnIndex("AnswerB"));
                    temp.AnswerC = cur.getString(cur.getColumnIndex("AnswerC"));
                    temp.AnswerD = cur.getString(cur.getColumnIndex("AnswerD"));
                    temp.Answer = cur.getString(cur.getColumnIndex("Answer"));
                    temp.Sound = cur.getString(cur.getColumnIndex("Sound"));
                    temp.flg = cur.getInt(cur.getColumnIndex("flg"));
                    answerABs.add(i, temp);
                    cur.moveToNext();
                }

            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }

        return answerABs;

    }

    /**
     * @param year     年份
     * @param testNum  题号
     * @param testtype 类型 A:1 B:2 C:3
     * @return 功能：查询解析内容
     */
    public ArrayList<Explain> getExplains(String year, String tablename,
                                          int testtype) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<Explain> explains = new ArrayList<Explain>();

        Cursor cur = null;

        // String sql =
        // "select *  from "+tablename+" where TestTime = "+year+" order by " +
        // FIELD_NUMBER + " asc";
        String sql = "select *  from " + tablename + " where TestTime = "
                + year + " and TestType = " + testtype + " order by "
                + FIELD_NUMBER + " asc";
        System.out.println(sql);
        try {
            cur = mDB.rawQuery(sql, null);

            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0; i < cur.getCount(); i++) {
                    Explain temp = new Explain();
                    int it = cur.getInt(cur.getColumnIndex("TestTime"));// TestTime
                    temp.TestTime = Integer.toString(cur.getInt(cur
                            .getColumnIndex("TestTime")));
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));

                    temp.Keys = cur.getString(cur.getColumnIndex(FIELD_KEYS));
                    temp.Explain = cur.getString(cur
                            .getColumnIndex(FIELD_EXPLAIN));
                    temp.Knowledge = cur.getString(cur
                            .getColumnIndex(FIELD_KNOWLEDGE));

                    explains.add(i, temp);
                    cur.moveToNext();

                }

            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }

        return explains;

    }

    /**
     * @param year     年份
     * @param testNum  题号
     * @param testtype 类型 A:1 B:2 C:3
     * @return 功能：查询解析内容
     */
    public ArrayList<Explain> getExplains(String year, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<Explain> explains = new ArrayList<Explain>();

        Cursor cur = null;

        // String sql =
        // "select *  from "+tablename+" where TestTime = "+year+" order by " +
        // FIELD_NUMBER + " asc";
        String sql = "select *  from " + tablename + " where TestTime = "
                + year + " order by "
                + FIELD_NUMBER + " asc";
        System.out.println(sql);
        try {
            cur = mDB.rawQuery(sql, null);

            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0; i < cur.getCount(); i++) {
                    Explain temp = new Explain();
                    int it = cur.getInt(cur.getColumnIndex("TestTime"));// TestTime
                    temp.TestTime = Integer.toString(cur.getInt(cur
                            .getColumnIndex("TestTime")));
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));

                    temp.Keys = cur.getString(cur.getColumnIndex(FIELD_KEYS));
                    temp.Explain = cur.getString(cur
                            .getColumnIndex(FIELD_EXPLAIN));
                    temp.Knowledge = cur.getString(cur
                            .getColumnIndex(FIELD_KNOWLEDGE));

                    explains.add(i, temp);
                    cur.moveToNext();

                }

            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }

        return explains;

    }

    /**
     * @return 功能： 获取原文
     * <p>
     * iSectionType: 0-> A , 1->B
     */
    // public ArrayList<TextAB> getTextsByYear(String year, String tablename)
    public ArrayList<TextAB> getTextsByYear(String year, String tablename,
                                            int iSectionType) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<TextAB> testNums = new ArrayList<TextAB>();
        Cursor cur = null;
        String type = "";

        // UserDBHelper userDBHelper = new UserDBHelper(mContext);
        String sql = "select * from " + tablename + " where TestTime = " + year
                + "  group by Number";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    TextAB temp = new TextAB();
                    temp.TestTime = year;
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));
                    // temp.subTexts = getSubTextByTestNum(year,
                    // cur.getString(cur.getColumnIndex("Number")), tablename);
                    temp.subTexts = getSubTextByTestNum(year,
                            cur.getString(cur.getColumnIndex("Number")),
                            tablename, iSectionType);
                    for (SubTextAB subTempABTemp : temp.subTexts) {
                        if (subTempABTemp.Sound != null
                                && subTempABTemp.Sound.length() != 0) {
                            String s = subTempABTemp.Sound;
                            temp.Sound = (TextUtils.isEmpty(s) || "null"
                                    .equalsIgnoreCase(s)) ? "" : s;
                            break;
                        }
                    }

                    testNums.add(i, temp);
                    cur.moveToNext();
                }
            }

            cur.close();
            //mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            //mDB.close();
        }
        return testNums;
    }

    /**
     * @return 功能：根据年份和题号从指定表中得到索引、时间、句子列表
     * <p>
     * iSectionType : 0->A, 1->B
     */
    // private ArrayList<SubTextAB> getSubTextByTestNum(String year, String
    // testNum, String tablename)
    private ArrayList<SubTextAB> getSubTextByTestNum(String year,
                                                     String testNum, String tablename, int iSectionType) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<SubTextAB> subTextAb = new ArrayList<SubTextAB>();
        Cursor cur = null;
        String sql = "select * from " + tablename + " where TestTime = " + year
                + " and Number = " + testNum + " order by NumberIndex asc";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    SubTextAB temp = new SubTextAB();
                    temp.NumberIndex = cur.getInt(cur
                            .getColumnIndex("NumberIndex"));
                    temp.Number = cur.getInt(cur
                            .getColumnIndex("Number"));
                    temp.TestTime = cur.getInt(cur
                            .getColumnIndex("TestTime"));
                    temp.Timing = cur.getInt(cur.getColumnIndex("Timing"));
                    temp.moreTiming = cur.getFloat(cur.getColumnIndex("moretiming"));
                    temp.Sentence = cur.getString(cur
                            .getColumnIndex("Sentence"));
                    temp.Sound = cur.getString(cur.getColumnIndex("Sound"));
                    if (0 == iSectionType) { // Section A
                        temp.Sex = cur.getString(cur.getColumnIndex("Sex"));
                    }
                    subTextAb.add(i, temp);
                    cur.moveToNext();
                }
            }

            cur.close();
            //mDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            //mDB.close();
        }
        return subTextAb;
    }

    /**
     * @return 功能：从指定表中查询某年份的所有试题结果，并得到是否加入收藏 : Section C
     */
    public ArrayList<AnswerC> getSectionCbyYear(String year, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<AnswerC> answerCs = new ArrayList<AnswerC>();

        Cursor cur = null;

        String sql = "select *  from " + tablename + " where TestTime = "
                + year + " order by " + FIELD_NUMBER + " asc";

        try {
            cur = mDB.rawQuery(sql, null);

            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0; i < cur.getCount(); i++) {
                    AnswerC temp = new AnswerC();

                    int it = cur.getInt(cur.getColumnIndex("TestTime"));// TestTime
                    temp.TestTime = Integer.toString(cur.getInt(cur
                            .getColumnIndex("TestTime")));
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));
                    temp.Question = cur.getString(cur
                            .getColumnIndex("Question"));
                    temp.Answer = cur.getString(cur.getColumnIndex("Answer"));
                    temp.Sound = cur.getString(cur.getColumnIndex("Sound"));
                    temp.KeyWord1 = cur.getString(cur
                            .getColumnIndex("KeyWord1"));
                    temp.KeyWord2 = cur.getString(cur
                            .getColumnIndex("KeyWord2"));
                    temp.KeyWord3 = cur.getString(cur
                            .getColumnIndex("KeyWord3"));
                    int keyWordsCount = 0;
                    if (temp.KeyWord1 != null && temp.KeyWord1.length() != 0) {
                        keyWordsCount = keyWordsCount + 1;
                    }
                    if (temp.KeyWord2 != null && temp.KeyWord2.length() != 0) {
                        keyWordsCount = keyWordsCount + 1;
                    }
                    if (temp.KeyWord3 != null && temp.KeyWord3.length() != 0) {
                        keyWordsCount = keyWordsCount + 1;
                    }
                    temp.KeyWordsCount = keyWordsCount;

                    answerCs.add(i, temp);
                    cur.moveToNext();

                }

            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }

        return answerCs;
    }

    /**
     * @return 功能： 获取Section C 原文
     */

    public ArrayList<TextC> getTextCsByYear(String year, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<TextC> testNums = new ArrayList<TextC>();
        Cursor cur = null;
        String type = "";

        // UserDBHelper userDBHelper = new UserDBHelper(mContext);
        String sql = "select distinct Number from " + tablename
                + " where TestTime = " + year + " order by Number asc";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    TextC temp = new TextC();
                    temp.TestTime = year;
                    temp.Number = cur.getInt(cur.getColumnIndex("Number"));

                    temp.subTextCs = getSubTextCbyTestNum(year,
                            cur.getString(cur.getColumnIndex("Number")),
                            tablename);

                    testNums.add(i, temp);
                    cur.moveToNext();
                }
            }
            cur.close();
            //mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            //mDB.close();
        }
        return testNums;
    }

    /**
     * @param sectionAnswerC
     * @return 功能： 获取Section C 问题
     */

    public ArrayList<String> getQuestionCsByYear(String year, String tablename,
                                                 ArrayList<TextC> sectionCtext, ArrayList<AnswerC> sectionAnswerC) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<String> question = new ArrayList<String>();
        for (int i = 0; i < sectionCtext.size(); i++) {
            for (int j = 0; j < sectionCtext.get(i).subTextCs.size(); j++) {
                if (sectionCtext.get(i).subTextCs.get(j).Qwords > 0) {
                    // 当QWords>0,则这句话为问题句,将answerC表中Question句替换掉textC当前的句子
                    question.add(sectionAnswerC.get(i).Question);
                } else {
                    question.add(sectionCtext.get(i).subTextCs.get(j).Sentence);
                }
            }
        }

        return question;
    }

    /**
     * @return 功能：根据年份和题号从指定表中得到索引、时间、句子列表
     * <p>
     * Section C
     */

    private ArrayList<SubTextC> getSubTextCbyTestNum(String year,
                                                     String testNum, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<SubTextC> subTextc = new ArrayList<SubTextC>();
        Cursor cur = null;
        String sql = "select * from " + tablename + " where TestTime = " + year
                + " and Number = " + testNum + " order by NumberIndex asc";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    SubTextC temp = new SubTextC();
                    temp.NumberIndex = cur.getInt(cur
                            .getColumnIndex("NumberIndex"));

                    temp.Timing1 = cur.getInt(cur.getColumnIndex("Timing1"));
                    temp.Timing2 = cur.getInt(cur.getColumnIndex("Timing2"));
                    temp.Timing3 = cur.getInt(cur.getColumnIndex("Timing3"));
                    temp.Sentence = cur.getString(cur
                            .getColumnIndex("Sentence"));
                    temp.Qwords = cur.getInt(cur.getColumnIndex("QWords"));

                    subTextc.add(i, temp);
                    cur.moveToNext();
                }
            }

            cur.close();
            //mDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            //mDB.close();
        }
        return subTextc;
    }


    public String getSentence(int year, String paraid,
                              String idIndex, String type) {
        String s = "";
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
//		ArrayList<Explain> explains = new ArrayList<Explain>();

        String tb = NEWTYPE_TABLE_TEXTA;
        if ("A".equals(type)) {
            tb = NEWTYPE_TABLE_TEXTA;
        } else if ("B".equals(type)) {
            tb = NEWTYPE_TABLE_TEXTB;
        } else {
            tb = NEWTYPE_TABLE_TEXTC;
        }
        Cursor cur = null;

        // String sql =
        // "select *  from "+tablename+" where TestTime = "+year+" order by " +
        // FIELD_NUMBER + " asc";
        String sql = "select *  from " + tb + " where TestTime = "
                + year + " and Number = " + paraid + " and NumberIndex = " + idIndex + " order by "
                + FIELD_NUMBER + " asc";

        try {
            cur = mDB.rawQuery(sql, null);

            int iC = cur.getCount();
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                s = cur.getString(cur.getColumnIndex("Sentence"));
            }

            cur.close();
            //mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                //mDB.close();
            }
        }
        return s;
    }


    public ArrayList<SubTextAB> searchArticleByWords(String search, String testType, boolean isNew) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String tablename = NEWTYPE_TABLE_TEXTA;
        if (TextUtils.equals("A", testType)) {
            tablename = NEWTYPE_TABLE_TEXTA;
        } else if (TextUtils.equals("B", testType)) {
            tablename = NEWTYPE_TABLE_TEXTB;
        } else if (TextUtils.equals("C", testType)) {
            tablename = NEWTYPE_TABLE_TEXTC;
        }
        if (!isNew) {
            if (TextUtils.equals("A", testType)) {
                tablename = TABLE_TEXTA;
            } else if (TextUtils.equals("B", testType)) {
                tablename = TABLE_TEXTB;
            }
        }
        ArrayList<SubTextAB> list = new ArrayList<>();
        String queryString = "select * from " + tablename + " where " + FIELD_SENTENCE + " like \"%" + search + "%\"";


        Cursor cursor = mDB.rawQuery(queryString, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isLast()) {
                SubTextAB subTextAB = new SubTextAB();
                subTextAB.TestTime = cursor.getInt(cursor.getColumnIndex(FIELD_TESTTIME));
                subTextAB.Section = testType;
                subTextAB.Sentence = cursor.getString(cursor.getColumnIndex(FIELD_SENTENCE));
                subTextAB.NumberIndex = cursor.getInt(cursor.getColumnIndex(FIELD_NUMBERINDEX));
                subTextAB.Number = cursor.getInt(cursor.getColumnIndex(FIELD_NUMBER));
                subTextAB.Timing = cursor.getInt(cursor.getColumnIndex(FIELD_TIMING));
                //subTextAB.moreTiming = cursor.getFloat(cursor.getColumnIndex("moretiming"));  //有这一行时会有崩溃，注掉貌似也不会有影响
                String queryNextString = "select * from " + tablename + " where " + FIELD_NUMBERINDEX + " = " + (subTextAB.NumberIndex + 1) +
                        " and " + FIELD_TESTTIME + " = " + subTextAB.TestTime + " and " + FIELD_NUMBER + " = " + subTextAB.Number;
                String querySoundString = "select * from " + tablename + " where " + FIELD_NUMBERINDEX + " = 1" +
                        " and " + FIELD_TESTTIME + " = " + subTextAB.TestTime + " and " + FIELD_NUMBER + " = " + subTextAB.Number;
                Cursor tempCursor = mDB.rawQuery(queryNextString, null);
                Cursor soundCursor = mDB.rawQuery(querySoundString, null);
                if (tempCursor != null && tempCursor.getCount() > 0) {
                    tempCursor.moveToFirst();
                    subTextAB.endTiming = tempCursor.getFloat(tempCursor.getColumnIndex(FIELD_TIMING));
                    tempCursor.close();
                } else {
                    if (tempCursor != null)
                        tempCursor.close();
                    if (subTextAB.moreTiming == 0) {
                        subTextAB.endTiming = subTextAB.Timing + 20;
                    } else {
                        subTextAB.endTiming = subTextAB.moreTiming + 20;
                    }

                }
                if (soundCursor != null && soundCursor.getCount() > 0) {
                    soundCursor.moveToFirst();
                    subTextAB.Sound = soundCursor.getString(tempCursor.getColumnIndex(FIELD_SOUND));
                    soundCursor.close();
                }
                list.add(subTextAB);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return list;
    }

    /**
     * 添加到学习记录
     *
     * @return
     */
    public boolean saveTestRecord(TestRecord testRecord) {
        String sqlString = "insert into TestRecord(uid,LessonId,TestNumber,UserAnswer,RightAnswer,AnswerResult,BeginTime,"
                + "TestTime,IsUpload)" + " values(?,?,?,?,?,?,?,?,?)";
        Object[] objects = new Object[]{testRecord.uid, testRecord.LessonId, testRecord.TestNumber, testRecord.UserAnswer,
                testRecord.RightAnswer, testRecord.AnswerResult, testRecord.BeginTime, testRecord.TestTime, testRecord.IsUpload};

        //未添加用户名的查询命令
//		String sqlString = "insert into FavoriteWord(word,audio,pron,def,CreateDate)"
//				+" values(?,?,?,?,?)";
//		Object[] objects=new Object[]{newWord.Word,
//				newWord.audio,newWord.pron,newWord.def,newWord.CreateDate};

//		Log.d("NewWord Content:", "word: " + newWord.Word + " audio:"
//				+ newWord.audio + " pron:" + newWord.pron + " def:"
//				+ newWord.def + " userName:" + newWord.userName + " CreateDate"
//				+ newWord.CreateDate);

        return exeSql(sqlString, objects);
    }


    public boolean exeSql(String sqlString, Object[] objects) {
        boolean flag;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString, objects);
            //mDB.close();
            flag = true;
        } catch (Exception e) {
            flag = false;
            // TODO: handle exception
        } finally {
            if (mDB.isOpen()) {
                //mDB.close();
            }
        }
        return flag;
    }

    public void writeToRoomDB(Context context) {
        List<CetRootWord> words = new ArrayList<>();
        Cursor c;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        if (Constant.level.equals("4")) {
            c = mDB.rawQuery("select * from cet4word", null);
        } else {
            c = mDB.rawQuery("select * from cet6word", null);
        }
        c.moveToFirst();

        while (c.moveToNext()) {
            CetRootWord word = new CetRootWord();
            word.def = c.getString(c.getColumnIndex("def"));
            word.pron = c.getString(c.getColumnIndex("pron"));
            word.sound = c.getString(c.getColumnIndex("sound"));
            word.word = c.getString(c.getColumnIndex("word"));
            word.remembered = 0;
            words.add(word);
        }
        CetDataBase.getInstance(context.getApplicationContext()).getUserDao().insertWord(words);
        ConfigManager.Instance().putBoolean("wordloaded", true);
        c.close();
    }

}

