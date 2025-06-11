package com.ai.bbcpro.ui.fragment.content;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;

import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.AnnouncerBean;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.mvp.presenter.home.OriPresenter;
import com.ai.bbcpro.mvp.view.home.OriContract;
import com.ai.bbcpro.service.MediaService;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.activity.MyWebActivity;
import com.ai.bbcpro.ui.fragment.dialog.FamousDialogFragment;
import com.ai.bbcpro.ui.fragment.dialog.LoadingDialogPopup;
import com.ai.bbcpro.ui.helper.GoToVipHelper;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.ai.bbcpro.ui.player.subtitle.Subtitle;
import com.ai.bbcpro.ui.player.subtitle.SubtitleSum;
import com.ai.bbcpro.ui.player.subtitle.SubtitleSynView;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.iyuba.module.toolbox.DensityUtil;
import com.yd.saas.base.interfaces.AdViewBannerListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdBanner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 新闻播放页面
 */
public class AudioContentFragment extends Fragment implements OriContract.OriView {

    private int famousPos = 0;

    private Context mContext;
    private ImageView mPlayButton, add_cn, former, next, controlSpeed;
    //模式
    private ImageView ac_iv_mode;

    private SeekBar mSeekBar;
    private TextView mMusicTotalTime, mCurrentPlayTime;
    private SubtitleSynView subView;
    private int clickSpeedCount;
    private int clickEnCnCount = 0;
    private List<Long> timings;
    private Timer timer;
    private int location = 0;
    private List<BBCContentBean.DataBean> data;
    private HeadlinesDataManager dataManager;
    private AccountManager accountManager;
    private String TAG = "AudioContentFragment";

    private ServiceConnection serviceConnection;

    private MediaService.MusicController musicController;
    private BroadcastReceiver broadcastReceiver;

    private OriPresenter oriPresenter;

    private FrameLayout ap_fl_ad;

    private YdBanner mBanner;

    private boolean isRequestTitle = false;

    private AdEntryBean.DataDTO adDataDTO;

    private SumBean.DataDTO sumBeanData;

    //接口请求的分类值
    private int parentId;

    private TextView ori_tv_famous;

    private Handler soundPlayHandler;

    private LoadingDialogPopup loadingDialogPopup;

