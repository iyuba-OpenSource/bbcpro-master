package com.ai.bbcpro.ui.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CollectionBean {


    @SerializedName("result")
    private int result;
    @SerializedName("total")
    private int total;
    @SerializedName("data")
    private List<DataBean> data;
    @SerializedName("message")
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        @SerializedName("CollectDate")
        private String collectDate;
        @SerializedName("SentenceFlg")
        private int sentenceFlg;
        @SerializedName("Category")
        private String category;
        @SerializedName("CreateTime")
        private String createTime;
        @SerializedName("SentenceId")
        private String sentenceId;
        @SerializedName("Title")
        private String title;
        @SerializedName("Sound")
        private String sound;
        @SerializedName("Pic")
        private String pic;
        @SerializedName("pic")
        private String pic2;
        @SerializedName("title")
        private String title2;
        @SerializedName("Flag")
        private String flag;
        @SerializedName("voaid")
        private String voaid;
        @SerializedName("Type")
        private String type;
        @SerializedName("DescCn")
        private String descCn;
        @SerializedName("Title_cn")
        private String titleCn;
        @SerializedName("series")
        private String series;
        @SerializedName("titlecn")
        private String titlecn;
        @SerializedName("CategoryName")
        private String categoryName;
        @SerializedName("topic")
        private String topic;
        @SerializedName("Id")
        private String id;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName("ReadCount")
        private String readCount;
        @SerializedName("hotflg")
        private String hotflg;

        public String getCollectDate() {
            return collectDate;
        }

        public void setCollectDate(String collectDate) {
            this.collectDate = collectDate;
        }

        public int getSentenceFlg() {
            return sentenceFlg;
        }

        public void setSentenceFlg(int sentenceFlg) {
            this.sentenceFlg = sentenceFlg;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getSentenceId() {
            return sentenceId;
        }

        public void setSentenceId(String sentenceId) {
            this.sentenceId = sentenceId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSound() {
            return sound;
        }

        public void setSound(String sound) {
            this.sound = sound;
        }

        public String getPic2() {
            return pic2;
        }

        public void setPic2(String pic) {
            this.pic2 = pic;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title) {
            this.title2 = title;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getVoaid() {
            return voaid;
        }

        public void setVoaid(String voaid) {
            this.voaid = voaid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescCn() {
            return descCn;
        }

        public void setDescCn(String descCn) {
            this.descCn = descCn;
        }

        public String getTitleCn() {
            return titleCn;
        }

        public void setTitleCn(String titleCn) {
            this.titleCn = titleCn;
        }

        public String getSeries() {
            return series;
        }

        public void setSeries(String series) {
            this.series = series;
        }

        public String getTitlecn() {
            return titlecn;
        }

        public void setTitlecn(String titlecn) {
            this.titlecn = titlecn;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSentence() {
            return sentence;
        }

        public void setSentence(String sentence) {
            this.sentence = sentence;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }

        public String getHotflg() {
            return hotflg;
        }

        public void setHotflg(String hotflg) {
            this.hotflg = hotflg;
        }
    }
}
