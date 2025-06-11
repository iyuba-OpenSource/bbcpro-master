package com.ai.bbcpro.word.cetDB;



import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;


import com.ai.bbcpro.Constant;
import com.ai.bbcpro.word.AnswerAB;
import com.ai.bbcpro.word.AnswerC;
import com.ai.bbcpro.word.Explain;
import com.ai.bbcpro.word.NetTextA;
import com.ai.bbcpro.word.NetTextB;
import com.ai.bbcpro.word.NetTextC;
import com.ai.bbcpro.word.SubTextAB;
import com.ai.bbcpro.word.SubTextC;
import com.ai.bbcpro.word.TextAB;
import com.ai.bbcpro.word.TextC;


import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * @author zqq
 * <p>
 * 功能：对已有数据库文件进行操作
 */
public class Cet4DBHelper {
    public String TABLE_TEXTA4 = "texta4";// SectionA原文
    public String TABLE_TEXTB4 = "textb4";// SectionB原文
    public String TABLE_TEXTC4 = "textc4";// SectionC原文
    public String TABLE_ANSWERA4 = "answera4";// SectionA题目及答案
    public String TABLE_ANSWERB4 = "answerb4";// SectionB题目及答案
    public String TABLE_ANSWERC4 = "answerc4";// SectionC题目及答案
    public String TABLE_EXPLAIN4 = "explain4";// 所有解析
    public String NEWTYPE_TABLE_TEXTA = "newtype_texta4";// 新题型SectionA原文
    public String NEWTYPE_TABLE_TEXTB = "newtype_textb4";// 新题型SectionB原文
    public String NEWTYPE_TABLE_TEXTC = "newtype_textc4";// 新题型SectionC原文

    public String NEWTYPE_TABLE_ANSWERA4 = "newtype_answera4";//  新题型SectionA题目及答案
    public String NEWTYPE_TABLE_ANSWERB4 = "newtype_answerb4";//  新题型SectionB题目及答案
    public String NEWTYPE_TABLE_ANSWERC4 = "newtype_answerc4";//  新题型SectionC题目及答案
    public String NEWTYPE_TABLE_EXPLAIN4 = "newtype_explain4";//  新题型解析


    public String TABLE_ROLLVIEWINFO = "RollViewInfo";
    // 数据表共有字段
    public String FIELD_TESTTIME = "TestTime";// 真题年份
    public String FIELD_NUMBER = "Number";// 题号int
    public String FIELD_TIMING = "Timing";// 本句原文的时间 秒 20
    public String FIELD_NUMBERINDEX = "NumberIndex";// 本题对应的句子 2
    public String FIELD_SENTENCE = "Sentence";// 对应原文的句子
    public String FIELD_SOUND = "Sound";// 对应mp3文件名
    public String FIELD_VIPFLG = "VipFlg";// 对应mp3文件名

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

    public SQLiteDatabase mDB;
    private Context mContext;

    /**
     * 打开已经存到sd卡上的数据库文件，若没有则重新复制
     *
     * @param context
     */

    public SQLiteDatabase getmDB() {
        return mDB;
    }


    public Cet4DBHelper(Context context) {// 如果数据库文件不存在则重新拷贝
        mContext = context;
//		if (TextUtils.isEmpty())
        Timber.tag("dddd").d("11012");
//        mDB = SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/" + Constant.DB_NAME_CET, null);
    }

    /**
     * @return 功能：打开数据库
     */
    public SQLiteDatabase openDatabase() {
        Timber.tag("dddd").d("11011");
//		try {
//			synchronized (SQLiteDatabase.class) {
        return SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/" + Constant.DB_NAME_CET, null);
//			}
//		} catch (SQLiteException e) {
//			e.printStackTrace();
//			return SQLiteDatabase.openOrCreateDatabase(Constant.DB_PATH + "/" + Constant.DB_NAME_CET, null);
//		}
    }

    /**
     * 功能：关闭数据库
     */

    public void closeDatabase() {
        mDB.close();
    }

