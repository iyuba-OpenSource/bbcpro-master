package com.ai.bbcpro.ui.register;


import com.ai.bbcpro.http.protocol.VOABaseJsonResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseUserDetailInfo extends VOABaseJsonResponse {
    public String result;// 返回代码
    public String message;// 返回信息
    public String realname;// 真实姓名
    public String gender;// 性别
    public String birthday;// 生日
    public String constellation;// 星座
    public String zodiac;// 生肖
    public String telephone;// 联系电话
    public String mobile;// 手机
    public String idcardtype;// 证件类型
    public String idcard;// 证件号
    public String address;// 邮件地址
    public String zipcode;// 邮编
    public String nationality;// 国籍
    public String birthLocation;// 出生地
    public String resideLocation;// 现住地
    public String graduateschool;// 毕业学校
    public String company;// 公司
    public String education;// 学历
    public String occupation;// 职业
    public String position;// 职位
    public String revenue;// 年收入
    public String affectivestatus;// 情感状态
    public String lookingfor;// 交友目的
    public String bloodtype;// 血型
    public String height;// 身高
    public String weight;// 体重
    public String bio;// 自我介绍
    public String interest;// 兴趣爱好
    private String plevel, ptag, glevel, gtag, ptalklevel, ptalktag,
            gtalklevel, gtalktag, preadlevel, preadtag, greadlevel, greadtag;//用户学习目标相关变量
    public EditUserInfo editUserInfo = new EditUserInfo();

    @Override
    protected boolean extractBody(JSONObject headerEleemnt, String bodyElement) {
        JSONObject jsonBody = null;
        try {
            jsonBody = new JSONObject(bodyElement);
        } catch (Exception e3) {
            e3.printStackTrace();
            return false;
        }
        try {
            result = jsonBody.getString("result");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            message = jsonBody.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result.equals("211")) {
            try {
                realname = jsonBody.getString("realname");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                gender = jsonBody.getString("gender");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                birthday = jsonBody.getString("birthday");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                constellation = jsonBody.getString("constellation");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                zodiac = jsonBody.getString("zodiac");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                telephone = jsonBody.getString("telephone");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                mobile = jsonBody.getString("mobile");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                idcardtype = jsonBody.getString("idcardtype");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                idcard = jsonBody.getString("idcard");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                address = jsonBody.getString("address");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                zipcode = jsonBody.getString("zipcode");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                nationality = jsonBody.getString("nationality");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                birthLocation = jsonBody.getString("birthLocation");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                resideLocation = jsonBody.getString("resideLocation");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                graduateschool = jsonBody.getString("graduateschool");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                company = jsonBody.getString("company");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                education = jsonBody.getString("education");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                occupation = jsonBody.getString("occupation");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                position = jsonBody.getString("position");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                revenue = jsonBody.getString("revenue");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                affectivestatus = jsonBody.getString("affectivestatus");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                lookingfor = jsonBody.getString("lookingfor");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                height = jsonBody.getString("height");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                weight = jsonBody.getString("weight");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                bio = jsonBody.getString("bio");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                interest = jsonBody.getString("interest");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                plevel = jsonBody.getString("plevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                ptag = jsonBody.getString("ptag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                glevel = jsonBody.getString("glevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                gtag = jsonBody.getString("gtag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                ptalklevel = jsonBody.getString("ptalklevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                ptalktag = jsonBody.getString("ptalktag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                gtalklevel = jsonBody.getString("gtalklevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                gtalktag = jsonBody.getString("gtalktag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                preadlevel = jsonBody.getString("preadlevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                preadtag = jsonBody.getString("preadtag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                greadlevel = jsonBody.getString("greadlevel");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                greadtag = jsonBody.getString("greadtag");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            setEditContent();
        }
        return true;
    }

    /**
     *
     */
    private void setEditContent() {
        editUserInfo.setBirthday(birthday);
        editUserInfo.setEdConstellation(constellation);
        editUserInfo.setEdGender(gender);
        editUserInfo.setEdResideCity(resideLocation);
        editUserInfo.setEdZodiac(zodiac);
        editUserInfo.setEdAffectivestatus(affectivestatus);
        editUserInfo.setEdLookingfor(lookingfor);
        editUserInfo.setEdIntro(bio);
        editUserInfo.setEdInterest(interest);
        editUserInfo.setEdOccupation(occupation);
        editUserInfo.setEdEducation(education);
        editUserInfo.setUniversity(graduateschool);
        String componentOfData[] = birthday.split("-");
        editUserInfo.setEdBirthYear(Integer.parseInt(componentOfData[0]));
        editUserInfo.setEdBirthMonth(Integer.parseInt(componentOfData[1]));
        editUserInfo.setEdBirthDay(Integer.parseInt(componentOfData[2]));
        editUserInfo.setPlevel(plevel);
        editUserInfo.setPtag(ptag);
        editUserInfo.setGlevel(glevel);
        editUserInfo.setGtag(gtag);
        editUserInfo.setPtalklevel(ptalklevel);
        editUserInfo.setPtalktag(ptalktag);
        editUserInfo.setGtalklevel(gtalklevel);
        editUserInfo.setGtalktag(gtalktag);
        editUserInfo.setPreadlevel(preadlevel);
        editUserInfo.setPreadtag(preadtag);
        editUserInfo.setGreadlevel(greadlevel);
        editUserInfo.setGreadtag(greadtag);
    }

}

