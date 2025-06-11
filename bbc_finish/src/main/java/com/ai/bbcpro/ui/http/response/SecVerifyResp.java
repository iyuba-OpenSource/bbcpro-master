package com.ai.bbcpro.ui.http.response;

import com.ai.bbcpro.ui.logout.LoginResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by YM on 2021/3/16 19:02
 */
public class SecVerifyResp {

		private String isLogin;

		private SecVerifyPhoneMsg res;

		@SerializedName("userinfo")
		private LoginResponse userInfo;

		public String getIsLogin() {
				return isLogin;
		}

		public void setIsLogin(String isLogin) {
				this.isLogin = isLogin;
		}

		public SecVerifyPhoneMsg getRes() {
				return res;
		}

		public void setRes(SecVerifyPhoneMsg res) {
				this.res = res;
		}

		public LoginResponse getUserInfo() {
				return userInfo;
		}

		public void setUserInfo(LoginResponse userInfo) {
				this.userInfo = userInfo;
		}
}



