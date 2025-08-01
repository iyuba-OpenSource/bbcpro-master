package com.ai.bbcpro.word;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CetRootWord implements Serializable {

    @PrimaryKey
    @NonNull
    public String word;

    public int groupflg ;

    public String sound ;

    public String pron ;

    public String def ;

    public String grouptitle ;

    public String root ;

    public int remembered ;

    public int stage ;

    public int flag ; //是否标记为收藏
    public int wrong ; //是否做错    1正确 2错误



    public String sentence ;

    public String sentenceCN ;


    public String sentencePron;
}

