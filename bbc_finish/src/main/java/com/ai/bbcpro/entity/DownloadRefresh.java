package com.ai.bbcpro.entity;

public class DownloadRefresh {

    private String bbcid;


    public DownloadRefresh(String bbcid) {
        this.bbcid = bbcid;
    }

    public String getBbcid() {
        return bbcid;
    }

    public void setBbcid(String bbcid) {
        this.bbcid = bbcid;
    }
}
