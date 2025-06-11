package com.ai.bbcpro.util;


import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.ai.bbcpro.word.TestRecord;


public class JsonUtil {
    public static String buildJsonForTestRecord(List<TestRecord> tRecords) throws JSONException
    {
        String jsondata;
        JSONObject jsonRoot = new JSONObject();
        JSONArray json=new JSONArray();
        for(int i=0;i<tRecords.size();i++)
        {
            JSONObject jsonObj=new JSONObject();
            TestRecord tRecord = tRecords.get(i);
            jsonObj.put("uid", tRecord.uid);
            jsonObj.put("LessonId", tRecord.LessonId);
            jsonObj.put("TestNumber", tRecord.TestNumber);
            jsonObj.put("BeginTime", tRecord.BeginTime);
            jsonObj.put("TestTime", tRecord.TestTime);
            jsonObj.put("RightAnswer", tRecord.RightAnswer);
            jsonObj.put("UserAnswer", tRecord.UserAnswer);
            jsonObj.put("AnswerResut", tRecord.AnswerResult);
            //把每个数据当作一对象添加到数组里
            json.put(jsonObj);
        }
        jsonRoot.put("datalist", json);
        jsondata=jsonRoot.toString();
        Log.d("JSON", jsondata);
        return jsondata;
        //调用解析JSON方法
        //parserJson(jsondata);
    }

    public static String buildJsonForTestRecordSingle(TestRecord tRecord) throws JSONException
    {
        String jsondata;
        JSONObject jsonRoot = new JSONObject();
        JSONArray json=new JSONArray();
        JSONObject jsonObj=new JSONObject();
        jsonObj.put("uid", tRecord.uid);
        jsonObj.put("LessonId", tRecord.LessonId);
        jsonObj.put("TestNumber", tRecord.TestNumber);
        jsonObj.put("BeginTime", tRecord.BeginTime);
        jsonObj.put("TestTime", tRecord.TestTime);
        jsonObj.put("RightAnswer", tRecord.RightAnswer);
        jsonObj.put("UserAnswer", tRecord.UserAnswer);
        jsonObj.put("AnswerResut", tRecord.AnswerResult);
        //把每个数据当作一对象添加到数组里
        json.put(jsonObj);
        jsonRoot.put("datalist", json);
        jsondata=jsonRoot.toString();
        Log.d("JSON", jsondata);
        return jsondata;
        //调用解析JSON方法
        //parserJson(jsondata);
    }

}