    public AudioContentFragment() {

        //默认得有
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    /**
     * @param dataDTO 数据列表
     * @return
     */
    public static AudioContentFragment newInstance(SumBean.DataDTO dataDTO, int parentId) {

        AudioContentFragment audioContentFragment = new AudioContentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", dataDTO);
        bundle.putInt("PARENT_ID", parentId);
        audioContentFragment.setArguments(bundle);
        return audioContentFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Bundle bundle = getArguments();
        if (bundle != null) {

            sumBeanData = (SumBean.DataDTO) bundle.getSerializable("DATA");
            parentId = bundle.getInt("PARENT_ID", 0);
        }

        oriPresenter = new OriPresenter();
        oriPresenter.attchView(this);

        initService();
        initBroadcast();

        adHandler.sendEmptyMessageDelayed(1, 3000);
    }

    Handler adHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            isRequestTitle = false;
            getAd();
            adHandler.sendEmptyMessageDelayed(1, 30 * 1000);
            return false;
        }
    });

    private void getAd() {

        if (oriPresenter != null) {

            String uid = ConfigManager.Instance().loadString("userId", "0");

            if (!AccountManager.isVip()) {

                oriPresenter.getAdEntryAll(Constant.ADAPPID, 4, uid);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    /**
     * 初始化
     */
    private void initService() {

        Intent intent = new Intent(MainApplication.getApplication(), MediaService.class);
        MainApplication.getApplication().startService(intent);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                musicController = (MediaService.MusicController) service;

                musicController.setParentId(parentId);//用来判断是不是要在全部分类中查找
                //是否正在播放，正在播放则判断是否是同一个数据，如果是同一个数据则不做操作，如果不是同一个数据则直接播新的数据；没有正在播放则直接播放新的数据
                if (musicController.isPlaying()) {

                    SumBean.DataDTO newDataDTO = sumBeanData;
                    SumBean.DataDTO dataDTO = musicController.getCurData();

                    if (!newDataDTO.getBbcId().equals(dataDTO.getBbcId())) {

                        musicController.setData(newDataDTO);
                        musicController.play();
                    }
                    mPlayButton.setImageResource(R.drawable.headline_audio_pause);
                } else {

                    musicController.setData(sumBeanData);
                    musicController.play();
                    mPlayButton.setImageResource(R.drawable.headline_audio_pause);
                }
                //模式
                int mode = musicController.getMode();
                if (mode == MediaService.MODE_RANDOM) {

                    ac_iv_mode.setImageResource(R.mipmap.icon_home_random_g);
                } else if (mode == MediaService.MODE_LIST) {

                    ac_iv_mode.setImageResource(R.mipmap.icon_home_list_g);
                } else {

                    ac_iv_mode.setImageResource(R.mipmap.icon_home_single_g);
                }
                //启动新闻首页的播放器
                LocalBroadcastManager
                        .getInstance(MainApplication.getApplication())
                        .sendBroadcast(new Intent(MainApplication.getApplication().getPackageName() + ".player"));
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        MainApplication.getApplication().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void initBroadcast() {

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();
                if (action.equals(MediaService.FLAG_MEDIA_BROADCAST)) {//通知点击事件间接相关，用来更新播放按钮

                    if (musicController != null) {

                        if (musicController.isPlaying()) {

                            mPlayButton.setImageResource(R.drawable.headline_audio_pause);
                        } else {

                            mPlayButton.setImageResource(R.drawable.headline_audio_play);
                        }
                    }
                } else if (action.equals(MediaService.FLAG_SWITCH_DATA)) {//切换数据

                    AudioContentActivity audioContentActivity = (AudioContentActivity) getActivity();
                    if (audioContentActivity != null && musicController != null) {
                        audioContentActivity.setNewData(musicController.getCurData(), -1);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MediaService.FLAG_SWITCH_DATA);
        intentFilter.addAction(MediaService.FLAG_MEDIA_BROADCAST);
        LocalBroadcastManager
                .getInstance(MainApplication.getApplication())
                .registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (adHandler != null) {

            adHandler.removeCallbacksAndMessages(null);
        }
        if (mBanner != null) {

            mBanner.destroy();
        }
        if (oriPresenter != null) {

            oriPresenter.detachView();
        }
        if (timer != null) {

            timer.cancel();
        }

        if (broadcastReceiver != null) {

            LocalBroadcastManager.getInstance(MainApplication.getApplication()).unregisterReceiver(broadcastReceiver);
        }

        if (serviceConnection != null) {

            MainApplication.getApplication().unbindService(serviceConnection);
        }
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_audio_play, container, false);
        dataManager = HeadlinesDataManager.getInstance(mContext);
        initView(root);
        accountManager = AccountManager.Instance(mContext);
        initFunction();
        return root;
    }

    private void initFunction() {

        getData(sumBeanData.getBbcId());

        controlSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountManager.checkUserLogin()) {
                    if (AccountManager.isVip()) {
                        clickSpeedCount += 1;
                        int finalCount = clickSpeedCount % 5;
                        if (musicController != null) {
                            if (finalCount == 0) {
                                controlSpeed.setImageResource(R.mipmap.speed_1);
                                musicController.setPlaySpeed(1.0f);
                            } else if (finalCount == 1) {
                                controlSpeed.setImageResource(R.mipmap.speed_1_25);
                                musicController.setPlaySpeed(1.25f);
                            } else if (finalCount == 2) {
                                controlSpeed.setImageResource(R.mipmap.speed_1_5);
                                musicController.setPlaySpeed(1.5f);
//                    }else if (finalCount == 3){
//                        controlSpeed.setImageResource(R.mipmap.speed_2);
//                        setPlaySpeed(2.0f);
                            } else if (finalCount == 3) {
                                controlSpeed.setImageResource(R.mipmap.speed_0_5);
                                musicController.setPlaySpeed(0.5f);
                            } else if (finalCount == 4) {
                                controlSpeed.setImageResource(R.mipmap.speed_0_75);
                                musicController.setPlaySpeed(0.75f);
                            }
                        }
                    } else {
                        GoToVipHelper.getInstance().takeYourMoney(mContext);
                    }
                } else {
                    GoToVipHelper.getInstance().goToLogin(getActivity());
                }


            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController != null) {

                    if (musicController.isPlaying()) {
                        musicController.pause();
                        mPlayButton.setImageResource(R.drawable.headline_audio_play);
                    } else {
                        musicController.start();
                        mPlayButton.setImageResource(R.drawable.headline_audio_pause);
                    }
                }
            }
        });

        add_cn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickEnCnCount += 1;
                if (clickEnCnCount % 2 != 0) {
                    // lrcView.loadLrc(lrcText, lrcTextCn);
                    subView.changeToCN();
                    subView.setTextSize(16);
                    add_cn.setImageResource(R.drawable.encn);

                } else {
                    // lrcView.loadLrc(lrcText);
                    subView.changeToEn();
                    subView.setTextSize(16);
                    add_cn.setImageResource(R.drawable.only_en);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }

                long curTime2 = musicController.getPosition();
                for (int i = 0; i < timings.size(); i++) {
                    // System.out.println(getPosition() + "=======" + timings.get(i));
                    if (curTime2 > timings.get(timings.size() - 1)) {
                        musicController.seek(timings.get(timings.size() - 1).intValue());
                        musicController.start();
                        break;
                    } else if (curTime2 < timings.get(0)) {
                        System.out.println("==========seek" + timings.get(timings.size() - 1).intValue());
                        musicController.seek(timings.get(0).intValue());
                        musicController.start();
                        break;
                    } else if (curTime2 > timings.get(i) && curTime2 < timings.get(i + 1)) {
                        // pause();
                        if (i != timings.size() - 1) {
                            musicController.seek(timings.get(i + 1).intValue());
                        } else {
                            musicController.seek(timings.get(i).intValue());
                        }
                        musicController.start();
                        break;
                    }
                }
            }
        });
        former.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                long curTime = musicController.getPosition();
                for (int i = 0; i < timings.size(); i++) {
                    // System.out.println(getPosition() + "=======" + timings.get(i));
                    if (curTime < timings.get(0)) {
                        musicController.seek(0);
                        musicController.start();
                        break;
                    } else if (curTime > timings.get(timings.size() - 1)) {
                        System.out.println("==========seek" + timings.get(timings.size() - 1).intValue());
                        musicController.seek(timings.get(timings.size() - 1).intValue());
                        musicController.start();
                        break;
                    } else if (curTime > timings.get(i) && curTime < timings.get(i + 1)) {
                        // pause();
                        if (i != 0) {
                            musicController.seek(timings.get(i - 1).intValue());
                        } else {
                            musicController.seek(timings.get(i).intValue());
                        }
                        musicController.start();
                        break;
                    }
                }
            }
        });
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    location = progress;
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                    Log.e("TAG", "onStopTrackingTouch: " + location);
                    musicController.seek(seekBar.getProgress());
                } else {
                    musicController.seek(seekBar.getProgress());
                }
            }
        });
        ac_iv_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (musicController == null) {

                    return;
                }
                int mode = musicController.getMode();
                if (mode == 1) {

                    musicController.setPlayMode(MediaService.MODE_LIST);
                    ac_iv_mode.setImageResource(R.mipmap.icon_home_list_g);
                    toast("列表循环");
                } else if (mode == 2) {

                    musicController.setPlayMode(MediaService.MODE_SINGLE);
                    ac_iv_mode.setImageResource(R.mipmap.icon_home_single_g);
                    toast("单曲循环");
                } else {

                    musicController.setPlayMode(MediaService.MODE_RANDOM);
                    ac_iv_mode.setImageResource(R.mipmap.icon_home_random_g);
                    toast("随机播放");
                }
            }
        });
    }

    @Override
    public void showLoading(String msg) {

        if (loadingDialogPopup == null) {

            loadingDialogPopup = new LoadingDialogPopup();
            loadingDialogPopup.show(getChildFragmentManager(), "loading");
        } else {

            loadingDialogPopup.show(getChildFragmentManager(), "loading");
        }
    }

    @Override
    public void hideLoading() {

        if (loadingDialogPopup != null) {

            loadingDialogPopup.dismiss();
        }
    }

    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 2) {
                    ToastUtil.showToast(mContext, "您尚未开通VIP,暂时无法使用此功能");
                }
                break;
        }
    }

    private void initView(View root) {
        subView = root.findViewById(R.id.subtitle);
        mPlayButton = root.findViewById(R.id.media_play);
        mSeekBar = root.findViewById(R.id.seekbar_headlines_player);
        mCurrentPlayTime = root.findViewById(R.id.tv_current_time);
        mMusicTotalTime = root.findViewById(R.id.tv_total_time);
        add_cn = root.findViewById(R.id.add_cn);
        former = root.findViewById(R.id.former_button);
        next = root.findViewById(R.id.next_button);
        controlSpeed = root.findViewById(R.id.change_speed);
        ac_iv_mode = root.findViewById(R.id.ac_iv_mode);

        ap_fl_ad = root.findViewById(R.id.ap_fl_ad);

        ori_tv_famous = root.findViewById(R.id.ori_tv_famous);
        ori_tv_famous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isLogin = AccountManager.Instance(MainApplication.getApplication()).checkUserLogin();
                if (isLogin) {

                    String uid = ConfigManager.Instance().loadString("userId", "0");
                    oriPresenter.getBroadcastOptions(uid);
                } else {

                    EventBus.getDefault().post(new LoginEventbus());;
                }
            }
        });
    }

    public void getData(String bbcid) {

        //使用名人配音的句子
        if (musicController != null && musicController.getCurData() != null && musicController.getCurData().getAnnouncerBean() != null) {

            data = musicController.getCurData().getAnnouncerBean().getVoatext();
        } else {//使用数据库里面的句子

            data = dataManager.loadSentence(sumBeanData.bbcId);
        }

        ArrayList<Subtitle> list = new ArrayList<>();
        timings = new ArrayList<>();
        for (BBCContentBean.DataBean sen : data) {
            long temp = (long) Double.parseDouble(sen.getTiming()) * 1000;
            timings.add(temp);
            Subtitle tempTitle = new Subtitle();
            tempTitle.content = sen.getSentence();
            tempTitle.paragraph = data.indexOf(sen);
            tempTitle.contentCn = sen.getSentenceCn();
            tempTitle.pointInTime = Double.parseDouble(sen.getTiming()) * 1000;
            list.add(tempTitle);
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SubtitleSum subtitleSum = new SubtitleSum();
        subtitleSum.subtitles = list;
        subView.setSubtitleSum(subtitleSum);
        subView.setTextSize(16);
        timeUpdate();
    }


    private void timeUpdate() {


        if (soundPlayHandler == null) {

            soundPlayHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message msg) {


                    if (musicController != null) {

                        if (musicController.isPlaying()) {

                            int curPosition = (int) musicController.getPosition();
                            int duration = (int) musicController.getMusicDuration();

                            int currentTime = Math.round((float) curPosition / 1000);
                            int totalTime = Math.round((float) duration / 1000);

                            String str_totalTime = String.format("%02d:%02d", totalTime / 60, totalTime % 60);
                            String str_currTime = String.format("%02d:%02d", currentTime / 60, currentTime % 60);

                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {

                                    mMusicTotalTime.setText(str_totalTime);
                                    mCurrentPlayTime.setText(str_currTime);
                                    mSeekBar.setProgress(curPosition);
                                    mSeekBar.setMax(duration);
                                }
                            });

                            for (int i = 0; i < timings.size(); i++) {
                                if (curPosition > timings.get(timings.size() - 1)) {
                                    subView.snyParagraph(timings.size());
                                } else if (curPosition > timings.get(i) && curPosition < timings.get(i + 1)) {
                                    subView.snyParagraph(i + 1);
                                }
                            }
                        }
                    }
                    if (!soundPlayHandler.hasMessages(1)) {

                        soundPlayHandler.sendEmptyMessageDelayed(1, 200);
                    }
                    return false;
                }
            });
        }

        if (!soundPlayHandler.hasMessages(1)) {

            soundPlayHandler.sendEmptyMessage(1);
        }
    }

    /**
     * 切换数据
     */
    public void switchData() {

        Bundle bundle = getArguments();
        if (bundle != null && sumBeanData != null) {

            sumBeanData = (SumBean.DataDTO) bundle.getSerializable("DATA");
            if (sumBeanData != null) {

                getData(sumBeanData.getBbcId());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaPause mediaPause) {

        pauseMedia();
    }


    /**
     * 代码控制关闭播放的新闻
     */
    public void pauseMedia() {

        if (musicController == null) {

            return;
        }
        if (musicController.isPlaying()) {

            musicController.pause();
        }
    }

    /**
     * 更新数据
     */
    public void updateData() {

        data = dataManager.loadSentence(sumBeanData.bbcId);
        ArrayList<Subtitle> list = new ArrayList<>();
        timings = new ArrayList<>();
        for (BBCContentBean.DataBean sen : data) {
            long temp = (long) Double.parseDouble(sen.getTiming()) * 1000;
            timings.add(temp);
            Subtitle tempTitle = new Subtitle();
            tempTitle.content = sen.getSentence();
            tempTitle.paragraph = data.indexOf(sen);
            tempTitle.contentCn = sen.getSentenceCn();
            tempTitle.pointInTime = Double.parseDouble(sen.getTiming()) * 1000;
            list.add(tempTitle);
        }
        if (subView != null) {

            SubtitleSum subtitleSum = new SubtitleSum();
            subtitleSum.subtitles = list;
            subView.setSubtitleSum(subtitleSum);
            subView.setTextSize(16);
        }
    }

    @Override
    public void getAdEntryAll(List<AdEntryBean> adEntryBeans) {


        AdEntryBean adEntryBean = adEntryBeans.get(0);
        adDataDTO = adEntryBean.getData();
        String type = adDataDTO.getType();
        if (type.equals("web")) {//自己加的广告


            dealWeb();
        } else if (type.startsWith("ads")) {//第三方广告

            dealAds(type);
        }
    }

    @Override
    public void getBroadcastOptions(FamousPersonBean famousPersonBean) {

        showFamousList(famousPersonBean.getAnnouncerData());
    }

    @Override
    public void announcer(AnnouncerBean announcerBean) {

        hideLoading();
        if (announcerBean != null) {

            if (announcerBean.getResult().equals("200")) {

                if (musicController != null) {

                    musicController.getCurData().setAnnouncerBean(announcerBean);
                    musicController.playFamous();
                }
                getData(null);
            }
        }
    }

    @Override
    public void announcerFail(Exception e) {

        hideLoading();
        Toast.makeText(MainApplication.getApplication(), "请求超时", Toast.LENGTH_SHORT).show();
    }


    /**
     * 展示配音列表
     * @param announcerDataDTOList
     */
    private void showFamousList(List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList) {

        FamousDialogFragment famousDialogFragment = new FamousDialogFragment();
        famousDialogFragment.show(getChildFragmentManager(), "名人配音");


        announcerDataDTOList.add(0, new FamousPersonBean.AnnouncerDataDTO("", "官方"));

        if (musicController != null && musicController.getCurData() != null && musicController.getCurData().getAnnouncerBean() != null) {

            String speaker = musicController.getCurData().getAnnouncerBean().getSpeaker();
            int fPos = 0;
            for (int i = 0; i < announcerDataDTOList.size(); i++) {

                FamousPersonBean.AnnouncerDataDTO announcerDataDTO = announcerDataDTOList.get(i);
                if (announcerDataDTO.getSpeaker().equals(speaker)) {

                    fPos = i;
                    break;
                }
            }
            famousDialogFragment.setFamousPos(fPos);
        } else {

            famousDialogFragment.setFamousPos(famousPos);
        }

        famousDialogFragment.setAnnouncerDataDTOList(announcerDataDTOList);
        famousDialogFragment.setCallback(new FamousDialogFragment.Callback() {
            @Override
            public void generate(int pos, FamousPersonBean.AnnouncerDataDTO announcerDataDTO) {

                famousDialogFragment.dismiss();

                if (announcerDataDTO.getSpeakerReal().equals("官方")) {

                    if (musicController != null) {

                        musicController.getCurData().setAnnouncerBean(null);
                        musicController.play();
                    }
                    getData(null);
                } else {

                    famousPos = pos;
                    showLoading(null);
                    oriPresenter.announcer(announcerDataDTO.getSpeaker(), "普通", sumBeanData.bbcId, "bbc", 0, 0);
                }
            }
        });


    }

    private void dealWeb() {

        ap_fl_ad.setVisibility(View.VISIBLE);
        ap_fl_ad.removeAllViews();
        View bannerView = LayoutInflater.from(MainApplication.getApplication()).inflate(R.layout.banner_ad, null);
        ImageView banner_iv_pic = bannerView.findViewById(R.id.banner_iv_pic);
        TextView banner_tv_title = bannerView.findViewById(R.id.banner_tv_title);
        ImageView banner_iv_ad = bannerView.findViewById(R.id.banner_iv_ad);
        ap_fl_ad.addView(bannerView);

        bannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (adDataDTO.getStartuppicUrl() != null && !adDataDTO.getStartuppicUrl().isEmpty()) {

                    MyWebActivity.startActivity(requireActivity(), adDataDTO.getStartuppicUrl(), "详情");
                }
            }
        });

        String picUrl = "http://static3." + CommonConstant.domain + "/dev/" + adDataDTO.getStartuppic();
        Glide.with(MainApplication.getApplication()).load(picUrl).into(banner_iv_pic);
        banner_tv_title.setText(adDataDTO.getAdId());
        banner_iv_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ap_fl_ad.removeAllViews();
                ap_fl_ad.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 处理广告
     *
     * @param type
     */
    private void dealAds(String type) {

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = DensityUtil.dp2px(MainApplication.getApplication(), 70);

        String adKey = null;
        if (type.equals(Constant.AD_ADS2)) {

            adKey = BuildConfig.BANNER_AD_KEY_CSJ;
        } else if (type.equals(Constant.AD_ADS4)) {

            adKey = BuildConfig.BANNER_AD_KEY_YLH;
        }
        if (adKey != null) {

            mBanner = new YdBanner.Builder(requireActivity())
                    .setKey(adKey)
                    .setWidth(width)
                    .setHeight(height)
                    .setMaxTimeoutSeconds(5)
                    .setBannerListener(adViewBannerListener)
                    .build();

            mBanner.requestBanner();

            Log.d("banner___", adKey);
        }
    }

    /**
     * 广告的回调事件
     */
    AdViewBannerListener adViewBannerListener = new AdViewBannerListener() {
        @Override
        public void onReceived(View view) {

            ap_fl_ad.removeAllViews();
            ap_fl_ad.setVisibility(View.VISIBLE);
            ap_fl_ad.addView(view);

            Log.d("banner ad", "onReceived");
        }

        @Override
        public void onAdExposure() {

        }

        @Override
        public void onAdClick(String s) {

        }

        @Override
        public void onClosed() {

            ap_fl_ad.setVisibility(View.GONE);
        }

        @Override
        public void onAdFailed(YdError ydError) {

            if (!isRequestTitle) {

                isRequestTitle = true;
                dealAds(adDataDTO.getTitle());
            }
            Log.d("banner ad", ydError.toString());
        }
    };


}
