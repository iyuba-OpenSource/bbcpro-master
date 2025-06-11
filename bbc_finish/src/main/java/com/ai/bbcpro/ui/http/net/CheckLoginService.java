package com.ai.bbcpro.ui.http.net;


import com.ai.bbcpro.ui.bean.RegisterBean;
import com.ai.bbcpro.ui.http.response.SecVerifyResp;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by YM on 2021/3/20 14:31
 */
public interface CheckLoginService {


		/**
		 * http://api.iyuba.com.cn/v2/api.iyuba?&protocol=10010&appId=259&appkey=2f6dcf8b701cc&opToken=STsid0000001614914253149fIWlULI00u20qy00RLQZ8qGN16w9QnmY&operator=CMCC&token=0%253AAAAAhAAAAIAHyVEAt2UG8sQ%252FVjqLscIx7EsUFYPDngZWOApiZu88SJ2zk4dcZFLvY1ylNeYaJzQY5HW9iEgDzxl6Y1HdLr4NL%252FfYKlKx7ix0YaMJlYM6hIc2t1LTb6TndTkXqNbyPtIwcWymuqE2%252BpQ56fu%252BAFVUTyyrmGmvujsjH2pEnqRNBgAAAQCkgEAqp7OIF8hW4IkPBzH9%252BolSQEMMyfKhRE%252Bu81DsSKEchwWVcDrHEaTFxMEB4ZrjsZ4KxXFM7UoeTPsCuLXB4XX2vKO26JuiFUj52LUj9jAaBAKBT9B8U4RtAzSS7g6d3Nnn%252FeJqbrdhoSfv6p5%252BRnCjhINEbC2C5ysWD8EmbXZ836Ftc5q%252F0ms0NmT9n3kpm2qYsVuW09%252Flo6fVAV5R7B2vkGAaDS%252FiI1bwC8nL3yILp98gIg%252FM1Wfa8ingUnN3xcOwtDv1WdgWZ3hsTPOUrodg1HG1MDrcQVj%252Bdf1DUx7jcEebU59hiCcJBDChD0FQfRGRu26WKSwYh4yufzuv
		 */
		@FormUrlEncoded
		@POST("v2/api.iyuba?protocol=10010")
		Call<SecVerifyResp> fetchUserMsgForSecVerify(@FieldMap Map<String, String> map);

		// "http://api.iyuba.com.cn/v2/api.iyuba"    邮箱注册和手机号注册都走这个接口
		@GET("v2/api.iyuba?platform=android&format=json")
		Call<RegisterBean> registerNoMail(@Query("username") String username,
										  @Query("protocol") String protocol,
										  @Query("password") String password,
										  @Query("sign") String sign,
										  @Query("mobile") String mobile,
										  @Query("app") String app);

}
