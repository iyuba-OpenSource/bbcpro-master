package com.ai.bbcpro.util.popup;

import android.content.Context;
import android.view.View;


import com.ai.bbcpro.R;
import com.ai.bbcpro.databinding.PopupAnalysisBinding;
import com.ai.bbcpro.word.Word;

import razerdp.basepopup.BasePopupWindow;

public class AnalysisPopup extends BasePopupWindow {

    private PopupAnalysisBinding popupAnalysisBinding;

    public AnalysisPopup(Context context) {
        super(context);
        View view = createPopupById(R.layout.popup_analysis);
        popupAnalysisBinding = PopupAnalysisBinding.bind(view);
        setContentView(view);

        initOperation();
    }

    private void initOperation() {

        popupAnalysisBinding.analysisIvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
    }


    public void setJpWord(Word word) {

        popupAnalysisBinding.analysisTvWord.setText(word.key);
        popupAnalysisBinding.analysisTvPron.setText("[" + word.pron + "]");
        popupAnalysisBinding.analysisTvWordch.setText(word.def);
//        popupAnalysisBinding.analysisTvSentence.setText(word.getSentence());
//        popupAnalysisBinding.analysisTvSentencech.setText(word.getSentenceCh());
    }


}
