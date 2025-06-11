package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecommendBean {

    /**
     * {
     *     "result": "200",
     *     "data": [
     *         "largest machine",
     *         "power grid",
     *         "energy use",
     *         "renewable energy",
     *         "Cost of living",
     *         "energy crisis",
     *         "UK",
     *         "businesses",
     *         "Doodle",
     *         "art"
     *     ],
     *     "message": "查询成功"
     * }
     */

    @SerializedName("result")
    private String result;
    @SerializedName("data")
    private List<String> data;
    @SerializedName("message")
    private String message;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
