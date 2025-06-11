package com.ai.bbcpro.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CorrectPronBean {
    @SerializedName("result")
    private int result;
    @SerializedName("user_pron")
    private String userPron;
    @SerializedName("ori_pron")
    private String oriPron;
    @SerializedName("match_idx")
    private List<List<Integer>> matchIdx;
    @SerializedName("insert_id")
    private List<List<Integer>> insertId;
    @SerializedName("delete_id")
    private List<List<Integer>> deleteId;
    @SerializedName("substitute_id")
    private List<List<Integer>> substituteId;
    @SerializedName("key")
    private String key;
    @SerializedName("audio")
    private String audio;
    @SerializedName("pron")
    private String pron;
    @SerializedName("proncode")
    private String proncode;
    @SerializedName("def")
    private String def;
    @SerializedName("sent")
    private List<SentBean> sent;

    public CorrectPronBean(String userPron, String oriPron, String audio, String def) {
        this.userPron = userPron;
        this.oriPron = oriPron;
        this.audio = audio;
        this.def = def;
    }

    public CorrectPronBean() {
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getUserPron() {
        return userPron;
    }

    public void setUserPron(String userPron) {
        this.userPron = userPron;
    }

    public String getOriPron() {
        return oriPron;
    }

    public void setOriPron(String oriPron) {
        this.oriPron = oriPron;
    }

    public List<List<Integer>> getMatchIdx() {
        return matchIdx;
    }

    public void setMatchIdx(List<List<Integer>> matchIdx) {
        this.matchIdx = matchIdx;
    }

    public List<List<Integer>> getInsertId() {
        return insertId;
    }

    public void setInsertId(List<List<Integer>> insertId) {
        this.insertId = insertId;
    }

    public List<List<Integer>> getDeleteId() {
        return deleteId;
    }

    public void setDeleteId(List<List<Integer>> deleteId) {
        this.deleteId = deleteId;
    }

    public List<List<Integer>> getSubstituteId() {
        return substituteId;
    }

    public void setSubstituteId(List<List<Integer>> substituteId) {
        this.substituteId = substituteId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getProncode() {
        return proncode;
    }

    public void setProncode(String proncode) {
        this.proncode = proncode;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public List<SentBean> getSent() {
        return sent;
    }

    public void setSent(List<SentBean> sent) {
        this.sent = sent;
    }

    public static class SentBean {
        @SerializedName("number")
        private int number;
        @SerializedName("orig")
        private String orig;
        @SerializedName("trans")
        private String trans;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }
    }

    @Override
    public String toString() {
        return "CorrectPronBean{" +
                "result=" + result +
                ", userPron='" + userPron + '\'' +
                ", oriPron='" + oriPron + '\'' +
                ", matchIdx=" + matchIdx +
                ", insertId=" + insertId +
                ", deleteId=" + deleteId +
                ", substituteId=" + substituteId +
                ", key='" + key + '\'' +
                ", audio='" + audio + '\'' +
                ", pron='" + pron + '\'' +
                ", proncode='" + proncode + '\'' +
                ", def='" + def + '\'' +
                ", sent=" + sent +
                '}';
    }
}
