package com.ai.bbcpro.ui.player.eval;


import java.util.ArrayList;
import java.util.List;

public class RecycleViewData {
    private int bbcId;  // 文章id
    private String sentence;  // 句子内容
    private String sentenceCn;  // 句子中文内容
    private String timing;  // 句子开始时间
    private String endTiming;  // 句子结束时间
    private String paraId; // 段落
    private String idIndex; // 第几句
    private int position;
    private int score;  // 本句得分
    private boolean showBtn;  // 是否展示评测按钮
    private boolean isPlayOri;  // 是否正在播放原文音频
    private boolean isRecording;  // 是否正在录音
    private boolean isPlayRec;  // 是否在播放录音
    private boolean isUnderRecPlay; //是否在录音播放器运行状态下
    private boolean isEval = false;  // 是否评测完毕（展示分数）
    private List<EvalWord> words;  // 句子拆分成的单词list，包含每个单词评测得分
    private String audioUrl;  // 本句用户评测录音
    private int recordDB;
    private boolean hasLowScoreWord = false;
    private boolean isUpload;
    private String shuoshuoId;

    /**
     * 收藏状态
     */
    private int collect = 0;

    public RecycleViewData() {

    }

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(String shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    public boolean isHasLowScoreWord() {
        return hasLowScoreWord;
    }

    public void setHasLowScoreWord(boolean hasLowScoreWord) {
        this.hasLowScoreWord = hasLowScoreWord;
    }

    public boolean isUpload() {
        return isUpload;
    }

    public void setUpload(boolean upload) {
        isUpload = upload;
    }

    public RecycleViewData(int bbcId, String sentence, String sentenceCn, String timing, String endTiming, String paraId, String idIndex, boolean isEval) {
        this.bbcId = bbcId;
        this.sentence = sentence;
        this.sentenceCn = sentenceCn;
        this.timing = timing;
        this.endTiming = endTiming;
        this.paraId = paraId;
        this.idIndex = idIndex;
        this.isEval = isEval;
        score = -1;
        showBtn = false;
        isPlayOri = false;
        isRecording = false;
        isPlayRec = false;

        // 未传入words参数时（即本句没有评测记录时），初始化words
        List<EvalWord> tempList = new ArrayList<>();
        String[] wordsArr = sentence.split(" ");
        for (int i = 0; i < wordsArr.length; i++) {
            String word = wordsArr[i];
            tempList.add(new EvalWord(word, -1, i + 1));
        }
        this.words = tempList;
        this.audioUrl = "";
    }

    public void setBbcId(int bbcId) {
        this.bbcId = bbcId;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public void setSentenceCn(String sentenceCn) {
        this.sentenceCn = sentenceCn;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public void setEndTiming(String endTiming) {
        this.endTiming = endTiming;
    }

    public void setParaId(String paraId) {
        this.paraId = paraId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getBbcId() {
        return bbcId;
    }

    public String getSentence() {
        return sentence;
    }

    public String getSentenceCn() {
        return sentenceCn;
    }

    public String getTiming() {
        return timing;
    }

    public String getEndTiming() {
        return endTiming;
    }

    public String getParaId() {
        return paraId;
    }

    public void setIdIndex(String idIndex) {
        this.idIndex = idIndex;
    }

    public String getIdIndex() {
        return idIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isShowBtn() {
        return showBtn;
    }

    public void setShowBtn(boolean showBtn) {
        this.showBtn = showBtn;
    }

    public boolean isPlayOri() {
        return isPlayOri;
    }

    public void setPlayOri(boolean playOri) {
        isPlayOri = playOri;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }

    public boolean isPlayRec() {
        return isPlayRec;
    }

    public void setPlayRec(boolean playRec) {
        isPlayRec = playRec;
    }

    public boolean isEval() {
        return isEval;
    }

    public void setEval(boolean eval) {
        isEval = eval;
    }

    public List<EvalWord> getWords() {
        return words;
    }

    public void setWords(List<EvalWord> words) {
        this.words = words;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public boolean isUnderRecPlay() {
        return isUnderRecPlay;
    }

    public void setUnderRecPlay(boolean underRecPlay) {
        isUnderRecPlay = underRecPlay;
    }

    public int getRecordDB() {
        return recordDB;
    }

    public void setRecordDB(int recordDB) {
        this.recordDB = recordDB;
    }

    @Override
    public String toString() {
        return "RecycleViewDate{" +
                "sentence='" + sentence + '\'' +
                ", sentenceCn='" + sentenceCn + '\'' +
                ", timing='" + timing + '\'' +
                ", endTiming='" + endTiming + '\'' +
                ", paraId='" + paraId + '\'' +
                ", idIndex='" + idIndex + '\'' +
                ", score=" + score +
                ", showBtn=" + showBtn +
                ", isPlayOri=" + isPlayOri +
                ", isRecording=" + isRecording +
                ", isPlayRec=" + isPlayRec +
                ", isEval=" + isEval +
                '}';
    }
}