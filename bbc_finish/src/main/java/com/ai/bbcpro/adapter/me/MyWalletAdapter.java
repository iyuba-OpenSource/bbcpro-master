package com.ai.bbcpro.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.ai.bbcpro.model.bean.me.RewardBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyWalletAdapter extends BaseQuickAdapter<RewardBean.DataDTO, BaseViewHolder> {


    private DecimalFormat decimalFormat;

    private SimpleDateFormat simpleDateFormat;

    private SimpleDateFormat hourSimpleDateFormat;

    private SimpleDateFormat sdf;

    public MyWalletAdapter(int layoutResId, @Nullable List<RewardBean.DataDTO> data) {
        super(layoutResId, data);
        decimalFormat = new DecimalFormat("#.##");
        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();

        simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        sdf = new SimpleDateFormat("yyyyMMdd");

        hourSimpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        hourSimpleDateFormat.applyPattern("HH:mm");
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, RewardBean.DataDTO item) {

        helper.setText(R.id.mywallet_tv_type, item.getType());
        float s = Integer.parseInt(item.getScore()) / 100.0f;
        helper.setText(R.id.mywallet_tv_money, decimalFormat.format(s) + "元");
        Date date;
        try {
            date = simpleDateFormat.parse(item.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        boolean isToday = sdf.format(new Date()).equals(sdf.format(date));
        if (isToday) {

            helper.setText(R.id.mywallet_tv_date, hourSimpleDateFormat.format(date));
        } else {

            helper.setText(R.id.mywallet_tv_date, simpleDateFormat.format(date));
        }

    }
}
