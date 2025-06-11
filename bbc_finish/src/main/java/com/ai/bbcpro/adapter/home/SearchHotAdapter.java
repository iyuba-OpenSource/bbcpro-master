package com.ai.bbcpro.adapter.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class SearchHotAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SearchHotAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {

        baseViewHolder.setText(R.id.search_tv_title, s);
    }
}
