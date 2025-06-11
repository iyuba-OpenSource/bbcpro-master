package com.ai.bbcpro.ui.activity;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.entity.RewardEventbus;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.http.main.Http;
import com.ai.bbcpro.http.main.HttpCallback;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.QQGroupBean;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.MainPresenter;
import com.ai.bbcpro.mvp.view.MainContract;
import com.ai.bbcpro.ui.AlertActivity;
import com.ai.bbcpro.ui.activity.headline.DropdownTitleFragmentNew2;
import com.ai.bbcpro.ui.activity.imooc.NewImoocFragment;
import com.ai.bbcpro.ui.activity.training_camp.NewTrainingCampFragment;
import com.ai.bbcpro.ui.activity.training_camp.PicWebActivity;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.event.RefreshPersonalEvent;
import com.ai.bbcpro.ui.event.SecVerifyLoginSuccessEvent;
import com.ai.bbcpro.ui.helper.SecVerifyManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.vip.PayActivity;
import com.ai.bbcpro.ui.vip.VipCenterActivity;
import com.ai.bbcpro.word.BackgroundManager;
import com.ai.bbcpro.ui.fragment.HomePageFragment;
import com.ai.bbcpro.ui.fragment.PersonalCenterFragment;

import com.ai.common.CommonConstant;
import com.google.gson.Gson;


import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.data.model.Headline;
import com.iyuba.headlinelibrary.event.HeadlineGoVIPEvent;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivity;
import com.iyuba.headlinelibrary.ui.content.AudioContentActivityNew;
import com.iyuba.headlinelibrary.ui.content.TextContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivity;
import com.iyuba.headlinelibrary.ui.content.VideoContentActivityNew;
import com.iyuba.headlinelibrary.ui.video.VideoMiniContentActivity;
import com.iyuba.imooclib.event.ImoocBuyIyubiEvent;
import com.iyuba.imooclib.event.ImoocBuyVIPEvent;
import com.iyuba.imooclib.event.ImoocPayCourseEvent;
import com.iyuba.imooclib.ui.content.ContentActivity;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;

