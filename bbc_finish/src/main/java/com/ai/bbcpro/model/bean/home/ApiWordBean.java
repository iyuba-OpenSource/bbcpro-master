package com.ai.bbcpro.model.bean.home;

import java.util.List;

public class ApiWordBean {

    private int result;
    private String key;
    private String audio;
    private String pron;
    private String proncode;
    private String def;

    private List<SentBean> sent;


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getPron() {
        return pron;
    }

    public void setPron(String pron) {
        this.pron = pron;
    }

    public String getProncode() {
        return proncode;
    }

    public void setProncode(String proncode) {
        this.proncode = proncode;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public List<SentBean> getSent() {
        return sent;
    }

    public void setSent(List<SentBean> sent) {
        this.sent = sent;
    }

    public static class SentBean {

        private int number;
        private String orig;
        private  String trans;

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }
    }
}
