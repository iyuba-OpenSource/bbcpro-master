package com.ai.bbcpro.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.ai.bbcpro.ui.utils.FileUtils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.refactor.lib.colordialog.ColorDialog;

public class DownloadListAdapter extends RecyclerView.Adapter<DownloadListAdapter.DownloadViewHolder> {
    List<SumBean.DataDTO> dataList = new ArrayList<>();
    Context mContext;

    private Callback callback;


    public List<SumBean.DataDTO> getDataList() {
        return dataList;
    }

    public void setDataList(List<SumBean.DataDTO> dataList) {
        this.dataList = dataList;
    }

    public DownloadListAdapter(List<SumBean.DataDTO> dataList, Context mContext) {
        this.dataList = dataList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.collection_article_item, null);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        SumBean.DataDTO dto = dataList.get(position);
        holder.tvTitle.setText(dto.title_cn);
        holder.tvCate.setText(getCateName(Integer.parseInt(dto.category)));
        holder.tvTime.setText(dto.creatTime.substring(0, 10));
        Glide.with(mContext).load(dto.getPic()).into(holder.iv);

    }

    private String getCateName(int cateNum) {

        String[] array = mContext.getResources().getStringArray(R.array.bbc_category);
        int[] numbers = mContext.getResources().getIntArray(R.array.bbc_category_number);
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < array.length; i++) {
            map.put(numbers[i], array[i]);
        }
        return map.get(cateNum);
    }

    private void showTip(int position) {
        ColorDialog colorDialog = new ColorDialog(mContext);
        colorDialog.setTitle("删除下载");
        colorDialog.setContentText("是否删除该下载?");
        colorDialog.setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        colorDialog.setPositiveListener("删除", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                SyncDataHelper.getInstance(mContext).deleteDownload(position);
                FileUtils.deleteSingleFile(mContext.getExternalFilesDir(Environment.DIRECTORY_MUSIC) + "/" + dataList.get(position).bbcId + ".mp3");
                dataList.remove(position);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        colorDialog.show();
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout ly;
        ImageView iv, delete;
        TextView tvTitle;
        TextView tvTime;
        TextView tvCate;

        public DownloadViewHolder(@NonNull View view) {
            super(view);

            iv = view.findViewById(R.id.iv_collection);
            tvTitle = view.findViewById(R.id.tv_collection_title);
            tvTime = view.findViewById(R.id.tv_collection_time);
            tvCate = view.findViewById(R.id.tv_collection_cate);
            ly = view.findViewById(R.id.ly_collection);
            delete = view.findViewById(R.id.delete_collection);

            ly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*                    Intent intent = new Intent(mContext, AudioContentActivity.class);
                    intent.putExtra("sound", dto.sound);
                    intent.putExtra("bbcid", dto.bbcId);
                    intent.putExtra("title_cn", dto.title_cn);
                    intent.putExtra("image", dto.pic);
                    mContext.startActivity(intent);*/
                    if (callback != null) {

                        callback.jumpAudioContent(getBindingAdapterPosition());
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showTip(getBindingAdapterPosition());
                }
            });
        }
    }


    public interface Callback {

        void jumpAudioContent(int position);
    }
}
