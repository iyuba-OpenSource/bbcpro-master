package com.ai.bbcpro.widget;


import com.ai.common.CommonConstant;
import com.google.gson.annotations.SerializedName;

public class SpeakRankWork {

    @SerializedName("id")
    public int id;
    @SerializedName("agreeCount")
    public int agreeCount = 0;
    @SerializedName("againstCount")
    public int againstCount = 0;
    @SerializedName("ShuoShuo")
    public String shuoshuo = "";
    @SerializedName("shuoshuotype")
    public int shuoshuoType;
    @SerializedName("CreateDate")
    public String createdate;
    @SerializedName("score")
    public int score = 0; //
    @SerializedName("paraid")
    public int paraid = 0; //
    @SerializedName("idIndex")
    public String idindex; //
    @SerializedName("TopicId")
    public int voaId;

    public String imgsrc = "";
    public String title = "";
    public String titleCn = "";
    public String description = "";

    public int userid;
    public String username = "none";

    public String readText = "";

    public boolean isAudioCommentPlaying = false;

    public String getShuoShuoUrl() {
        return CommonConstant.HTTP_SPEECH_ALL+ "/voa/" + shuoshuo;
    }

    public String getUsername() {
        return username;
    }

    public int getUpvoteCount() {
        return agreeCount;
    }

    public int getDownvoteCount() {
        return againstCount;
    }

    public String getScore() {
        return score + "分";
    }

}

