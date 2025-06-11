package com.ai.bbcpro.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ai.bbcpro.R;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.bumptech.glide.Glide;

import java.util.List;

public class RankDetailAdapter extends RecyclerView.Adapter<RankDetailAdapter.ViewHolder> implements View.OnClickListener {
    private String TAG = "RankDetailAdapter";
    private final Context context;
    private List<RankPersonalItem> dataList;
    private final String name;
    private final String head;
    private HeadlinesDataManager dataManager;


    public RankDetailAdapter(Context context, List<RankPersonalItem> dataList, String name, String head) {
        this.context = context;
        this.dataList = dataList;
        Log.e(TAG, "RankDetailAdapter: " + dataList.size());
        this.name = name;
        this.head = head;
        dataManager = HeadlinesDataManager.getInstance(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rank_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankPersonalItem data = dataList.get(position);
        holder.ivZan.setTag(position);
        holder.ivPlayImg.setTag(position);

        holder.rank_detail_tv_zan.setText(data.getAgreeCount() + "");

        holder.tvScore.setText(data.getScore() + "分");
        holder.tvDate.setText(data.getCreateDate().substring(0, 10));

        if (data.getShuoshuotype() == 4) {
            holder.tvParaId.setText("合成");
        } else if (data.getShuoshuotype() == 3) {
            holder.tvParaId.setText("配音");
        } else {
            // holder.tvParaId.setText(String.valueOf(data.getParaId()));
            holder.tvParaId.setText("单句");
        }
        String sen = dataManager.loadSentenceByIdParaIndex(data.getTopicId(), data.getParaid(), data.getIdIndex());
        holder.tvSen.setText(sen);
        Glide.with(context).load(head).into(holder.ivHead);
        boolean zanFlag = data.isHadZan();
        boolean playingFlag = data.isPlaying();

        if (zanFlag) {
            holder.ivZan.setImageResource(R.mipmap.zan_1);
        } else {
            holder.ivZan.setImageResource(R.mipmap.zan);
        }

        if (playingFlag) {
            holder.ivPlayImg.setImageResource(R.drawable.play_animation);
            AnimationDrawable animationDrawable = (AnimationDrawable) (holder.ivPlayImg).getDrawable();
            animationDrawable.start();//播放动画
        } else {
            holder.ivPlayImg.setImageResource(R.mipmap.rec3);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    //自定义一个回调接口来实现Click和LongClick事件
    public interface OnItemClickListener {
        void onItemClick(View v, String viewName, int position);
    }

    //声明自定义的接口
    private OnItemClickListener mOnItemClickListener;

    //定义方法并传给外面的使用者
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataList(List<RankPersonalItem> dataList, int position) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        int position = (int) view.getTag();

        if (mOnItemClickListener != null) {
            int id = view.getId();
            if (id == R.id.rank_detail_play_img) {
                mOnItemClickListener.onItemClick(view, "0", position);
            } else if (id == R.id.rank_detail_zan) {
                mOnItemClickListener.onItemClick(view, "1", position);
            }
        }
    }

    public List<RankPersonalItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<RankPersonalItem> dataList) {
        this.dataList = dataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivHead;
        ImageView ivPlayImg;
        ImageView ivZan;
        TextView tvName;
        TextView tvDate;
        TextView tvParaId;
        TextView tvSen;
        TextView tvScore;

        TextView rank_detail_tv_zan;

        public ViewHolder(View view) {
            super(view);
            ivHead = view.findViewById(R.id.rank_detail_head);
            ivPlayImg = view.findViewById(R.id.rank_detail_play_img);
            ivZan = view.findViewById(R.id.rank_detail_zan);
            tvName = view.findViewById(R.id.rank_detail_name);
            tvDate = view.findViewById(R.id.rank_detail_date);
            tvParaId = view.findViewById(R.id.rank_detail_para);
            tvSen = view.findViewById(R.id.rank_detail_sen);
            tvScore = view.findViewById(R.id.rank_detail_score);
            tvName.setText(name);
            rank_detail_tv_zan = view.findViewById(R.id.rank_detail_tv_zan);

            ivPlayImg.setOnClickListener(RankDetailAdapter.this);
            ivZan.setOnClickListener(RankDetailAdapter.this);
        }
    }

}
