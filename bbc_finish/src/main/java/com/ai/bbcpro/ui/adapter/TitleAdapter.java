package com.ai.bbcpro.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class TitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<SumBean.DataDTO> data;
    private Context context;
    private static final int VIEW_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private static final int TYPE_FINISH = -1;  //完成FootView
    private int lastVisibleItemPosition;
    private MyProgressViewHolder mpHolder;
    private int totalNum = 1000;
    private RecyclerView.LayoutParams param;
    ILoadMoreData iLoadMoreData;
    ItemClickCallback callback;
    public void setCallback(ItemClickCallback callback) {
        this.callback = callback;
    }

    public TitleAdapter(List<SumBean.DataDTO>  data, Context context, RecyclerView recyclerView, final ILoadMoreData iLoadMoreData) {
        this.data = data;
        this.context = context;
        this.iLoadMoreData =iLoadMoreData;
//        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState ==RecyclerView.SCROLL_STATE_IDLE &&
//                        lastVisibleItemPosition + 1 == getItemCount()){
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            iLoadMoreData.loadMoreData();
//                        }
//                    },500);
//                }
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                lastVisibleItemPosition =linearLayoutManager.findLastVisibleItemPosition();
//            }
//        });
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.item_samll_for_list, parent, false);
            return new TitleHolder(view);
//        }else if (viewType == TYPE_FOOTER) {
//            View view = LayoutInflater.from(context).inflate(R.layout.item_swipe_footer, parent, false);
//            mpHolder = new MyProgressViewHolder(view);
//            return mpHolder;
//        }else{
//            View view = LayoutInflater.from(context).inflate(R.layout.item_swipe_footer, parent, false);
//            mpHolder = new MyProgressViewHolder(view);
//            param = (RecyclerView.LayoutParams) view.getLayoutParams();
//            view.setVisibility(View.GONE);
//            param.height = 0;
//            param.width = 0;
//            view.setLayoutParams(param);
//            return mpHolder;
//        }
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        if (position+1 != getItemCount()){
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.dto = data.get(position);
            titleHolder.title.setText(data.get(position).title_cn);
            titleHolder.count.setText(data.get(position).readCount+"人浏览");
            titleHolder.time.setText(data.get(position).creatTime);
//            RequestOptions options = RequestOptions.bitmapTransform(new RoundedCorners(100));
            Glide.with(context).load(data.get(position).pic).into(titleHolder.titleImage);
            titleHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iLoadMoreData.myOnClick(data.get(position).sound,data.get(position).bbcId,data.get(position).title_cn,data.get(position).pic);
                }
            });

//        }
    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }else {
            return data.size();
        }
    }



    @Override
    public int getItemViewType(int position) {
        if(position + 1 != getItemCount()){
            return VIEW_ITEM;
        }else{
            if(getItemCount()-1 ==totalNum){
                return TYPE_FINISH;
            }else{
                return TYPE_FOOTER;
            }
        }
    }

    public class TitleHolder extends RecyclerView.ViewHolder {
        TextView title,count,time;
        ImageView titleImage;
        View itemLayout;
        SumBean.DataDTO dto;


        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.iv_headlines);
            title = itemView.findViewById(R.id.title_tv);
            count = itemView.findViewById(R.id.tv_view_count);
            time = itemView.findViewById(R.id.text_category);
            itemLayout = itemView.findViewById(R.id.linear_container);
        }


    }

    class MyProgressViewHolder extends RecyclerView.ViewHolder {
        public MyProgressViewHolder(View view) {
            super(view);
        }
    }
}
