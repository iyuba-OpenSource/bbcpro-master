package com.ai.bbcpro.http;


import com.ai.common.CommonConstant;

public class WordUpdateRequest extends BaseXMLRequest {

    String userId;
    public static final String MODE_INSERT = "insert";
    public static final String MODE_DELETE = "delete";
    String groupname = "Iyuba";
    String word;

    public WordUpdateRequest(String userId, String update_mode, String word) {
        this.userId = userId;
        this.word = word;
        setAbsoluteURI("http://word."+ CommonConstant.domain+"words/updateWord.jsp?userId="
                + this.userId + "&mod=" + update_mode + "&groupName="
                + groupname + "&word=" + this.word);
    }


    @Override
    public BaseHttpResponse createResponse() {
        // TODO Auto-generated method stub
        return new WordUpdateResponse();
    }

}
