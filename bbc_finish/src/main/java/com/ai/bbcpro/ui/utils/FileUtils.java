package com.ai.bbcpro.ui.utils;

import android.content.Context;

import java.io.File;

public class FileUtils {
    Context mContext;
    public static boolean fileIsExist(String fileName)
    {
        //传入指定的路径，然后判断路径是否存在
        File file=new File(fileName);
        if (file.exists())
            return true;
        else{
            //file.mkdirs() 创建文件夹的意思
            return file.mkdirs();
        }
    }

    public void setContext(Context context){
        this.mContext = context;
    }

    public static boolean deleteSingleFile(String filePath$Name) {
        File file = new File(filePath$Name);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {

                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
