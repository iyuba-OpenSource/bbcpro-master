package com.ai.bbcpro.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.home.HomePagePresenter;
import com.ai.bbcpro.mvp.view.home.HomePageContract;
import com.ai.bbcpro.service.MediaService;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.activity.home.SearchActivity;
import com.ai.bbcpro.ui.adapter.TitlePagerAdapter;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面 -  首页fragment
 */
public class HomePageFragment extends Fragment implements HomePageContract.HomePageView {
    private Context mContext;
    private List<String> titleTypes = new ArrayList();
    private List<Fragment> fragmentList = new ArrayList();
    private ViewPager viewPager;
    private TitlePagerAdapter adapter;
    private String TAG = "HomePageFragment";
    //    private List<SumBean.DataDTO> mData;
    private MagicIndicator indicator;


    /**
     * mediaservice的binder
     */
    private MediaService.MusicController musicController;

    private ServiceConnection serviceConnection;

    /**
     * 播放器标题
     */
    private TextView title_tv_title;

    /**
     * 播放器播放按钮
     */
    private ImageView title_iv_sound;

    /**
     * 播放器的播放模式
     */
    private ImageView title_iv_playtype;

    /**
     * 播放器的
     */
    private LinearLayout title_ll_sound;

    /**
     * 接收音频相关的广播。暂停  播放
     */
    private BroadcastReceiver broadcastReceiver;


    private HomePagePresenter homePagePresenter;

    //正常情况下的标题
    private String[] cates = {"全部", "新闻", "六分钟英语", "新闻词汇", "地道英语", "职场英语", "经济学人", "ITV新闻", "Skype新闻"};
    //隐藏状态下显示的title
    private String[] cates2 = {"Skype新闻", "ITV新闻", "经济学人"};

    /**
     * 分类请求超时
     */
    private LinearLayout home_ll_error;

    /**
     * 界面
     */
    private LinearLayout home_ll_jiemian;


