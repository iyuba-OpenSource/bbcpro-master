package com.ai.bbcpro.widget;




import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 获取用户练习测试结果  做过的每一个题目的详情
 * Created by liuzhenli on 2017/5/16.
 */

public interface AddCreditApi {
    //
//    public AddCreditsRequest(String uid,String idindex,String srid){
//        String  addIntegralUrl ="http://api."+WebConstant.WEB_SUFFIX+"credits/updateScore.jsp?srid="+srid;
//        String url =addIntegralUrl+
//                "&uid="+uid+
//                "&idindex="+idindex+
//                "&mobile=1"+
//                "&flag="+"1234567890"+ Base64Coder.getTime();
//        Log.e("AddCoinRequest",url);
//        setAbsoluteURI(url);
//    }
    @GET("credits/updateScore.jsp?mobile=1")
    Call<AddCreditModule> addCredit(@Query("uid") String uid,
                                    @Query("idindex") String idindex,
                                    @Query("srid") String srid,
                                    @Query("flag") String flag
    );
}

