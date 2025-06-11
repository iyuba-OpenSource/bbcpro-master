package com.ai.bbcpro.ui.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.appcompat.app.AlertDialog;

import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.ui.vip.VipCenterActivity;

import org.greenrobot.eventbus.EventBus;

import cn.refactor.lib.colordialog.ColorDialog;

public class GoToVipHelper {
    private static GoToVipHelper instance;

    public static GoToVipHelper getInstance() {
        if (instance == null) {
            instance = new GoToVipHelper();
        }
        return instance;
    }

    public void takeYourMoney(Context context) {
        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle("温馨提示");
        colorDialog.setContentText("您尚未开通VIP账户，开通后方可使用此功能，是否开通？");
        colorDialog.setPositiveListener("开通", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
                context.startActivity(new Intent(context, VipCenterActivity.class));
            }
        });
        colorDialog.setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        colorDialog.show();

    }

    public void goToLogin(Activity context) {


        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("温馨提示")
                .setMessage("您尚未登录，是否跳转至登录页面")
                .setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        EventBus.getDefault().post(new LoginEventbus());;
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
/*
        ColorDialog colorDialog = new ColorDialog(context);
        colorDialog.setTitle("温馨提示");
        colorDialog.setContentText("您尚未登录，是否跳转至登录页面");
        colorDialog.setPositiveListener("跳转", new ColorDialog.OnPositiveListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
//                context.startActivity(new Intent(context, Login.class));

                EventBus.getDefault().post(new WxLoginEventbus());
            }
        });
        colorDialog.setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
            @Override
            public void onClick(ColorDialog dialog) {
                dialog.dismiss();
            }
        });
        colorDialog.show();*/
    }

    public void EvalTimesOver(Context context) {

        AlertDialog colorDialog = new AlertDialog.Builder(context).setTitle("提示")
                .setMessage("非会员每篇文章仅可免费评测三次")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("购买会员", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        VipCenterActivity.start(context, false);
                    }
                }).show();


    }


}
