package com.ai.bbcpro.ui.player.rank;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankContentResponse {

    @SerializedName("result")
    private int result;
    @SerializedName("myimgSrc")
    private String myimgSrc;
    @SerializedName("myid")
    private int myid;
    @SerializedName("myranking")
    private int myranking;
    @SerializedName("data")
    private List<DataBean> data;
    @SerializedName("myname")
    private String myname;
    @SerializedName("myscores")
    private int myscores;
    @SerializedName("mycount")
    private int mycount;
    @SerializedName("vip")
    private String vip;
    @SerializedName("message")
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMyimgSrc() {
        return myimgSrc;
    }

    public void setMyimgSrc(String myimgSrc) {
        this.myimgSrc = myimgSrc;
    }

    public int getMyid() {
        return myid;
    }

    public void setMyid(int myid) {
        this.myid = myid;
    }

    public int getMyranking() {
        return myranking;
    }

    public void setMyranking(int myranking) {
        this.myranking = myranking;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public int getMyscores() {
        return myscores;
    }

    public void setMyscores(int myscores) {
        this.myscores = myscores;
    }

    public int getMycount() {
        return mycount;
    }

    public void setMycount(int mycount) {
        this.mycount = mycount;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RankContentResponse{" +
                "result=" + result +
                ", myimgSrc='" + myimgSrc + '\'' +
                ", myid=" + myid +
                ", myranking=" + myranking +
                ", data=" + data +
                ", myname='" + myname + '\'' +
                ", myscores=" + myscores +
                ", mycount=" + mycount +
                ", vip='" + vip + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public static class DataBean {
        @SerializedName("uid")
        private int uid;
        @SerializedName("scores")
        private int scores;
        @SerializedName("name")
        private String name;
        @SerializedName("count")
        private int count;
        @SerializedName("ranking")
        private int ranking;
        @SerializedName("sort")
        private int sort;
        @SerializedName("vip")
        private String vip;
        @SerializedName("imgSrc")
        private String imgSrc;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getScores() {
            return scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getRanking() {
            return ranking;
        }

        public void setRanking(int ranking) {
            this.ranking = ranking;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getVip() {
            return vip;
        }

        public void setVip(String vip) {
            this.vip = vip;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "uid=" + uid +
                    ", scores=" + scores +
                    ", name='" + name + '\'' +
                    ", count=" + count +
                    ", ranking=" + ranking +
                    ", sort=" + sort +
                    ", vip='" + vip + '\'' +
                    ", imgSrc='" + imgSrc + '\'' +
                    '}';
        }

    }
}
