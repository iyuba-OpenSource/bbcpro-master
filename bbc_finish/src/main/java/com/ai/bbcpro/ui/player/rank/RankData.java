package com.ai.bbcpro.ui.player.rank;

public class RankData {
    private String uid;
    private int score;
    private String name;
    private int count;
    private int rank;
    private String headImg;
    private int vipState;

    public RankData(String uid, int score, String name, int count, int rank, String headImg, int vipState) {
        this.uid = uid;
        this.score = score;
        this.name = name;
        this.count = count;
        this.rank = rank;
        this.headImg = headImg;
        this.vipState = vipState;
    }

    public String getUid() {
        return uid;
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public int getRank() {
        return rank;
    }

    public String getHeadImg() {
        return headImg;
    }

    public int getVipState() {
        return vipState;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setVipState(int vipState) {
        this.vipState = vipState;
    }

    @Override
    public String toString() {
        return "RankData{" +
                "uid='" + uid + '\'' +
                ", score=" + score +
                ", name='" + name + '\'' +
                ", count=" + count +
                ", rank=" + rank +
                ", headImg='" + headImg + '\'' +
                ", vipState=" + vipState +
                '}';
    }
}
