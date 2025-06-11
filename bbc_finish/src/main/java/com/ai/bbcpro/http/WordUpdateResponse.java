package com.ai.bbcpro.http;

public class WordUpdateResponse extends BaseXMLResponse {
    // public List<Word> words;

    public int result;
    public String word;

    @Override
    protected boolean extractBody(kXMLElement headerEleemnt,
                                  kXMLElement bodyElement) {
        try {
            result = Integer.parseInt(Utility.getSubTagContent(bodyElement,
                    "result"));
        }catch (NumberFormatException e){
            result = 0 ;
            e.printStackTrace();
        }
        word = Utility.getSubTagContent(bodyElement, "word");
        return true;
    }

}
