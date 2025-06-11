package com.ai.bbcpro.ui.register;




import com.ai.bbcpro.widget.BaseJSONResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class ResponsePhoneNumRegister extends BaseJSONResponse {
    public String resultCode;
    public String message;
    public boolean isRegSuccess;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        // TODO 自动生成的方法存根
        try {
            JSONObject jsonObjectRoot = new JSONObject(bodyElement);
            resultCode = jsonObjectRoot.getString("result");
            message = jsonObjectRoot.getString("message");
            if (resultCode.equals("111")) {
                isRegSuccess = true;
            } else {
                isRegSuccess = false;
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return true;
    }

}

