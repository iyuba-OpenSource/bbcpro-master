package com.ai.bbcpro.ui.player.eval;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 评测句子中的每一个单词
 * 包含：
 * 单词内容
 * 单词index
 * 单词评测得分
 */
public class EvalWord {

    String bbcId;
    int position;
    String para_id;
    String id_index;
    @SerializedName("score")
    private float score;
    @SerializedName("substitute_orgi")
    private String substituteOrgi;
    @SerializedName("pron")
    private String pron;
    @SerializedName("index")
    private int index;
    @SerializedName("insert")
    private String insert;
    @SerializedName("user_pron2")
    private String userPron2;
    @SerializedName("delete")
    private String delete;
    @SerializedName("substitute_user")
    private String substituteUser;
    @SerializedName("content")
    private String content;
    @SerializedName("pron2")
    private String pron2;
    @SerializedName("user_pron")
    private String userPron;
    private int length;


    public EvalWord(String content, float score, int index) {
        this.content = content;
        this.score = score;
        this.index = index;
        length = content.length();
    }

    public EvalWord() {

    }

    public String getPara_id() {
        return para_id;
    }

    public void setPara_id(String para_id) {
        this.para_id = para_id;
    }

    public String getId_index() {
        return id_index;
    }

    public void setId_index(String id_index) {
        this.id_index = id_index;
    }

    public String getBbcId() {
        return bbcId;
    }

    public void setBbcId(String bbcId) {
        this.bbcId = bbcId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getSubstituteOrgi() {
        return substituteOrgi;
    }

    public void setSubstituteOrgi(String substituteOrgi) {
        this.substituteOrgi = substituteOrgi;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getInsert() {
        return insert;
    }

    public void setInsert(String insert) {
        this.insert = insert;
    }

    public String getUserPron2() {
        return userPron2;
    }

    public void setUserPron2(String userPron2) {
        this.userPron2 = userPron2;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getSubstituteUser() {
        return substituteUser;
    }

    public void setSubstituteUser(String substituteUser) {
        this.substituteUser = substituteUser;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPron2() {
        return pron2;
    }

    public void setPron2(String pron2) {
        this.pron2 = pron2;
    }

    public String getUserPron() {
        return userPron;
    }

    public void setUserPron(String userPron) {
        this.userPron = userPron;
    }

    public int getLength() {
        return content.length();
    }

    @Override
    public String toString() {
        return "EvalWord{" +
                "score='" + score + '\'' +
                ", substituteOrgi='" + substituteOrgi + '\'' +
                ", pron='" + pron + '\'' +
                ", index='" + index + '\'' +
                ", insert='" + insert + '\'' +
                ", userPron2='" + userPron2 + '\'' +
                ", delete='" + delete + '\'' +
                ", substituteUser='" + substituteUser + '\'' +
                ", content='" + content + '\'' +
                ", pron2='" + pron2 + '\'' +
                ", userPron='" + userPron + '\'' +
                '}';
    }
}


