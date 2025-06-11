package com.ai.bbcpro.word;


import java.util.ArrayList;

/**
 *
 * @author zqq
 *
 *         功能：SectionC原文对应实体类，textc4
 *
 */
public class TextC {
    public String TestTime = "";// 真题年份201112
    public int Number = 0;// 题号

    public int iQWords = 0;

    public ArrayList<SubTextC> subTextCs = new ArrayList<SubTextC>();// //某年某题对应的句子序号、三遍音频对应时间、句子内容、填词数
    // public String Sound = "";//对应mp3文件名

    /**
     *
     * @param second
     * @return 功能：根据当前时间（秒）得到句子段落
     */
    public int getParagraph(int second) {
        int step = 0;
        if (subTextCs != null && subTextCs.size() != 0) {
            for (int i = 0; i < subTextCs.size(); i++) {
                if (second >= subTextCs.get(i).Timing1) {// Timing1,Timing2,Timing3
                    step = i;
                } else {
                    break;
                }
            }
        }
        return step;
    }

}

