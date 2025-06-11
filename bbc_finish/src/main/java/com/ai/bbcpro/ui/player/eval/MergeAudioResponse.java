package com.ai.bbcpro.ui.player.eval;

import com.google.gson.annotations.SerializedName;

public class MergeAudioResponse implements Shareable {
    @SerializedName("ResultCode")
    public int result;
    @SerializedName("Message")
    public String message;
    @SerializedName("ShuoShuoId")
    public int id;
    @SerializedName("AddScore")
    public int credit;
    @SerializedName("FilePath")
    public String path;

    public int voaId;
    public String imgUrl;
    public String username;
    public String title;
    public String titleCn;
    public String description;

    @Override
    public String getShareUrl(String url) {
//        if (Constant.BROADCAST_URL.lastIndexOf("?") == Constant.BROADCAST_URL.length() - 1) {
//            return Constant.BROADCAST_URL + "id=" + voaId + "&shuoshuo=" + path+ "&apptype="
//                    + Constant.APP_TYPE;
//        } else {
//            return Constant.BROADCAST_URL + "&id=" + voaId + "&shuoshuo=" + path+ "&apptype="
//                    + Constant.APP_TYPE;
//        }
        return url;
    }

    @Override
    public String getShareImageUrl() {
        return imgUrl;
    }

    @Override
    public String getShareAudioUrl() {
        return "";
    }

    @Override
    public String getShareTitle() {
        return "播音员:" + username + "。标题:" + titleCn;
    }

    @Override
    public String getShareLongText() {
        return getShareShortText();
    }

    @Override
    public String getShareShortText() {
        return "@爱语吧 " + title + ":" + description;
    }
}
