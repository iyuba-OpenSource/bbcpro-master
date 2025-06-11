package com.ai.bbcpro.ui.player.subtitle;


public class Word {
    private String content;  // 单词
    private String Phonetic;  // 音标
    private String explanation;  // 释义

    public Word(String content, String phonetic, String explanation) {
        this.content = content;
        Phonetic = phonetic;
        this.explanation = explanation;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhonetic() {
        return Phonetic;
    }

    public void setPhonetic(String phonetic) {
        Phonetic = phonetic;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "Word{" +
                "content='" + content + '\'' +
                ", Phonetic='" + Phonetic + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}

