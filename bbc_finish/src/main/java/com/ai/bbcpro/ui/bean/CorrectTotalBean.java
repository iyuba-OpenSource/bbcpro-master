package com.ai.bbcpro.ui.bean;

public class CorrectTotalBean {
    String wordContent;
    CorrectPronBean correctPronBean;
    String bbcId;
    int sequence;
    int position;

    

    public CorrectTotalBean(String wordContent, CorrectPronBean correctPronBean, String bbcId, int sequence,int position) {
        this.wordContent = wordContent;
        this.correctPronBean = correctPronBean;
        this.bbcId = bbcId;
        this.sequence = sequence;
        this.position = position;
    }

    public CorrectTotalBean() {
    }

    public int getPosition() {
        return position;
    }

    public String getWordContent() {
        return wordContent;
    }

    public void setWordContent(String wordContent) {
        this.wordContent = wordContent;
    }

    public CorrectPronBean getCorrectPronBean() {
        return correctPronBean;
    }

    public void setCorrectPronBean(CorrectPronBean correctPronBean) {
        this.correctPronBean = correctPronBean;
    }

    public String getBbcId() {
        return bbcId;
    }

    public void setBbcId(String bbcId) {
        this.bbcId = bbcId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return getWordContent() +" ";
    }
}
