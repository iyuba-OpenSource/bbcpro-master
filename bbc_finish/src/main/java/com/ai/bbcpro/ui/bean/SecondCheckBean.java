package com.ai.bbcpro.ui.bean;

import com.google.gson.annotations.SerializedName;

public class SecondCheckBean {


    @SerializedName("isLogin")
    private String isLogin;
    @SerializedName("res")
    private ResBean res;
    @SerializedName("userinfo")
    private UserinfoBean userinfo;

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public ResBean getRes() {
        return res;
    }

    public void setRes(ResBean res) {
        this.res = res;
    }

    public UserinfoBean getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserinfoBean userinfo) {
        this.userinfo = userinfo;
    }

    public static class ResBean {
        @SerializedName("valid")
        private boolean valid;
        @SerializedName("phone")
        private String phone;
        @SerializedName("isValid")
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

    public static class UserinfoBean {
        @SerializedName("uid")
        private int uid;
        @SerializedName("expireTime")
        private int expireTime;
        @SerializedName("result")
        private String result;
        @SerializedName("Amount")
        private String amount;
        @SerializedName("vipStatus")
        private String vipStatus;
        @SerializedName("credits")
        private int credits;
        @SerializedName("message")
        private String message;
        @SerializedName("username")
        private String username;
        @SerializedName("email")
        private String email;
        @SerializedName("jiFen")
        private int jiFen;
        @SerializedName("imgSrc")
        private String imgSrc;
        @SerializedName("money")
        private String money;
        @SerializedName("mobile")
        private String mobile;
        @SerializedName("isteacher")
        private String isteacher;

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(int expireTime) {
            this.expireTime = expireTime;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getVipStatus() {
            return vipStatus;
        }

        public void setVipStatus(String vipStatus) {
            this.vipStatus = vipStatus;
        }

        public int getCredits() {
            return credits;
        }

        public void setCredits(int credits) {
            this.credits = credits;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getJiFen() {
            return jiFen;
        }

        public void setJiFen(int jiFen) {
            this.jiFen = jiFen;
        }

        public String getImgSrc() {
            return imgSrc;
        }

        public void setImgSrc(String imgSrc) {
            this.imgSrc = imgSrc;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getIsteacher() {
            return isteacher;
        }

        public void setIsteacher(String isteacher) {
            this.isteacher = isteacher;
        }

        @Override
        public String toString() {
            return "UserinfoBean{" +
                    "uid=" + uid +
                    ", expireTime=" + expireTime +
                    ", result='" + result + '\'' +
                    ", amount='" + amount + '\'' +
                    ", vipStatus='" + vipStatus + '\'' +
                    ", credits=" + credits +
                    ", message='" + message + '\'' +
                    ", username='" + username + '\'' +
                    ", email='" + email + '\'' +
                    ", jiFen=" + jiFen +
                    ", imgSrc='" + imgSrc + '\'' +
                    ", money='" + money + '\'' +
                    ", mobile='" + mobile + '\'' +
                    ", isteacher='" + isteacher + '\'' +
                    '}';
        }
    }
}
