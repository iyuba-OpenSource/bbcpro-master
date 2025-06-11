package com.ai.bbcpro.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.ai.bbcpro.R;

public class WaittingDialog {

    public static CustomDialog showDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.waitting_dialog, null);
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);

        CustomDialog cDialog = customBuilder.setContentView(layout).create();

        cDialog.setCanceledOnTouchOutside(false);
        return cDialog;
    }
}
