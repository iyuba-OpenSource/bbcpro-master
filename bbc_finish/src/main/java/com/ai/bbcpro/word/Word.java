package com.ai.bbcpro.word;

import java.io.Serializable;

public class Word implements Serializable {

    public String userid;
    public String key = ""; // 单词
    public String lang = "";
    public String audioUrl = ""; // 多媒体网络路�?
    public String pron = ""; // 音标
    public String def = ""; // 解释
    public String examples = ""; // 例句，多条以�?”分�?
    public String createDate = ""; // 创建时间
    public boolean isDelete = false;
    public String delete;


    /**
     * 答题状态
     * 0：未答题
     * 1：回答正确
     * 2：回答错误
     */
    public int answer_status;


    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public String getExamples() {
        return examples;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public int getAnswer_status() {
        return answer_status;
    }

    public void setAnswer_status(int answer_status) {
        this.answer_status = answer_status;
    }
}
