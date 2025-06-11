package com.ai.bbcpro.widget;


import org.json.JSONException;
import org.json.JSONObject;

import com.ai.bbcpro.word.BlogContent;

import android.util.Log;



public class BlogContentResponse extends BaseJSONResponse {

    public BlogContent blogContent;
    public String result;
    public String blogid;
    public String subject;
    public String viewnum;
    public String replynum;
    public String message;
    private String responseString;

    public BlogContentResponse() {
        // TODO 自动生成的构造函数存根
    }

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        // TODO 自动生成的方法存根
        try {
            responseString = bodyElement.toString().trim();
			/*JSONObject jsonObjectRootRoot = new JSONObject(
					responseString.substring(responseString.indexOf("{", 2),
							responseString.lastIndexOf("}") + 1));*/
            JSONObject  jsonObjectRootRoot =new JSONObject(responseString);
            // JSONObject jsonObjectRootRoot = new JSONObject(bodyElement);
            blogContent = new BlogContent();
            //result = bodyElement.charAt(10) + "";
            result =jsonObjectRootRoot.getString("result");
            message = jsonObjectRootRoot.getString("message");
            //message = message.replaceAll("'", "''");
            blogid = jsonObjectRootRoot.getString("blogid");
            viewnum = jsonObjectRootRoot.getString("viewnum");
            replynum = jsonObjectRootRoot.getString("replynum");
            subject = jsonObjectRootRoot.getString("subject");

            blogContent.message = message;
            blogContent.blogid = blogid;
            blogContent.viewnum = viewnum;
            blogContent.replynum = replynum;
            blogContent.subject = subject;

            Log.d("result", result+"");
            Log.d("message", message+"");
            if (result.equals("1")) {
                Log.d("获取内容成功", "成功");
            } else {
                Log.d("获取内容失败", "失败");
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

}

