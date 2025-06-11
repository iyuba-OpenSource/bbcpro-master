package com.ai.bbcpro.ui.player.rank;

public class RankDetailItem {

    private int paraId;
    private int idIndex;
    private int score;
    private int shuoshuoType;
    private int againstCount;
    private int topicId;
    private String audioUrl;
    private String uid;
    private String date;
    private boolean hadZan;  // 是否点过赞
    private boolean playing;  // 是否正在播放


    public RankDetailItem(int paraId, int idIndex, int score, int shuoshuoType, int againstCount, int topicId, String audioUrl, String uid, String date) {
        this.paraId = paraId;
        this.idIndex = idIndex;
        this.score = score;
        this.shuoshuoType = shuoshuoType;
        this.againstCount = againstCount;
        this.topicId = topicId;
        this.audioUrl = audioUrl;
        this.uid = uid;
        this.date = date;
        hadZan = false;
        playing = false;
    }

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

    public int getParaId() {
        return paraId;
    }

    public void setParaId(int paraId) {
        this.paraId = paraId;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getShuoshuoType() {
        return shuoshuoType;
    }

    public void setShuoshuoType(int shuoshuoType) {
        this.shuoshuoType = shuoshuoType;
    }

    public int getAgainstCount() {
        return againstCount;
    }

    public void setAgainstCount(int againstCount) {
        this.againstCount = againstCount;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RankDetailItem{" +
                "paraId=" + paraId +
                ", idIndex=" + idIndex +
                ", score=" + score +
                ", shuoshuoType=" + shuoshuoType +
                ", againstCount=" + againstCount +
                ", topicId=" + topicId +
                ", audioUrl='" + audioUrl + '\'' +
                ", uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
