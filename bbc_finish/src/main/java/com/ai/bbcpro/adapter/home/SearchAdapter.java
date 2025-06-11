package com.ai.bbcpro.adapter.home;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.view.BaseView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.widget.rpb.RoundProgressBar;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

public class SearchAdapter extends BaseQuickAdapter<SearchBean.TitleDataDTO, BaseViewHolder> {

    private boolean isPlay = false;

    private boolean isRecord = false;

    //分贝
    private double db;

    /**
     * 正在操作的数据位置
     */
    private int position = -1;

    public SearchAdapter(int layoutResId, @Nullable List<SearchBean.TitleDataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SearchBean.TitleDataDTO titleDataDTO) {

        baseViewHolder.setGone(R.id.search_ll_word, false);
        baseViewHolder.setGone(R.id.search_ll_text, false);
        baseViewHolder.setGone(R.id.search_ll_essay, false);
        baseViewHolder.setGone(R.id.search_ll_name, false);
        int flag = titleDataDTO.getFlag();
        if (flag == 1) {//单词

            baseViewHolder.setGone(R.id.search_ll_word, true);
            try {
                if (titleDataDTO.getPh_en().equals("")) {

                    baseViewHolder.setText(R.id.search_tv_word, titleDataDTO.getWord());
                } else {
                    baseViewHolder.setText(R.id.search_tv_word, titleDataDTO.getWord()
                            + " [" + URLDecoder.decode(titleDataDTO.getPh_en(), "utf-8") + "]");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            baseViewHolder.setText(R.id.search_tv_def, titleDataDTO.getDef());
            if (titleDataDTO.getPhAmMp3().equals("")) {

                baseViewHolder.setGone(R.id.search_iv_sound, false);
            } else {
                baseViewHolder.setGone(R.id.search_iv_sound, true);
            }
            baseViewHolder.addOnClickListener(R.id.search_iv_sound);
        } else if (flag == 2) {//句子

            baseViewHolder.setGone(R.id.search_ll_text, true);
            baseViewHolder.setText(R.id.searcj_tv_sentence, titleDataDTO.getSentence());
            baseViewHolder.setText(R.id.searcj_tv_sentence_cn, titleDataDTO.getSentenceCn());
            baseViewHolder.addOnClickListener(R.id.search_iv_play);
            baseViewHolder.addOnClickListener(R.id.search_iv_record);
            baseViewHolder.addOnClickListener(R.id.search_iv_collect);

            if (titleDataDTO.getCollect() == 0) {

                baseViewHolder.setImageResource(R.id.search_iv_collect, R.drawable.icon_uncollect);
            } else {

                baseViewHolder.setImageResource(R.id.search_iv_collect, R.drawable.icon_collect);
            }

        } else if (flag == 3) {//文章

            baseViewHolder.setGone(R.id.search_ll_essay, true);
            ImageView search_iv_pic = baseViewHolder.getView(R.id.search_iv_pic);
            Glide.with(search_iv_pic.getContext()).load(titleDataDTO.getPic()).into(search_iv_pic);
            baseViewHolder.setText(R.id.search_tv_title, titleDataDTO.getTitle());
            baseViewHolder.setText(R.id.search_tv_title_cn, titleDataDTO.getTitleCn());
            baseViewHolder.addOnClickListener(R.id.search_ll_essay);
        } else if (flag == 103) {//标题  文章

            baseViewHolder.setGone(R.id.search_ll_name, true);
            baseViewHolder.setText(R.id.search_tv_name, "文章");
            baseViewHolder.setGone(R.id.search_tv_more, true);
            baseViewHolder.addOnClickListener(R.id.search_tv_more);
        } else if (flag == 101) {//单词

            baseViewHolder.setGone(R.id.search_ll_name, true);
            baseViewHolder.setText(R.id.search_tv_name, "单词");
            baseViewHolder.setGone(R.id.search_tv_more, false);
        } else if (flag == 102) {//句子

            baseViewHolder.setGone(R.id.search_ll_name, true);
            baseViewHolder.setText(R.id.search_tv_name, "句子");
            baseViewHolder.setGone(R.id.search_tv_more, true);
            baseViewHolder.addOnClickListener(R.id.search_tv_more);
        }

        RoundProgressBar roundProgressBar = baseViewHolder.getView(R.id.search_iv_play);
        //设置播放按钮的状态
        if (isPlay && position == baseViewHolder.getBindingAdapterPosition()) {

            roundProgressBar.setBackgroundResource(R.drawable.ic_play_line);
        } else {

            roundProgressBar.setBackgroundResource(R.drawable.ic_pause_line);
        }
        //设置录音按钮
        RoundProgressBar search_iv_record = baseViewHolder.getView(R.id.search_iv_record);
        if (isRecord && position == baseViewHolder.getBindingAdapterPosition()) {

            search_iv_record.setProgress((int) db);
        } else {

            search_iv_record.setProgress(0);
        }
        //评分
        if (titleDataDTO.getScore() == -1) {

            baseViewHolder.setVisible(R.id.search_tv_score, false);
        } else {

            baseViewHolder.setVisible(R.id.search_tv_score, true);
            baseViewHolder.setText(R.id.search_tv_score, titleDataDTO.getScore() + "");
        }
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isRecord() {
        return isRecord;
    }

    public void setRecord(boolean record) {
        isRecord = record;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    public double getDb() {
        return db;
    }

    public void setDb(double db) {
        this.db = db;
    }
}
