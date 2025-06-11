package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

public class ScoreBean {

    @SerializedName("result")
    private String result;
    @SerializedName("addcredit")
    private String addcredit;
    @SerializedName("days")
    private String days;
    @SerializedName("money")
    private String money;
    @SerializedName("totalcredit")
    private String totalcredit;
    @SerializedName("message")
    private String message;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getAddcredit() {
        return addcredit;
    }

    public void setAddcredit(String addcredit) {
        this.addcredit = addcredit;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTotalcredit() {
        return totalcredit;
    }

    public void setTotalcredit(String totalcredit) {
        this.totalcredit = totalcredit;
    }
}
