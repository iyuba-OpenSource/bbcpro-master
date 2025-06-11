package com.ai.bbcpro.http.protocol;


import android.util.Log;

import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.manager.RuntimeManager;
import com.ai.bbcpro.sqlite.bean.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author yao
 */
public class ResponseBasicUserInfo extends VOABaseJsonResponse {
    public String message;// 返回信息
    public String result;// 返回代码

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            result = jsonBody.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            String vipStatus = jsonBody.getString("vipStatus");
            ConfigManager.Instance().putInt("isvip2", Integer.parseInt(vipStatus));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            String username = jsonBody.getString("username");
            ConfigManager.Instance().putString("userName", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return true;
    }

}
