package com.ai.bbcpro.ui.widget.singleSubtitle;

import com.iyuba.subtitle.SLGSViewable;

import java.util.Iterator;

class SubtitleLangModeHelper implements SubtitleChForeignChange {
    private int mSubtitleMode = 2;
    private final SLGSViewable<ChForeignSubtitle> mSubtitleView;

    public SubtitleLangModeHelper(SLGSViewable<ChForeignSubtitle> subtitleView) {
        this.mSubtitleView = subtitleView;
    }

    public void setSubtitleMode(int mode) {
        if (this.isModeLegal(mode) && this.mSubtitleMode != mode) {
            this.mSubtitleMode = mode;
            Iterator var2 = this.mSubtitleView.getSubtitles().iterator();

            while(var2.hasNext()) {
                ChForeignSubtitle subtitle = (ChForeignSubtitle)var2.next();
                subtitle.setMode(this.mSubtitleMode);
            }

            this.mSubtitleView.updateContentViews();
        }

    }

    public int getSubtitleMode() {
        return this.mSubtitleMode;
    }

    private boolean isModeLegal(int mode) {
        return mode >= 0 && mode <= 3;
    }
}

