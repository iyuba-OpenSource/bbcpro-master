package com.ai.bbcpro.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.ui.vip.OrderGenerateRequest;
import com.ai.bbcpro.ui.vip.PayResult;
import com.ai.bbcpro.ui.vip.RequestCallBack;
import com.ai.bbcpro.ui.widget.IyuButton;
import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class IyubiPayOrderActivity extends AppCompatActivity {
    TextView username, priceTv, tv_header;
    IyuButton submitOrder;
    private ImageView back;
    private String price;
    private int type;
    private String subject;
    private String body;
    private String out_trade_no;
    private String productId;
    private String amount;
    private String TAG = "PayActivity";
    private static final String Seller = "iyuba@sina.com";
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyvip);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
//        price = "0.1";
        type = intent.getIntExtra("type", -1);
        subject = intent.getStringExtra("subject");
        body = intent.getStringExtra("body");
        out_trade_no = intent.getStringExtra("out_trade_no");
        productId = intent.getStringExtra("productID");
        amount = intent.getStringExtra("amount");
        initView();
    }

    private void initView() {
        tv_header = (TextView) findViewById(R.id.tv_header);
        tv_header.setText("购买爱语币");
        back = findViewById(R.id.btn_back);
        username = findViewById(R.id.payorder_username_tv);
        priceTv = findViewById(R.id.payorder_rmb_amount_tv);
        submitOrder = findViewById(R.id.payorder_submit_btn);
        username.setText(": " + ConfigManager.Instance().loadString("userName"));
        priceTv.setText(price + "元");
        submitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payByAlipay(body, subject);
            }
        });
        back.setOnClickListener((view) -> onBackPressed());
    }

    private void payByAlipay(String body, String subject) {
        RequestCallBack rc = new RequestCallBack() {
            @Override
            public void requestResult(Request result) {
                OrderGenerateRequest request = (OrderGenerateRequest) result;
                if (request.isRequestSuccessful()) {

                    Runnable payRunnable = () -> {

                        PayTask payTask = new PayTask(IyubiPayOrderActivity.this);
                        Map<String, String> result1 = payTask.payV2(request.alipayTradeStr, true);
//                            Log.e(TAG, "run: "+result );
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = result1;
                        alipayHandler.sendMessage(msg);
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else {
                    ToastUtil.showToast(IyubiPayOrderActivity.this, "网络故障");
                }
            }
        };
        OrderGenerateRequest orderRequest = new OrderGenerateRequest(productId,
                subject, price, body, Constant.APPID,
                String.valueOf(ConfigManager.Instance().loadString("userId")), amount,
                mOrderErrorListener, rc);
        Log.e(TAG, "payByAlipay: " + productId + " " + Seller + " " + out_trade_no + " " + subject + " " + price
                + " " + body + " " + amount);
        queue = Volley.newRequestQueue(this);
        queue.add(orderRequest);

    }

    private Response.ErrorListener mOrderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            new AlertDialog.Builder(IyubiPayOrderActivity.this).setTitle("订单提交出现问题!").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            IyubiPayOrderActivity.this.finish();
                        }
                    }).show();
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler alipayHandler = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    submitOrder.setClickable(false);
                    Map<String, String> payResult = (Map<String, String>) msg.obj;
                    String resultInfo = payResult.get("result");
                    String resultStatus = payResult.get("resultStatus");
//                    Log.e("resultstatus:", resultStatus+" resultInfo:"+resultInfo);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        SyncDataHelper.getInstance(IyubiPayOrderActivity.this).refreshUserInfo();
                        EventBus.getDefault().post(new RefreshListEvent("refresh"));
                        CustomToast.showToast(IyubiPayOrderActivity.this,
                                "如果爱语币数量没改变，请重新登录一下。", 1500);
//                     /*   // TODO refresh user's iyubi information!!!
                        new android.app.AlertDialog.Builder(IyubiPayOrderActivity.this)
                                .setTitle("提示")
                                .setMessage("支付成功")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        IyubiPayOrderActivity.this.finish();
                                    }
                                })
                                .show();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            CustomToast.showToast(IyubiPayOrderActivity.this, "支付结果确认中", 1500);
                            submitOrder.setClickable(true);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            CustomToast.showToast(IyubiPayOrderActivity.this, "您已取消支付", 1500);
                            submitOrder.setClickable(true);
                            onBackPressed();
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            CustomToast.showToast(IyubiPayOrderActivity.this, "网络连接出错", 1500);
                            submitOrder.setClickable(true);
                            onBackPressed();
                        } else {
                            // 其他值就可以判断为支付失败，或者系统返回的错误
                            CustomToast.showToast(IyubiPayOrderActivity.this, "支付失败", 1500);
                        }
                    }
                    break;
                }
            }
        }
    };
}
