package com.ai.bbcpro.ui.register;
/**
 * 手机注册完善内容界面
 *
 * @author czf
 * @version 1.0
 */

import android.text.TextUtils;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.util.MD5;
import com.ai.bbcpro.widget.BaseHttpResponse;
import com.ai.bbcpro.widget.BaseJSONRequest;
import com.ai.bbcpro.word.TextAttr;
import com.ai.common.CommonConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册协议
 *
 * @author yaoyao
 * @protocolCode 10002
 */
public class RequestPhoneNumRegister extends BaseJSONRequest {
    public static final String protocolCode = "11002";
    public String md5Status = "1"; // 0=未加密,1=加密
    public String emailStatus = "0";


    public RequestPhoneNumRegister(String userName, String password, String tuid,
                                   String mobile) {

        setAbsoluteURI("http://api."+ CommonConstant.domainLong +"/v2/api.iyuba?platform=android&app="
                + Constant.APPName
                + "&format=json&protocol=11002"
                + "&username="
                + TextAttr.encode(userName)
                + (TextUtils.isEmpty(tuid) ? "" : "&tuid=" + tuid)
                + "&password="
                + MD5.getMD5ofStr(password)
                + "&sign="
                + MD5.getMD5ofStr(protocolCode + userName
                + MD5.getMD5ofStr(password) + "iyubaV2")
                + "&mobile="
                + mobile);

    }

    @Override
    protected void fillBody(JSONObject jsonObject) throws JSONException {
    }

    @Override
    public BaseHttpResponse createResponse() {
        return new ResponsePhoneNumRegister();
    }

}

