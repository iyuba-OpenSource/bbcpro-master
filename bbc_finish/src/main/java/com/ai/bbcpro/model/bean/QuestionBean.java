package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 问题的实体类
 */
public class QuestionBean {

    @SerializedName("IndexId")
    private int indexId;
    @SerializedName("Answer")
    private String answer;
    @SerializedName("Answer2")
    private String answer2;
    @SerializedName("Answer3")
    private String answer3;
    @SerializedName("Answer1")
    private String answer1;
    @SerializedName("Question")
    private String question;
    @SerializedName("BbcId")
    private int bbcId;

    private int upload = 0;

    private String Answer4;

    /**
     * 选择的选项
     */
    private String checkAnswer;

    /**
     * 选择的位置
     */
    private int checkPosition = -1;

    /**
     * 开始时间
     */
    private String BeginTime;

    /**
     * 做题时间
     */
    private String TestTime;

    public QuestionBean(int indexId, String answer, String answer2, String answer3, String answer1, String question, int bbcId, int upload) {
        this.indexId = indexId;
        this.answer = answer;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer1 = answer1;
        this.question = question;
        this.bbcId = bbcId;
        this.upload = upload;
    }

    public QuestionBean(int indexId, String answer, String answer2, String answer3, String answer1, String question, int bbcId, int upload, String answer4) {
        this.indexId = indexId;
        this.answer = answer;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer1 = answer1;
        this.question = question;
        this.bbcId = bbcId;
        this.upload = upload;
        Answer4 = answer4;
    }


    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getTestTime() {
        return TestTime;
    }

    public void setTestTime(String testTime) {
        TestTime = testTime;
    }

    public int getCheckPosition() {
        return checkPosition;
    }

    public void setCheckPosition(int checkPosition) {
        this.checkPosition = checkPosition;
    }

    public String getAnswer4() {
        return Answer4;
    }

    public void setAnswer4(String answer4) {
        Answer4 = answer4;
    }

    public String getCheckAnswer() {
        return checkAnswer;
    }

    public void setCheckAnswer(String checkAnswer) {
        this.checkAnswer = checkAnswer;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public int getIndexId() {
        return indexId;
    }

    public void setIndexId(int indexId) {
        this.indexId = indexId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getBbcId() {
        return bbcId;
    }

    public void setBbcId(int bbcId) {
        this.bbcId = bbcId;
    }
}
