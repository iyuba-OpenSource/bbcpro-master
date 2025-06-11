package com.ai.bbcpro.ui.widget.singleSubtitle;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.iyuba.subtitle.SLGSHelper;
import com.iyuba.subtitle.SLGSViewable;
import com.iyuba.textpage.TextPage;

import java.util.List;

public class SingleLineSubtitleTextPage extends TextPage implements SLGSViewable<ChForeignSubtitle>, SubtitleChForeignChange {
    private SLGSHelper<ChForeignSubtitle, SingleLineSubtitleTextPage> mSLGSHelper;
    private SubtitleLangModeHelper mLangHelper;

    public SingleLineSubtitleTextPage(Context context) {
        super(context);
        this.initHelper();
    }

    public SingleLineSubtitleTextPage(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initHelper();
    }

    public SingleLineSubtitleTextPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.initHelper();
    }

    private void initHelper() {
        this.mSLGSHelper = new SLGSHelper(this);
        this.mLangHelper = new SubtitleLangModeHelper(this);
    }

    public void setSubtitleMode(int mode) {
        this.mLangHelper.setSubtitleMode(mode);
    }

    public int getSubtitleMode() {
        return this.mLangHelper.getSubtitleMode();
    }

    public boolean getShowCHN() {
        return this.getSubtitleMode() == 2;
    }

    public void setSubtitles(@NonNull List<ChForeignSubtitle> subtitles) {
        this.mSLGSHelper.setSubtitles(subtitles);
    }

    @NonNull
    public List<ChForeignSubtitle> getSubtitles() {
        return this.mSLGSHelper.getSubtitles();
    }

    public int getCurrentParagraph() {
        return this.mSLGSHelper.getCurrentParagraph();
    }

    public void updateContentViews() {
        this.mSLGSHelper.updateContentViews();
    }

    public void syncParagraph(int paragraph) {
        this.mSLGSHelper.syncParagraph(paragraph);
    }

    public void syncProgress(long time) {
        this.mSLGSHelper.syncProgress(time);
    }

    public void reset() {
        this.mSLGSHelper.reset();
    }
}

