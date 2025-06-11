package com.ai.bbcpro.word;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.ai.bbcpro.R;

import java.util.ArrayList;

/**
 * Created by yq on 2017/2/24.
 */

public class Cet4WordRoot extends AppCompatActivity {

    private Button backBtn;
    private ArrayList<Cet4Word> rootWords;
    private ArrayList<Cet4Word> wordList;
    private Cet4WordOp wordOp;
    private ListView listView;
    private Cet4RootListAdapter adapter;
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabularyroot);
        backBtn = (Button) findViewById(R.id.button_back);
        mContext = this;
        backBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();

        int groupflg = intent.getIntExtra("rootWord", -1);
        wordOp = new Cet4WordOp(mContext);
        rootWords = wordOp.findWordByRoot(groupflg);
        listView = (ListView) findViewById(R.id.list);
        adapter = new Cet4RootListAdapter();
        adapter.setList(rootWords);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(mContext, Cet4WordContent.class);
                WordDataManager.Instance().pos = position;
                WordDataManager.Instance().words = rootWords;
                startActivity(intent);

            }
        });

    }

    class Cet4RootListAdapter extends BaseAdapter {


        private ArrayList<Cet4Word> list;

        public void setList(ArrayList<Cet4Word> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            final ViewHolder curViewHolder;
            final Cet4Word word = list.get(i);
            if (convertView == null) {
                curViewHolder = new ViewHolder();
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.item_cet_word, null);
                curViewHolder.word = (TextView) convertView.findViewById(R.id.word);
                curViewHolder.pron = (TextView) convertView.findViewById(R.id.pron);
                curViewHolder.desc = (TextView) convertView.findViewById(R.id.desc);
                curViewHolder.search = (TextView) convertView.findViewById(R.id.search);
                convertView.setTag(curViewHolder);
            } else {
                curViewHolder = (ViewHolder) convertView.getTag();
            }
            curViewHolder.word.setText(word.word);
            curViewHolder.pron.setText(word.pron);
            curViewHolder.desc.setText(word.def);
            curViewHolder.search.setVisibility(View.GONE);
            return convertView;
        }

    }

    public class ViewHolder {
        TextView word, pron;
        TextView desc, search;
    }

}

