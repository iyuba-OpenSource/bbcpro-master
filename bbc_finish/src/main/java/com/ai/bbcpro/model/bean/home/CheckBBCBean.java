package com.ai.bbcpro.model.bean.home;

import com.google.gson.annotations.SerializedName;

public class CheckBBCBean {


    @SerializedName("result")
    private int result;
    @SerializedName("flag")
    private int flag;


    public CheckBBCBean(int result, int flag) {
        this.result = result;
        this.flag = flag;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
