package com.ai.bbcpro.word;


import android.content.Context;

import com.ai.bbcpro.manager.RuntimeManager;


import java.util.ArrayList;

/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class WordDataManager {
    Context mContext;
    public static WordDataManager dataManager;
    public String cate;
    public int number;
    public ArrayList<Cet4Word> words;
    public int pos;
    public WordDataManager() {
    }

    public static synchronized WordDataManager Instance() {
        if (dataManager == null) {
            dataManager = new WordDataManager();
            dataManager.mContext = RuntimeManager.getContext();
        }
        return dataManager;
    }
}

