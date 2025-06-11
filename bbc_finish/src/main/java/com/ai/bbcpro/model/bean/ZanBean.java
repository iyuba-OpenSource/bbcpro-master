package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

public class ZanBean {


    @SerializedName("ResultCode")
    private String resultCode;
    @SerializedName("Message")
    private String message;

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
