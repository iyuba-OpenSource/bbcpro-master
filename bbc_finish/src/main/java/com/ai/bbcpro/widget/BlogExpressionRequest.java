package com.ai.bbcpro.widget;



import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;


/**
 * 发送发表评论请求
 *
 * @author ct
 *
 */

public class BlogExpressionRequest extends BaseJSONRequest {
    String uid = "242141";		//四级对应的ID
    String authorid;			//发表评论的用户ID
    String author;				//发表评论的用户名
    String message;				//用户的评论内容
    String id;					//BlogID
    String sign;

    /**
     * @param userid
     * @param bbcid
     * @param content
     *
     *            用于发表文字评论
     */
    public BlogExpressionRequest(String blogId,String authorId,String author,String message) {

//		http://voa."+WebConstant.WEB_SUFFIX+"iyubaApi/v2/api.iyuba?protocol=30005&uid=333&type=sid&authorid=17228&author=011272200&message=hahaha&id=33&sign=52ad7e075c3ce63b696480e0f3dd1eee

        http://voa."+WebConstant.WEB_SUFFIX+"iyubaApi/v2/api.iyuba?protocol=30005&uid=242141&type=sid&authorid=361701&author=iloveen&message=发评论。&id=8395&sign=b7ad696b467932922b8e77d8381c86ec

        this.id = blogId;
        this.authorid = authorId;
        this.author = author;
        this.message = message;

        sign= MD5.getMD5ofStr("30005"+id+"blogid"+uid+author+authorid+"iyubaV2");

        setAbsoluteURI("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=30005&uid="
                + uid
                + "&type=blogid&authorid="
                + authorid
                + "&author="
                + author
                + "&message="
                + message
                + "&id="
                + id
                + "&sign="
                + sign);

        Log.d("BlogExpressionRequest:", "http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=30005&uid="
                + uid
                + "&type=blogid&authorid="
                + authorid
                + "&author="
                + author
                + "&message="
                + message
                + "&id="
                + id
                + "&sign="
                + sign);
    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
        // TODO Auto-generated method stub

    }

    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new BlogExpressionResponse();
    }
}
