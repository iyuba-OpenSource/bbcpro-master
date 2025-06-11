package com.ai.bbcpro.widget;


import com.ai.bbcpro.http.Utility;
import com.ai.bbcpro.http.kXMLElement;
import com.ai.bbcpro.word.Word;

public class DictResponse extends BaseXMLResponse {
    public Word word;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {
        // TODO Auto-generated method stub
        word = new Word();
        word.key = Utility.getSubTagContent(bodyElement, "key");
        word.lang = Utility.getSubTagContent(bodyElement, "lang");
        word.audioUrl = Utility.getSubTagContent(bodyElement, "audio");
        word.pron = Utility.getSubTagContent(bodyElement, "pron");
        word.def = Utility.getSubTagContent(bodyElement, "def");
        // Log.e("&&&&&&&&&&&&&&&&", word.toString());
        return true;
    }

}

