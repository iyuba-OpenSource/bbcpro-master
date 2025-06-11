package com.ai.bbcpro.ui.utils;

import java.util.ArrayList;

public class ListUtils {
    public static ArrayList<Integer> getEnglishFilter(){
        ArrayList<Integer> filter  = new ArrayList<>();
        for(int i = 0 ; i < 30 ; i++){
            filter.add(i);
        }
        filter.remove((Integer)1);
        filter.remove((Integer)5);
        filter.remove((Integer)6);
        //应用宝中需屏蔽三个条目
        filter.remove((Integer)24);
        filter.remove((Integer)25);
        filter.remove((Integer)21);
        filter.add(61);
        filter.add(91);
        filter.add(52);
        return filter ;
    }
}
