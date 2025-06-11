package com.ai.bbcpro.word;


import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.BaseXMLRequest;
import com.ai.bbcpro.http.DictResponse;
import com.ai.common.CommonConstant;

/**
 * 获取网页单词本
 *
 * @author Administrator
 */
public class DictRequest extends BaseXMLRequest {

    public DictRequest(String word) {
        word = TextAttr.encode(word);
        setAbsoluteURI("http://word." + CommonConstant.domain + "/words/apiWord.jsp?q=" + word);
    }


    @Override
    public BaseHttpResponse createResponse() {
        return new DictResponse();
    }

//	@Override
//	public com.iyuba.http.BaseHttpResponse createResponse() {
//		// TODO Auto-generated method stub
//		return new DictResponse();
//	}

}

