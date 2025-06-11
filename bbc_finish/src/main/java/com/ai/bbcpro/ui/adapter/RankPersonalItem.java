package com.ai.bbcpro.ui.adapter;

import com.google.gson.annotations.SerializedName;

public class RankPersonalItem {
    @SerializedName("paraid")
    private int paraid;
    @SerializedName("score")
    private int score;
    @SerializedName("shuoshuotype")
    private int shuoshuotype;
    @SerializedName("againstCount")
    private int againstCount;
    @SerializedName("agreeCount")
    private int agreeCount;
    @SerializedName("TopicId")
    private int topicId;
    @SerializedName("ShuoShuo")
    private String shuoShuo;
    @SerializedName("id")
    private int id;
    @SerializedName("idIndex")
    private int idIndex;
    @SerializedName("CreateDate")
    private String createDate;
    private boolean hadZan;  // 是否点过赞

    public boolean isHadZan() {
        return hadZan;
    }

    public void setHadZan(boolean hadZan) {
        this.hadZan = hadZan;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    private boolean playing;  // 是否正在播放

    public int getParaid() {
        return paraid;
    }

    public void setParaid(int paraid) {
        this.paraid = paraid;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShuoshuotype() {
        return shuoshuotype;
    }

    public void setShuoshuotype(int shuoshuotype) {
        this.shuoshuotype = shuoshuotype;
    }

    public int getAgainstCount() {
        return againstCount;
    }

    public void setAgainstCount(int againstCount) {
        this.againstCount = againstCount;
    }

    public int getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(int agreeCount) {
        this.agreeCount = agreeCount;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getShuoShuo() {
        return shuoShuo;
    }

    public void setShuoShuo(String shuoShuo) {
        this.shuoShuo = shuoShuo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "RankPersonalItem{" +
                "paraid=" + paraid +
                ", score=" + score +
                ", shuoshuotype=" + shuoshuotype +
                ", againstCount=" + againstCount +
                ", agreeCount=" + agreeCount +
                ", topicId=" + topicId +
                ", shuoShuo='" + shuoShuo + '\'' +
                ", id=" + id +
                ", idIndex=" + idIndex +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
