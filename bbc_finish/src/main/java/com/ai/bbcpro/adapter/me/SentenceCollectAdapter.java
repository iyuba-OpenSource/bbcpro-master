package com.ai.bbcpro.adapter.me;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ai.bbcpro.R;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.iyuba.widget.rpb.RoundProgressBar;

import java.util.List;

/**
 * 我的收藏  句子适配器
 */
public class SentenceCollectAdapter extends BaseQuickAdapter<SentenceCollectBean.DataDTO, BaseViewHolder> {

    private boolean isPlay = false;

    private boolean isRecord = false;

    //分贝
    private double db;

    /**
     * 正在操作的数据位置
     */
    private int position = -1;

    public SentenceCollectAdapter(int layoutResId, @Nullable List<SentenceCollectBean.DataDTO> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, SentenceCollectBean.DataDTO titleDataDTO) {

        baseViewHolder.setGone(R.id.search_ll_word, false);
        baseViewHolder.setGone(R.id.search_ll_text, false);
        baseViewHolder.setGone(R.id.search_ll_essay, false);
        baseViewHolder.setGone(R.id.search_ll_name, false);

        //句子
        baseViewHolder.setGone(R.id.search_ll_text, true);
        baseViewHolder.setText(R.id.searcj_tv_sentence, titleDataDTO.getSentence());
        baseViewHolder.setText(R.id.searcj_tv_sentence_cn, titleDataDTO.getTitlecn());
        baseViewHolder.addOnClickListener(R.id.search_iv_play);
        baseViewHolder.addOnClickListener(R.id.search_iv_record);
        baseViewHolder.addOnClickListener(R.id.search_tv_collect);


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
