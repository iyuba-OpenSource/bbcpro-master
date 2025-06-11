package com.ai.bbcpro.http;


import java.util.Vector;

public class Utility {
    public static kXMLElement getChildByName(kXMLElement parent, String tagName) {
        try {
            Vector vecChilds = parent.getChildren();
            for (int i = 0; i < vecChilds.size(); i++) {
                kXMLElement element = (kXMLElement) vecChilds.elementAt(i);
                if ((element.getTagName() != null)
                        && element.getTagName().equals(tagName)) {
                    return element;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSubTagContent(kXMLElement parent, String tagName) {
        kXMLElement xmlElement = getChildByName(parent, tagName);
        if (xmlElement != null) {
            return xmlElement.getContents();
        }
        return "";
    }

    // 返回节点中指定名称的子节点集合
    public static Vector<kXMLElement> getChildrenByName(kXMLElement parent,
                                                        String tagName) {
        Vector<kXMLElement> vecRet = new Vector<>();
        if (parent == null) {
            return vecRet;
        }
        vecRet.clear();
        Vector vecChilds = parent.getChildren();
        for (int i = 0; i < vecChilds.size(); i++) {
            kXMLElement element = (kXMLElement) vecChilds.elementAt(i);
            if ((element.getTagName() != null)
                    && element.getTagName().equals(tagName)) {
                vecRet.add(element);
            }
        }
        return vecRet;
    }
}
