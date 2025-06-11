package com.ai.bbcpro.word;


import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.setting.SettingConfig;


/**
 * 查单词界面
 *
 * @author chentong
 * @version 1.0
 */
public class SearchWordActivity extends AppCompatActivity {
    private Context mContext;
    private ImageButton backBtn;
    private ListView wordList;
    private WordListAdapter wordListAdapter;
    private EditText searchText;
    private Button search;
    private View clear;
    private ArrayList<Word> words = new ArrayList<Word>();
    private WordDBOp wOp;
    private String tempWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_word);
        mContext = this;
//        CrashApplication.getInstance().addActivity(this);
        wOp = new WordDBOp(mContext);
        initWidget();
    }

    /**
     *
     */
    private void initWidget() {

        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        searchText = findViewById(R.id.search_text);
        clear = findViewById(R.id.clear);
        search = findViewById(R.id.search);
        searchText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchText.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                searchAppointWord(v.getText().toString());
                searchText.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                return true;
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {


            }

            @Override
            public void afterTextChanged(Editable arg0) {

                tempWord = arg0.toString();
                fuzzySearchWord(tempWord);
            }
        });
        clear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                searchText.setText("");
                handler.sendEmptyMessage(0);
            }
        });
        search.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                String word = searchText.getText().toString();
                if (!word.equals("") && word.length() != 0) {
                    searchAppointWord(word);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        });
        wordList = findViewById(R.id.word_list);
        wordList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                searchAppointWord(words.get(arg2).key);
            }
        });
        handler.sendEmptyMessage(0);
    }

    /**
     * @param word 根据String模糊查询
     */
    private void fuzzySearchWord(String word) {

        handler.sendEmptyMessage(1);
    }

    /**
     * @param word 根据String精确查询 同时跳转内容界面
     */
    private void searchAppointWord(String word) {

        wOp.updateWord(word);
        Intent intent = new Intent(mContext, WordContentActivity.class);
        intent.putExtra("word", word);
        SettingConfig.Instance().setIsCaChe(false);
        startActivity(intent);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    words = wOp.findDataByView();
                    if (wordListAdapter == null) {
                        wordListAdapter = new WordListAdapter(mContext, words);
                        wordListAdapter.setModeDelete(false);
                        wordList.setAdapter(wordListAdapter);
                    } else {
                        wordListAdapter.setData(words);
                        wordListAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
                    words = wOp.findDataByFuzzy(tempWord);
                    wordListAdapter.setData(words);
                    wordListAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.word_null);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(0);
    }

}

