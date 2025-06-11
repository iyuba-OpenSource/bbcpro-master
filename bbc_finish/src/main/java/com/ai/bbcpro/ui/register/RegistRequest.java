package com.ai.bbcpro.ui.register;


import android.text.TextUtils;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.BaseJSONRequest;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;


/**
 * 用户注册
 *
 * @author chentong
 */
public class RegistRequest extends BaseJSONRequest {

    private String userName, email;

    public RegistRequest(String userName, String password, String email, String tuid) {
        this.userName = userName;
        this.email = email;
        setAbsoluteURI("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?protocol=10002&email="
                + this.email
                + "&username="
                + this.userName
                + (TextUtils.isEmpty(tuid) ? "" : "&tuid=" + tuid)
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&platform=android&app="
                + Constant.APPName
                + "&format=xml&sign="
                + MD5.getMD5ofStr("10002" + userName
                + MD5.getMD5ofStr(password) + email + "iyubaV2"));
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new RegistResponse();
    }

}
