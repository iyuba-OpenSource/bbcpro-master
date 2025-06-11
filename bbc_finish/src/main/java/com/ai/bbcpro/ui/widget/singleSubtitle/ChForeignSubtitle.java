package com.ai.bbcpro.ui.widget.singleSubtitle;

import com.iyuba.subtitle.Subtitleable;

public class ChForeignSubtitle implements Subtitleable {
    public String mChContent = "";
    public String mForeignContent = "";
    public long mStartTime;
    public int wordCount;
    private int mode = 2;

    public ChForeignSubtitle() {
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public String getContent() {
        switch(this.mode) {
            case 0:
            default:
                return this.mForeignContent;
            case 1:
                return this.mChContent + "\n" + this.mForeignContent;
            case 2:
                return this.mForeignContent + "\n" + this.mChContent;
            case 3:
                return this.mChContent;
        }
    }

    public interface ShowMode {
        int FOREIGN_ONLY = 0;
        int DOUBLE_CH_ABOVE = 1;
        int DOUBLE_FOREIGN_ABOVE = 2;
        int CHINESE_ONLY = 3;
    }
}