import com.iyuba.imooclib.ui.web.Web;
import com.iyuba.module.dl.BasicDLPart;
import com.iyuba.module.dl.DLItemEvent;
import com.iyuba.module.favor.data.model.BasicFavorPart;
import com.iyuba.module.favor.event.FavorItemEvent;
import com.iyuba.module.headlinesearch.event.HeadlineSearchItemEvent;
import com.iyuba.module.movies.ui.series.SeriesActivity;
import com.iyuba.sdk.data.iyu.IyuAdClickEvent;
import com.mob.secverify.ui.component.CommonProgressDialog;
import com.sd.iyu.training_camp.entity.TCIntroduceEventbus;
import com.sd.iyu.training_camp.entity.TCSpeakEventbus;
import com.sd.iyu.training_camp.entity.TCVipEventbus;
import com.sd.iyu.training_camp.model.bean.CourseTitleBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import personal.iyuba.personalhomelibrary.event.ArtDataSkipEvent;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, MainContract.MainView {

    private ImageView homePageImage, personalCenterImage, videoImage, moocImage, main_iv_trainingcamp;
    private TextView homePageText, personalCenterText, videoText, moocText, main_tv_trainingcamp;
    private LinearLayout main_ll_trainingcamp;

    private int currentFragmentId;
    private HomePageFragment homePageFragment;
    private PersonalCenterFragment personalCenterFragment;
    //    private VideoMainFragment videoMainFragment;
    private NewTrainingCampFragment trainingCampFragment;
    private DropdownTitleFragmentNew2 dropdownTitleFragmentNew;
    private NewImoocFragment iMoocFragment;
    private View me, video, mooc;
    private LinearLayout ll_home;
    Activity mContext;
    private long touchTime = 0;
    private String TAG = "MainActivity";
    private FragmentTransaction transaction;
    private List<Fragment> fragmentList;

    private MainPresenter mainPresenter;
    private HeadlinesDataManager dataManager;

    private int getStatusBarHeight(Activity activity) {
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return activity.getResources().getDimensionPixelSize(resourceId);
        } else {

            return (int) Math.ceil(25 * getResources().getDisplayMetrics().density);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        Constant.STATUSBAR_HEIGHT = getStatusBarHeight(this);

        mContext = this;
        ll_home = findViewById(R.id.ll_home);

        mainPresenter = new MainPresenter();
        mainPresenter.attchView(this);

        String uid = ConfigManager.Instance().loadString("userId", "0");
        String vipStatus = ConfigManager.Instance().loadInt("isvip2", 0) + "";
//        mainPresenter.doCheckIPBBC(uid, Constant.APPID, Build.BRAND, vipStatus);

        bindBackgroundService();
        initView();
//        initPush();
        initData();
//        autoLogin();
        requestQQGroupNumber();
    }


    /**
     * 听力进度，获取的奖励
     *
     * @param rewardEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(RewardEventbus rewardEventbus) {

        AlertActivity.startActivity(MainActivity.this, rewardEventbus.getReward());
    }

    /**
     * 购买爱语币
     *
     * @param imoocBuyIyubiEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocBuyIyubiEvent imoocBuyIyubiEvent) {


        startActivity(new Intent(mContext, BuyIyubiActivity.class));
    }

    /**
     * 微课直购
     *
     * @param imoocPayCourseEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ImoocPayCourseEvent imoocPayCourseEvent) {


        String username = ConfigManager.Instance().loadString("userName", null);

        Intent intent = new Intent(this, PayActivity.class);
        intent.putExtra("price", imoocPayCourseEvent.price + "");
        intent.putExtra("username", username);
        intent.putExtra("type", imoocPayCourseEvent.courseId);
        intent.putExtra("productId", imoocPayCourseEvent.productId);
        intent.putExtra("body", imoocPayCourseEvent.body);
        startActivity(intent);
    }

    /**
     * 训练营 跳转会员
     *
     * @param tcVipEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TCVipEventbus tcVipEventbus) {

        VipCenterActivity.start(mContext, true);
    }

    /**
     * 训练营  点击课程介绍
     *
     * @param tcIntroduceEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TCIntroduceEventbus tcIntroduceEventbus) {

        PicWebActivity.startActivity(MainActivity.this, tcIntroduceEventbus.getUrl(), "课程介绍");
    }


    /**
     * 训练营  发音课程
     *
     * @param tcSpeakEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(TCSpeakEventbus tcSpeakEventbus) {

        ArrayList<Integer> integerList = new ArrayList<>();
        integerList.add(9);
        Intent intent = MobClassActivity.buildIntent(MainApplication.getApplication(), 9, true, integerList);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        MainApplication.getApplication().startActivity(intent);
    }

    /**
     * 训练营 学前热身
     *
     * @param dataDTO
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CourseTitleBean.DataDTO dataDTO) {

        if (dataManager == null) {

            dataManager = HeadlinesDataManager.getInstance(this);
        }
        long count = dataManager.getQuestionCountById(dataDTO.getBbcId());
        if (count > 0) {//是否有数据

            SumBean.DataDTO dto = new SumBean.DataDTO();
            dto.setBbcId(dataDTO.getBbcId() + "");
            dto.setCategory(dataDTO.getCategory() + "");
            dto.setCreatTime(dataDTO.getCreatTime() + "");
            dto.setDescCn(dataDTO.getDescCn() + "");
            dto.setHotFlg(dataDTO.getHotFlg() + "");
            dto.setPic(dataDTO.getPic() + "");
            dto.setReadCount(dataDTO.getReadCount() + "");
            dto.setSound(dataDTO.getSound() + "");
            dto.setTitle(dataDTO.getTitle() + "");
            dto.setTitle_cn(dataDTO.getTitleCn() + "");
            dto.setIsDownload(0);
            dto.setTexts(dataDTO.getTexts());

            Intent intent = new Intent(mContext, com.ai.bbcpro.ui.player.AudioContentActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("DATA", dto);
            bundle.putInt("POSITION", 0);
            bundle.putInt("VPAGE", 0);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            mainPresenter.textAllApi("json", dataDTO.getBbcId(), dataDTO);
        }
    }


    /**
     * 获取视频模块“现在升级的点击”
     *
     * @param headlineGoVIPEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineGoVIPEvent headlineGoVIPEvent) {

        VipCenterActivity.start(mContext, false);
    }

    //有道
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void event(IyuAdClickEvent iyuAdClickEvent) {

        Intent intent = Web.buildIntent(MainActivity.this, iyuAdClickEvent.info.linkUrl, "推广");
        startActivity(intent);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void fromMoocBuyGoldenVip(ImoocBuyVIPEvent event) {
        if (AccountManager.Instance(mContext).checkUserLogin()) {
            VipCenterActivity.start(mContext, true);
        } else {
            EventBus.getDefault().post(new LoginEventbus());;
        }

    }

    /**
     * 搜一搜 item的点击事件
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HeadlineSearchItemEvent event) {
        Headline headline = event.headline;
        switch (headline.type) {
            case "news":

                startActivity(TextContentActivity.buildIntent(MainActivity.this, headline));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.buildIntent(MainActivity.this, headline));
                break;
            case "song":
                startActivity(AudioContentActivity.buildIntent(MainActivity.this, headline));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
                startActivity(VideoContentActivityNew.buildIntent(MainActivity.this, headline));
                break;
            case "bbcwordvideo":
            case "topvideos":

                startActivity(VideoContentActivityNew.buildIntent(MainActivity.this, headline));
                break;
            case "class": {
                int packId = Integer.parseInt(headline.id);
                Intent intent = ContentActivity.buildIntent(MainActivity.this, packId, headline.description);
                startActivity(intent);
                break;
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DLItemEvent dlEvent) {
        //视频下载后点击
        BasicDLPart dlPart = dlEvent.items.get(dlEvent.position);
        switch (dlPart.getType()) {
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.getIntent2Me(this, dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "song":
                startActivity(AudioContentActivity.getIntent2Me(this, dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
//                startActivity(VideoContentActivityNew.buildIntent(this,
//                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
//                        dlPart.getPic(), dlPart.getType(), dlPart.getId(), null));
                startActivity(VideoContentActivity.buildIntent(this,
                        dlPart.getCategoryName(), dlPart.getTitle(), dlPart.getTitleCn(),
                        dlPart.getPic(), dlPart.getType(), dlPart.getId()));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ArtDataSkipEvent event) {
        if (event.voa != null) {
            BasicFavorPart part = new BasicFavorPart();
            part.setType(event.voa.categoryString);
            part.setCategoryName(event.voa.categoryString);
            part.setTitle(event.voa.title);
            part.setTitleCn(event.voa.title_cn);
            part.setPic(event.voa.pic);
            part.setId(event.voa.voaid + "");
            part.setSound(event.voa.sound + "");
            jumpToCorrectFavorActivityByCate(this, part);
        }
    }


    public void jumpToCorrectFavorActivityByCate(Context context, BasicFavorPart item) {
        switch (item.getType()) {
            case "news":
                startActivity(TextContentActivity.getIntent2Me(context, item.getId(), item.getTitle(), item.getTitleCn(), item.getType(),
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource()));
                break;
            case "headnews":
                startActivity(TextContentActivity.getIntent2Me(context, item.getId(), item.getTitle(), item.getTitleCn(), "news",
                        item.getCategoryName(), item.getCreateTime(), item.getPic(), item.getSource()));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
            case "song":
                startActivity(AudioContentActivityNew.getIntent2Me(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "japanvideos":
            case "bbcwordvideo":
            case "topvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(context, item.getCategoryName(), item.getTitle(),
                        item.getTitleCn(), item.getPic(), item.getType(), item.getId(), item.getSound()));
                break;
            case "series":
//                startActivity(new Intent(context, OneMvSerisesView.class)
//                        .putExtra("serisesid", item.getSeriseId())
//                        .putExtra("voaid", item.getId()));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(FavorItemEvent fEvent) {
        //收藏页面点击
        BasicFavorPart fPart = fEvent.items.get(fEvent.position);
        goFavorItem(fPart);
    }


    private void goFavorItem(BasicFavorPart part) {

        switch (part.getType()) {
            case "news":
                startActivity(TextContentActivity.getIntent2Me(mContext, part.getId(), part.getTitle(), part.getTitleCn(), part.getType()
                        , part.getCategoryName(), part.getCreateTime(), part.getPic(), part.getSource()));
                break;
            case "voa":
            case "csvoa":
            case "bbc":
                startActivity(AudioContentActivityNew.getIntent2Me(
                        mContext, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;

            case "song":
                startActivity(AudioContentActivity.getIntent2Me(
                        mContext, part.getCategoryName(), part.getTitle(), part.getTitleCn(),
                        part.getPic(), part.getType(), part.getId(), part.getSound()));
                break;
            case "voavideo":
            case "meiyu":
            case "ted":
            case "bbcwordvideo":
            case "topvideos":
            case "japanvideos":
                startActivity(VideoContentActivityNew.getIntent2Me(mContext,
                        part.getCategoryName(), part.getTitle(), part.getTitleCn(), part.getPic(),
                        part.getType(), part.getId(), part.getSound()));
                break;
            case "series":
                Intent intent = SeriesActivity.buildIntent(mContext, part.getSeriesId(), part.getId());
                startActivity(intent);
                break;
            case "smallvideo":
                startActivity(VideoMiniContentActivity.buildIntentForOne(this, part.getId(), 0, 1, 1));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SecVerifyLoginSuccessEvent event) {
        EventBus.getDefault().post(new RefreshPersonalEvent());
//        initCommonConstants();

    }

//    private void initCommonConstants() {
//
//        String userId = ConfigManager.Instance().loadString("userId", "");
//
//        User user = new User();
//        user.vipStatus = AccountManager.getVipStatus() + "";
//        user.uid = Integer.parseInt(userId);
//        user.name = ConfigManager.Instance().loadString("userName", null);
//        IyuUserManager.getInstance().setCurrentUser(user);
//    }

    private void requestQQGroupNumber() {
        String type = Build.MANUFACTURER.trim().toLowerCase();
        String userID = ConfigManager.Instance().loadString("userId", "0");
        String url = "http://m." + CommonConstant.domain + "/m_login/getQQGroup.jsp?type=" + type + "&userId=" + userID + "&appId=" + Constant.APPID;
        Http.get(url, new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                try {
                    QQGroupBean bean = new Gson().fromJson(response, QQGroupBean.class);
                    if ("true".equals(bean.message)) {
                        ConfigManager.Instance().putString("qq_group_number", bean.QQ);
                        ConfigManager.Instance().putString("qq_group_key", bean.key);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
            }
        });
    }

//    private void autoLogin() {
//        if (SettingConfig.Instance().isAutoLogin()) { // 自动登录
//
//            String[] nameAndPwd = AccountManager.Instance(mContext)
//                    .getUserNameAndPwd();
//            String userName = nameAndPwd[0];
//            String userPwd = nameAndPwd[1];
//            if (NetWorkState.isConnectingToInternet()
//                    && NetWorkState.getAPNType() != 1) {
//                if (userName != null && !userName.equals("")) {
//                    AccountManager.Instance(mContext).login(userName, userPwd,
//                            new OperateCallBack() {
//
//                                @Override
//                                public void success(String message) {
//                                    //AccountManager  处理了
//                                    setCommonData();
//                                    EventBus.getDefault().post(new RefreshPersonalEvent());
//                                }
//
//                                @Override
//                                public void fail(String message) {
//                                    AccountManager.Instance(mContext).setLoginState(1);
//                                    setCommonData();
//                                }
//                            });
//                }
//            } else if (userName != null && !userName.equals("")) {
//                AccountManager.Instance(mContext).setLoginState(1);
//                setCommonData();
//            }
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unBind();
        if (mainPresenter != null) {

            mainPresenter.detachView();
        }
    }

    private void unBind() {
        unbindService(BackgroundManager.Instace().conn);
    }

    private void bindBackgroundService() {
//        Intent intent = new Intent(mContext, Background.class);

//        bindService(intent, BackgroundManager.Instace().conn, Context.BIND_AUTO_CREATE);
    }

    private void initData() {
/*
        ImportCetDatabase dbCourse = new ImportCetDatabase(mContext);
        dbCourse.setPackageName(mContext.getPackageName());
        dbCourse.setVersion(Constant.lastVersion, Constant.currentVersion);// 有需要数据库更改使用
        dbCourse.openDatabase(dbCourse.getDBPath());*/


        ConfigManager.Instance().putInt("lately", 1001);
//        ConfigManager.Instance().putInt("version", currentVersion);
        ConfigManager.Instance().putBoolean("firstuse", true);
        ConfigManager.Instance().putInt("mode", 1);
        ConfigManager.Instance().putInt("type", 2);
        ConfigManager.Instance().putInt("applanguage", 0);
//        ConfigManager.Instance().putBoolean("isvip", false);
        ConfigManager.Instance().putString("updateAD", "1970-01-01");
        ConfigManager.Instance().putInt("download", 1);
        ConfigManager.Instance().putString("wordsort", "0-0");
        ConfigManager.Instance().putString("vocabulary", "study");
        ConfigManager.Instance().putBoolean("push", true);
        ConfigManager.Instance().putBoolean("saying", true);
    }

    private void initView() {

        ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(this);
        video = findViewById(R.id.ll_video);
        mooc = findViewById(R.id.ll_mooc);
        mooc.setOnClickListener(this);
        me = findViewById(R.id.ll_me);
        me.setOnClickListener(this);
        video.setOnClickListener(this);
        main_ll_trainingcamp = findViewById(R.id.main_ll_trainingcamp);
        main_ll_trainingcamp.setOnClickListener(this);


        homePageImage = findViewById(R.id.iv_home_bottom_image_1);
        homePageText = findViewById(R.id.tv_home_bottom_1);
        personalCenterImage = findViewById(R.id.iv_home_bottom_image_me);
        personalCenterText = findViewById(R.id.tv_home_bottom_me);
        videoImage = findViewById(R.id.iv_main_video);
        moocImage = findViewById(R.id.iv_main_mooc);
        main_iv_trainingcamp = findViewById(R.id.main_iv_trainingcamp);

        main_tv_trainingcamp = findViewById(R.id.main_tv_trainingcamp);
        videoText = findViewById(R.id.tv_main_video);
        moocText = findViewById(R.id.tv_main_mooc);
        homePageImage.setImageResource(R.drawable.icon_home_home_on);
        homePageText.setTextColor(getResources().getColor(R.color.colorPrimary));
        fragmentList = new ArrayList<>();
        if (homePageFragment == null) {
            homePageFragment = new HomePageFragment();
            fragmentList.add(homePageFragment);
        }
        if (personalCenterFragment == null) {
            personalCenterFragment = new PersonalCenterFragment();
            fragmentList.add(personalCenterFragment);
        }
        if (dropdownTitleFragmentNew == null) {

            dropdownTitleFragmentNew = DropdownTitleFragmentNew2.newInstance();
            fragmentList.add(dropdownTitleFragmentNew);
        }
        if (iMoocFragment == null) {
            iMoocFragment = NewImoocFragment.newInstance();
            fragmentList.add(iMoocFragment);
        }
        if (trainingCampFragment == null) {
            trainingCampFragment = NewTrainingCampFragment.newInstance();
            fragmentList.add(trainingCampFragment);
        }
        Log.e(TAG, "initView: " + fragmentList.size());
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.blank_layout, homePageFragment);
        transaction.add(R.id.blank_layout, personalCenterFragment);
        transaction.add(R.id.blank_layout, dropdownTitleFragmentNew);
        transaction.add(R.id.blank_layout, iMoocFragment);
        transaction.add(R.id.blank_layout, trainingCampFragment);
        transaction.commit();
        switchFragment(0);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == currentFragmentId) {
            return;
        }
        homePageImage.setImageResource(R.drawable.icon_home_home_bg);
        personalCenterImage.setImageResource(R.drawable.icon_home_me_bg);
        videoImage.setImageResource(R.drawable.ic_video_main_bg);
        moocImage.setImageResource(R.drawable.imooc_bg);
        main_iv_trainingcamp.setImageResource(R.drawable.nav_tc_off);

        videoText.setTextColor(getResources().getColor(R.color.font_grey));
        homePageText.setTextColor(getResources().getColor(R.color.font_grey));
        personalCenterText.setTextColor(getResources().getColor(R.color.font_grey));
        moocText.setTextColor(getResources().getColor(R.color.font_grey));
        main_tv_trainingcamp.setTextColor(getResources().getColor(R.color.font_grey));

        int id = v.getId();

        if (id == R.id.ll_home) {

            View decor = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            currentFragmentId = v.getId();
            homePageImage.setImageResource(R.drawable.icon_home_home_on);
            homePageText.setTextColor(getResources().getColor(R.color.colorPrimary));
            switchFragment(0);
        } else if (id == R.id.ll_me) {

            View decor = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }

            currentFragmentId = v.getId();
            personalCenterImage.setImageResource(R.drawable.icon_home_me_on);
            personalCenterText.setTextColor(getResources().getColor(R.color.colorPrimary));
            switchFragment(1);
