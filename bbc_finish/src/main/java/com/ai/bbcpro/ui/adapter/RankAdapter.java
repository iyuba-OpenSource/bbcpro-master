package com.ai.bbcpro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.ui.player.rank.RankContentResponse;
import com.ai.bbcpro.ui.player.rank.RankPersonalActivity;
import com.bumptech.glide.Glide;


public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankItemViewHolder> implements View.OnClickListener{
    private Context mContext;
    private RankContentResponse mResponse;
    private String TAG = "RankAdapter";
    private int bbcId;

    public RankAdapter(Context mContext, RankContentResponse response,int bbcId) {
        this.mContext = mContext;
        this.mResponse = response;
        this.bbcId = bbcId;
    }

    @NonNull
    @Override
    public RankItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_rank, parent, false);
        return new RankItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull RankItemViewHolder holder, int position) {
        RankContentResponse.DataBean data = mResponse.getData().get(position);
        holder.ly.setTag(position);
        if (data.getRanking() == 1){
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankIndex.setVisibility(View.VISIBLE);
            holder.ivRankIndex.setImageResource(R.drawable.rank_fir);
        }else if(data.getRanking() == 2){
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankIndex.setVisibility(View.VISIBLE);
            holder.ivRankIndex.setImageResource(R.drawable.rank_sec);
        }else if(data.getRanking() == 3){
            holder.tvRank.setVisibility(View.GONE);
            holder.ivRankIndex.setVisibility(View.VISIBLE);
            holder.ivRankIndex.setImageResource(R.drawable.rank_thr);
        } else{
            holder.tvRank.setText(String.valueOf(data.getRanking()));
        }
        if (data.getRanking() == mResponse.getMyranking()){
            holder.ly.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        holder.tvName.setText(data.getName());
        holder.tvCount.setText("句子数" + data.getCount());
        holder.tvScore.setText(data.getScores() + "分");

        if (data.getCount() == 0) {
            holder.tvAvr.setText("平均分: 0");
        }else {
            holder.tvAvr.setText("平均分: " + (int) Math.ceil(data.getScores() / data.getCount()));
        }
        Glide.with(mContext).load(data.getImgSrc()).into(holder.ivHead);
    }

    @Override
    public int getItemCount() {
        return mResponse.getResult();
    }

    @Override
    public void onClick(View v) {
        int position = (int)v.getTag();
        switch (v.getId()){
            case R.id.eval_ly:
                int userId = mResponse.getData().get(position).getUid();
                String name = mResponse.getData().get(position).getName();
                String head = mResponse.getData().get(position).getImgSrc();
                Intent intent = new Intent(mContext, RankPersonalActivity.class);
                Log.e(TAG, "onClick: "+userId );
                intent.putExtra("bbcId",bbcId);
                intent.putExtra("uid",userId);
                intent.putExtra("name",name);
                intent.putExtra("head",head);
                mContext.startActivity(intent);
                break;
        }
    }

    public class RankItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ly;
        TextView tvRank,tvName,tvCount,tvAvr,tvScore;
        ImageView ivHead,ivRankIndex;
        public RankItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.rank_index);
            tvName = itemView.findViewById(R.id.rank_name);
            tvCount = itemView.findViewById(R.id.rank_count);
            tvAvr = itemView.findViewById(R.id.rank_avr);
            tvScore = itemView.findViewById(R.id.rank_score);
            ivHead = itemView.findViewById(R.id.rank_head);
            ivRankIndex = itemView.findViewById(R.id.rank_index_iv);
            ly = itemView.findViewById(R.id.eval_ly);
            ly.setOnClickListener(RankAdapter.this);
        }
    }
    
}
