package com.ai.bbcpro.widget;


import com.ai.bbcpro.http.BaseXMLRequest;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.common.CommonConstant;

public class WordSynRequest extends BaseXMLRequest {
    String user;

    public WordSynRequest(String userid, String userName, int page) {
        this.user = userid;
        setAbsoluteURI("http://word." + CommonConstant.domain + "/words/wordListService.jsp?u="
                + userid + "&pageCounts=50&pageNumber=" + page);
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new WordSynResponse(user);
    }

}