    //****************************************************************************************
    //****************************应用启动时比较数据库是否为最新的试题*********************************
    //插入A表中的Answer表数据
    public synchronized void insertAnswerA(ArrayList<AnswerAB> answerABs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertAnswerA、B:", "执行插入操作");
        if (answerABs != null && answerABs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_ANSWERA4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_QUESTION + "," + FIELD_ANSWERA + ","
                    + FIELD_ANSWERB + "," + FIELD_ANSWERC + "," + FIELD_ANSWERD + "," + FIELD_ANSWER + ","
                    + FIELD_SOUND + "," + " flg " + ") values(?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < answerABs.size(); i++) {
                AnswerAB answerAB = answerABs.get(i);
                Object[] objects = new Object[]{answerAB.TestTime, answerAB.Number, answerAB.Question,
                        answerAB.AnswerA, answerAB.AnswerB, answerAB.AnswerC, answerAB.AnswerD, answerAB.Answer,
                        answerAB.Sound, answerAB.flg};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入A表中的Text表数据
    public synchronized void insertTextA(ArrayList<NetTextA> textAs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertTextA:", "执行插入操作");
        if (textAs != null && textAs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_TEXTA4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_NUMBERINDEX + "," + FIELD_TIMING + "," + FIELD_SENTENCE + ","
                    + FIELD_VIPFLG + "," + FIELD_SEX + "," + FIELD_SOUND + ") values(?,?,?,?,?,?,?,?)";

            for (int i = 0; i < textAs.size(); i++) {
                NetTextA textA = textAs.get(i);
                Object[] objects = new Object[]{textA.TestTime, textA.Number, textA.NumberIndex,
                        textA.Timing, textA.Sentence, textA.VipFlg, textA.Sex, textA.Sound};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入B表中的Answer表数据
    public synchronized void insertAnswerB(ArrayList<AnswerAB> answerABs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertAnswerB:", "执行插入操作");
        if (answerABs != null && answerABs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_ANSWERB4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_QUESTION + "," + FIELD_ANSWERA + ","
                    + FIELD_ANSWERB + "," + FIELD_ANSWERC + "," + FIELD_ANSWERD + "," + FIELD_ANSWER + ","
                    + FIELD_SOUND + "," + " flg " + ") values(?,?,?,?,?,?,?,?,?,?)";

            for (int i = 0; i < answerABs.size(); i++) {
                AnswerAB answerAB = answerABs.get(i);
                Object[] objects = new Object[]{answerAB.TestTime, answerAB.Number, answerAB.Question,
                        answerAB.AnswerA, answerAB.AnswerB, answerAB.AnswerC, answerAB.AnswerD, answerAB.Answer,
                        answerAB.Sound, answerAB.flg};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入B表中的Text表数据
    public synchronized void insertTextB(ArrayList<NetTextB> textBs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertTextB:", "执行插入操作");
        if (textBs != null && textBs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_TEXTB4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_NUMBERINDEX + "," + FIELD_TIMING + "," + FIELD_SENTENCE + ","
                    + FIELD_VIPFLG + "," + FIELD_SOUND + ") values(?,?,?,?,?,?,?)";

            for (int i = 0; i < textBs.size(); i++) {
                NetTextB textB = textBs.get(i);
                Object[] objects = new Object[]{textB.TestTime, textB.Number, textB.NumberIndex,
                        textB.Timing, textB.Sentence, textB.VipFlg, textB.Sound};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入C表中的Answer表数据
    public synchronized void insertAnswerC(ArrayList<AnswerC> answerCs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertAnswerC:", "执行插入操作");
        if (answerCs != null && answerCs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_ANSWERC4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_QUESTION + "," + FIELD_ANSWER + ","
                    + FIELD_KEYWORD1 + "," + FIELD_KEYWORD2 + "," + FIELD_KEYWORD3 + ","
                    + FIELD_SOUND + ") values(?,?,?,?,?,?,?,?)";

            for (int i = 0; i < answerCs.size(); i++) {
                AnswerC answerC = answerCs.get(i);
                Object[] objects = new Object[]{answerC.TestTime, answerC.Number, answerC.Question,
                        answerC.Answer, answerC.KeyWord1, answerC.KeyWord2, answerC.KeyWord3, answerC.Sound};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入C表中的Text表数据
    public synchronized void insertTextC(ArrayList<NetTextC> textCs) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertTextC:", "执行插入操作");
        if (textCs != null && textCs.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_TEXTC4 + " (" + FIELD_TESTTIME + ","
                    + FIELD_NUMBER + "," + FIELD_TIMING1 + "," + FIELD_TIMING2 + "," + FIELD_TIMING3 + "," + FIELD_NUMBERINDEX + ","
                    + FIELD_SENTENCE + "," + FIELD_QWORDS + ") values(?,?,?,?,?,?,?,?)";

            for (int i = 0; i < textCs.size(); i++) {
                NetTextC textC = textCs.get(i);
                Object[] objects = new Object[]{textC.TestTime, textC.Number, textC.Timing1, textC.Timing2,
                        textC.Timing3, textC.NumberIndex, textC.Sentence, textC.Qwords};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }

    //插入表中的Explain表数据
    public synchronized void insertExplain(ArrayList<Explain> explains) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Log.d("insertExplain:", "执行插入操作");
        if (explains != null && explains.size() != 0) {
            String sqlString = "insert or replace into " + TABLE_EXPLAIN4 + " (" + FIELD_TESTTIME + "," + FIELD_TESTTYPE + ","
                    + FIELD_NUMBER + "," + FIELD_KEYS + "," + FIELD_EXPLAIN + "," + FIELD_KNOWLEDGE + ","
                    + "Demo" + "," + "flg" + ") values(?,?,?,?,?,?,?,?)";

            for (int i = 0; i < explains.size(); i++) {
                Explain explain = explains.get(i);
                Object[] objects = new Object[]{explain.TestTime, explain.TestType, explain.Number,
                        explain.Keys, explain.Explain, explain.Knowledge, "", ""};
                mDB.execSQL(sqlString, objects);
            }
        }
        mDB.close();
    }


    public void deleteAnswerAByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_ANSWERA4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();

    }

    public void deleteAnswerBByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_ANSWERB4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();
    }

    public void deleteAnswerCByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_ANSWERC4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();

    }

    public void deleteTextAByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_TEXTA4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();
    }

    public void deleteTextBByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_TEXTB4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();
    }

    public void deleteTextCByYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_TEXTC4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();
    }

    public void deleteExplainByTestYear(String testYear) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sql = "delete from " + TABLE_EXPLAIN4 + " where " + FIELD_TESTTIME + "=" + testYear;
        mDB.execSQL(sql);
        mDB.close();
    }


    //****************************应用启动时比较数据库是否为最新的试题*********************************
    //****************************************************************************************

    //****************************************************************************************
    //***************************移动课堂相关的数据库操作(CoursePack)*******************************


    public synchronized void insertCoursePacks(List<CoursePack> courses) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        if (courses != null && courses.size() != 0) {
            String sqlString = "insert into " + TABLE_NAME_COURSEPACK + " (" + ID + ","
                    + PRICE + "," + DESC + "," + NAME + ","
                    + OWNERID + "," + PICURL + "," + CLASSNUM + ") values(?,?,?,?,?,?,?)";

            for (int i = 0; i < courses.size(); i++) {
                CoursePack course = courses.get(i);
                Object[] objects = new Object[]{course.id, course.price, course.desc,
                        course.name, course.ownerid, course.picUrl, course.classNum};
                mDB.execSQL(sqlString, objects);

            }
        }
        mDB.close();
    }

    /**
     * 删除课程包信息
     */

    public synchronized boolean deleteCoursePackData() {
        String sqlString = "delete from CoursePack";
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString);

            mDB.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 删除课程包中的每一小节课程的信息
     */

    public synchronized boolean deleteCourseContentData() {
        String sqlString = "delete from CourseContent";
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString);

            mDB.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 查找所有的移动课堂的课程包的信息
     *
     * @return
     */
    public synchronized ArrayList<CoursePack> findDataByAll() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CoursePack> courses = new ArrayList<CoursePack>();

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
//					"select *" + " from " + TABLE_NAME_COURSEPACK + " ORDER BY " + OWNERID + " asc"+","+ ID +" desc"
                    "select *" + " from " + TABLE_NAME_COURSEPACK
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CoursePack course = new CoursePack();
                course.id = cursor.getInt(0);
                course.price = cursor.getDouble(1);
                course.desc = cursor.getString(2);
                course.name = cursor.getString(3);
                course.ownerid = cursor.getInt(4);
                course.picUrl = cursor.getString(5);
                course.classNum = cursor.getInt(6);
                courses.add(course);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CoursePackSize:", courses.size() + " ");
        return courses;
    }


    /**
     * 查找所有的移动课堂的课程包的信息
     *
     * @return
     */
    public synchronized ArrayList<CoursePack> findDataByOwnerId(String ownerId) {

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CoursePack> courses = new ArrayList<CoursePack>();

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
//					"select *" + " from " + TABLE_NAME_COURSEPACK + " ORDER BY " + OWNERID + " asc"+","+ ID +" desc"
                    "select *" + " from " + TABLE_NAME_COURSEPACK + " where " + OWNERID + "=" + ownerId
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CoursePack course = new CoursePack();
                course.id = cursor.getInt(0);
                course.price = cursor.getDouble(1);
                course.desc = cursor.getString(2);
                course.name = cursor.getString(3);
                course.ownerid = cursor.getInt(4);
                course.picUrl = cursor.getString(5);
                course.classNum = cursor.getInt(6);
                courses.add(course);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Timber.tag("PackSizeByOwner").e(courses.size() + " ");
        return courses;
    }


    //**************************移动课堂相关的数据库操作(CoursePack)********************************
    //****************************************************************************************


    //****************************************************************************************
    //***************************移动课堂相关的数据库操作(CourseContent)****************************

    /**
     * 查找移动课堂的某个课程包的一级标题
     *
     * @return
     */
    public ArrayList<FirstTitleInfo> findCourseContentFirTitleBySpecial(String packId) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<FirstTitleInfo> firCourseTitles = new ArrayList<FirstTitleInfo>();

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select DISTINCT " + BTNAME + "," + BTID + " from " + TABLE_NAME_COURSECONTENT + " where " + PACKID + " = " + packId
                            + " ORDER BY " + BTID + " asc"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Log.d("查找移动课堂的某个课程包的一级标题", "执行");
                FirstTitleInfo firTitleInfo = new FirstTitleInfo();
                firTitleInfo.btname = cursor.getString(0);
                firTitleInfo.btid = cursor.getInt(1);
                firCourseTitles.add(firTitleInfo);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CourseFirTitle SEPCIAL:", firCourseTitles.size() + " ");
        return firCourseTitles;
    }

    /**
     * 查找移动课堂的某个课程包的一级标题下的二级标题
     *
     * @return
     */
    public ArrayList<SecondTitleInfo> findCourseContentSecTitleBySpecial(String btid) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<SecondTitleInfo> secCourseTitles = new ArrayList<SecondTitleInfo>();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select " + TITLENAME + "," + ID + " from " + TABLE_NAME_COURSECONTENT + " where " + BTID + " = " + btid
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Log.d("查找移动课堂的某个课程包的一级标题", "执行");
                SecondTitleInfo secTitleInfo = new SecondTitleInfo();

                secTitleInfo.titleName = cursor.getString(0);
                secTitleInfo.id = cursor.getInt(1);
                secCourseTitles.add(secTitleInfo);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CourseSecitle SEPCIAL:", secCourseTitles.size() + " ");
        return secCourseTitles;
    }


    /**
     * 查找移动课堂的某个TitleId对应的CourseContent对象
     *
     * @return
     */
    public CourseContent findCourseContentBySpecial(String TitleId) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        CourseContent course = new CourseContent();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSECONTENT + " where " + ID + " = " + TitleId
                    , new String[]{});
            if (cursor.moveToFirst()) {
                course.id = cursor.getInt(0);
                course.titleName = cursor.getString(1);
                course.cost = cursor.getDouble(2);
                course.IsFree = Boolean.parseBoolean(cursor.getString(3));
                course.Progress = cursor.getFloat(4);
                course.PackId = cursor.getInt(5);
                course.IsDownload = cursor.getInt(6);
                course.lesson = cursor.getString(7);
                course.btid = cursor.getInt(8);
                course.btname = cursor.getString(9);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return course;
    }

    /**
     * 查找移动课堂的某个课程包的一级标题下的二级标题对应的CourseContent对象
     *
     * @return
     */
    public ArrayList<CourseContent> findCourseContentBTidBySpecial(String btid) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CourseContent> courses = new ArrayList<CourseContent>();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSECONTENT + " where " + BTID + " = " + btid + " ORDER BY " + ID + " asc"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CourseContent course = new CourseContent();
                course.id = cursor.getInt(0);
                course.titleName = cursor.getString(1);
                course.cost = cursor.getDouble(2);
                course.IsFree = Boolean.parseBoolean(cursor.getString(3));
                course.Progress = cursor.getFloat(4);
                course.PackId = cursor.getInt(5);
                course.IsDownload = cursor.getInt(6);
                course.lesson = cursor.getString(7);
                course.btid = cursor.getInt(8);
                course.btname = cursor.getString(9);
                courses.add(course);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Timber.tag("BTidSEPCIAL:").e(courses.size() + " ");
        return courses;


    }


    /**
     * 查找  数据库里面的移动课堂中每套题中的课程数
     */
    public int findCourseContentSize() {

        int courseContentSize = 0;

        Cursor cursor = null;

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }

        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSECONTENT + " ORDER BY " + ID + " desc"
                    , new String[]{});
            courseContentSize = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CourseContentSize:", courseContentSize + " ");
        return courseContentSize;
    }

    /**
     * 插入  数据库里面的移动课堂中每套题中每一节课的内容
     */
    public synchronized void insertCourseContent(List<CourseContent> courses) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        if (courses != null && courses.size() != 0) {
            String sqlString = "insert into " + TABLE_NAME_COURSECONTENT + " (" + ID + ","
                    + TITLENAME + "," + COST + "," + ISFREE + ","
                    + PROGRESS + "," + ISDOWNLOAD + "," + PACKID + "," + LESSON + ","
                    + BTID + "," + BTNAME + ") values(?,?,?,?,?,?,?,?,?,?)";
            for (int i = 0; i < courses.size(); i++) {
                CourseContent course = courses.get(i);
                Object[] objects = new Object[]{course.id, course.titleName,
                        course.cost, course.IsFree, course.Progress,
                        course.IsDownload, course.PackId, course.lesson, course.btid, course.btname};
                mDB.execSQL(sqlString, objects);

            }
        }
        mDB.close();
    }

    /**
     * 查找移动课堂的所有课程包的内容信息
     *
     * @return
     */
    public ArrayList<CourseContent> findCourseContentDataByAll() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CourseContent> courses = new ArrayList<CourseContent>();
