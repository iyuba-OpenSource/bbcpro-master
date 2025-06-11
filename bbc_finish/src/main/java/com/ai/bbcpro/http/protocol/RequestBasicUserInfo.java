package com.ai.bbcpro.http.protocol;


import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.util.MD5;

/**
 * @author ct 显示用户的基本资料信息 protocolCode 20001
 */

public class RequestBasicUserInfo extends VOABaseJsonRequest {
    public static final String protocolCode = "20001";
    public String md5Status = "1"; // 0=未加密,1=加密

    public RequestBasicUserInfo(String userId, String myid) {
        super(protocolCode);
        // TODO Auto-generated constructor stub;
        setRequestParameter("id", userId);
        setRequestParameter("myid", myid);
        setRequestParameter("sign", MD5.getMD5ofStr(protocolCode + userId + "iyubaV2"));
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new ResponseBasicUserInfo();
    }

}

