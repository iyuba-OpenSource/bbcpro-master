package com.ai.bbcpro.ui.vip;

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

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.SplashActivity;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.activity.MyWebActivity;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.ui.widget.IyuButton;
import com.alipay.sdk.app.PayTask;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iyuba.imooclib.IMooc;

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class PayActivity extends AppCompatActivity {
    TextView username, priceTv, buyvip_tv_agreement;
    IyuButton submitOrder;
    private ImageView back;
    private String price;
    private int type;
    private String subject;
    private String body;
    private String productId;
    private String amount;
    private String TAG = "PayActivity";
    private RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyvip);
        Intent intent = getIntent();
        price = intent.getStringExtra("price");
        type = intent.getIntExtra("type", -1);
        subject = intent.getStringExtra("subject");
        body = intent.getStringExtra("body");
        productId = String.valueOf(intent.getIntExtra("productId", 10));
        amount = getAmount(type);
        initView();
    }

    private String getAmount(int type) {
        String amount;
        if (type == 0) {
            amount = "0";
        } else if (type == 120) {
            amount = "12";
        } else {
            amount = type + "";
        }
        return amount;
    }


    private void initView() {
        back = findViewById(R.id.btn_back);
        username = findViewById(R.id.payorder_username_tv);
        priceTv = findViewById(R.id.payorder_rmb_amount_tv);
        submitOrder = findViewById(R.id.payorder_submit_btn);
        buyvip_tv_agreement = findViewById(R.id.buyvip_tv_agreement);


        buyvip_tv_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "http://iuserspeech.iyuba.cn:9001/api/vipServiceProtocol.jsp?company=" + BuildConfig.AGREEMENT_COMPANY + "&type=app";
                MyWebActivity.startActivity(PayActivity.this, url, "会员服务协议");
            }
        });

        username.setText(": " + getIntent().getStringExtra("username"));
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

                        PayTask payTask = new PayTask(PayActivity.this);
                        Map<String, String> result1 = payTask.payV2(request.alipayTradeStr, true);
                        Log.e(TAG, "run: " + result1);
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = result1;
                        alipayHandler.sendMessage(msg);
                    };
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                } else if (request.result.equals("413")) {

                    ToastUtil.showToast(PayActivity.this, request.message);
                } else {
                    ToastUtil.showToast(PayActivity.this, "网络故障");
                }
            }
        };

        if (BuildConfig.DEBUG) {

            price = 0.01 + "";
        }
        OrderGenerateRequest orderRequest = new OrderGenerateRequest(productId,
                subject, price, body, Constant.APPID,
                String.valueOf(ConfigManager.Instance().loadString("userId")), amount,
                mOrderErrorListener, rc);

        queue = Volley.newRequestQueue(this);
        queue.add(orderRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (queue != null) {

            queue.stop();
        }
    }

    private Response.ErrorListener mOrderErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            new AlertDialog.Builder(PayActivity.this).setTitle("订单提交出现问题!").
                    setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            PayActivity.this.finish();
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
                    Log.e("resultstatus:", resultStatus + " resultInfo:" + resultInfo);
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        SyncDataHelper.getInstance(PayActivity.this).refreshUserInfo();
                        ConfigManager.Instance().putBoolean("isvip", true);
                        EventBus.getDefault().post(new RefreshListEvent("refresh"));
                        CustomToast.showToast(PayActivity.this,
                                "如果vip状态没改变，请重新登录一下。", 1500);

                        if (productId.trim().equals("200")) {

                            //购买成功后更新微课
                            IMooc.notifyCoursePurchased();
                        }

//                     /*   // TODO refresh user's iyubi information!!!
                        new android.app.AlertDialog.Builder(PayActivity.this)
                                .setTitle("提示")
                                .setMessage("支付成功")
                                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        PayActivity.this.finish();
                                    }
                                })
                                .show();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，
                        // 最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            CustomToast.showToast(PayActivity.this, "支付结果确认中", 1500);
                            submitOrder.setClickable(true);
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            CustomToast.showToast(PayActivity.this, "您已取消支付", 1500);
                            submitOrder.setClickable(true);
                            onBackPressed();
                        } else if (TextUtils.equals(resultStatus, "6002")) {
                            CustomToast.showToast(PayActivity.this, "网络连接出错", 1500);
                            submitOrder.setClickable(true);
                            onBackPressed();
                        } else {
                            // 其他值就可以判断为支付失败，或者系统返回的错误
                            CustomToast.showToast(PayActivity.this, "支付失败", 1500);
                        }
                    }
                    break;
                }
            }
        }
    };
}
