package com.ai.bbcpro.http.protocol;

import android.util.Log;

import com.ai.bbcpro.http.BaseJSONResponse;

import org.json.JSONObject;

public class LoginResponse extends BaseJSONResponse {
    public String result, uid, username, imgsrc, vipStatus, validity, amount, isteacher, money, expireTime, mobile, email;
    public int credits;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            JSONObject root = new JSONObject(bodyElement);
            Log.d("测试", "LoginResponse: 服务器返回数据并解析json" + bodyElement);
            if (root.has("result")) {
                result = root.getString("result");
            }
            if (root.has("uid")) {
                uid = root.getString("uid");
            }
            if (root.has("username")) {
                username = root.getString("username");
            }
            if (root.has("imgSrc")) {
                imgsrc = root.getString("imgSrc");
            }
            if (root.has("vipStatus")) {
                vipStatus = root.getString("vipStatus");
            }
            if (root.has("expireTime")) {
                validity = root.getString("expireTime");
            }
            if (root.has("Amount")) {
                amount = root.getString("Amount");
            }
            if (root.has("isteacher")) {
                isteacher = root.getString("isteacher");
            }
            if (root.has("money")) {
                Log.e("expireTime", "money: " + money);
                money = root.getString("money");
            }
            if (root.has("expireTime")) {
                expireTime = root.getString("expireTime");
            }
            if (root.has("mobile")) {
                mobile = root.getString("mobile");
            }
            if (root.has("credits")) {
                credits = root.getInt("credits");
            }
            if (root.has("email")) {
                email = root.getString("email");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

	/*@Override
	protected boolean extractBody(kXMLElement headerEleemnt,
			kXMLElement bodyElement) {
		// TODO Auto-generated method stub
		try {
			result = Utility.getSubTagContent(bodyElement, "result");
			uid = Utility.getSubTagContent(bodyElement, "uid");
			username = Utility.getSubTagContent(bodyElement, "username");
			imgsrc = Utility.getSubTagContent(bodyElement, "imgSrc");
			vip = Utility.getSubTagContent(bodyElement, "vipStatus");
			validity = Utility.getSubTagContent(bodyElement, "expireTime");
			amount = Utility.getSubTagContent(bodyElement, "Amount");
			isteacher=Utility.getSubTagContent(bodyElement, "isteacher");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}*/

}

