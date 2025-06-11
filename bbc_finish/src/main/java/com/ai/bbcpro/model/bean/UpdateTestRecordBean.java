package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

public class UpdateTestRecordBean {


    @SerializedName("result")
    private String result;
    @SerializedName("jiFen")
    private String jiFen;
    @SerializedName("message")
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getJiFen() {
        return jiFen;
    }

    public void setJiFen(String jiFen) {
        this.jiFen = jiFen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
