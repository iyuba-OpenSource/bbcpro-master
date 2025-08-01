package com.ai.bbcpro.ui.register;



import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.protocol.VOABaseJsonRequest;
import com.ai.bbcpro.util.MD5;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * @author yao 编辑用户信息
 */
public class RequestEditUserInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20003";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestEditUserInfo(String userId, String key, String value) {
        super(protocolCode);
        setRequestParameter("id", userId);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
        setRequestParameter("key", key);
        setRequestParameter("value",
                URLEncoder.encode(URLEncoder.encode(value)));
        // TODO Auto-generated constructor stub
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new ResponseEditUserInfo();
    }

}

