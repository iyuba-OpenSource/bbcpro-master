package com.ai.bbcpro;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.startup.Initializer;

import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.sqlite.db.DBInitHelper;
import com.ai.bbcpro.ui.helper.ShareHelper;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.movies.data.local.InfoHelper;
import com.iyuba.module.movies.data.local.db.DBManager;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.mob.MobSDK;
import com.sd.iyu.training_camp.TCApplication;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.OAIDHelper;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.data.local.HLDBManager;
import personal.iyuba.personalhomelibrary.ui.publish.PublishDoingActivity;

public class AppInit implements Initializer<String> {


    public static void init(Context context) {


        UMConfigure.init(context, Constant.UMENGKEY, "", UMConfigure.DEVICE_TYPE_PHONE, null);
        //禁止初始化时请求权限
        //取消申请读取应用列表权限
        YouDaoAd.getNativeDownloadOptions().setConfirmDialogEnabled(true);
        YouDaoAd.getYouDaoOptions().setAllowSubmitInstalledPackageInfo(false);
        YouDaoAd.getYouDaoOptions().setAppListEnabled(false);
        YouDaoAd.getYouDaoOptions().setPositionEnabled(false);
        YouDaoAd.getYouDaoOptions().setSdkDownloadApkEnabled(true);
        YouDaoAd.getYouDaoOptions().setDeviceParamsEnabled(false);
        YouDaoAd.getYouDaoOptions().setDebugMode(BuildConfig.DEBUG);
        YouDaoAd.getYouDaoOptions().setWifiEnabled(false);
        YouDaoAd.getYouDaoOptions().setCanObtainAndroidId(false);

        YoudaoSDK.init(context);
        YdConfig.getInstance().init(context, Constant.APPID);

        OAIDHelper.getInstance().init(context);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {

            MyOaidHelper myOaidHelper = new MyOaidHelper(new MyOaidHelper.AppIdsUpdater() {
                @Override
                public void onIdsValid(String ids) {

                    OAIDHelper.getInstance().setOAID(ids);
                }
            });
            myOaidHelper.getDeviceIds(context);
        }
        //广告初始化
        YdConfig.getInstance().init(context, Constant.APPID);

        //视频模块
        IHeadline.init(context, Constant.APPID, "BBE英语");
        IHeadline.setAdAppId(Constant.ADAPPID);
        IHeadline.setYdsdkTemplateKey(
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_CSJ,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_YLH,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_KS,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_BD,
                null);
        IHeadline.setEnableSmallVideoTalk(true);
        IHeadline.setEnableGoStore(false);
        IHeadline.setDebug(false);
        if (YoudaoSDK.hasInit()) {

            IHeadline.setEnableAd(true);
        } else {
            IHeadline.setEnableAd(false);
        }
        if (BuildConfig.FLAVOR.equals("meizu")) {
            IHeadline.setEnableShare(false);
        } else {
            IHeadline.setEnableShare(true);
        }
        //微课
        IMooc.init(context, Constant.APPID, Constant.APPName);
        IMooc.setAdAppId(Constant.ADAPPID);
        IMooc.setYdsdkTemplateKey(
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_CSJ,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_YLH,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_KS,
                BuildConfig.TEMPLATE_SCREEN_AD_KEY_BD,
                null);
        if (BuildConfig.FLAVOR.equals("meizu")) {
            IMooc.setEnableShare(false);
        } else {
            IMooc.setEnableShare(true);
        }
        if (!YoudaoSDK.hasInit()) {

            IMooc.setYoudaoId("");
        }
    }


    @NonNull
    @Override
    public String create(@NonNull Context context) {


        MobSDK.submitPolicyGrantResult(true);
        //关系到广告是否能显示
        PrivacyInfoHelper.getInstance().putApproved(true);


        ShareHelper.init(context);

        PersonalHome.init(context, Constant.APPID, context.getString(R.string.app_name));
        PersonalHome.setMainPath("com.ai.bbcpro.ui.activity.MainActivity");
        PersonalHome.setCategoryType(Constant.CATEGORY_TYPE);

        //训练营
        TCApplication.init(context);
        TCApplication.setLesson("bbc");
        TCApplication.setAppid(Integer.parseInt(Constant.APPID));
        TCApplication.setMediaPauseEventbusPackage(MediaPause.class.getSimpleName());


        //设置我的收藏 过滤

        List<String> typeFilter = new ArrayList<>();
        typeFilter.add(HeadlineType.MEIYU);
        typeFilter.add(HeadlineType.BBCWORDVIDEO);
        typeFilter.add(HeadlineType.SMALLVIDEO);
        typeFilter.add(HeadlineType.TED);
        typeFilter.add(HeadlineType.TOPVIDEOS);
        typeFilter.add(HeadlineType.VOAVIDEO);
        typeFilter.add(HeadlineType.VOA);
        typeFilter.add(HeadlineType.NEWS);
        typeFilter.add(HeadlineType.SONG);
        typeFilter.add(HeadlineType.BBC);
        typeFilter.add(HeadlineType.VIDEO);
        BasicFavor.setTypeFilter(typeFilter);


        if (BuildConfig.FLAVOR.equals("meizu")) {
            PersonalHome.setEnableShare(false);
        } else {//都禁用
            PersonalHome.setEnableShare(false);
        }

        PublishDoingActivity.IS_JAPANESE_APP = false;


        IMoocDBManager.init(context);


        IUniversityPicker.init(context);

//            PushApplication.initPush(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                BasicDLDBManager.init(context);
                DBInitHelper.init(context);
                BasicFavorDBManager.init(context);
                DLManager.init(context, 5);
                HLDBManager.init(context);
                BasicFavorInfoHelper.init(context);
                InfoHelper.init(context);
                DBManager.init(context);
                IMoocDBManager.init(context);
                BasicFavor.init(context, Constant.APPID);

            }
        }).start();

        //设置IyuUserManager
        String userId = ConfigManager.Instance().loadString("userId", "");
        if (!userId.equals("")) {

            User user = new User();
            user.vipStatus = ConfigManager.Instance().loadInt("isvip2", 0) + "";
            user.uid = Integer.parseInt(userId);
            user.name = ConfigManager.Instance().loadString("userName", null);
            IyuUserManager.getInstance().setCurrentUser(user);
            //传递id、会员状态给训练营
            TCApplication.setUserid(userId);
            TCApplication.setVipStatus(AccountManager.getVipStatus() + "");

            PersonalHome.setSaveUserinfo(user.uid, user.name, user.vipStatus);
        }

        return "app init";
    }

    @NonNull
    @Override
    public List<Class<? extends Initializer<?>>> dependencies() {
        return Collections.emptyList();
    }
}
