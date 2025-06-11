package com.ai.bbcpro.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.ui.bean.TotalContentBean;
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
//import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class TotalContentRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //底部FootView
    private static final int TYPE_FINISH = -1;  //完成FootView
    private int lastVisibleItemPosition;
    private List<TotalContentBean.DataBean> data;
    private ILoadMoreData iLoadMoreData;
    private Context context;
    private int totalNum = 5000;
    private int pageCount;
    private MyProgressViewHolder mpHolder;
    private RecyclerView.LayoutParams params;

    public TotalContentRvAdapter(Context context,List<TotalContentBean.DataBean> data,RecyclerView recyclerView) {
        this.data = data;
        this.context = context;
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition+1 == getItemCount()){
                    iLoadMoreData.loadMoreData();

                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (position +1 != getItemCount()){
            return VIEW_ITEM;
        }else{
            if (getItemCount()-1 == totalNum){
                return TYPE_FINISH;
            }else{
                return TYPE_FOOTER;
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_ITEM){
            View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            return new ItemHolder(view);
        }else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_swipe_footer, parent, false);
            mpHolder = new MyProgressViewHolder(view);
            return mpHolder;
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_swipe_footer, parent, false);
            mpHolder = new MyProgressViewHolder(view);
            params = (RecyclerView.LayoutParams)view.getLayoutParams();
            view.setVisibility(View.GONE);
            params.height = 0;
            params.width = 0;
            view.setLayoutParams(params);
            return mpHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position+1 != getItemCount()){
            ItemHolder holder1 = (ItemHolder) holder;
            holder1.title.setText(data.get(position).getTitle());
            holder1.count.setText(data.get(position).getReadCount()+"浏览");
            holder1.time.setText(data.get(position).getCreatTime());

//            Glide.with(context).load(data.get(position).getPic()).into(((ItemHolder) holder).titleImage);
            holder1.titleImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title,count,time;
        ImageView titleImage;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            titleImage = itemView.findViewById(R.id.titleImage);
            title = itemView.findViewById(R.id.title);
            count = itemView.findViewById(R.id.count);
            time = itemView.findViewById(R.id.time);
        }

    }

    class MyProgressViewHolder extends RecyclerView.ViewHolder {
        public MyProgressViewHolder(View view) {
            super(view);
        }
    }
}
