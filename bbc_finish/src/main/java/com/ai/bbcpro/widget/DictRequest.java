package com.ai.bbcpro.widget;


import android.util.Log;


import com.ai.bbcpro.Constant;

import com.ai.common.CommonConstant;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class DictRequest extends BaseXMLRequest {
    {
        requestId = 0;
    }

    String word = "";

    public DictRequest(String word) {
        this.word = word;
        setAbsoluteURI("http://word."+ CommonConstant.domain+"words/apiWord.jsp?q=" + word);
    }

    public DictRequest(String word, String uid, String testmode) {
        this.word = word;
        setAbsoluteURI("http://word."+CommonConstant.domain+"words/apiWord.jsp?q=" + word + "&uid=" + uid + "&testmode=" + testmode + "&appid=" + Constant.APPID);
        Log.e("DictRequest", "" + "http://word."+CommonConstant.domain+"words/apiWord.jsp?q=" + word + "&uid=" + uid + "&testmode=" + testmode + "&appid=" + Constant.APPID);
    }

    @Override
    protected void fillBody(XmlSerializer serializer) throws IOException {
        // TODO Auto-generated method stub
    }

    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new DictResponse();
    }

}
