package com.ai.bbcpro.widget;



import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;


/**
 *
 *
 * @author chentong
 * @time 13.4.18 获取文章评论列表API
 */
public class BlogCommentRequest extends BaseJSONRequest {

    String sign;
    String blogContentId;

    public BlogCommentRequest(String BlogContentId) {
        this.blogContentId = BlogContentId;

        sign= MD5.getMD5ofStr("20007"+blogContentId+"blogid"+"iyubaV2");

        setAbsoluteURI("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=20007&id="
                + blogContentId
                + "&type=blogid"
                + "&sign="
                + sign);
        Log.e("BlogCommentRequest:", "http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=20007&id="
                + blogContentId
                + "&type=blogid"
                + "&sign="
                + sign);
    }

    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new BlogCommentResponse();
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
        // TODO 自动生成的方法存根

    }

}

