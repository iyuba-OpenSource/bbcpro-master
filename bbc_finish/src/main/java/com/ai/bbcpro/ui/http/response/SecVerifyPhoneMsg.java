package com.ai.bbcpro.ui.http.response;

/**
 * Created by YM on 2021/3/17 13:53
 */
public class SecVerifyPhoneMsg {
		private boolean valid;
		private String phone;
		private int isValid;

		public boolean isValid() {
				return valid;
		}

		public void setValid(boolean valid) {
				this.valid = valid;
		}

		public String getPhone() {
				return phone;
		}

		public void setPhone(String phone) {
				this.phone = phone;
		}

		public int getIsValid() {
				return isValid;
		}

		public void setIsValid(int isValid) {
				this.isValid = isValid;
		}
}
