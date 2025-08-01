package com.ai.bbcpro.ui.register;

import com.ai.bbcpro.http.protocol.VOABaseJsonResponse;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yao
 *
 */
public class ResponseEditUserInfo extends VOABaseJsonResponse {

    public String message;// 返回信息
    public String result;// 返回代码

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        // TODO Auto-generated method stub
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (JSONException e3) {
            // TODO Auto-generated catch block
            e3.printStackTrace();
        }
        try {
            result = jsonBody.getString("result");
        } catch (JSONException e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public String toString() {
        return "ResponseEditUserInfo{" +
                "message='" + message + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
