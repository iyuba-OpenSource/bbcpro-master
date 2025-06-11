package com.ai.bbcpro.widget;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserWorks {
    @SerializedName("result")
    public boolean result;
    @SerializedName("message")
    public String message;
    @SerializedName("count")
    public int count;
    @SerializedName("data")
    public List<SpeakRankWork> data;
}