//            SecVerifyManager.getInstance().verify(mContext);
            //检测音频是否播放，如果播放则停止
            if (homePageFragment != null) {
                homePageFragment.pauseAudio();
            }

        } else if (id == R.id.ll_video) {

            View decor = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            currentFragmentId = v.getId();
            videoImage.setImageResource(R.drawable.ic_video_main);
            videoText.setTextColor(getResources().getColor(R.color.colorPrimary));
            switchFragment(2);
            //检测音频是否播放，如果播放则停止
            if (homePageFragment != null) {
                homePageFragment.pauseAudio();
            }
        } else if (id == R.id.ll_mooc) {
            currentFragmentId = v.getId();
            moocImage.setImageResource(R.drawable.imooc_select);
            moocText.setTextColor(getResources().getColor(R.color.colorPrimary));
            switchFragment(3);
            Log.e(TAG, "onClick: " + "3");
            //检测音频是否播放，如果播放则停止
            if (homePageFragment != null) {
                homePageFragment.pauseAudio();
            }
        } else if (id == R.id.main_ll_trainingcamp) {

            View decor = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {

                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            currentFragmentId = v.getId();
            main_iv_trainingcamp.setImageResource(R.drawable.nav_tc_on);
            main_tv_trainingcamp.setTextColor(getResources().getColor(R.color.colorPrimary));
            switchFragment(4);
            Log.e(TAG, "onClick: " + "3");
            //检测音频是否播放，如果播放则停止
            if (homePageFragment != null) {
                homePageFragment.pauseAudio();
            }
        }

    }

    private void switchFragment(int index) {

        transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < fragmentList.size(); i++) {

            if (index == i) {
                transaction.show(fragmentList.get(i));
                Log.e(TAG, "switchFragment: " + fragmentList.get(i).toString());
            } else {
                transaction.hide(fragmentList.get(i));
            }
        }
       /* if (index != 2) {
            if (GSYVideoManager.instance().getCurPlayerManager() != null) {
                GSYVideoManager.instance().getCurPlayerManager().getMediaPlayer().pause();
            }
        }*/
        transaction.commit();
    }


    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long waitTime = 2000;
        if ((currentTime - touchTime) >= waitTime) {
            //让Toast的显示时间和等待时间相同
            Toast.makeText(this, "再按一次退出", (int) waitTime).show();
            touchTime = currentTime;
        } else {
            // MainActivity.this.finish();
            System.exit(0);
        }
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void textAllApi(BBCContentBean bbcContentBean, CourseTitleBean.DataDTO dataDTO) {

        if (dataManager == null) {

            dataManager = HeadlinesDataManager.getInstance(this);
        }
        dataManager.saveDetail(bbcContentBean.getData());

        //存储问题到数据库
        List<QuestionBean> questionBeanList = bbcContentBean.getDataQuestion();
        for (int i = 0; i < questionBeanList.size(); i++) {

            QuestionBean questionBean = questionBeanList.get(i);
            long count = dataManager.hasQuestionBean(questionBean.getBbcId(), questionBean.getIndexId());
            if (count > 0) {//有数据

                dataManager.updateQuestion(questionBean);
            } else {

                dataManager.addQuestion(questionBean);
            }
        }

        //创建SumBean.DataDTO
        SumBean.DataDTO dto = new SumBean.DataDTO();
        dto.setBbcId(dataDTO.getBbcId() + "");
        dto.setCategory(dataDTO.getCategory() + "");
        dto.setCreatTime(dataDTO.getCreatTime() + "");
        dto.setDescCn(dataDTO.getDescCn() + "");
        dto.setHotFlg(dataDTO.getHotFlg() + "");
        dto.setPic(dataDTO.getPic() + "");
        dto.setReadCount(dataDTO.getReadCount() + "");
        dto.setSound(dataDTO.getSound() + "");
        dto.setTitle(dataDTO.getTitle() + "");
        dto.setTitle_cn(dataDTO.getTitleCn() + "");
        dto.setIsDownload(0);
        dto.setTexts(dataDTO.getTexts());

        Intent intent = new Intent(mContext, com.ai.bbcpro.ui.player.AudioContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dto);
        bundle.putInt("POSITION", 0);
        bundle.putInt("VPAGE", 0);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void doCheckIPBBC(CheckBBCBean checkBBCBean) {

       /* if (checkBBCBean == null) {

            Pair<Integer, Integer> p = Pair.create(R.array.category_bbc_name_bjhd, R.array.category_bbc_code_bihd);
            IHeadline.setCategoryArrayId(HeadlineType.BBCWORDVIDEO, p);
        } else {


            if (checkBBCBean.getFlag() == 1) {//控制显示

                Pair<Integer, Integer> p = Pair.create(R.array.category_bbc_name_bjhd, R.array.category_bbc_code_bihd);
                IHeadline.setCategoryArrayId(HeadlineType.BBCWORDVIDEO, p);
            } else {

            }
        }*/
    }


    /**
     * 登录
     * @param loginEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEventbus loginEventbus) {

        CommonProgressDialog.showProgressDialog(MainActivity.this);
        SecVerifyManager.getInstance().verify(this);
    }


}