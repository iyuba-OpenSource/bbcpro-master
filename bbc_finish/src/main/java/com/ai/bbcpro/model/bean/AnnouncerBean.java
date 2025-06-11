package com.ai.bbcpro.model.bean;

import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AnnouncerBean implements Serializable {


    @SerializedName("result")
    private String result;
    @SerializedName("message")
    private String message;
    @SerializedName("path")
    private String path;

    //http://iuserspeech.iyuba.cn:9001/voice/getBroadcastOptions?uid=0  的 speaker
    private String speaker;

    private List<BBCContentBean.DataBean> voatext;


    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public List<BBCContentBean.DataBean> getVoatext() {
        return voatext;
    }

    public void setVoatext(List<BBCContentBean.DataBean> voatext) {
        this.voatext = voatext;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
