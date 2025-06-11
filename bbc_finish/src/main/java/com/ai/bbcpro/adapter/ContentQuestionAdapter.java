package com.ai.bbcpro.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ContentQuestionAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    private int postion = -1;

    private boolean isCheck = true;

    public ContentQuestionAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {


        if (baseViewHolder.getAdapterPosition() == postion) {

            baseViewHolder.setChecked(R.id.cq_cb_check, true);
        } else {

            baseViewHolder.setChecked(R.id.cq_cb_check, false);
        }
        String poStr;
        if (baseViewHolder.getAdapterPosition() == 0) {

            poStr = "A.";
        } else if (baseViewHolder.getAdapterPosition() == 1) {

            poStr = "B.";
        } else if (baseViewHolder.getAdapterPosition() == 2) {

            poStr = "C.";
        } else {

            poStr = "D.";
        }

        baseViewHolder.setText(R.id.cq_cb_check, poStr + s);
    }

    public int getPostion() {
        return postion;
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
