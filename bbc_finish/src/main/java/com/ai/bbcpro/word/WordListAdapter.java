package com.ai.bbcpro.word;


import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.player.Player;


/**
 * 单词列表适配器NewSectionAactivity
 *
 * @author 陈彤
 * @version 1.0
 */

public class WordListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Word> mList = new ArrayList<>();
    public boolean modeDelete = false;

    private ClickCallback clickCallback;


    public WordListAdapter(Context context) {
        mContext = context;
    }

    public WordListAdapter(Context context, ArrayList<Word> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public void setData(ArrayList<Word> list) {
        mList = list;
    }


    public ClickCallback getClickCallback() {
        return clickCallback;
    }

    public void setClickCallback(ClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    public ArrayList<Word> getmList() {
        return mList;
    }

    public void setmList(ArrayList<Word> mList) {
        this.mList = mList;
    }


    public boolean isModeDelete() {
        return modeDelete;
    }

    public void setModeDelete(boolean modeDelete) {
        this.modeDelete = modeDelete;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        Word word = mList.get(position);
        if (convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
            viewHolder = new ViewHolder(convertView);
            viewHolder.setWord(word);
            viewHolder.setClickCallback(clickCallback);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setWord(word);
        if (modeDelete) {
            viewHolder.deleteBox.setVisibility(View.VISIBLE);
            viewHolder.word_tv_del.setVisibility(View.VISIBLE);
        } else {
            viewHolder.deleteBox.setVisibility(View.GONE);
            viewHolder.word_tv_del.setVisibility(View.GONE);
        }
        if (word.isDelete) {
            viewHolder.deleteBox.setImageResource(R.drawable.check_box_checked);
        } else {
            viewHolder.deleteBox.setImageResource(R.drawable.check_box);
        }
        viewHolder.key.setText(word.key);
        if (word.pron != null) {
            StringBuffer sb = new StringBuffer();
            sb.append('[').append(word.pron).append(']');
            viewHolder.pron.setText(TextAttr.decode(sb.toString()));
        }
        if (!TextUtils.isEmpty(word.def)) {
            viewHolder.def.setText(word.def.replaceAll("\n", ""));
        }
        if (word.audioUrl != null && word.audioUrl.length() != 0) {
            viewHolder.speaker.setVisibility(View.VISIBLE);
        } else {
            viewHolder.speaker.setVisibility(View.INVISIBLE);
        }
        viewHolder.speaker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Player player = new Player(mContext, null);
                String url = word.audioUrl;
                player.playUrl(url);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        TextView key, pron;
        public TextView def;
        ImageView deleteBox;
        ImageView speaker;

        TextView word_tv_del;

        private Word word;

        private ClickCallback clickCallback;

        public ViewHolder(View convertView) {

            key = convertView.findViewById(R.id.word_key);
            pron = convertView
                    .findViewById(R.id.word_pron);
            key.setTextColor(Color.BLACK);
            def = convertView.findViewById(R.id.word_def);

            deleteBox = convertView
                    .findViewById(R.id.checkBox_isDelete);
            speaker = convertView
                    .findViewById(R.id.word_speaker);
            //删除
            word_tv_del = convertView.findViewById(R.id.word_tv_del);
            word_tv_del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (clickCallback != null) {

                        clickCallback.delClick(word);
                    }
                }
            });
        }

        public Word getWord() {
            return word;
        }

        public void setWord(Word word) {
            this.word = word;
        }


        public ClickCallback getClickCallback() {
            return clickCallback;
        }

        public void setClickCallback(ClickCallback clickCallback) {
            this.clickCallback = clickCallback;
        }
    }


    public interface ClickCallback {

        void delClick(Word word);
    }

}

