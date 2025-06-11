package com.ai.bbcpro.widget;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 获取评论的Response
 */

public class BlogCommentResponse extends BaseJSONResponse {
    public ArrayList<BlogComment> Comments = new ArrayList<BlogComment>();
    public String result;
    public String message;
    public String commentCounts;
    private BlogComment tempComment;

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        // TODO 自动生成的方法存根
        JSONObject jsonObjectRoot;
        try {
            jsonObjectRoot = new JSONObject(bodyElement);
            result = jsonObjectRoot.getString("result");
            message = jsonObjectRoot.getString("message");
            if (result != null && result.equals("261")) {
                commentCounts = jsonObjectRoot.getString("commentCounts");
                JSONArray JsonArrayData = jsonObjectRoot.getJSONArray("data");
                JSONObject jsonObjectData;
                int size = JsonArrayData.length();
                for (int i = 0; i < size; i++) {
                    tempComment = new BlogComment();
                    jsonObjectData = JsonArrayData.getJSONObject(i);
                    tempComment.authorid = jsonObjectData.getInt("authorid");
                    tempComment.message = jsonObjectData.getString("message");
                    tempComment.author = jsonObjectData.getString("author");
                    tempComment.dateline = jsonObjectData.getString("dateline");

                    Comments.add(tempComment);
                }
            } else if (result != null && result.equals("262")) {
            }
        } catch (JSONException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }

        return true;
    }
}

