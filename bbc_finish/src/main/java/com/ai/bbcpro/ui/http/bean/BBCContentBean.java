package com.ai.bbcpro.ui.http.bean;

import com.ai.bbcpro.model.bean.QuestionBean;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BBCContentBean implements Serializable {


    @SerializedName("total")
    private String total;
    @SerializedName("data")
    private List<DataBean> data;
    @SerializedName("dataQuestion")
    private List<QuestionBean> dataQuestion;
    @SerializedName("dataWords")
    private List<DataWordsBean> dataWords;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public List<QuestionBean> getDataQuestion() {
        return dataQuestion;
    }

    public void setDataQuestion(List<QuestionBean> dataQuestion) {
        this.dataQuestion = dataQuestion;
    }

    public List<DataWordsBean> getDataWords() {
        return dataWords;
    }

    public void setDataWords(List<DataWordsBean> dataWords) {
        this.dataWords = dataWords;
    }

    public static class DataBean implements Serializable {
        @SerializedName("ImgPath")
        private String imgPath;
        @SerializedName("EndTiming")
        private String endTiming;
        @SerializedName("ParaId")
        private String paraId;
        @SerializedName("IdIndex")
        private String idIndex;
        @SerializedName("Announcer")
        private String announcer;
        @SerializedName("sentence_cn")
        private String sentenceCn;
        @SerializedName("ImgWords")
        private String imgWords;
        @SerializedName("Timing")
        private String timing;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName(value = "BbcId", alternate = "voaid")
        private int bbcId;

        public String getImgPath() {
            return imgPath;
        }

        public void setImgPath(String imgPath) {
            this.imgPath = imgPath;
        }

        public String getEndTiming() {
            return endTiming;
        }

        public void setEndTiming(String endTiming) {
            this.endTiming = endTiming;
        }

        public String getParaId() {
            return paraId;
        }

        public void setParaId(String paraId) {
            this.paraId = paraId;
        }

        public String getIdIndex() {
            return idIndex;
        }

        public void setIdIndex(String idIndex) {
            this.idIndex = idIndex;
        }

        public String getAnnouncer() {
            return announcer;
        }

        public void setAnnouncer(String announcer) {
            this.announcer = announcer;
        }

        public String getSentenceCn() {
            return sentenceCn;
        }

        public void setSentenceCn(String sentenceCn) {
            this.sentenceCn = sentenceCn;
        }

        public String getImgWords() {
            return imgWords;
        }

        public void setImgWords(String imgWords) {
            this.imgWords = imgWords;
        }

        public String getTiming() {
            return timing;
        }

        public void setTiming(String timing) {
            this.timing = timing;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public int getBbcId() {
            return bbcId;
        }

        public void setBbcId(int bbcId) {
            this.bbcId = bbcId;
        }
    }


    public static class DataWordsBean {
        @SerializedName("IndexId")
        private String indexId;
        @SerializedName("Def")
        private String def;
        @SerializedName("Words")
        private String words;
        @SerializedName("BbcId")
        private int bbcId;

        public String getIndexId() {
            return indexId;
        }

        public void setIndexId(String indexId) {
            this.indexId = indexId;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }

        public String getWords() {
            return words;
        }

        public void setWords(String words) {
            this.words = words;
        }

        public int getBbcId() {
            return bbcId;
        }

        public void setBbcId(int bbcId) {
            this.bbcId = bbcId;
        }
    }
}
