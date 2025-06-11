package com.ai.bbcpro.adapter.home;

import android.text.Html;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.ai.bbcpro.model.bean.home.ApiWordBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class WordSentAdapter extends BaseQuickAdapter<ApiWordBean.SentBean, BaseViewHolder> {

    public WordSentAdapter(int layoutResId, @Nullable List<ApiWordBean.SentBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, ApiWordBean.SentBean sentBean) {

        TextView ws_tv_orig = baseViewHolder.getView(R.id.ws_tv_orig);
        ws_tv_orig.setText(Html.fromHtml(sentBean.getOrig()));
        baseViewHolder.setText(R.id.ws_tv_trans, sentBean.getTrans());
    }
}
