package com.ai.bbcpro.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClockInLogBean {

    @SerializedName("result")
    private int result;
    @SerializedName("record")
    private List<RecordDTO> record;
    @SerializedName("count")
    private int count;
    @SerializedName("message")
    private String message;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public List<RecordDTO> getRecord() {
        return record;
    }

    public void setRecord(List<RecordDTO> record) {
        this.record = record;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class RecordDTO {
        @SerializedName("uid")
        private String uid;
        @SerializedName("createtime")
        private String createtime;
        @SerializedName("appid")
        private String appid;
        @SerializedName("scan")
        private String scan;
        @SerializedName("shareId")
        private String shareId;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getScan() {
            return scan;
        }

        public void setScan(String scan) {
            this.scan = scan;
        }

        public String getShareId() {
            return shareId;
        }

        public void setShareId(String shareId) {
            this.shareId = shareId;
        }
    }
}