//		mDB=getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSECONTENT + " ORDER BY " + ID + " asc"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CourseContent course = new CourseContent();
                course.id = cursor.getInt(0);
                course.titleName = cursor.getString(1);
                course.cost = cursor.getDouble(2);
                course.IsFree = Boolean.parseBoolean(cursor.getString(3));
                course.Progress = cursor.getFloat(4);
                course.PackId = cursor.getInt(5);
                course.IsDownload = cursor.getInt(6);
                course.lesson = cursor.getString(7);
                course.btid = cursor.getInt(8);
                course.btname = cursor.getString(9);
                courses.add(course);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CourseContentSize ALL:", courses.size() + " ");
        return courses;
    }


    /**
     * 查找移动课堂的某个课程包的内容信息
     *
     * @return
     */
    public ArrayList<CourseContent> findCourseContentDataBySpecial(String packId) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CourseContent> courses = new ArrayList<CourseContent>();
//		mDB=getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSECONTENT + " where " + PACKID + " = " + packId + " ORDER BY " + ID + " asc"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CourseContent course = new CourseContent();
                course.id = cursor.getInt(0);
                course.titleName = cursor.getString(1);
                course.cost = cursor.getDouble(2);
                course.IsFree = Boolean.parseBoolean(cursor.getString(3));
                course.Progress = cursor.getFloat(4);
                course.PackId = cursor.getInt(5);
                course.IsDownload = cursor.getInt(6);
                course.lesson = cursor.getString(7);
                course.btid = cursor.getInt(8);
                course.btname = cursor.getString(9);
                courses.add(course);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Timber.tag("CourseentSize SEPCIAL:").e(courses.size() + " ");
        return courses;
    }

    public float findCourseContentProgressByClassName(String ClassName) {

        float CourseContentProgress = 0;

        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select Progress from " + TABLE_NAME_COURSECONTENT
                            + " where titleName = ?", new String[]{ClassName});
            if (cursor.moveToFirst()) {

                CourseContentProgress = cursor.getFloat(0);
            }
            cursor.close();
        } catch (Exception e) {
            if (cursor != null) {
                cursor.close();

            }
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        //Log.e("titleName:", ClassName+" ");
        //Log.e("CourseContentProgress:", CourseContentProgress+" ");
        return CourseContentProgress;
    }

    /**
     * 查找  数据库里面的移动课堂中某一节课的下载的进度
     */
    public float findCourseContentProgress(String pos) {

        float CourseContentProgress = 0;

        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select Progress from " + TABLE_NAME_COURSECONTENT
                            + " where titleName = ?", new String[]{pos});
            if (cursor.moveToFirst()) {

                CourseContentProgress = cursor.getFloat(0);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
//		Log.e("CourseContentProgress:", CourseContentProgress+" ");
        return CourseContentProgress;
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
        mDB.close();
    }

    public void setIsDownLoad(String titleId, int isdownload) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSECONTENT
                + " set IsDownLoad='" + isdownload + "', Progress=" + "1.0"
                + " where id='" + titleId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /**
     * 更新ContentCourse中的购买详情
     *
     * @param titleId
     * @param isdownload
     * @return
     */
    public void setIsFree(String titleId, boolean isfree) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSECONTENT
                + " set IsFree='" + isfree + "'"
                + " where id='" + titleId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    public void setIsFreeForPack(String packId, boolean isfree) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSECONTENT
                + " set IsFree='" + isfree + "'"
                + " where PackId='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /**
     * 更新ContentCourse中的下载,清空所有下载资源时，所有下载标志设为FALSE
     *
     * @param isdownload
     * @return
     */
    public void updateIsDownload(boolean isdownload) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSECONTENT
                + " set IsDownLoad='" + isdownload + "', Progress=" + "0";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    //**************************移动课堂相关的数据库操作(CourseContent)*****************************
    //****************************************************************************************

    //****************************************************************************************
    //**************************移动课堂相关的数据库操作(MobClassRes)*******************************

    /**
     * 查找  数据库里面的移动课堂中某一节课的资源信息数目
     */
    public synchronized int findMbTextSize(String titleId) {

        int MbTextSize = 0;

        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_MOBCLASSRES + " where TitleId =" + titleId + " ORDER BY " + ID + " desc"
                    , new String[]{});
            MbTextSize = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("MbTextSize:", MbTextSize + " ");
        return MbTextSize;
    }

    /**
     * 查找  数据库里面的移动课堂中某一节课的资源信息数目
     */
    public synchronized int findMbTextSize() {

        int MbTextSize = 0;

        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_MOBCLASSRES + " ORDER BY " + ID + " desc"
                    , new String[]{});
            MbTextSize = cursor.getCount();
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("MbTextSize:", MbTextSize + " ");
        return MbTextSize;
    }

    /**
     * 插入  数据库里面的移动课堂中某一节课的资源信息
     */
    public synchronized void insertMbText(List<MbText> mbTexts) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        if (mbTexts != null && mbTexts.size() != 0) {
            String sqlString = "insert into " + TABLE_NAME_MOBCLASSRES + " (" + ID + ","
                    + SECONDS + "," + IMAGENAME + "," + ANSWER
                    + "," + NUMBER + "," + TYPE
                    + "," + TITLEID + "," + PACKID
                    + ") values(?,?,?,?,?,?,?,?)";
            for (int i = 0; i < mbTexts.size(); i++) {
                MbText mbText = mbTexts.get(i);
                Object[] objects = new Object[]{mbText.id, mbText.seconds, mbText.imageName,
                        mbText.answer, mbText.number, mbText.type, mbText.TitleId, mbText.PackId};
                mDB.execSQL(sqlString, objects);
                Log.d("MbText内容的插入执行id", mbText.id + "");
            }
        }
        mDB.close();
    }


    /**
     * 查找移动课堂的某个课程包中某节课的资源信息
     *
     * @return
     */
    public synchronized ArrayList<MbText> findSpecialCourseResourceData(String titleId) {
        Log.e("111111111111", "2");
        ArrayList<MbText> mbTexts = new ArrayList<MbText>();
        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            Log.e("111111111111", "22");
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_MOBCLASSRES + " where TitleId =" + titleId
                            + " ORDER BY " + ID + " asc", new String[]{});
            Log.e("111111111111", "2222");
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MbText mbtext = new MbText();
                Log.e("111111111111", "2222222222");
                mbtext.id = cursor.getInt(0);
                mbtext.imageName = cursor.getString(1);
                mbtext.seconds = cursor.getInt(2);
                mbtext.answer = cursor.getInt(3);
                mbtext.number = cursor.getInt(4);
                mbtext.type = cursor.getInt(5);
                mbtext.TitleId = cursor.getInt(6);
                mbtext.PackId = cursor.getInt(7);
                mbTexts.add(mbtext);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("SpecialMbTextSize:", mbTexts.size() + " ");
        return mbTexts;
    }

    /**
     * 查找移动课堂的某个课程包中某节课的资源信息
     *
     * @return
     */
    public synchronized ArrayList<MbText> findSpecialCourseResourceData(String titleId, String PackId) {
        ArrayList<MbText> mbTexts = new ArrayList<MbText>();
//		mDB=getReadableDatabase();
        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_MOBCLASSRES + " where TitleId =" + titleId + " and PackId = " + PackId
                            + " ORDER BY " + ID + " asc", new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MbText mbtext = new MbText();
                mbtext.id = cursor.getInt(0);
                mbtext.imageName = cursor.getString(1);
                mbtext.seconds = cursor.getInt(2);
                mbtext.answer = cursor.getInt(3);
                mbtext.number = cursor.getInt(4);
                mbtext.type = cursor.getInt(5);
                mbtext.TitleId = cursor.getInt(6);
                mbtext.PackId = cursor.getInt(7);
                mbTexts.add(mbtext);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("SpecialMbTextSize:", mbTexts.size() + " ");
        return mbTexts;
    }

    /**
     * 查找移动课堂的某个课程包的资源信息
     *
     * @return
     */
    public synchronized ArrayList<MbText> findCourseResourceDataByAll() {
        ArrayList<MbText> mbTexts = new ArrayList<MbText>();
        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_MOBCLASSRES + " ORDER BY " + ID + " desc"
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                MbText mbtext = new MbText();
                mbtext.id = cursor.getInt(0);
                mbtext.imageName = cursor.getString(1);
                mbtext.seconds = cursor.getInt(2);
                mbtext.answer = cursor.getInt(3);
                mbtext.number = cursor.getInt(4);
                mbtext.type = cursor.getInt(5);
                mbtext.TitleId = cursor.getInt(6);
                mbtext.PackId = cursor.getInt(7);
                mbTexts.add(mbtext);
            }
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("MbTextSize:", mbTexts.size() + " ");
        return mbTexts;
    }

    //**************************移动课堂相关的数据库操作(MobClassRes)*****************************
    //****************************************************************************************

    //**********************************************************************************************
    //*********************移动课堂相关的数据库操作(CoursePackDescInfo)***********************************

    public synchronized void insertCoursePackDescs(CoursePackDescInfo cpdInfo) {

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        if (cpdInfo != null) {
            String sqlString = "insert into " + TABLE_NAME_COURSEPACKDESCINFO + " (" + ID + ","
                    + DETAIL + "," + CONDITION + "," + TID + ","
                    + RECOMMENDID + "," + VIEWCOUNT + ") values(?,?,?,?,?,?)";

            Object[] objects = new Object[]{cpdInfo.id, cpdInfo.detail, cpdInfo.condition,
                    cpdInfo.tid, cpdInfo.recommendId, cpdInfo.viewCount};
            mDB.execSQL(sqlString, objects);

        }
        mDB.close();
    }

    /**
     * 删除  数据库里面的课程描述
     */

    public synchronized boolean deleteCoursePackDescInfoData() {
        String sqlString = "delete from CoursePackDescInfo";
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString);

            mDB.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 查找所有的移动课堂的课程包的描述信息
     *
     * @return
     */
    public synchronized ArrayList<CoursePackDescInfo> findDataByAllCoursePackDesc() {
        ArrayList<CoursePackDescInfo> cpdInfos = new ArrayList<CoursePackDescInfo>();

        Cursor cursor = null;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSEPACKDESCINFO
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CoursePackDescInfo cpd = new CoursePackDescInfo();
                cpd.id = cursor.getInt(0);
                cpd.detail = cursor.getString(1);
                cpd.condition = cursor.getString(2);
                cpd.tid = cursor.getInt(3);
                cpd.recommendId = cursor.getInt(4);
                cpd.viewCount = cursor.getInt(5);
                cpdInfos.add(cpd);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CoursePackDescInfo:", cpdInfos.size() + " ");
        return cpdInfos;
    }

    /**
     * 查找所有的移动课堂的课程包的描述信息
     *
     * @return
     */
    public synchronized CoursePackDescInfo findCoursePackDescDataByOwnerId(String packId) {
        CoursePackDescInfo cpd = null;

        Cursor cursor = null;

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_COURSEPACKDESCINFO + " where " + ID + "=" + packId
                    , new String[]{});
            if (cursor.moveToFirst()) {
                cpd = new CoursePackDescInfo();
                cpd.id = cursor.getInt(0);
                cpd.detail = cursor.getString(1);
                cpd.condition = cursor.getString(2);
                cpd.tid = cursor.getInt(3);
                cpd.recommendId = cursor.getInt(4);
                cpd.viewCount = cursor.getInt(5);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return cpd;


    }

    /*
     * 更新课程包描述信息表的detail字段
     * */
    public void setDetailForPackDesc(String packId, String detail) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }

        String sqlString = "update " + TABLE_NAME_COURSEPACKDESCINFO
                + " set detail='" + detail + "'"
                + " where id='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /*
     * 更新课程包描述信息表的condition字段
     * */
    public void setConditionForPackDesc(String packId, String condition) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSEPACKDESCINFO
                + " set condition='" + condition + "'"
                + " where id='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /*
     * 更新课程包描述信息表的tid字段
     * */
    public void setTidForPackDesc(String packId, String tid) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSEPACKDESCINFO
                + " set tid='" + tid + "'"
                + " where id='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /*
     * 更新课程包描述信息表的recommendId字段
     * */
    public void setRecommendIdForPackDesc(String packId, String recommendId) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSEPACKDESCINFO
                + " set recommendId='" + recommendId + "'"
                + " where id='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }

    /*
     * 更新课程包描述信息表的viewCount字段
     * */
    public void setViewCountForPackDesc(String packId, String viewCount) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "update " + TABLE_NAME_COURSEPACKDESCINFO
                + " set viewCount='" + viewCount + "'"
                + " where id='" + packId + "'";
        mDB.execSQL(sqlString);
        mDB.close();
    }


    //**************************移动课堂相关的数据库操作(CoursePackDescInfo)******************************
    //**********************************************************************************************

    //**********************************************************************************************
    //**************************移动课堂相关的数据库操作(CoursePackType)**********************************

    public synchronized void insertCoursePackType(List<CoursePackType> courses) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            if (courses != null && courses.size() != 0) {
                String sqlString = "insert into " + TABLE_NAME_COURSEPACKTYPE + " (" + ID + ","
                        + DESC + "," +  CONDITION + "," + NAME + ","
                        + TYPE + "," + DESTINATION + ") values(?,?,?,?,?,?)";

                for (int i = 0; i < courses.size(); i++) {
                    CoursePackType course = courses.get(i);
                    Object[] objects = new Object[]{course.id, course.desc, course.condition,
                            course.name, course.type, course.destination};
                    mDB.execSQL(sqlString, objects);

                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            mDB.close();
        }

    }

    /**
     * 删除  数据库里面的课程包分类数据
     */

    public synchronized boolean deleteCoursePackTypeData() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        String sqlString = "delete from CoursePackType";
        try {
            mDB.execSQL(sqlString);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            mDB.close();
        }

    }

    /**
     * 查找所有的移动课堂的课程包的分类信息
     *
     * @return
     */
    public synchronized ArrayList<CoursePackType> findCoursePackTypeDataByAll() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<CoursePackType> courses = new ArrayList<CoursePackType>();

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
//					"select *" + " from " + TABLE_NAME_COURSEPACKTYPE + " ORDER BY " + OWNERID + " asc"+","+ ID +" desc"
                    "select *" + " from " + TABLE_NAME_COURSEPACKTYPE
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                CoursePackType course = new CoursePackType();
                course.id = cursor.getInt(0);
                course.desc = cursor.getString(1);
                course.condition = cursor.getString(2);
                course.name = cursor.getString(3);
                course.type = cursor.getInt(4);
                course.destination = cursor.getString(5);
                courses.add(course);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("CoursePackTypeSize:", courses.size() + " ");
        return courses;
    }

    //**************************移动课堂相关的数据库操作(CoursePackType)**********************************
    //**********************************************************************************************


    //**********************************************************************************************
    //**************************移动课堂相关的数据库操作(TeacherInfo)*************************************

    public synchronized void insertTeachers(TeacherInfo teacherInfo) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            if (teacherInfo != null) {
                String sqlString = "insert into " + TABLE_NAME_TEACHERINFO + " (" + TID + ","
                        + TNAME + "," + TDES + "," + TIMG + ") values(?,?,?,?)";

                Object[] objects = new Object[]{teacherInfo.tid, teacherInfo.tname,
                        teacherInfo.tdes, teacherInfo.timg};
                mDB.execSQL(sqlString, objects);

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            mDB.close();
        }
    }


    /**
     * 删除  数据库里面的课程描述
     */

    public synchronized boolean deleteTeachersData() {
        String sqlString = "delete from TeacherInfo";
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString);

            mDB.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 查找所有的教师信息
     *
     * @return
     */
    public synchronized ArrayList<TeacherInfo> findTeacherDataByAll() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<TeacherInfo> teachers = new ArrayList<TeacherInfo>();

        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_TEACHERINFO
                    , new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                TeacherInfo teacher = new TeacherInfo();
                teacher.tid = cursor.getInt(0);
                teacher.tname = cursor.getString(1);
                teacher.timg = cursor.getString(2);
                teacher.tdes = cursor.getString(3);
                teachers.add(teacher);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.e("TeacherInfoSize:", teachers.size() + " ");
        return teachers;
    }


    /**
     * 查找特定的教师的信息
     *
     * @return
     */
    public synchronized TeacherInfo findTeacherDataByOwnerId(String teacherId) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        Cursor cursor = null;
        TeacherInfo teacher = new TeacherInfo();
        try {
            cursor = mDB.rawQuery(
                    "select *" + " from " + TABLE_NAME_TEACHERINFO + " where " + TID + "=" + teacherId
                    , new String[]{});

            if (cursor.moveToFirst()) {
                teacher.tid = cursor.getInt(0);
                teacher.tname = cursor.getString(1);
                teacher.timg = cursor.getString(2);
                teacher.tdes = cursor.getString(3);
            }
            cursor.close();
        } catch (Exception e) {
        } finally {
            mDB.close();
            if (cursor != null) {
                cursor.close();
            }
        }
        return teacher;
    }


    //**************************移动课堂相关的数据库操作(TeacherInfo)**********************************
    //**********************************************************************************************


    /**
     * @return 功能：得到answera4中的年份
     */
    public ArrayList<String> getYears() {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<String> yearList = new ArrayList<String>();
        Cursor cur = null;
        String sql = "select distinct " + FIELD_TESTTIME + " from "
                + TABLE_ANSWERA4 + " order by " + FIELD_TESTTIME + " desc";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    if (cur.getString(0).length() == 6) {
                        yearList.add(cur.getString(0).substring(0, 4) + "年"
                                + cur.getString(0).substring(4) + "月");
                    } else if (cur.getString(0).length() == 7) {
                        yearList.add(cur.getString(0).substring(0, 4) + "年"
                                + cur.getString(0).substring(4, 6) + "月" + "第"
                                + cur.getString(0).substring(6) + "套");
                    }
                    cur.moveToNext();
                }
            }

            cur.close();
            mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
        }

        return yearList;
    }

    public ArrayList<String> getTestYears() {

        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<String> yearList = new ArrayList<String>();
        Cursor cur = null;
        String sql = "select distinct " + FIELD_TESTTIME + " from "
                + TABLE_ANSWERA4 + " order by " + FIELD_TESTTIME + " desc";
        try {
            cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                for (int i = 0, count = cur.getCount(); i < count; i++) {
                    yearList.add(cur.getString(0));
                    cur.moveToNext();
                }
            }
            cur.close();
            mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
        }
        return yearList;
    }

    public ArrayList<String> getNewTypeYear() {
        {
            if (!mDB.isOpen()) {
                mDB = openDatabase();
            }
            ArrayList<String> yearList = new ArrayList<String>();
            Cursor cur = null;
            String sql = "select distinct " + FIELD_TESTTIME + " from "
                    + NEWTYPE_TABLE_ANSWERA4 + " order by " + FIELD_TESTTIME + " desc";

            try {
                cur = mDB.rawQuery(sql, null);// rawQuery专门执行select语句
                if (cur.getCount() > 0) {
                    cur.moveToFirst();
                    for (int i = 0, count = cur.getCount(); i < count; i++) {
                        yearList.add(cur.getString(0));
                        cur.moveToNext();
                    }
                }
                cur.close();
                mDB.close();
            } catch (Exception e) {
                // e.printStackTrace();
            } finally {
                if (cur != null) {
                    cur.close();
                }
                if (mDB != null)
                    mDB.close();
            }
            return yearList;
        }
    }

    /**
     * @return 功能：从指定表中查询某年份的所有试题结果，并得到是否加入收藏 : Section A ,B
     */
    public ArrayList<AnswerAB> getSectionAByYear(String year, String tablename) {
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        ArrayList<AnswerAB> answerABs = new ArrayList<AnswerAB>();
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
            mDB.close();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                mDB.close();
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
        ArrayList<Explain> explains = new ArrayList<>();

        Cursor cur = null;

        // String sql =
        // "select *  from "+tablename+" where TestTime = "+year+" order by " +
        // FIELD_NUMBER + " asc";
        String sql = "select *  from " + tablename + " where TestTime = "
                + year + " and TestType = " + testtype + " order by "
                + FIELD_NUMBER + " asc";

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
            mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                mDB.close();
            }
        }

        return explains;

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
            mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                mDB.close();
            }
        }
        return s;
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
        Log.d("测试", "getTextsByYear: 查询数据库语句1" + sql);
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
            mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
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
        Log.d("测试", "getTextsByYear: 查询数据库语句2" + sql);
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
            mDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
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
            mDB.close();

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null) {
                mDB.close();
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
            mDB.close();
        } catch (Exception e) {
            // e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
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
            mDB.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cur != null) {
                cur.close();
            }
            if (mDB != null)
                mDB.close();
        }
        return subTextc;
    }

    /**
     * 包装的辅助执行数据库操作
     */
    public boolean exeSql(String sqlString) {
        boolean flag;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString);
            mDB.close();
            flag = true;
        } catch (Exception e) {
            flag = false;
            // TODO: handle exception
        } finally {
            if (mDB.isOpen()) {
                mDB.close();
            }
        }
        return flag;
    }

    public boolean exeSql(String sqlString, Object[] objects) {
        boolean flag;
        if (!mDB.isOpen()) {
            mDB = openDatabase();
        }
        try {
            mDB.execSQL(sqlString, objects);
            mDB.close();
            flag = true;
        } catch (Exception e) {
            flag = false;
            // TODO: handle exception
        } finally {
            if (mDB.isOpen()) {
                mDB.close();
            }
        }
        return flag;
    }
}

