package com.ai.bbcpro.entity;

public class Exercises {

    private int AnswerResut;
    private String BeginTime;
    private int LessonId;
    private String RightAnswer;
    private int TestNumber;
    private String TestTime;
    private String UserAnswer;
    private int uid;

    public Exercises(int answerResut, String beginTime, int lessonId, String rightAnswer,
                     int testNumber, String testTime, String userAnswer, int uid) {
        AnswerResut = answerResut;
        BeginTime = beginTime;
        LessonId = lessonId;
        RightAnswer = rightAnswer;
        TestNumber = testNumber;
        TestTime = testTime;
        UserAnswer = userAnswer;
        this.uid = uid;
    }

    public int getAnswerResut() {
        return AnswerResut;
    }

    public void setAnswerResut(int answerResut) {
        AnswerResut = answerResut;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
    }

    public String getRightAnswer() {
        return RightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        RightAnswer = rightAnswer;
    }

    public int getTestNumber() {
        return TestNumber;
    }

    public void setTestNumber(int testNumber) {
        TestNumber = testNumber;
    }

    public String getTestTime() {
        return TestTime;
    }

    public void setTestTime(String testTime) {
        TestTime = testTime;
    }

    public String getUserAnswer() {
        return UserAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        UserAnswer = userAnswer;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
