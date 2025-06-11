package com.ai.bbcpro.ui.player.rank;

import com.ai.bbcpro.ui.adapter.RankPersonalItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RankPersonalTotal {

    @SerializedName("result")
    private boolean result;
    @SerializedName("data")
    private List<RankPersonalItem> data;
    @SerializedName("count")
    private int count;
    @SerializedName("message")
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<RankPersonalItem> getData() {
        return data;
    }

    public void setData(List<RankPersonalItem> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RankPersonalTotal{" +
                "result=" + result +
                ", data=" + data +
                ", count=" + count +
                ", message='" + message + '\'' +
                '}';
    }
}
