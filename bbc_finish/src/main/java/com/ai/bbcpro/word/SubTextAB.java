package com.ai.bbcpro.word;



import com.ai.bbcpro.widget.SendEvaluateResponse;

import java.util.List;

/**
 *
 * @author zqq
 *
 *         功能：当前题号对应子句的实体，SectionA/B，B中没有sex字段值
 *
 */
public class SubTextAB  {
    public String Section = "A";
    public int NumberIndex = 0;// 句子序号
    public int Number = 0;// 段落
    public int TestTime = 0;// 年份
    public int Timing = 0;// 句子时间  开始时间
    public float moreTiming;
    public float endTiming = 0;// 句子时间  开始时间
    public List<Integer> goodList ;
    public List<Integer> badList ;
    public String Sentence = "";// 句子内容
    public String Sex = "";// 男女标识//在sectionB中没有值
    public String Sound;

    public String id;
    public int shuoshuoid;
    public String paraID;
    public String indexID;
    public int  readScore;

    public boolean isRead;
    public String  record_url;

    public List<SendEvaluateResponse.DataBean.WordsBean> mDataBean;
}


