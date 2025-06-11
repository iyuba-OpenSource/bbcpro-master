package com.ai.bbcpro.util.popup;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;


import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.home.WordSentAdapter;
import com.ai.bbcpro.databinding.PopupApiWordBinding;
import com.ai.bbcpro.model.bean.home.ApiWordBean;
import com.ai.bbcpro.util.decoration.LineItemDecoration;

import java.io.IOException;
import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

public class ApiWordPopup extends BasePopupWindow {

    private PopupApiWordBinding popupApiWordBinding;

    private WordSentAdapter wordSentAdapter;

    private MediaPlayer mediaPlayer;

    private ApiWordBean word;

    private Callback callback;

    private LineItemDecoration lineItemDecoration;

    public ApiWordPopup(Context context) {
        super(context);
        View view = createPopupById(R.layout.popup_api_word);
        popupApiWordBinding = PopupApiWordBinding.bind(view);
        setContentView(view);

        initMediaPlayer();

        lineItemDecoration = new LineItemDecoration(context, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(context.getDrawable(R.drawable.space_10dp));

        popupApiWordBinding.awRvList.addItemDecoration(lineItemDecoration);

        popupApiWordBinding.awRvList.setLayoutManager(new LinearLayoutManager(getContext()));

        wordSentAdapter = new WordSentAdapter(R.layout.item_word_sent, new ArrayList<>());
        popupApiWordBinding.awRvList.setAdapter(wordSentAdapter);

        popupApiWordBinding.awIvAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (word == null) {

                    return;
                }

                mediaPlayer.reset();
                try {
                    mediaPlayer.setDataSource(word.getAudio());
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        popupApiWordBinding.awTvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callback != null) {

                    callback.add();
                }
            }
        });
        popupApiWordBinding.awTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (callback != null) {

                    callback.cancel();
                }
            }
        });
    }


    private void initMediaPlayer() {

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                mediaPlayer.start();
            }
        });
    }

    public ApiWordBean getWord() {
        return word;
    }

    public void setWord(ApiWordBean word) {

        this.word = word;
        if (word.getPron() == null) {

            popupApiWordBinding.awTvContent.setText(word.getKey());
        } else {

            popupApiWordBinding.awTvContent.setText(word.getKey() + " [" + word.getPron() + "]");
        }
        //解释
        popupApiWordBinding.awTvDef.setText(word.getDef());
        //例句
        wordSentAdapter.setNewData(word.getSent());
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void add();

        void cancel();
    }
}
