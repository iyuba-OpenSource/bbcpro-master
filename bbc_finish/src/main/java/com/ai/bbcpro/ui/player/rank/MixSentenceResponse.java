package com.ai.bbcpro.ui.player.rank;

import com.google.gson.annotations.SerializedName;

public class MixSentenceResponse {

    @SerializedName("ResultCode")
    private String resultCode;
    @SerializedName("AddScore")
    private int addScore;
    @SerializedName("ShuoshuoId")
    private int shuoshuoId;
    @SerializedName("Message")
    private String message;


    private String reward;

    private String rewardMessage;


    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getRewardMessage() {
        return rewardMessage;
    }

    public void setRewardMessage(String rewardMessage) {
        this.rewardMessage = rewardMessage;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public int getAddScore() {
        return addScore;
    }

    public void setAddScore(int addScore) {
        this.addScore = addScore;
    }

    public int getShuoshuoId() {
        return shuoshuoId;
    }

    public void setShuoshuoId(int shuoshuoId) {
        this.shuoshuoId = shuoshuoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MixSentenceResponse{" +
                "resultCode='" + resultCode + '\'' +
                ", addScore=" + addScore +
                ", shuoshuoId=" + shuoshuoId +
                ", message='" + message + '\'' +
                '}';
    }
}
