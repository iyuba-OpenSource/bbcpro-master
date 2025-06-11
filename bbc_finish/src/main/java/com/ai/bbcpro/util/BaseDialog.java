package com.ai.bbcpro.util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.view.View;

import com.ai.bbcpro.R;


public abstract class BaseDialog extends AlertDialog implements View.OnClickListener{

    protected BaseDialog(Context context) {
        super(context, R.style.BaseDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
        initData();
    }
    protected abstract void initView();
    protected abstract void initListener();
    protected abstract void initData();
    protected abstract void processClick(View v);
    @Override
    public void onClick(View v) {
        processClick(v);
    }



}
