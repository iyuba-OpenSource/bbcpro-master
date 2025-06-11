package com.ai.bbcpro.util.xml;

import com.ai.bbcpro.model.bean.home.ApiWordBean;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class ApiWordXmlToBean {


    public static ApiWordBean parseXMLWithPull(String xml) {

        ApiWordBean apiWordBean = new ApiWordBean();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xml));
            int type = xmlPullParser.getEventType();
            String result = "";
            String key = "";
            String audio = "";
            String pron = "";
            String proncode = "";
            String def = "";
            String number = "";
            String orig = "";
            String trans = "";
            while (type != XmlPullParser.END_DOCUMENT) {
                String node = xmlPullParser.getName();
                switch (type) {
                    case XmlPullParser.START_TAG:
                        if ("result".equals(node)) {//是否是name的节点
                            result = xmlPullParser.nextText();//获取节点的具体内容
                        } else if ("key".equals(node)) {
                            key = xmlPullParser.nextText();
                        } else if ("audio".equals(node)) {
                            audio = xmlPullParser.nextText();
                        } else if ("pron".equals(node)) {
                            pron = xmlPullParser.nextText();
                        } else if ("proncode".equals(node)) {
                            proncode = xmlPullParser.nextText();
                        } else if ("def".equals(node)) {
                            def = xmlPullParser.nextText();
                        } else if ("number".equals(node)) {
                            number = xmlPullParser.nextText();
                        } else if ("orig".equals(node)) {
                            orig = xmlPullParser.nextText();
                        } else if ("trans".equals(node)) {
                            trans = xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //当读取完Book节点时，输出一下获取的内容
                        if ("data".equals(node)) {
                            apiWordBean.setKey(key);
                            apiWordBean.setAudio(audio);
                            apiWordBean.setDef(def);
                            apiWordBean.setResult(Integer.parseInt(result));
                            apiWordBean.setPron(pron);
                        } else if ("sent".equals(node)) {

                            ApiWordBean.SentBean sentBean = new ApiWordBean.SentBean();
                            sentBean.setNumber(Integer.parseInt(number));
                            sentBean.setOrig(orig);
                            sentBean.setTrans(trans);
                            if (apiWordBean.getSent() == null) {
                                apiWordBean.setSent(new ArrayList<>());
                            }
                            apiWordBean.getSent().add(sentBean);
                        }
                        break;
                }
                type = xmlPullParser.next();
            }//while
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return apiWordBean;
    }
}
