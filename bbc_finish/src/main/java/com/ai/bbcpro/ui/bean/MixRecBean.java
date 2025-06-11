package com.ai.bbcpro.ui.bean;

import com.google.gson.annotations.SerializedName;

public class MixRecBean {

    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;
    @SerializedName("URL")
    private String url;
    private int avrScore;

    public int getAvrScore() {
        return avrScore;
    }

    public void setAvrScore(int avrScore) {
        this.avrScore = avrScore;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