    /**
     * 接口需要的参数，平台brand
     */
    private String brand;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void initView(View view) {


        home_ll_error = view.findViewById(R.id.home_ll_error);
        home_ll_jiemian = view.findViewById(R.id.home_ll_jiemian);

        indicator = view.findViewById(R.id.magic_indicator);
        viewPager = view.findViewById(R.id.vp1);

        //新闻播放器
        title_ll_sound = view.findViewById(R.id.title_ll_sound);
        title_iv_playtype = view.findViewById(R.id.title_iv_playtype);
        title_iv_sound = view.findViewById(R.id.title_iv_sound);
        title_tv_title = view.findViewById(R.id.title_tv_title);

        //异常的点击事件
        home_ll_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                home_ll_error.setVisibility(View.GONE);
                String uid = ConfigManager.Instance().loadString("userId", "0");
                String vipStatus = ConfigManager.Instance().loadInt("isvip2", 0) + "";
                homePagePresenter.doCheckIPBBC(uid, Constant.APPID + "", brand, vipStatus);
            }
        });

        title_ll_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController != null) {

                    SumBean.DataDTO dataDTO = musicController.getCurData();
                    AudioContentActivity.startActivity(getActivity(), dataDTO, 0, 0);
                }
            }
        });

        title_iv_playtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }

                if (musicController.getMode() == 1) {

                    musicController.setPlayMode(2);
                    title_iv_playtype.setImageResource(R.mipmap.icon_home_list);
                    Toast.makeText(MainApplication.getApplication(), "列表播放", Toast.LENGTH_SHORT).show();
                } else if (musicController.getMode() == 2) {

                    musicController.setPlayMode(3);
                    title_iv_playtype.setImageResource(R.mipmap.icon_home_single);
                    Toast.makeText(MainApplication.getApplication(), "单曲播放", Toast.LENGTH_SHORT).show();
                } else {

                    musicController.setPlayMode(1);
                    title_iv_playtype.setImageResource(R.mipmap.icon_home_random);
                    Toast.makeText(MainApplication.getApplication(), "随机播放", Toast.LENGTH_SHORT).show();
                }
            }
        });
        title_iv_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                if (musicController.isPlaying()) {

                    musicController.pause();
                    title_iv_sound.setImageResource(R.mipmap.icon_home_play);
                } else {

                    musicController.start();
                    title_iv_sound.setImageResource(R.mipmap.icon_home_pause);
                }
            }
        });
        ImageView news_iv_search = view.findViewById(R.id.news_iv_search);
        news_iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
    }


    /**
     * @param flag 1 隐藏title    非1：全部显示
     */
    private void initTitles(int flag) {

        String[] array;
        int[] numbers;
        if (flag == 1) {

            array = cates2;
        } else {

            array = cates;
        }
        numbers = new int[array.length];
        for (int i = 0; i < array.length; i++) {

            numbers[i] = getParentID(array[i]);
        }

        for (int i = 0; i < array.length; i++) {
            titleTypes.add(array[i]);
            Bundle bundle = TitleFragmentNew.buildArguments(numbers[i]);
            fragmentList.add(TitleFragmentNew.newInstance(bundle));
        }
    }


    /**
     * 根据
     *
     * @param cate
     * @return
     */
    private int getParentID(String cate) {
        int result = 0;
        if ("新闻".equals(cate)) {
            result = 3;
        } else if ("六分钟英语".equals(cate)) {
            result = 1;
        } else if ("新闻词汇".equals(cate)) {
            result = 4;
        } else if ("地道英语".equals(cate)) {
            result = 5;
        } else if ("职场英语".equals(cate)) {
            result = 2;
        } else if ("经济学人".equals(cate)) {
            result = 8;
        } else if ("ITV新闻".equals(cate)) {
            result = 9;
        } else if ("Skype新闻".equals(cate)) {
            result = 10;
        }
        return result;
    }

    /**
     * 处理串音问题，暂停后台播放
     *
     * @param mediaPause
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPause mediaPause) {

        if (musicController != null && musicController.isPlaying()) {

            musicController.pause();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        homePagePresenter = new HomePagePresenter();
        homePagePresenter.attchView(this);
        initBroadcast();
        EventBus.getDefault().register(this);

        brand = BuildConfig.BRAND;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (homePagePresenter != null) {

            homePagePresenter.detachView();
        }
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment_new, container, false);
        initView(view);
        //设置fake_status_bar的高度
        View v = view.findViewById(R.id.fake_status_bar);
        v.getLayoutParams().height = Constant.STATUSBAR_HEIGHT;

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String uid = ConfigManager.Instance().loadString("userId", "0");
        String vipStatus = ConfigManager.Instance().loadInt("isvip2", 0) + "";
        homePagePresenter.doCheckIPBBC(uid, Constant.APPID + "", brand, vipStatus);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateMediaStatus();
    }

    /**
     * 绑定数据
     */
    private void bindServer() {

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                musicController = (MediaService.MusicController) service;
                SumBean.DataDTO dataDTO = musicController.getCurData();
                title_tv_title.setText(dataDTO.getTitle());

                title_ll_sound.setVisibility(View.VISIBLE);
                title_iv_sound.setImageResource(R.mipmap.icon_home_pause);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(getContext(), MediaService.class);
        MainApplication.getApplication().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * 用来接收播放器动作的的监听器
     */
    private void initBroadcast() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(MediaService.FLAG_MEDIA_BROADCAST)) {//通知点击事件间接相关，用来更新播放按钮

                    if (musicController != null) {

                        SumBean.DataDTO dataDTO = musicController.getCurData();
                        title_tv_title.setText(dataDTO.getTitle());
                        if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                            title_iv_sound.setImageResource(R.mipmap.icon_home_pause);
                        } else {

                            title_iv_sound.setImageResource(R.mipmap.icon_home_play);
                        }

                        //模式
                        int mode = musicController.getMode();
                        if (mode == MediaService.MODE_RANDOM) {

                            title_iv_playtype.setImageResource(R.mipmap.icon_home_random);
                        } else if (mode == MediaService.MODE_LIST) {

                            title_iv_playtype.setImageResource(R.mipmap.icon_home_list);
                        } else {

                            title_iv_playtype.setImageResource(R.mipmap.icon_home_single);
                        }
                    }
                } else if (action.equals(MediaService.FLAG_MEDIA_PLAY)) {//绑定播放器获取musicController

                    if (musicController == null) {
                        bindServer();
                    }
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_MEDIA_PLAY);
        intentFilter.addAction(MediaService.FLAG_MEDIA_BROADCAST);
        intentFilter.addAction(MediaService.FLAG_SWITCH_DATA);
        LocalBroadcastManager
                .getInstance(MainApplication.getApplication())
                .registerReceiver(broadcastReceiver, intentFilter);
    }


    /**
     * 获取后台播放的状态来更新当前界面的播放器
     */
    private void updateMediaStatus() {

        if (musicController != null) {

            title_tv_title.setText(musicController.getCurData().getTitle());
            if (musicController.isPlaying()) {//判断是否正在播放来设置按钮的图标

                title_iv_sound.setImageResource(R.mipmap.icon_home_pause);
            } else {

                title_iv_sound.setImageResource(R.mipmap.icon_home_play);
            }
            //模式更新
            int mode = musicController.getMode();
            if (mode == MediaService.MODE_RANDOM) {

                title_iv_playtype.setImageResource(R.mipmap.icon_home_random);
            } else if (mode == MediaService.MODE_LIST) {

                title_iv_playtype.setImageResource(R.mipmap.icon_home_list);
            } else {

                title_iv_playtype.setImageResource(R.mipmap.icon_home_single);
            }
        }
    }

    /**
     * 点击其他导航栏触发暂停，防止串音
     */
    public void pauseAudio() {

        if (musicController != null) {

            if (musicController.isPlaying()) {

                musicController.pause();
                //更新主界面的接口
                title_iv_sound.setImageResource(R.mipmap.icon_home_play);
            }
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
    public void doCheckIPBBC(CheckBBCBean checkBBCBean) {


        if (checkBBCBean == null) {

            home_ll_error.setVisibility(View.VISIBLE);
        } else {

            home_ll_jiemian.setVisibility(View.VISIBLE);
            initTitles(checkBBCBean.getFlag());

            CommonNavigator commonNavigator = new CommonNavigator(mContext);
            commonNavigator.setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return titleTypes == null ? 0 : titleTypes.size();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, int index) {
                    ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                    colorTransitionPagerTitleView.setNormalColor(Color.BLACK);
                    colorTransitionPagerTitleView.setSelectedColor(MainApplication.getApplication().getResources().getColor(R.color.colorPrimary));
                    colorTransitionPagerTitleView.setText(titleTypes.get(index));
                    colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewPager.setCurrentItem(index);
                        }
                    });
                    return colorTransitionPagerTitleView;

                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                    indicator.setColors(MainApplication.getApplication().getResources().getColor(R.color.colorPrimary));
                    return indicator;
                }
            });
            indicator.setNavigator(commonNavigator);
            adapter = new TitlePagerAdapter(getChildFragmentManager(), fragmentList, titleTypes);
            viewPager.setAdapter(adapter);
            viewPager.setOffscreenPageLimit(1);
            ViewPagerHelper.bind(indicator, viewPager);
        }
    }
}
