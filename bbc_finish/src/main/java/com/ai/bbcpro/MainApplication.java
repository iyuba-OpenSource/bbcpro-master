package com.ai.bbcpro;

import android.app.Application;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;


import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.startup.AppInitializer;

import com.ai.bbcpro.download.DownloadConfiguration;
import com.ai.bbcpro.download.DownloadManager;

import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.manager.RuntimeManager;
import com.ai.bbcpro.model.NetWorkManager;
import com.ai.bbcpro.sqlite.db.DBInitHelper;
import com.ai.bbcpro.ui.helper.ShareHelper;
import com.ai.bbcpro.word.cetDB.ImportCetDatabase;
import com.ai.common.CommonConstant;
import com.iyuba.dlex.bizs.DLManager;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.data.local.HeadlineInfoHelper;
import com.iyuba.imooclib.IMooc;
import com.iyuba.imooclib.data.local.IMoocDBManager;
import com.iyuba.module.commonvar.CommonVars;
import com.iyuba.module.dl.BasicDLDBManager;
import com.iyuba.module.favor.BasicFavor;
import com.iyuba.module.favor.data.local.BasicFavorDBManager;
import com.iyuba.module.favor.data.local.BasicFavorInfoHelper;
import com.iyuba.module.movies.data.local.InfoHelper;
import com.iyuba.module.movies.data.local.db.DBManager;
import com.iyuba.module.privacy.IPrivacy;
import com.iyuba.module.privacy.PrivacyInfoHelper;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;
import com.iyuba.widget.unipicker.IUniversityPicker;
import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sd.iyu.training_camp.TCApplication;
import com.umeng.commonsdk.UMConfigure;
import com.yd.saas.ydsdk.manager.YdConfig;
import com.youdao.sdk.common.OAIDHelper;
import com.youdao.sdk.common.YouDaoAd;
import com.youdao.sdk.common.YoudaoSDK;

import org.litepal.LitePal;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import personal.iyuba.personalhomelibrary.PersonalHome;
import personal.iyuba.personalhomelibrary.data.local.HLDBManager;
import personal.iyuba.personalhomelibrary.ui.publish.PublishDoingActivity;
import timber.log.Timber;

public class MainApplication extends Application {

    private String TAG = "MainApplication";

    public static MainApplication application;

    /**
     * 初始化第三方action
     */
    public static final String ACTION_INIT = "com.ai.bbcpro.init";

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //加载oaid的so
        System.loadLibrary("msaoaidsec");

        Timber.plant(new Timber.DebugTree());

        //个人中心初始化
        RuntimeManager.setApplicationContext(this);
        RuntimeManager.setApplication(this);


        CommonVars.domain = CommonConstant.domain;
        CommonVars.domainLong = CommonConstant.domainLong;

        IHeadline.resetMseUrl();
        NetWorkManager.getInstance().initApi();
        //隐私协议
        //3 爱语吧    Constant.BaseUrl1 + PackageUtil.getAppName(this) + "&company=" + Constant.Company;
        // 国东 隐私政策 "http://www.bbe.net.cn/protocolpri.jsp?company=1&apptype=" + PackageUtil.getAppName(this);
        //珠穆拉玛 "http://www.qomolama.com.cn/protocolpri.jsp?apptype=" +  PackageUtil.getAppName(this) + "&company=5";
        Constant.PROTOCOLPRI = BuildConfig.PROTOCOL_PRI;
        //爱语吧 Constant.BaseUrl2 + PackageUtil.getAppName(this) + "&company=" + Constant.Company;
        //国东  "http://www.bbe.net.cn/protocoluse.jsp?company=1&apptype=" + PackageUtil.getAppName(this);
        //珠穆拉玛 "http://www.qomolama.com.cn/protocoluse.jsp?apptype=" + PackageUtil.getAppName(this) + "&company=5";
        Constant.PROTOCOLUSE = BuildConfig.PROTOCOL_USE;

        PrivacyInfoHelper.init(this);
        String privacyUrl = Constant.PROTOCOLPRI;
        String usageUrl = Constant.PROTOCOLUSE;
        IPrivacy.setPrivacyUsageUrl(usageUrl, privacyUrl);


        //视频模块评测url
        IHeadline.setExtraMseUrl(CommonConstant.IUSERSPEECH_URL + "/test/ai/");
        IHeadline.setExtraMergeAudioUrl(CommonConstant.IUSERSPEECH_URL + "/test/merge/");

        UMConfigure.preInit(this, Constant.UMENGKEY, "");

        ImportCetDatabase dbCourse = new ImportCetDatabase(this);
        dbCourse.setPackageName(getPackageName());
        dbCourse.setVersion(Constant.lastVersion, Constant.currentVersion);// 有需要数据库更改使用
        dbCourse.openDatabase(dbCourse.getDBPath());


        LitePal.initialize(this);
        initDownloader();

        HeadlineInfoHelper.init(this);
        if (!ConfigManager.Instance().loadString("short1", "").equals("")) {
            CommonConstant.domain = ConfigManager.Instance().loadString("short1", "");
            CommonConstant.domainLong = ConfigManager.Instance().loadString("short2", "");
        }

//        queue = Volley.newRequestQueue(this);
        if (ConfigManager.Instance().loadBoolean("initPermission")) {

            AppInit.init(getApplicationContext());
            AppInitializer.getInstance(getApplicationContext()).initializeComponent(AppInit.class);
        } else {

            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
            localBroadcastManager.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    AppInit.init(getApplicationContext());
                    AppInitializer.getInstance(getApplicationContext()).initializeComponent(AppInit.class);
                }
            }, new IntentFilter(ACTION_INIT));
        }
    }

    private void initDownloader() {
        DownloadConfiguration configuration = new DownloadConfiguration();
        configuration.setMaxThreadNum(3);
        configuration.setThreadNum(1);
        DownloadManager.getInstance().init(getApplicationContext(), configuration);
    }



    public static MainApplication getApplication() {
        return application;
    }
}
