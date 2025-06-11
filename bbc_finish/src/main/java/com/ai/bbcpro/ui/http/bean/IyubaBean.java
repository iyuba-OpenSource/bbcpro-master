package com.ai.bbcpro.ui.http.bean;

import java.io.Serializable;
import java.util.List;

public class IyubaBean implements Serializable {

    public String total;
    public List<VoatextBean> voatext;

    public IyubaBean(String total, List<VoatextBean> voatext) {
        this.total = total;
        this.voatext = voatext;
    }

    public static class VoatextBean implements Serializable{
        public String imgPath;
        public int endTiming;
        public String paraId;
        public String idIndex;
        public String sentence_cn;
        public String imgWords;
        public double timing;
        public String Sentence;
    }
}
