package com.ai.bbcpro.ui.register;


import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.protocol.VOABaseJsonRequest;
import com.ai.bbcpro.util.MD5;

/**
 * @author yao 用户的详细资料
 */
public class RequestUserDetailInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20002";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestUserDetailInfo(String userId) {
        super(protocolCode);
        // TODO Auto-generated constructor stub
        setRequestParameter("id", userId);
        setRequestParameter("sign",
                MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));

    }

//	@Override
//	protected void fillBody(JSONObject jsonObject) throws JSONException {
//		// TODO Auto-generated method stub
//	}

    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new ResponseUserDetailInfo();
    }

}

