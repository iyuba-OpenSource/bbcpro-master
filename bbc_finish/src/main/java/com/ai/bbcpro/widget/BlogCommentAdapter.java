package com.ai.bbcpro.widget;


import java.util.ArrayList;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;


public class BlogCommentAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<BlogComment> mList = new ArrayList<BlogComment>();
    private String uid;
    private int type;

    public BlogCommentAdapter(Context mContext, int type) {
        this.mContext = mContext;
        if (AccountManager.Instance(mContext).checkUserLogin()) {
            uid = AccountManager.Instance(mContext).userId;
        } else {
            uid = "0";
        }
        this.type = type;
    }

    public BlogCommentAdapter(Context mContext, ArrayList<BlogComment> mList,
                              int type) {
        this.mContext = mContext;
        this.mList = mList;
        if (AccountManager.Instance(mContext).checkUserLogin()) {
            uid = AccountManager.Instance(mContext).userId;
        } else {
            uid = "0";
        }
        this.type = type;
    }

    public void setData(ArrayList<BlogComment> Comments) {
        mList = Comments;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BlogComment curItem = mList.get(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.blogcomment_item, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.comment_image);
            viewHolder.body = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder.name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.comment_time);
            viewHolder.comment = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(curItem.author);
        viewHolder.time.setText(DateFormat.format(
                "yyyy-MM-dd kk:mm:ss",
                Long.parseLong(curItem.dateline) * 1000));
        viewHolder.body.setText(curItem.message);

        Glide.with(mContext).load("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=10005&uid="
                + mList.get(position).authorid + "&size=middle")
                .placeholder(R.drawable.defaultavatar)
                .into(viewHolder.image);

//        ImageLoader.getInstance().displayImage("", viewHolder.image);

//		Log.d("ImageLoader:", "http://api.iyuba.com.cn/v2/api.iyuba?protocol=10005&uid="
//		+ mList.get(position).authorid + "&size=middle");

//		GitHubImageLoader.Instance(mContext).setCireclePic( mList.get(position).authorid+"",
//				"middle",viewHolder.image);

        return convertView;
    }

    public class ViewHolder {
        ImageView image;// 头像图片
        TextView name; // 用户名
        TextView time; // 发布时间
        TextView body; // 评论体
        View comment; // 整体
    }
}

