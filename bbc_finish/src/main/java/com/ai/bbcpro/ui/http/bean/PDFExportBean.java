package com.ai.bbcpro.ui.http.bean;

import com.google.gson.annotations.SerializedName;

public class PDFExportBean {
    @SerializedName("exists")
    private Boolean exists;
    @SerializedName("path")
    private String path;

    public boolean getExists() {
        return exists;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
