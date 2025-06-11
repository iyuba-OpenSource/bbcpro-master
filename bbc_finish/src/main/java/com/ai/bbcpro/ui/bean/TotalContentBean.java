package com.ai.bbcpro.ui.bean;

import java.util.List;

public class TotalContentBean {

    private String total;
    private List<DataBean> data;

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

    public static class DataBean {
        private String creatTime;
        private String category;
        private String title;
        private Integer texts;
        private String sound;
        private String pic;
        private Integer bbcId;
        private String url;
        private String descCn;
        private String title_cn;
        private String publishTime;
        private String hotFlg;
        private String readCount;

        public String getCreatTime() {
            return creatTime;
        }

        public void setCreatTime(String creatTime) {
            this.creatTime = creatTime;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Integer getTexts() {
            return texts;
        }

        public void setTexts(Integer texts) {
            this.texts = texts;
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

        public Integer getBbcId() {
            return bbcId;
        }

        public void setBbcId(Integer bbcId) {
            this.bbcId = bbcId;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getDescCn() {
            return descCn;
        }

        public void setDescCn(String descCn) {
            this.descCn = descCn;
        }

        public String getTitle_cn() {
            return title_cn;
        }

        public void setTitle_cn(String title_cn) {
            this.title_cn = title_cn;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getHotFlg() {
            return hotFlg;
        }

        public void setHotFlg(String hotFlg) {
            this.hotFlg = hotFlg;
        }

        public String getReadCount() {
            return readCount;
        }

        public void setReadCount(String readCount) {
            this.readCount = readCount;
        }
    }
}
