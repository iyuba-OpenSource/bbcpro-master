package com.ai.bbcpro.util;

import java.io.Serializable;

public class PlayBean implements Serializable {
    public String name;
    public String author;
    public String playUrl ;

    public String examTime ;

    public PlayBean(String name, String author, String playUrl, String examTime, String section, String subTitle) {
        this.name = name;
        this.author = author;
        this.playUrl = playUrl;
        this.examTime = examTime;
        this.section = section;
        this.subTitle = subTitle;
    }

    public String section ;
    public String subTitle ;


}
