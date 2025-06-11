package com.ai.bbcpro.ui.adapter;

public interface ILoadMoreData {
    void loadMoreData();

    void myOnClick(String sound, String bbcid, String title, String imageUrl);

    void clickForPosition(int position);

}
