package com.ai.bbcpro.http.protocol;

import android.util.Log;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.BaseJSONRequest;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;

import java.net.URLEncoder;

public class LoginRequest extends BaseJSONRequest {
    private String userName, password;

    public LoginRequest(String userName, String password, String latitude,String longitude) {
        Log.d("测试", "LoginRequest: " + "登录请求");
        this.userName = userName;
        this.password = password;
        String url = "http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=11001&format=json&username=" + this.userName +
                "&password=" + MD5.getMD5ofStr(password) +
                "&appid=" + Constant.APPID +
                "&app=" + Constant.APPName +
                "&sign=" + MD5.getMD5ofStr("11001" + this.userName
                + MD5.getMD5ofStr(this.password) + "iyubaV2");
//        Log.d("测试", "LoginRequest: 临时账户" + SettingConfig.Instance().getIstour());
        Log.d("测试", "LoginRequest: 临时账户" + url);
        String[] split = url.split("&");
        String[] split1 = split[2].split("=");
        StringBuilder sb = new StringBuilder();
        sb.append(split[0] + "&");
        sb.append(split[1] + "&");
        //用户名前端
        sb.append(split1[0] + "=");
        try {
            sb.append(URLEncoder.encode(split1[1], "utf-8") + "&");
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append(split[3] + "&");
        sb.append(split[4] + "&");
        sb.append(split[5] + "&");
        sb.append(split[6]);
        String Eurl = sb.toString();
        setAbsoluteURI(Eurl);
        Log.d("测试", "LoginRequest:Eurl " + Eurl);
    }

    
    @Override
    public BaseHttpResponse createResponse() {
        return new LoginResponse();
    }
}
