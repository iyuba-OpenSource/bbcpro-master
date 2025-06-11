package com.ai.bbcpro.widget;


import org.json.JSONException;
import org.json.JSONObject;



public class BlogExpressionResponse extends BaseJSONResponse {
    public String jiFen;
    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        try {
            JSONObject root = new JSONObject(bodyElement);
            if(root.has("jiFen")){
                jiFen =root.getString("jiFen");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

}

