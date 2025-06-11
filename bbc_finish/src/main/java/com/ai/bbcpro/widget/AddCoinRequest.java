package com.ai.bbcpro.widget;

import android.util.Log;


import com.ai.bbcpro.util.Base64Coder;
import com.ai.common.CommonConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class AddCoinRequest extends BaseJSONRequest{
    public AddCoinRequest(String uid,String idindex,String userpwd,String srid){
        String  addIntegralUrl ="http://api."+ CommonConstant.domain+"credits/updateScore.jsp?srid=";
        String url =addIntegralUrl+srid+
                "&uid="+uid+
                "&idindex="+idindex+
                "&mobile=1"+
                "&flag="+"1234567890"+ Base64Coder.getTime();
        Log.e("AddCoinRequest",url);
        setAbsoluteURI(url);
    }
    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
    }
    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new AddCoinResponse();
    }


}
