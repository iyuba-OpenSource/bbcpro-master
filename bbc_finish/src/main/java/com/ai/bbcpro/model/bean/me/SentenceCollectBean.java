package com.ai.bbcpro.model.bean.me;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SentenceCollectBean {

    @SerializedName("result")
    private int result;
    @SerializedName("total")
    private int total;
    @SerializedName("data")
    private List<DataDTO> data;
    @SerializedName("counts")
    private int counts;
    @SerializedName("totalPage")
    private int totalPage;
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

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataDTO {
        @SerializedName("CollectDate")
        private String collectDate;
        @SerializedName("SentenceFlg")
        private int sentenceFlg;
        @SerializedName("Category")
        private String category;
        @SerializedName("soundsentence")
        private String soundsentence;
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
        @SerializedName("endTime")
        private String endTime;
        @SerializedName("Sentence")
        private String sentence;
        @SerializedName("ReadCount")
        private String readCount;

        /**
         * 分数
         */
        private int score = -1;

        private String para_id;
        private String id_index;


        public String getPara_id() {
            return para_id;
        }

        public void setPara_id(String para_id) {
            this.para_id = para_id;
        }

        public String getId_index() {
            return id_index;
        }

        public void setId_index(String id_index) {
            this.id_index = id_index;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

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

        public String getSoundsentence() {
            return soundsentence;
        }

        public void setSoundsentence(String soundsentence) {
            this.soundsentence = soundsentence;
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

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
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
    }
}
