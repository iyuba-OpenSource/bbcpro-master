package com.ai.bbcpro.word;

public class Explain {
    public String TestTime = "";// 考试时间201112
    public int TestType = 0;// Section类型，1为SectionA，2为SectionB，3为SectionC
    public int Number = 0;// 题号

    public String Keys = "";// 关键
    public String Explain = "";// 解释
    public String Knowledge = "";// 知识

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return Keys + "\n\n" + Explain + "\n\n" + Knowledge;
    }

}
