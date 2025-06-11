package com.ai.bbcpro.word;

import java.util.ArrayList;

/**
 * @author zqq
 *         <p>
 *         功能：SectionA/B原文对应实体类，texta4/textb4
 */
public class TextAB {
    public String TestTime = "";// 真题年份201112
    public int Number = 0;// 题号
    public ArrayList<SubTextAB> subTexts = new ArrayList<>();// 某年某题对应的索引、时间、句子内容，男女标识列表
    public String Sound = "";// 对应mp3文件名
    public String localSound = ""; // 本地MP3文件路径

    /**
     * @param second
     * @return 功能：根据当前时间（秒）得到句子段落
     */
    public int getParagraph(int second) {
        int step = 0;
        if (subTexts != null && subTexts.size() != 0) {
            for (int i = 0; i < subTexts.size(); i++) {
                if (second >= subTexts.get(i).Timing && (subTexts.get(i).moreTiming == 0 ? subTexts.get(i).Timing : subTexts.get(i).moreTiming) >= 0) {
                    step = i;
                } else {
                    break;
                }
            }
        }
        return step;
    }


}
