package com.ai.bbcpro.ui.function;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ai.bbcpro.R;
import com.iyuba.module.user.IyuUserManager;

public class MoreFunctionDialog implements MoreInterface {
    private MoreInterface moreInterface;
    private Context mContext;
    private SeekBar seekBar;
    private ImageView plus, minus;
    private AlertDialog moreDialog;

    public MoreFunctionDialog(MoreInterface moreInterface, Context mContext) {
        this.moreInterface = moreInterface;
        this.mContext = mContext;
    }

    public void initDialog() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.more_function_dialog, null);
        moreDialog = (new AlertDialog.Builder(mContext,R.style.MyDialogStyle)).setView(view).create();
        moreDialog.getWindow().setWindowAnimations(R.style.HeadlineDialogAnimal);
        seekBar = view.findViewById(R.id.seekbar_headline_dialog);
        plus = view.findViewById(R.id.iv_plus);
        minus = view.findViewById(R.id.iv_minus);
        initSeekBar();
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInterface.itemOnclick(seekBar.getProgress() + 5 - 2);
                seekBar.setProgress(seekBar.getProgress() - 2);
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreInterface.itemOnclick(seekBar.getProgress() + 5 + 2);
                seekBar.setProgress(seekBar.getProgress() + 2);
            }
        });


    }

    public void showMoreDialog() {
        this.moreDialog.show();
        this.moreDialog.getWindow().setGravity(80);
        this.moreDialog.getWindow().setLayout(-1, -2);
    }

    private void initSeekBar() {
        this.seekBar.setMax(21);
        this.seekBar.setProgress(5);
        this.seekBar.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        this.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moreInterface.itemOnclick(seekBar.getProgress() + 5);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }


    @Override
    public void itemOnclick(int speed) {
        this.moreInterface.itemOnclick(speed);
        moreDialog.dismiss();
    }
}
