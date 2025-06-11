package com.ai.bbcpro.word;

public class SubTextC {
    public int NumberIndex = 0;// 句子序号
    public int Timing1 = 0;// 第一遍音频的句子时间
    public int Timing2 = 0;// 第二遍音频的句子时间
    public int Timing3 = 0;// 第三遍音频的句子时间
    public String Sentence = "";// 句子内容
    public int Qwords = 0;// 本句中要填的词数，若大于0则用表answera中对应句子替换
}
