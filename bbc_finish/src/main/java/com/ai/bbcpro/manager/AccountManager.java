package com.ai.bbcpro.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.ExeProtocol;
import com.ai.bbcpro.http.ProtocolResponse;
import com.ai.bbcpro.http.protocol.LoginRequest;
import com.ai.bbcpro.http.protocol.LoginResponse;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.sqlite.bean.UserInfo;
import com.ai.bbcpro.sqlite.listener.OperateCallBack;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.util.GetLocation;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.sd.iyu.training_camp.TCApplication;

import org.greenrobot.eventbus.EventBus;

import personal.iyuba.personalhomelibrary.PersonalHome;

/**
 * 用户管理 用于用户信息的保存及权限判断
 *
 * @author chentong
 * @version 1.1 更改内容 引入userinfo数据结构统一管理用户信息
 */
public class AccountManager {

    private static AccountManager instance;
    private static Context mContext;
    public String userId; // 用户ID
    private static final String TAG = "AccountManager";

    private AccountManager() {
    }

    public static synchronized AccountManager Instance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }

    /**
     * 检查当前用户是否登录
     */
    public boolean checkUserLogin() {

        userId = ConfigManager.Instance().loadString("userId", "");
        if (userId.equals("")) {

            return false;
        } else {

            return true;
        }
    }

    /**
     * 用户登录
     *
     * @param userName
     * @param userPwd
     * @return
     */
    public boolean login(final String userName, String userPwd,
                         final OperateCallBack rc) {
        //获取用户的地理位置返回的是一个String字符串
        String[] strings = GetLocation.getInstance(mContext).getLocation();
        //其中String0和String1来表示经度和纬度
        if (strings[0] != null && strings[1] != null) {
        } else {
            strings[0] = "0";
            strings[1] = "0";
        }
        //发起登录请求
        ExeProtocol.exe(new LoginRequest(userName, userPwd, strings[0], strings[1]), new ProtocolResponse() {

            @Override
            //其中的rr是用来保存登录时候从服务器获取到的用户登录信息,这是一个回调
            public void finish(BaseHttpResponse bhr) {
                LoginResponse rr = (LoginResponse) bhr;
                Log.d("测试", "finish:我是rr " + rr.toString());
                if (rr.result.equals("101")) {
                    Log.d("测试", "Accountmanager登录接口:验证成功 ");

                    HeadlinesDataManager.getInstance(MainApplication.getApplication()).resetProgress();
                    Refresh(rr);
                    if (rc != null) {
                        rc.success(null);
                    }
                } else {
                    // 登录失败
                    //handler.sendEmptyMessage(1); // 添加一键登录后 本地可能没密码   故 去掉
                    if (rc != null) {
                        rc.fail(null);
                    }
                }
            }

            @Override
            public void error() {
                handler.sendEmptyMessage(2);
                if (rc != null) {
                    rc.fail(null);
                }
            }
        });
        return true;
    }

    /**
     * 用户登出
     *
     * @return
     */
    public boolean loginOut() {
//        EventBus.getDefault().post(new LogoutEvent(Integer.parseInt(userId)));

//        ConfigManager.Instance().loadString("userId", "0");
        userId = null; // 用户ID
        Log.d("测试", "loginOut:点击退出 false");
        //并保存用户名和密码为空
        ConfigManager.Instance().putString("userId", "");
        ConfigManager.Instance().putBoolean("isvip", false);
        ConfigManager.Instance().putInt("isvip2", 0);
        ConfigManager.Instance().putString("userName", "");
        //判断用户是否退出另一个标志.
        SettingConfig.Instance().setIsCaChe(false);
        IyuUserManager.getInstance().logout();
        //重置进度
        HeadlinesDataManager.getInstance(MainApplication.getApplication()).resetProgress();
        return true;
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    break;
                case 1: // 弹出错误信息
                    CustomToast.showToast(mContext, R.string.login_fail);
                    break;
                case 2:
                    CustomToast.showToast(mContext, R.string.login_faild);
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.person_vip_limit);
                    break;
                default:
                    break;
            }
        }
    };

    /*
     * 处理userinfo数据写入将数据ku通过uid
     */
    public void Refresh(LoginResponse rr) {

        userId = rr.uid; // 成功返回用户ID
        PersonalHome.setSaveUserinfo(Integer.parseInt(rr.uid), rr.username, rr.vipStatus);

        SyncDataHelper.getInstance(mContext).loadCollectionData();

        //训练营
        TCApplication.setUserid(rr.uid);
        TCApplication.setVipStatus(rr.vipStatus);
        //0表示不是会员
        if (!rr.vipStatus.equals("0")) {
            SettingConfig.Instance().setHighSpeed(true);
            ConfigManager.Instance().putBoolean("isvip", true);
            EventBus.getDefault().post(new RefreshListEvent("refresh"));
        } else {
            SettingConfig.Instance().setHighSpeed(false);
            ConfigManager.Instance().putBoolean("isvip", false);
        }
//        new UserInfoOp(mContext).saveData(userInfo);

        ConfigManager.Instance().putString("userId", userId);
        ConfigManager.Instance().putString("imgUrl", rr.imgsrc);
        ConfigManager.Instance().putString("userName", rr.username);
        ConfigManager.Instance().putString("expireTime", rr.expireTime);
        ConfigManager.Instance().putInt("isvip2", Integer.parseInt(rr.vipStatus));
        //共同模块的登录
        User user = new User();
        user.vipStatus = AccountManager.getVipStatus() + "";
        user.uid = (AccountManager.Instance(mContext).userId) == null ? 0 : Integer.parseInt(AccountManager.Instance(mContext).userId);
        user.name = ConfigManager.Instance().loadString("userName", "");
        IyuUserManager.getInstance().setCurrentUser(user);
    }

    public static boolean isVip() {

        boolean isVip = ConfigManager.Instance().loadBoolean("isvip", false);
        String expireTimeStr = ConfigManager.Instance().loadString("expireTime", "0");
        if (expireTimeStr.length() == 10) {
            expireTimeStr = expireTimeStr + "000";
        }
        long expireTimeLong = 0;
        try {

            expireTimeLong = Long.parseLong(expireTimeStr);
        } catch (NumberFormatException numberFormatException) {//处理数据格式不正确的崩溃问题

            ConfigManager.Instance().putString("expireTime", "0");
        }

        return isVip && (expireTimeLong > System.currentTimeMillis());
    }

    public static int getVipStatus() {
        return ConfigManager.Instance().loadInt("isvip2");
    }

    public String getUserId() {

        userId = ConfigManager.Instance().loadString("userId", "");
        if (TextUtils.isEmpty(userId)) {
            return "0";
        }
        return userId;
    }

    public void setUserInfoStatus(UserInfo uInfo) {
        //rr是保存了用户的一些信息
        userId = uInfo.uid;
        // 成功返回用户ID
        //用户登录成功之后就保存到用户的uid并保存是否是Teacher
        ConfigManager.Instance().putString("userId", userId);

        //0表示是vip其他表示不是
        if (!uInfo.vipStatus.equals("0")) {
            SettingConfig.Instance().setHighSpeed(true);
            ConfigManager.Instance().putBoolean("isvip", true);
        } else {
            SettingConfig.Instance().setHighSpeed(false);
            ConfigManager.Instance().putBoolean("isvip", false);
        }
        ConfigManager.Instance().putInt("isvip2", Integer.parseInt(uInfo.vipStatus));
    }

    public void setUserInfo(UserInfo userInfo) {
        setUserInfoStatus(userInfo);
    }
}
