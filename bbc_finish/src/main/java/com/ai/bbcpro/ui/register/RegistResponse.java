package com.ai.bbcpro.ui.register;

import com.ai.bbcpro.http.BaseXMLResponse;
import com.ai.bbcpro.http.Utility;
import com.ai.bbcpro.http.kXMLElement;

public class RegistResponse extends BaseXMLResponse {
    public String result;
    public String message;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt, kXMLElement bodyElement) {
        result = Utility.getSubTagContent(bodyElement, "result");
        message = Utility.getSubTagContent(bodyElement, "message");
        return true;
    }

}
