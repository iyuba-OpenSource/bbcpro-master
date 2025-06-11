package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FamousPersonBean {


    @SerializedName("result")
    private String result;
    @SerializedName("announcer")
    private List<String> announcer;
    @SerializedName("emotions")
    private List<String> emotions;
    @SerializedName("announcer_data")
    private List<AnnouncerDataDTO> announcerData;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<String> getAnnouncer() {
        return announcer;
    }

    public void setAnnouncer(List<String> announcer) {
        this.announcer = announcer;
    }

    public List<String> getEmotions() {
        return emotions;
    }

    public void setEmotions(List<String> emotions) {
        this.emotions = emotions;
    }

    public List<AnnouncerDataDTO> getAnnouncerData() {
        return announcerData;
    }

    public void setAnnouncerData(List<AnnouncerDataDTO> announcerData) {
        this.announcerData = announcerData;
    }

    public static class AnnouncerDataDTO {

        @SerializedName("speaker")
        private String speaker;
        @SerializedName("speaker_real")
        private String speakerReal;


        public AnnouncerDataDTO(String speaker, String speakerReal) {
            this.speaker = speaker;
            this.speakerReal = speakerReal;
        }

        public String getSpeaker() {
            return speaker;
        }

        public void setSpeaker(String speaker) {
            this.speaker = speaker;
        }

        public String getSpeakerReal() {
            return speakerReal;
        }

        public void setSpeakerReal(String speakerReal) {
            this.speakerReal = speakerReal;
        }
    }
}
