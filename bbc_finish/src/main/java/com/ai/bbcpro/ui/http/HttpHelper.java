package com.ai.bbcpro.ui.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by YM on 2020/12/7 10:51
 * 用于配置 OkHttpClient
 */
public class HttpHelper {
		private static volatile OkHttpClient sClient;

		public static OkHttpClient getClient() {
				if (sClient == null) {
						synchronized (HttpHelper.class) {
								if (sClient == null) {
										sClient = new OkHttpClient.Builder() // 拦截器后续加
												.connectTimeout(15, TimeUnit.SECONDS)
												.readTimeout(15, TimeUnit.SECONDS)
												.writeTimeout(15, TimeUnit.SECONDS)
												.build();
								}
						}
				}
				return sClient;
		}
}
