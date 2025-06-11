package com.ai.bbcpro.widget;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class AddCoinResponse extends BaseJSONResponse{
    public int result;
    public String addcredit;
    public String totalcredit;
    public String message;
    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            Log.e("AddCoinResponse", ""+bodyElement);
            JSONObject  jsonObject =new JSONObject(bodyElement);
            if(jsonObject.has("result")){
                result =jsonObject.getInt("result");
            }
            if(result==200){
                if(jsonObject.has("addcredit")){
                    addcredit=jsonObject.getString("addcredit");
                }
                if(jsonObject.has("totalcredit")){
                    totalcredit =jsonObject.getString("totalcredit");
                }
            }else if(result==201){
                if(jsonObject.has("message")){
                    message=jsonObject.getString("message");
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return true;
    }

}

