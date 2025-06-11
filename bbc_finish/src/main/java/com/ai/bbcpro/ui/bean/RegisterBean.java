package com.ai.bbcpro.ui.bean;

public class RegisterBean {
    private String result;
    private String message;
    private String uid;
    private String username;
    private String email;
    private String imgSrc;
    private String jiFen;
    private String credits;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public String getJiFen() {
        return jiFen;
    }

    public void setJiFen(String jiFen) {
        this.jiFen = jiFen;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    @Override
    public String toString() {
        return "RegisterBean{" +
                "result='" + result + '\'' +
                ", message='" + message + '\'' +
                ", uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", imgSrc='" + imgSrc + '\'' +
                ", jiFen='" + jiFen + '\'' +
                ", credits='" + credits + '\'' +
                '}';
    }
}

