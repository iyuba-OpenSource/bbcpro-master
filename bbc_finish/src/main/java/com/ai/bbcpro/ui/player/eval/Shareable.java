package com.ai.bbcpro.ui.player.eval;

public interface Shareable {
    String getShareUrl(String appName);
    String getShareImageUrl();
    String getShareAudioUrl();
    String getShareTitle();
    String getShareLongText();
    String getShareShortText();
}
