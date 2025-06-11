package com.ai.bbcpro.ui.vip;

import android.util.Log;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.util.MD5;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;

public class OrderGenerateRequest extends BaseJsonObjectRequest {
    //    public String productId;
    public String result;
    public String message;
    public String alipayTradeStr;


    /**
     * http://vip.iyuba.cn/alipay.jsp?
     * WIDsubject=VIP&
     * WIDtotal_fee=30&
     * WIDbody=%25E8%25B4%25AD%25E4%25B9%25B01%25E4%25B8%25AA%25E6%259C%2588%25E5%25BA%2594%25E7%2594%25A8%25E4%25BC%259A%25E5%2591%2598&
     * app_id=221&
     * userId=6307010&
     * amount=1&
     * product_id=10&
     * code=8df3c5376065cd237122909b67cfe78d
     */

    public OrderGenerateRequest(String productId, String subject, String total_fee, String body,
                                String app_id, String userId, String amount,
                                ErrorListener el, final RequestCallBack rc) {
//        this.productId = productId;
        super(Request.Method.POST, Constant.URL_ALIPAY + "?" + "WIDsubject=" + subject + "&WIDtotal_fee=" + total_fee
                + "&WIDbody=" + body
                + "&app_id=" + app_id + "&userId=" + userId + "&amount=" + amount + "&product_id=" + productId
                + "&code=" + generateCode(userId), el);
        setResListener(jsonObjectRoot -> {

            try {
                result = jsonObjectRoot.getString("result");
                Log.e("支付result", jsonObjectRoot.toString());
                message = jsonObjectRoot.getString("message");
                if (isRequestSuccessful()) {

                    alipayTradeStr = jsonObjectRoot.getString("alipayTradeStr");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            rc.requestResult(OrderGenerateRequest.this);
        });
    }

    @Override
    public boolean isRequestSuccessful() {
        return "200".equals(result);
    }

    private static String generateCode(String userId) {
        String code = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        code = MD5.getMD5ofStr(userId + "iyuba" + df.format(System.currentTimeMillis()));
        return code;
    }

}
