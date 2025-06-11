package com.ai.bbcpro.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.ui.bean.CollectionBean;
import com.ai.bbcpro.ui.bean.CorrectPronBean;
import com.ai.bbcpro.ui.bean.CorrectTotalBean;
import com.ai.bbcpro.ui.bean.MixRecBean;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.eval.EvalWord;
import com.ai.bbcpro.ui.player.eval.RecycleViewData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeadlinesDataManager {
    private static HeadlinesDataManager instance;
    private final SQLiteDatabase db;
    private static Context mContext;

    public static synchronized HeadlinesDataManager getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new HeadlinesDataManager(context);
        }
        return instance;
    }

    private HeadlinesDataManager(Context context) {
        HeadlineDBHelper dbHelper = new HeadlineDBHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void saveFreeTimes(String bbcId, int times) {
        db.beginTransaction();
        String sql = "UPDATE HeadlinesList SET FreeTimes = " + times + " WHERE BbcId =" + bbcId;
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();

    }


    public void saveEvalSentence(RecycleViewData data) {
        db.beginTransaction();
        db.replace("EvalSentenceCache", null, toCVEvalSentence(data));
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @SuppressLint("Range")
    public RecycleViewData loadEvalSentence(int bbcId, String para_id, String id_index) {

        RecycleViewData data = null;
        String sql = "select * from EvalSentenceCache where bbc_id=" + bbcId + " and para_id=" + para_id + " and id_index=" + id_index;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            data = new RecycleViewData();
            data.setWords(loadEvalWords(bbcId, para_id, id_index));
            data.setBbcId(bbcId);
            data.setSentence(cursor.getString(3));
            data.setSentenceCn(cursor.getString(4));
            data.setTiming(cursor.getString(5));
            data.setEndTiming(cursor.getString(6));
            data.setParaId(cursor.getString(7));
            data.setIdIndex(cursor.getString(8));
            data.setScore((int) cursor.getFloat(9));
            data.setAudioUrl(cursor.getString(10));
            data.setEval(true);
            data.setHasLowScoreWord(cursor.getInt(11) > 0);
            return data;
        }
        return data;
    }

    /**
     * 获取评测句子的列表
     *
     * @param bbcId
     * @return
     */
    @SuppressLint("Range")
    public List<RecycleViewData> getEvalSentenceList(int bbcId) {

        String sql = "select * from HeadlinesDetail where bbcid = ? ORDER by ABS(EndTiming) ";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcId + ""});
        List<RecycleViewData> viewDataList = new ArrayList<>();
        while (cursor.moveToNext()) {

            RecycleViewData data = new RecycleViewData();
            data.setBbcId(cursor.getInt(cursor.getColumnIndex("BbcId")));
            data.setSentence(cursor.getString(cursor.getColumnIndex("Sentence")));
            data.setSentenceCn(cursor.getString(cursor.getColumnIndex("sentence_cn")));
            data.setTiming(cursor.getString(cursor.getColumnIndex("Timing")));
            data.setEndTiming(cursor.getString(cursor.getColumnIndex("EndTiming")));
            data.setParaId(cursor.getString(cursor.getColumnIndex("ParaId")));
            data.setIdIndex(cursor.getString(cursor.getColumnIndex("IdIndex")));
            viewDataList.add(data);
        }
        cursor.close();
        return viewDataList;
    }


    /**
     * 获取单条句子
     *
     * @param bbcId
     * @return
     */
    @SuppressLint("Range")
    public RecycleViewData getSingleEvalSentence(int bbcId, String timing) {

        String sql = "select * from HeadlinesDetail where bbcid = ? and Timing = ? ORDER by ABS(EndTiming) ";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcId + "", timing});
        RecycleViewData data = null;
        while (cursor.moveToNext()) {

            data = new RecycleViewData();
            data.setBbcId(cursor.getInt(cursor.getColumnIndex("BbcId")));
            data.setSentence(cursor.getString(cursor.getColumnIndex("Sentence")));
            data.setSentenceCn(cursor.getString(cursor.getColumnIndex("sentence_cn")));
            data.setTiming(cursor.getString(cursor.getColumnIndex("Timing")));
            data.setEndTiming(cursor.getString(cursor.getColumnIndex("EndTiming")));
            data.setParaId(cursor.getString(cursor.getColumnIndex("ParaId")));
            data.setIdIndex(cursor.getString(cursor.getColumnIndex("IdIndex")));
            data.setCollect(cursor.getInt(cursor.getColumnIndex("collect")));
            data.setEval(cursor.getInt(cursor.getColumnIndex("is_eval")) > 0);
        }
        cursor.close();
        return data;
    }


    private ContentValues toCVEvalSentence(RecycleViewData data) {
        ContentValues values = new ContentValues();
        values.put("sen_key", new StringBuilder().append(String.valueOf(data.getBbcId())).
                append(data.getParaId()).append(data.getIdIndex()).toString());
        values.put("bbc_id", data.getBbcId());
        values.put("position", data.getPosition());
        values.put("sentence", data.getSentence());
        values.put("sentence_cn", data.getSentenceCn());
        values.put("timing", data.getTiming());
        values.put("endtiming", data.getEndTiming());
        values.put("para_id", data.getParaId());
        values.put("id_index", data.getIdIndex());
        values.put("score", data.getScore());
        values.put("audio_url", data.getAudioUrl());
        values.put("is_correct", data.isHasLowScoreWord());
        return values;
    }

    public void saveEvalWords(EvalWord data) {
        db.beginTransaction();
        db.replace("EvalWordCache", null, toCVEvalWords(data));
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public List<EvalWord> loadEvalWords(int bbcId, String para_id, String id_index) {
        List<EvalWord> list = new ArrayList<>();
        String sql = "select * from EvalWordCache where bbc_id=" + bbcId + " and para_id=" + para_id + " and id_index=" + id_index + " order by word_index asc";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            EvalWord evalWord = new EvalWord();
            evalWord.setContent(cursor.getString(3));
            evalWord.setScore(cursor.getFloat(4));
            evalWord.setIndex(cursor.getInt(5));
            evalWord.setPron(cursor.getString(6));
            evalWord.setUserPron(cursor.getString(7));
            evalWord.setPron2(cursor.getString(8));
            evalWord.setUserPron2(cursor.getString(9));
            list.add(evalWord);
        }
        cursor.close();
        return list;
    }

    private ContentValues toCVEvalWords(EvalWord data) {
        ContentValues values = new ContentValues();
        values.put("word_key", new StringBuilder().append(data.getBbcId())
                .append(data.getPara_id()).append(data.getId_index())
                .append("_").append(String.valueOf(data.getIndex())).toString());
        values.put("bbc_id", data.getBbcId());
        values.put("position", data.getPosition());
        values.put("content", data.getContent());
        values.put("score", data.getScore());
        values.put("word_index", data.getIndex());
        values.put("pron", data.getPron());
        values.put("user_pron", data.getUserPron());
        values.put("pron2", data.getPron2());
        values.put("user_pron2", data.getUserPron2());
        values.put("user_delete", data.getDelete());
        values.put("user_insert", data.getInsert());
        values.put("para_id", data.getPara_id());
        values.put("id_index", data.getId_index());
        values.put("sen_key", new StringBuilder().append(String.valueOf(data.getBbcId()))
                .append(data.getPara_id()).append(data.getId_index()).toString());
        return values;
    }


    public List<CorrectTotalBean> loadPronData(String bbcId, String position) {
        List<CorrectTotalBean> list = new ArrayList<>();
        String sql = "SELECT * FROM CorrectPronData WHERE BBCID=" + bbcId + " AND POSITION=" + position + " ORDER BY SEQUENCE ASC";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            CorrectTotalBean pronBean = new CorrectTotalBean();
            pronBean.setWordContent(cursor.getString(2));
            pronBean.setCorrectPronBean(new CorrectPronBean(cursor.getString(4), cursor.getString(3), cursor.getString(6), cursor.getString(5)));
            list.add(pronBean);
        }
        return list;
    }


    private ContentValues toCVPron(CorrectTotalBean bean) {
        ContentValues values = new ContentValues();
        values.put("BBCID", bean.getBbcId());
        values.put("POSITION", String.valueOf(bean.getPosition()));
        values.put("CONTENT", bean.getWordContent());
        values.put("PRON", bean.getCorrectPronBean().getOriPron());
        values.put("USERPORN", bean.getCorrectPronBean().getUserPron());
        values.put("DEF", bean.getCorrectPronBean().getDef());
        values.put("AUDIO", bean.getCorrectPronBean().getAudio());
        values.put("SEQUENCE", bean.getSequence());
        return values;
    }

    public int loadFreeTimes(String bbcId) {
        int times = 0;
        String sql = "SELECT FreeTimes FROM HeadlinesList WHERE BbcId=" + bbcId;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            times = cursor.getInt(0);
        }
        cursor.close();
        return times;
    }

    public void saveHeadlines(List<SumBean.DataDTO> headlines) {
        db.beginTransaction();
        Iterator<SumBean.DataDTO> iterator = headlines.iterator();
        while (iterator.hasNext()) {

            SumBean.DataDTO next = iterator.next();
            String hasSql = "select count(*) from  HeadlinesList where BbcId = ?";
            Cursor cursor = db.rawQuery(hasSql, new String[]{next.getBbcId()});
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            if (count > 0) {

                String updateSql = "update HeadlinesList set Category = ?,CreateTime=?,DescCn=?," +
                        "Pic=?,ReadCount=?,Title=?,Sound=?,Texts =?,Title_cn=? where BbcId = ?";
                db.execSQL(updateSql, new Object[]{next.getCategory(), next.getCreatTime(), next.getDescCn(),
                        next.getPic(), next.getReadCount(), next.getTitle(), next.getSound(), next.getTexts(), next.getTitle_cn(),
                        next.getBbcId()});

            } else {

                String addSql = "insert into HeadlinesList(BbcId,Category,CreateTime,DescCn,HotFlg,Pic,ReadCount,Title,Sound," +
                        "Texts,Title_cn) values(?,?,?,?,?,?,?,?,?,?,?)";
                db.execSQL(addSql, new Object[]{next.getBbcId(), next.getCategory(), next.getCreatTime(),
                        next.getDescCn(), next.getHotFlg(), next.getPic(), next.getReadCount(), next.getTitle(),
                        next.getSound(), next.getTexts(), next.getTitle_cn()});
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void changeDownloadStatus(String bbcId, int status) {
        db.beginTransaction();
        String sql = "update HeadlinesList set is_download=" + status + " where BbcId=" + bbcId;
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    public void saveDetail(List<BBCContentBean.DataBean> headlines) {
        db.beginTransaction();
        Iterator<BBCContentBean.DataBean> iterator = headlines.iterator();
        while (iterator.hasNext()) {
            BBCContentBean.DataBean next = iterator.next();
            db.replace("HeadlinesDetail", (String) null, this.toCVDetail(next));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 添加或者更新句子
     *
     * @param headlines
     */
    public void saveAndUpdateDetail(List<BBCContentBean.DataBean> headlines) {

        db.beginTransaction();
        Iterator<BBCContentBean.DataBean> iterator = headlines.iterator();
        while (iterator.hasNext()) {

            BBCContentBean.DataBean next = iterator.next();
            if (isExist(next.getBbcId() + "", next.getTiming()) == 1) {

                String updateSql = "update HeadlinesDetail set ImgPath = ? ,EndTiming = ?, ParaId=?" +
                        ",IdIndex = ?,Announcer=?,sentence_cn=?,ImgWords=?,Sentence=? where BbcId=? and Timing = ?";
                db.execSQL(updateSql, new Object[]{
                        next.getImgPath(),
                        next.getEndTiming(),
                        next.getParaId(),
                        next.getIdIndex(),
                        next.getAnnouncer(),
                        next.getSentenceCn(),
                        next.getImgWords(),
                        next.getSentence(),
                        next.getBbcId(),
                        next.getTiming()
                });
            } else {

                db.replace("HeadlinesDetail", (String) null, this.toCVDetail(next));
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    public long isExist(String bbcid, String timing) {

        String sql = "select count(*) from HeadlinesDetail where BbcId =? and Timing = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcid, timing});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    @SuppressLint("Range")
    public List<RecycleViewData> loadDetail(int bbcID) {
        List<RecycleViewData> dataList = new ArrayList<>();
        String sql = "select * from HeadlinesDetail where BbcId=" + bbcID;

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {

            RecycleViewData data = new RecycleViewData(bbcID,
                    cursor.getString(cursor.getColumnIndex("Sentence")),
                    cursor.getString(cursor.getColumnIndex("sentence_cn")),
                    cursor.getString(cursor.getColumnIndex("Timing")),
                    cursor.getString(cursor.getColumnIndex("EndTiming")),
                    cursor.getString(cursor.getColumnIndex("ParaId")),
                    cursor.getString(cursor.getColumnIndex("IdIndex")),
                    cursor.getInt(cursor.getColumnIndex("is_eval")) > 0);
            data.setCollect(cursor.getInt(cursor.getColumnIndex("collect")));
            dataList.add(data);
        }

        cursor.close();
        return dataList;
    }

    public void changeState(String sentenceKey) {
        db.beginTransaction();
        String sql = "update HeadlinesDetail set is_eval=1 where sen_key=" + sentenceKey;
        db.execSQL(sql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 更具id获取句子列表
     *
     * @param bbcId
     * @return
     */
    @SuppressLint("Range")
    public List<BBCContentBean.DataBean> loadSentence(String bbcId) {

        List<BBCContentBean.DataBean> dataBeanList = new ArrayList<>();
        String sql = "select * from HeadlinesDetail where BbcId=" + bbcId;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {

            BBCContentBean.DataBean dataBean = new BBCContentBean.DataBean();
            dataBean.setSentence(cursor.getString(cursor.getColumnIndex("Sentence")));
            dataBean.setSentenceCn(cursor.getString(cursor.getColumnIndex("sentence_cn")));
            dataBean.setTiming(cursor.getString(cursor.getColumnIndex("Timing")));
            dataBean.setEndTiming(cursor.getString(cursor.getColumnIndex("EndTiming")));
            dataBean.setParaId(cursor.getString(cursor.getColumnIndex("ParaId")));
            dataBean.setIdIndex(cursor.getString(cursor.getColumnIndex("IdIndex")));
            dataBeanList.add(dataBean);
        }
        cursor.close();
        return dataBeanList;
    }


    public List<SumBean.DataDTO> loadHeadlines(int page, int count) {
        List<SumBean.DataDTO> headlines = new ArrayList();
        int realPage = (page - 1) * count;
        String sql = "select * from HeadlinesList  order by CreateTime desc limit (" + count + ")  offset (" + realPage + ")";
        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    headlines.add(this.parseCursor(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return headlines;
    }

    public List<SumBean.DataDTO> loadDownloadNews(List<String> list) {

        List<SumBean.DataDTO> headlines = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            String sql = "select * from HeadlinesList where BbcId=" + list.get(i) + " order by CreateTime desc";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        headlines.add(this.parseCursor(cursor));
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        return headlines;
    }


    /**
     * 通过bbcid获取单条数据
     *
     * @param bbcid
     * @return
     */
    @SuppressLint("Range")
    public SumBean.DataDTO getHeadlineForBbcid(String bbcid) {

        String sql = "select * from HeadlinesList where BbcId = ? ";
        Cursor cursor = this.db.rawQuery(sql, new String[]{bbcid});
        SumBean.DataDTO dataDTO = null;
        if (cursor.moveToNext()) {

            dataDTO = new SumBean.DataDTO();
            dataDTO.bbcId = cursor.getString(cursor.getColumnIndex("BbcId"));
            dataDTO.category = cursor.getString(cursor.getColumnIndex("Category"));
            dataDTO.creatTime = cursor.getString(cursor.getColumnIndex("CreateTime"));
            dataDTO.descCn = cursor.getString(cursor.getColumnIndex("DescCn"));
            dataDTO.hotFlg = cursor.getString(cursor.getColumnIndex("HotFlg"));
            dataDTO.pic = cursor.getString(cursor.getColumnIndex("Pic"));
            dataDTO.readCount = cursor.getString(cursor.getColumnIndex("ReadCount"));
            dataDTO.sound = cursor.getString(cursor.getColumnIndex("Sound"));
            dataDTO.title = cursor.getString(cursor.getColumnIndex("Title"));
            dataDTO.title_cn = cursor.getString(cursor.getColumnIndex("Title_cn"));
            dataDTO.isDownload = cursor.getInt(cursor.getColumnIndex("is_download"));
            dataDTO.setEndFlag(cursor.getInt(cursor.getColumnIndex("endFlag")));
            dataDTO.setTestNumber(cursor.getInt(cursor.getColumnIndex("TestNumber")));
            dataDTO.setTexts(cursor.getInt(cursor.getColumnIndex("Texts")));
        }
        cursor.close();
        return dataDTO;
    }

    public List<SumBean.DataDTO> loadHeadlinesType(int category, int page, int count) {
        List<SumBean.DataDTO> headlines = new ArrayList();
        int realPage = (page - 1) * count;
        String sql = "select * from HeadlinesList where Category = (" + category + ") order by CreateTime desc limit (" + count + ")  offset (" + realPage + ")";

        Cursor cursor = this.db.rawQuery(sql, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                do {
                    headlines.add(this.parseCursor(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        return headlines;
    }

    public String loadSentenceByIdParaIndex(int bbcId, int paraId, int idIndex) {
        String[] args = {String.valueOf(bbcId), String.valueOf(paraId), String.valueOf(idIndex)};
        String sql = "select Sentence from HeadlinesDetail where BbcId=? and ParaId=? and IdIndex=?";
        Cursor cursor = db.rawQuery(sql, args);
        while (cursor.moveToNext()) {
            return cursor.getString(0);
        }
        return "";
    }

    @SuppressLint("Range")
    private SumBean.DataDTO parseCursor(Cursor cursor) {
        SumBean.DataDTO headlines = new SumBean.DataDTO();
        headlines.bbcId = cursor.getString(cursor.getColumnIndex("BbcId"));
        headlines.category = cursor.getString(cursor.getColumnIndex("Category"));
        headlines.creatTime = cursor.getString(cursor.getColumnIndex("CreateTime"));
        headlines.descCn = cursor.getString(cursor.getColumnIndex("DescCn"));
        headlines.hotFlg = cursor.getString(cursor.getColumnIndex("HotFlg"));
        headlines.pic = cursor.getString(cursor.getColumnIndex("Pic"));
        headlines.readCount = cursor.getString(cursor.getColumnIndex("ReadCount"));
        headlines.sound = cursor.getString(cursor.getColumnIndex("Sound"));
        headlines.title = cursor.getString(cursor.getColumnIndex("Title"));
        headlines.title_cn = cursor.getString(cursor.getColumnIndex("Title_cn"));
        headlines.isDownload = cursor.getInt(cursor.getColumnIndex("is_download"));
        headlines.setEndFlag(cursor.getInt(cursor.getColumnIndex("endFlag")));
        headlines.setTestNumber(cursor.getInt(cursor.getColumnIndex("TestNumber")));
        headlines.setTexts(cursor.getInt(cursor.getColumnIndex("Texts")));
        return headlines;
    }

    private CollectionBean.DataBean parseCursorCollection(Cursor cursor) {
        CollectionBean.DataBean headlines = new CollectionBean.DataBean();
        headlines.setVoaid(cursor.getString(cursor.getColumnIndexOrThrow("BbcId")));
        headlines.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("Category")));
        headlines.setCreateTime(cursor.getString(cursor.getColumnIndexOrThrow("CreateTime")));
        headlines.setDescCn(cursor.getString(cursor.getColumnIndexOrThrow("DescCn")));
        headlines.setHotflg(cursor.getString(cursor.getColumnIndexOrThrow("HotFlg")));
        headlines.setPic(cursor.getString(cursor.getColumnIndexOrThrow("Pic")));
        headlines.setReadCount(cursor.getString(cursor.getColumnIndexOrThrow("ReadCount")));
        headlines.setSound(cursor.getString(cursor.getColumnIndexOrThrow("Sound")));
        headlines.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("Title")));
        headlines.setTitleCn(cursor.getString(cursor.getColumnIndexOrThrow("Title_cn")));
        return headlines;
    }

    private ContentValues toCV(SumBean.DataDTO headline) {
        ContentValues values = new ContentValues();
        values.put("BbcId", headline.bbcId);
        values.put("Category", headline.category);
        values.put("CreateTime", headline.creatTime);
        values.put("DescCn", headline.descCn);
        values.put("HotFlg", headline.hotFlg);
        values.put("Pic", headline.pic);
        values.put("ReadCount", headline.readCount);
        values.put("Sound", headline.sound);
        values.put("Title", headline.title);
        values.put("Title_cn", headline.title_cn);
        values.put("is_download", 0);
        values.put("Texts", headline.texts);
        return values;
    }

    private ContentValues toCVCollection(CollectionBean.DataBean headline) {
        ContentValues values = new ContentValues();
        values.put("BbcId", headline.getVoaid());
        values.put("Category", headline.getCategory());
        values.put("CreateTime", headline.getCreateTime());
        values.put("DescCn", headline.getDescCn());
        values.put("HotFlg", headline.getHotflg());
        values.put("Pic", headline.getPic());
        values.put("ReadCount", headline.getReadCount());
        values.put("Sound", headline.getSound());
        values.put("Title", headline.getTitle());
        values.put("Title_cn", headline.getTitleCn());
        return values;
    }

    private ContentValues toCVDetail(BBCContentBean.DataBean headline) {
        ContentValues values = new ContentValues();
        values.put("sen_key", new StringBuilder().append(String.valueOf(headline.getBbcId())).
                append(String.valueOf(headline.getParaId())).append(String.valueOf(headline.getIdIndex())).toString());
        values.put("BbcId", headline.getBbcId());
        values.put("ImgPath", headline.getImgPath());
        values.put("EndTiming", headline.getEndTiming());
        values.put("ParaId", headline.getParaId());
        values.put("IdIndex", headline.getIdIndex());
        values.put("Announcer", headline.getAnnouncer());
        values.put("sentence_cn", headline.getSentenceCn());
        values.put("ImgWords", headline.getImgWords());
        values.put("Timing", headline.getTiming());
        values.put("Sentence", headline.getSentence());
        values.put("is_eval", 0);
        return values;
    }

    private String buildQ(int size) {
        StringBuilder sb;
        for (sb = new StringBuilder(); size > 0; --size) {
            if (sb.length() == 0) {
                sb.append("?");
            } else {
                sb.append(",").append("?");
            }
        }
        return sb.toString();
    }

    public void saveMix(MixRecBean recBean, String bbcId) {
        db.beginTransaction();
        db.replace("MixRecCache", null, toCVMix(recBean, bbcId));
        db.setTransactionSuccessful();
        db.endTransaction();

    }

    private ContentValues toCVMix(MixRecBean recBean, String bbcId) {
        ContentValues values = new ContentValues();
        values.put("bbc_id", bbcId);
        values.put("url", recBean.getUrl());
        values.put("avrScore", recBean.getAvrScore());
        return values;
    }

    public MixRecBean loadMix(String bbcId) {
        MixRecBean recBean = new MixRecBean();
        String sql = "select * from MixRecCache where bbc_id=" + bbcId;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            recBean.setUrl(cursor.getString(1));
            recBean.setAvrScore(cursor.getInt(2));
            return recBean;
        }
        return recBean;
    }

    /**
     * 问题表的查询
     */


    /**
     * 添加练习题
     *
     * @param questionBean
     */
    public void addQuestion(QuestionBean questionBean) {


        String sqlStr = "insert into question(Answer3,Answer2,Answer ,IndexId ,Answer1,Question,BbcId,upload) values( ?,?,?,?,?,?,?,?)";

        db.execSQL(sqlStr,
                new Object[]{
                        questionBean.getAnswer3(),
                        questionBean.getAnswer2(),
                        questionBean.getAnswer(),
                        questionBean.getIndexId(),
                        questionBean.getAnswer1(),
                        questionBean.getQuestion(),
                        questionBean.getBbcId(),
                        questionBean.getUpload()
                });
    }

    /**
     * 获取问题列表
     *
     * @param bbcid
     */
    public List<QuestionBean> getQuestionList(int bbcid) {

        List<QuestionBean> questionBeanList = new ArrayList<>();
        String sqlStr = "select * from  question where bbcid = ? order by indexid";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid + ""});
        while (cursor.moveToNext()) {

            String Answer3 = cursor.getString(cursor.getColumnIndexOrThrow("Answer3"));
            String Answer2 = cursor.getString(cursor.getColumnIndexOrThrow("Answer2"));
            String Answer1 = cursor.getString(cursor.getColumnIndexOrThrow("Answer1"));
            String Answer = cursor.getString(cursor.getColumnIndexOrThrow("Answer"));
            int IndexId = cursor.getInt(cursor.getColumnIndexOrThrow("IndexId"));
            String question = cursor.getString(cursor.getColumnIndexOrThrow("Question"));
            int BbcId = cursor.getInt(cursor.getColumnIndexOrThrow("BbcId"));
            int upload = cursor.getInt(cursor.getColumnIndexOrThrow("upload"));
            int userAnswerPosition = cursor.getInt(cursor.getColumnIndexOrThrow("userAnswerPosition"));

            QuestionBean questionBean = new QuestionBean(IndexId, Answer, Answer2, Answer3, Answer1, question, BbcId, upload);
            questionBean.setCheckPosition(userAnswerPosition);
            questionBeanList.add(questionBean);
        }
        cursor.close();

        return questionBeanList;
    }

    /**
     * 是否存在此数据
     *
     * @param bbcid
     * @return
     */
    public long hasQuestionBean(int bbcid) {

        String sqlStr = "select count(*) from  question where bbcid = ?";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid + ""});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 是否存在此数据
     *
     * @param bbcid
     * @param indexid
     * @return
     */
    public long hasQuestionBean(int bbcid, int indexid) {

        String sqlStr = "select count(*) from  question where bbcid = ? and IndexId = ?";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid + "", indexid + ""});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 更新问题数据
     *
     * @param questionBean
     */
    public void updateQuestion(QuestionBean questionBean) {

        String sqlStr = " update question  set Answer3 = ?,Answer2=?,Answer =?,IndexId =?,Answer1=?,Question=? where bbcid= ?";

        db.execSQL(sqlStr, new Object[]{

                questionBean.getAnswer3(),
                questionBean.getAnswer2(),
                questionBean.getAnswer(),
                questionBean.getIndexId(),
                questionBean.getAnswer1(),
                questionBean.getQuestion(),
                questionBean.getBbcId(),
        });
    }

    /**
     * 更新上传标志位
     * IndexId 题的下标
     *
     * @param upload checkPosition  用户选择的位置
     */
    public void updateUpload(int upload, int bbcid, int IndexId, int checkPosition) {

        String sqlStr = "update question set upload = ?,userAnswerPosition = ?  where bbcid = ? and IndexId = ?";
        db.execSQL(sqlStr, new Object[]{
                upload, checkPosition, bbcid, IndexId
        });
    }

    /**
     * 获取bbcid的、做过的练习提的数量
     */
    public long getDoneQuestion(int bbcid) {

        String sqlStr = "select count(*) from question where   bbcid =? and upload = 1";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid + ""});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 查看是否有此id的数据
     *
     * @param bbcid
     */
    public long getQuestionCountById(int bbcid) {

        String sqlStr = "select count(*) from question where  bbcid = ?";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid + ""});
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }


    /**
     * 更新听力进度
     *
     * @param bbcid
     * @param EndFlg
     * @param TestNumber
     */
    public void updateListenProgress(int bbcid, int EndFlg, int TestNumber) {

        String sql = "update HeadlinesList set endFlag = ?,TestNumber = ? where BbcId = ?";
        db.execSQL(sql, new Object[]{EndFlg, TestNumber, bbcid});
    }

    /**
     * 获取TestNumber
     *
     * @param bbcid
     * @return
     */
    @SuppressLint("Range")
    public int getTestNumber(int bbcid) {

        int TestNumber = 0;
        String sql = "select TestNumber  from HeadlinesList where BbcId = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcid + ""});
        if (cursor.moveToNext()) {

            TestNumber = cursor.getInt(cursor.getColumnIndexOrThrow("TestNumber"));
        }
        return TestNumber;
    }


    /**
     * 获取已做练习题的数量
     */
    public long getExercises(int bbcid) {

        long count = 0;
        String sql = "select count(*) from question where  userAnswerPosition != -1 and bbcid = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcid + ""});
        cursor.moveToFirst();
        count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 获取评测的个数
     *
     * @param bbcid
     * @return
     */
    public long getEvalCount(int bbcid) {

        long count = 0;
        String sql = "select count(*) from  EvalSentenceCache where bbc_id = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{bbcid + ""});
        cursor.moveToFirst();
        count = cursor.getLong(0);
        cursor.close();
        return count;
    }

    /**
     * 重置听力进度、练习题、评测
     * 重置评测标志位
     */
    public void resetProgress() {

        String lSql = "update HeadlinesList  set endFlag =0,TestNumber=0  ";
        String eSql = "update question  set upload = 0,userAnswerPosition = -1";
        String t2Sql = "delete FROM EvalWordCache";
        String tSql = "delete FROM  EvalSentenceCache";
        String restEvalSql = "update HeadlinesDetail set is_eval = 0";


        db.execSQL(lSql);
        db.execSQL(eSql);
        db.execSQL(t2Sql);
        db.execSQL(tSql);
        db.execSQL(restEvalSql);
    }


    /**
     * 查找bbc_id和timing 的句子是否被收藏
     *
     * @param bbc_id
     * @param timing
     * @return
     */
    @SuppressLint("Range")
    public int isCollect(String bbc_id, String timing) {

        String sqlStr = "select collect from HeadlinesDetail where BbcId = ? and Timing = ?";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbc_id, timing});

        int collect = 0;
        while (cursor.moveToNext()) {

            collect = cursor.getInt(cursor.getColumnIndex("collect"));
        }
        cursor.close();
        return collect;
    }

    /**
     * 收藏句子
     *
     * @param bbc_id
     * @param timing
     * @param collect
     */
    public void collectSentence(String bbc_id, String timing, int collect) {

        String sqlStr = "update HeadlinesDetail set collect = ? where BbcId = ? and Timing = ?";
        db.execSQL(sqlStr, new Object[]{collect, bbc_id, timing});
    }

    /**
     * 收藏和取消收藏文章
     *
     * @param bbc_id
     * @param collect
     */
    public void collectText(String bbc_id, int collect) {

        String sqlStr = "update HeadlinesList set collect = ? where BbcId = ?";
        db.execSQL(sqlStr, new Object[]{collect, bbc_id});
    }


    /**
     * 获取文章收藏的状态
     *
     * @param bbcid
     * @return
     */
    @SuppressLint("Range")
    public int getTextCollect(String bbcid) {

        String sqlStr = "select collect from HeadlinesList where BbcId = ?";
        Cursor cursor = db.rawQuery(sqlStr, new String[]{bbcid});
        int collect = 0;
        while (cursor.moveToNext()) {

            collect = cursor.getInt(cursor.getColumnIndex("collect"));
        }
        cursor.close();
        return collect;
    }


    /**
     * 处理Parent=0时的获取顺序数据
     * @param dateTime  时间   格式：2023-06-06 13:53:00.0
     * @return
     */
    public SumBean.DataDTO getNextNewsByParent0(String dateTime) {

        Cursor cursor = db.rawQuery("select * from HeadlinesList  WHERE strftime('%s',?) > strftime('%s',CreateTime) order by strftime('%s',CreateTime) desc LIMIT 1",
                new String[]{dateTime});
        SumBean.DataDTO dataDTO = null;
        while (cursor.moveToNext()) {

            dataDTO = parseCursor(cursor);
        }
        cursor.close();
        return dataDTO;
    }


    /**
     * 获取bbcid的下一条数据
     * @param bbcid
     * @param category
     * @return
     */
    public SumBean.DataDTO getNextNewsByCate(int bbcid, int category) {

        Cursor cursor = db.rawQuery("select * from HeadlinesList where \"Category\" = ? and BbcId < ? order by strftime('%s',CreateTime) desc limit 1",
                new String[]{category + "", bbcid + ""});

        SumBean.DataDTO dataDTO = null;
        while (cursor.moveToNext()) {

            dataDTO = parseCursor(cursor);
        }
        cursor.close();
        return dataDTO;
    }

    /**
     * 获取category类中最大id的HomeListItem
     * @param category
     * @return
     */
    public SumBean.DataDTO getMaxNewsByCate(int category) {

        Cursor cursor = db.rawQuery("select * from HeadlinesList where \"Category\" = ? order by strftime('%s',CreateTime) desc limit 1",
                new String[]{category + ""});

        SumBean.DataDTO dataDTO = null;
        while (cursor.moveToNext()) {

            dataDTO = parseCursor(cursor);
        }
        cursor.close();
        return dataDTO;
    }

    /**
     * 获取随机数据
     * @param bbcid
     * @return
     */
    public SumBean.DataDTO getRandomNewsByParent0(int bbcid) {

        Cursor cursor = db.rawQuery("select * from HeadlinesList where BbcId != ? order by RANDOM() limit 1",
                new String[]{bbcid + ""});

        SumBean.DataDTO dataDTO = null;
        while (cursor.moveToNext()) {

            dataDTO = parseCursor(cursor);
        }
        cursor.close();
        return dataDTO;
    }

    /**
     * 获取随机数据
     * @param category
     * @return
     */
    public SumBean.DataDTO getRandomNewsByCate(int bbcid, int category) {

        Cursor cursor = db.rawQuery("select * from HeadlinesList where \"Category\" = ? and  BbcId != ? order by RANDOM() limit 1",
                new String[]{category + "", bbcid + ""});

        SumBean.DataDTO dataDTO = null;
        while (cursor.moveToNext()) {

            dataDTO = parseCursor(cursor);
        }
        cursor.close();
        return dataDTO;
    }

}
