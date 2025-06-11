package com.ai.bbcpro.widget;


import android.util.Log;



import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;

import org.json.JSONException;
import org.json.JSONObject;



public class BlogContentRequest extends BaseJSONRequest {
    public BlogContentRequest(String blogid) {
        // TODO 自动生成的构造函数存根
		/*setAbsoluteURI("http://api.iyuba.com.cn/v2/api.iyuba?protocol=200062"
				+ "&blogId=" + blogid + "&format=json&sign="
				+ MD5.getMD5ofStr("20006" + blogid + "iyubaV2"));
		Log.d("BlogContentRequest url",
				"http://api.iyuba.com.cn/v2/api.iyuba?protocol=200062"
						+ "&blogId=" + blogid + "&format=json&sign="
						+ MD5.getMD5ofStr("20006" + blogid + "iyubaV2"));*/
        setAbsoluteURI("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=200065" +
                "&sign="+ MD5.getMD5ofStr("20006" + blogid + "iyubaV2")+"&blogId="+blogid+"&pageNumber=1&pageCounts=20");
        Log.e("BlogContentRequest","http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=200065" +
                "&sign="+MD5.getMD5ofStr("20006" + blogid + "iyubaV2")+"&blogId="+blogid+"&pageNumber=1&pageCounts=20");

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
        // TODO 自动生成的方法存根

    }

    @Override
    public BaseHttpResponse createResponse() {
        // TODO 自动生成的方法存根
        return new BlogContentResponse();
    }

}

