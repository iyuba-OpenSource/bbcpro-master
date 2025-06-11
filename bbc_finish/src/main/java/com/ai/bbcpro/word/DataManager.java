package com.ai.bbcpro.word;



import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zqq
 *         <p>
 *         功能：页面间传递数据管理类
 */
public class DataManager {
    private static DataManager instance;
    public String year;

    public static DataManager Instance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * 问题
     */
    public ArrayList<AnswerAB> sectionAnswerA = new ArrayList<>();
    public ArrayList<AnswerC> sectionAnswerC = new ArrayList<>();
    /**
     * 原文
     */
    public ArrayList<TextAB> sectionAtext = new ArrayList<>();
    public ArrayList<TextC> sectionCtext = new ArrayList<>();

    public ArrayList<Explain> explains = new ArrayList<>();


    public String Section = "A";
    /**
     * 收藏列表
     */
    public ArrayList<Collection> collections = new ArrayList<>();
    /**
     * Section
     */
    public ArrayList<String> sectionCQuestion = new ArrayList<>();

    public BlogContent blogContent = new BlogContent();
}
