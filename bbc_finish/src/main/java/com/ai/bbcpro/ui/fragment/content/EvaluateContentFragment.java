package com.ai.bbcpro.ui.fragment.content;

import static com.ai.bbcpro.manager.RuntimeManager.getSystemService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.home.ApiWordBean;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.presenter.home.EvaluateContentPresenter;
import com.ai.bbcpro.mvp.view.home.EvaluateContentContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.adapter.EvaluateAdapter;
import com.ai.bbcpro.ui.bean.MixRecBean;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.event.RefreshRankEvent;
import com.ai.bbcpro.ui.helper.GoToVipHelper;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.net.MixSentenceUploadService;
import com.ai.bbcpro.ui.player.eval.AudioMergerView;
import com.ai.bbcpro.ui.player.eval.EvalResponse;
import com.ai.bbcpro.ui.player.eval.EvalWord;
import com.ai.bbcpro.ui.player.eval.MergeAudioMeta;
import com.ai.bbcpro.ui.player.eval.MergeAudioResponse;
import com.ai.bbcpro.ui.player.eval.RecycleViewData;
import com.ai.bbcpro.ui.player.rank.MixSentenceResponse;
import com.ai.bbcpro.ui.utils.AudioRecorderUtils;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.util.MD5;
import com.ai.bbcpro.util.popup.ApiWordPopup;
import com.ai.bbcpro.util.widget.SelectWordTextView;
import com.ai.common.CommonConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.mob.MobSDK;
import com.tbruyelle.rxpermissions3.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 首页-评测界面
 */
public class EvaluateContentFragment extends Fragment implements View.OnClickListener
        , EvaluateContentContract.EvaluateContentView {


    private String TAG = "Evaluate";
    private Context mContext;
    private HeadlinesDataManager dataManager;
    private String bbcId, sound;
    private int srcPlayCounts = 0;
    private RecyclerView recyclerView;
    private EvaluateAdapter evaluateAdapter;
    private List<RecycleViewData> dataList = new ArrayList<>();
    private MediaPlayer player;  // 原文音频播放器
    private boolean isPreparedOri = false;
    private String url;//元原音频地址


    private MediaPlayer recPlayer;  // 录音音频播放器
    private Timer timer; // 播放原文音频的定时器
    //    private Timer timerRec; // 播放用户录音的定时器
    private AudioRecorderUtils recorderManager;
    private Dialog dialog;
    private String recFilePath = "";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private AudioMergerView audioMergerView;
    private AccountManager accountManager;
    private String mixAudio = "";
    private String uid = "";

    OkHttpClient client = new OkHttpClient();
    private int avrScore = 0;
    private final OnekeyShare oks = new OnekeyShare();

    private final int LOAD_HISTORY_FINISH = 0; // 加载历史评测信息完成
    private final int PLAY_ORI_OVER = 1; // 原文音频播放完毕
    private final int PLAY_REC = 14; // 播放录音
    private final int PAUSE_REC = 15; // 录音暂停
    private final int CONTINUE_REC_PLAY = 16; // 录音暂停
    private final int PLAY_REC_OVER = 2; // 原文录音完毕
    private final int EVAL_OVER = 3; // 评测完毕
    private final int EVAL_COUNT_NOT_ENOUGH = 4; // 评测数量不足
    private final int EVAL_COUNT_ENOUGH = 5; // 评测数量足够，调用合成方法
    private final int MIX_SUCCESS = 6; // 合成成功
    private final int MIX_FAIL = 7; // 合成失败
    private final int UPLOAD_MIX_SUCCESS = 8; // 合成失败
    private final int UPLOAD_MIX_FAIL = 9; // 合成失败
    private final int PLAY_MIX_OVER = 10; // 合成音频播放完成
    private final int UPDATE_CUR_TIME = 11; // 更新合成音频进度条
    private final int UPLOAD_SINGLE_SUCCESS = 12; // 上传单句评测成功
    private final int UPLOAD_SINGLE_FAILED = 13; // 上传单句评测失败
    private final int CALLBACK_RECORD_DB = 17; // 录音分贝回调
    private final int RECORDING = 18; // 正在录音中


    private AudioManager audioManager;


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case PLAY_ORI_OVER:
                    dataList.get(msg.arg1).setPlayOri(false);
                    evaluateAdapter.setDataList(dataList);
                    break;
                case EVAL_OVER:
                    dialog.dismiss();
                    evaluateAdapter.setDataList(dataList);
                    audioMergerView.reset();
                    break;
                case PLAY_REC_OVER:
                    dataList.get(msg.arg1).setPlayRec(false);
                    evaluateAdapter.setDataList(dataList);
                    break;
                case PLAY_REC:
                    dataList.get(msg.arg1).setEval(true);
                    dataList.get(msg.arg1).setPlayRec(true);
                    dataList.get(msg.arg1).setUnderRecPlay(true);
                    evaluateAdapter.setDataList(dataList);
                    break;
                case PAUSE_REC:
                    dataList.get(msg.arg1).setEval(true);
                    dataList.get(msg.arg1).setPlayRec(true);
                    dataList.get(msg.arg1).setUnderRecPlay(false);
                    evaluateAdapter.setDataList(dataList);
                    break;
                case CONTINUE_REC_PLAY:
                    dataList.get(msg.arg1).setEval(true);
                    dataList.get(msg.arg1).setPlayRec(true);
                    dataList.get(msg.arg1).setUnderRecPlay(true);
                    evaluateAdapter.setDataList(dataList);
                    break;
                case CALLBACK_RECORD_DB:
                    dataList.get(msg.arg1).setRecordDB(msg.arg2);
                    evaluateAdapter.setDBData(dataList, msg.arg1);
                    break;
                case RECORDING:
                    dataList.get(msg.arg1).setRecording(true);
                    break;
                case EVAL_COUNT_NOT_ENOUGH:
                    ToastUtil.showToast(getContext(), "至少读两句方可合成");
                    break;
                case MIX_SUCCESS:
                    audioMergerView.setAudioFile(CommonConstant.HTTP_SPEECH_ALL + "/voa/" + mixAudio, meta);
                    dialog.dismiss();
                    break;
                case UPLOAD_MIX_SUCCESS:
                    dialog.dismiss();
                    ToastUtil.showToast(getContext(), "上传成功");
                    break;
                case UPLOAD_MIX_FAIL:
                    dialog.dismiss();
                    ToastUtil.showToast(getContext(), "上传失败");
                    break;
                case UPLOAD_SINGLE_SUCCESS:
                    dialog.dismiss();
                    dataList.get(currentPosition).setUpload(true);
                    evaluateAdapter.setDataList(dataList);
                    ToastUtil.showToast(getContext(), "上传成功");
                    break;
                case UPLOAD_SINGLE_FAILED:
                    dialog.dismiss();
                    ToastUtil.showToast(getContext(), "上传失败，请稍后重试");
                    break;

            }
            return false;
        }
    });
    private int currentPosition;
    private int currentPlayTime;
    private boolean isShowDB;
    private MergeAudioMeta meta;
    private int freeEvalTimes = 0;
    private List<EvalWord> evalWordList;
    private String pic;


    private EvaluateContentPresenter contentPresenter;
    /**
     * 单词弹窗
     */
    private ApiWordPopup apiWordPopup;

    /**
     * 权限请求
     */
    private RxPermissions rxPermissions;

    /**
     * 权限
     */
    private SharedPreferences pSP;

    private AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {

            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    //当其他应用申请焦点之后又释放焦点会触发此回调
                    //可重新播放音乐
                    Log.d(TAG, "AUDIOFOCUS_GAIN");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    //长时间丢失焦点,当其他应用申请的焦点为 AUDIOFOCUS_GAIN 时，
                    //会触发此回调事件，例如播放 QQ 音乐，网易云音乐等
                    //通常需要暂停音乐播放，若没有暂停播放就会出现和其他音乐同时输出声音
                    Log.d(TAG, "AUDIOFOCUS_LOSS");


                    //释放焦点，该方法可根据需要来决定是否调用
                    //若焦点释放掉之后，将不会再自动获得
//                        mAudioManager.abandonAudioFocus(mAudioFocusChange);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    //短暂性丢失焦点，当其他应用申请 AUDIOFOCUS_GAIN_TRANSIENT 或AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE 时，
                    //会触发此回调事件，例如播放短视频，拨打电话等。
                    //通常需要暂停音乐播放

                    if (player != null && player.isPlaying()) {

                        resetAll();
                        pauseSrc();
                        if (evaluateAdapter != null) {
                            evaluateAdapter.notifyDataSetChanged();
                        }
                    }
                    if (recPlayer != null && recPlayer.isPlaying()) {

                        resetAll();
                        recPlayer.pause();
                        if (evaluateAdapter != null) {
                            evaluateAdapter.notifyDataSetChanged();
                        }
                    }
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    //短暂性丢失焦点并作降音处理
                    Log.d(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
            }
        }
    };


    public EvaluateContentFragment() {

    }

    public static EvaluateContentFragment getInstance(String bbcId, String sound, String pic) {
        Bundle bundle = new Bundle();
        bundle.putString("bbcid", bbcId);
        bundle.putString("sound", sound);
        bundle.putString("image", pic);
        EvaluateContentFragment evaluateContentFragment = new EvaluateContentFragment();
        evaluateContentFragment.setArguments(bundle);
        return evaluateContentFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            bbcId = getArguments().getString("bbcid");
            sound = getArguments().getString("sound");
            pic = getArguments().getString("image");
        }

        pSP = requireContext().getSharedPreferences(Constant.SP_PERMISSIONS, Context.MODE_PRIVATE);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        contentPresenter = new EvaluateContentPresenter();
        contentPresenter.attchView(this);

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

                isPreparedOri = true;
            }
        });
    }

    /**
     * 获取音频焦点
     */
    private void requestAudioFocus() {

        int flag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0以上处理

            AudioFocusRequest audioFocusRequest
                    = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                    .setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).build())
                    .setOnAudioFocusChangeListener(onAudioFocusChangeListener)
                    .build();
            flag = audioManager.requestAudioFocus(audioFocusRequest);
//            audioManager.abandonAudioFocusRequest(audioFocusRequest);
        } else {

            flag = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
        if (flag == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            Log.d(TAG, "音频焦点获取成功");
        } else {

            Log.d(TAG, "音频焦点获取失败");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_evaluate, container, false);
        accountManager = AccountManager.Instance(mContext);
        recyclerView = root.findViewById(R.id.evaluate_recyclerView);

        //处理从搜索页面进入文章详情页的情况

        if (sound != null) {

            if (sound.startsWith("http://")) {

                url = sound;
            } else {

                url = CommonConstant.HTTP_STATIC + CommonConstant.domain + "/sounds/minutes/" + sound;
            }
            //播放原音频
            player.reset();
            try {
                if (SyncDataHelper.getInstance(mContext).loadDownload().contains(bbcId)) {

                    player.setDataSource(getContext(), Uri.parse(mContext.getFilesDir() + "/downloadaudio/" + bbcId + ".mp3"));
                    player.prepareAsync();
                } else {
                    player.setDataSource(url);
                    player.prepareAsync();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        audioMergerView = root.findViewById(R.id.merge_view);
        audioMergerView.setOnClickListener(this);
        audioMergerView.setActionListener(mMergeActionListener);
        audioMergerView.setCallback(() -> {//点击播放

            //todo
            resetAll();
            if (recPlayer != null && recPlayer.isPlaying()) {

                recPlayer.pause();
            }
            if (player != null && player.isPlaying()) {

                player.pause();
            }

            RecycleViewData recycleViewData = evaluateAdapter.getmDataList().get(currentPosition);
            if (recorderManager != null && recycleViewData.isRecording()) {

                recorderManager.stopRecord();
                recycleViewData.setRecording(false);
                Message msg = new Message();
                msg.what = EVAL_OVER;
                mHandler.sendMessage(msg);
            }
            evaluateAdapter.notifyDataSetChanged();

        });


        dataManager = HeadlinesDataManager.getInstance(mContext);
        getData();

        initRecManager();

        return root;
    }

    /**
     * 获取句子字数
     */
    private void getData() {

        dataList = dataManager.loadDetail(Integer.parseInt(bbcId));
        for (int i = 0; i < dataList.size(); i++) {

            RecycleViewData data = dataList.get(i);

            //如果这条数据
            RecycleViewData ndata = dataManager.getSingleEvalSentence(Integer.parseInt(bbcId), data.getTiming());
            if (ndata.isEval()) {

                RecycleViewData evalSentence = dataManager.loadEvalSentence(Integer.parseInt(bbcId), data.getParaId(),
                        data.getIdIndex());
                if (data.getCollect() == 1) {
                    evalSentence.setCollect(1);
                }
                if (evalSentence != null) { //可能没有数据，加判断

                    dataList.set(i, evalSentence);
                }
            } else {

                data.setEval(false);
            }
        }
        MixRecBean loadMix = dataManager.loadMix(bbcId);
        if (loadMix.getUrl() != null) {
            mixAudio = loadMix.getUrl();
            meta = new MergeAudioMeta();
            meta.bbcId = Integer.parseInt(bbcId);
            meta.score = loadMix.getAvrScore();
            audioMergerView.setAudioFile(CommonConstant.HTTP_SPEECH_ALL + "/voa/" + mixAudio, meta);
        }


        evaluateAdapter = new EvaluateAdapter(mContext, dataList, this);
        evaluateAdapter.setWordCallback(new EvaluateAdapter.WordCallback() {
            @Override
            public void selectWord(String word, int position) {

                //上一次有选择的单词并且与这次选择的不一样，重置文字
                if (evaluateAdapter.getSelectWordPos() != -1 && evaluateAdapter.getSelectWordPos() != position) {

                    evaluateAdapter.notifyItemChanged(evaluateAdapter.getSelectWordPos());
                }
                evaluateAdapter.setSelectWordPos(position);
                contentPresenter.apiWord(word);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(evaluateAdapter);
        evaluateAdapter.setItemClickListener(adapterMonitor);
    }


    AudioMergerView.ActionListener mMergeActionListener = new AudioMergerView.ActionListener() {
        @Override
        public void onMerge() {
            if (accountManager.checkUserLogin()) {
                mixRec();
            } else {
                showLoginDialog();
            }
        }

        @Override
        public void onPublish(String mergeAudio, MergeAudioMeta metaData) {
            if (accountManager.checkUserLogin()) {
                publishMix();
            } else {
                showLoginDialog();
            }

        }

        @Override
        public void onShare(MergeAudioResponse response) {

        }
    };

    private void publishMix() {
        dialog = null;
        showLoadingOld("上传中...");
        uid = ConfigManager.Instance().loadString("userId", "0");
        if (!"".equals(mixAudio)) {
            String uploadMixUrl = "http://voa." + CommonConstant.domain + "/voa/UnicomApi?" +
                    "platform=android" +
                    "&format=json" +
                    "&protocol=60003" +
                    "&topic=bbc" +
                    "&shuoshuotype=4" +
                    "&userid=" + uid +
                    "&voaid=" + bbcId +
                    "&score=" + avrScore +
                    "&content=" + mixAudio +
                    "&rewardVersion=1" +
                    "&appid=" + Constant.APPID;
            Log.e(TAG, "publishMix: " + uploadMixUrl);

            Retrofit retrofit = new Retrofit.Builder().baseUrl(MixSentenceUploadService.BASE_URL).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(new OkHttpClient()).
                    addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                    build();
            MixSentenceUploadService service = retrofit.create(MixSentenceUploadService.class);

            Observable.create(new ObservableOnSubscribe<MixSentenceResponse>() {
                @Override
                public void subscribe(@NonNull ObservableEmitter<MixSentenceResponse> emitter) throws Exception {
//                    MixSentenceResponse response = new Gson().fromJson(call.execute().body().string(), MixSentenceResponse.class);
                    retrofit2.Call<MixSentenceResponse> post = service.post("android", "json", 60003,
                            "bbc", 4, uid, Integer.parseInt(bbcId),
                            avrScore, mixAudio, 1, Integer.parseInt(Constant.APPID));

                    MixSentenceResponse body = post.execute().body();

                    emitter.onNext(body);
                }
            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<MixSentenceResponse>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull MixSentenceResponse mixSentenceResponse) {
                    Message message = new Message();
                    Log.e(TAG, "onNext: " + mixSentenceResponse.toString());
                    if (mixSentenceResponse.getResultCode().equals("501")) {
                        message.what = UPLOAD_MIX_SUCCESS;
                        showReward(mixSentenceResponse);
                    } else {
                        message.what = UPLOAD_MIX_FAIL;
                    }

                    mHandler.sendMessage(message);
                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        } else {
            ToastUtil.showToast(getContext(), "请先合成");
        }
    }


    /**
     * 展示获取的奖励
     *
     * @param publishEvalBean
     */
    private void showReward(MixSentenceResponse publishEvalBean) {


        new Handler(Looper.getMainLooper()).post(() -> {


            if (!publishEvalBean.getReward().equals("0")) {

                int reward = Integer.parseInt(publishEvalBean.getReward());
                double rewardDouble = reward / 100.0f;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");


                new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                        .setTitle("恭喜您！")
                        .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励，已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众号提现，继续学习领取更多红包奖励吧！")
                        .setPositiveButton("好的", (dialog, which) -> {

                            dialog.dismiss();
                        })
                        .show();
            }
        });
    }

    private void getHistory(int callMode) {
        uid = ConfigManager.Instance().loadString("userId", "0");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String sign = MD5.getMD5ofStr(uid + "getWorksByUserId" + sdf.format(System.currentTimeMillis()));
        String historyUrl = "http://voa." + CommonConstant.domain + "/voa/getWorksByUserId.jsp?" +
                "uid=" + uid +
                "&topic=bbc" +
                "&shuoshuoType=2%2C4" +
                "&sign=" + sign +
                "&topicId=" + bbcId;
        // System.out.println("history---------------------->" + historyUrl);
        Request request = new Request.Builder()
                .get()
                .url(historyUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {

                    JSONObject jo = JSONObject.parseObject(Objects.requireNonNull(response.body()).string());
                    Log.e(TAG, "onResponse: " + jo.toString());
                    if (callMode == 1) {
                        Message msg = new Message();
                        if (jo.getInteger("count") > 1) {
                            msg.what = EVAL_COUNT_ENOUGH;
                        } else {
                            int tempNum = 0;
                            for (RecycleViewData data : dataList) {
                                if (!"".equals(data.getAudioUrl()))
                                    tempNum++;
                            }
                            if (tempNum > 1)
                                msg.what = EVAL_COUNT_ENOUGH;
                            else
                                msg.what = EVAL_COUNT_NOT_ENOUGH;
                        }
                        mHandler.sendMessage(msg);
                    } else if (callMode == 0) {
                        if (jo.getInteger("count") != 0) {
                            JSONArray dataArr = JSON.parseArray(jo.getString("data"));
                            for (Object obj : dataArr) {
                                JSONObject tempJo = JSONObject.parseObject(obj.toString());
                                for (RecycleViewData data : dataList) {
                                    if (data.getParaId().equals(tempJo.getString("paraid")) && data.getIdIndex().equals(tempJo.getString("idIndex"))) {
                                        data.setEval(true);
                                        data.setScore(tempJo.getInteger("score"));
                                        data.setAudioUrl(tempJo.getString("ShuoShuo"));
                                    }
                                }
                            }
                            Message msg = new Message();
                            msg.what = LOAD_HISTORY_FINISH;
                            mHandler.sendMessage(msg);
                        }
                    }
                }
            }
        });
    }


    private EvaluateAdapter.ItemClickListener adapterMonitor = new EvaluateAdapter.ItemClickListener() {
        @Override
        public void onItemClick(String function, int position) {

            if (currentPosition != position) {

                RecycleViewData preViewData = dataList.get(currentPosition);

                if (preViewData.isPlayOri() || preViewData.isPlayRec()) {

                    if (player != null && player.isPlaying()) {

                        player.pause();
                        preViewData.setPlayOri(false);
                    }
                    if (recPlayer != null && recPlayer.isPlaying()) {

                        recPlayer.pause();
                        preViewData.setPlayRec(false);
                    }
                }
                if (preViewData.isRecording() && recorderManager != null) {

                    recorderManager.stopRecord();
                    preViewData.setRecording(false);
                }

                if (recPlayer != null) {
                    recPlayer.release();
                    recPlayer = null;
                }
                srcPlayCounts = 0;
                Message message = Message.obtain();
                message.what = PLAY_REC_OVER;
                message.arg1 = currentPosition;
                mHandler.sendMessage(message);
            }
            currentPosition = position;
            RecycleViewData viewData = dataList.get(position);
            switch (function) {
                case "showSelect":
                    hiddenAllBtn(position);
                    viewData.setShowBtn(true);
                    evaluateAdapter.setcPosition(position);
                    break;
                case "play":
                    if (viewData.isPlayOri()) {
                        pauseSrc();
                        srcPlayCounts++;
                        Message message = Message.obtain();
                        message.what = PLAY_ORI_OVER;
                        message.arg1 = position;
                        mHandler.sendMessage(message);
                    } else {

                        requestAudioFocus();
                        EventBus.getDefault().post(new MediaPause());
                        playSrc(position);
                    }
                    break;
                case "rec":

                    requestAudioFocus();
                    EventBus.getDefault().post(new MediaPause());
                    if (NetworkUtils.isConnected()) {
                        if (accountManager.checkUserLogin()) {

                            Activity activity = requireActivity();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {

                                if (activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

                                    if (accountManager.isVip() || (!accountManager.isVip() && (dataManager.loadFreeTimes(bbcId) < 3))) {
                                        if (viewData.isRecording()) {
                                            stopRec(position);
                                        } else {
                                            startRec(position);
                                        }
                                    } else {
                                        GoToVipHelper.getInstance().EvalTimesOver(mContext);
                                    }
                                } else {//权限不足

                                    String[] permissions = {Manifest.permission.RECORD_AUDIO};
                                    activity.requestPermissions(permissions, 2000);
                                }
                            } else {

                                if (activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                        && activity.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {

                                    if (accountManager.isVip() || (!accountManager.isVip() && (dataManager.loadFreeTimes(bbcId) < 3))) {
                                        if (viewData.isRecording()) {
                                            stopRec(position);
                                        } else {
                                            startRec(position);
                                        }
                                    } else {
                                        GoToVipHelper.getInstance().EvalTimesOver(mContext);
                                    }
                                } else {//权限不足

                                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};
                                    activity.requestPermissions(permissions, 2000);
                                }
                            }
                        } else {
                            showLoginDialog();
                        }
                    } else {
                        ToastUtil.showToast(mContext, "暂未网络连接，无法进行评测");
                    }
                    break;
                case "playRec":
                    requestAudioFocus();
                    EventBus.getDefault().post(new MediaPause());
                    Message msg = new Message();
                    msg.what = PLAY_REC;
                    msg.arg1 = position;
                    mHandler.sendMessage(msg);
                    playRec(position);
                    break;
                case "stopRec":
                    if (viewData.isUnderRecPlay()) {
                        pausePlayRec(position);
                    } else {
                        goOnRecPlay(position);
                    }
                    break;
                case "send":
                    if (accountManager.checkUserLogin()) {
                        upLoadEval(position);
                    } else {
                        showLoginDialog();
                    }
                    break;
                case "share":
                    showNavigate();
                    break;
                case "collect_sentence":

                    String uid = ConfigManager.Instance().loadString("userId", "0");
                    if (uid.equals("0")) {

                        showLoginDialog();
                    } else {

                        int count = dataManager.isCollect(viewData.getBbcId() + "", viewData.getTiming());
                        if (count == 0) {//没有收藏这个句子

                            contentPresenter.updateCollect(uid, viewData.getBbcId() + "", "Iyuba",
                                    viewData.getTiming(), 1, Constant.APPID, "insert", Constant.CATEGORY_TYPE,
                                    "json");
                        } else {//收藏了这个句子

                            contentPresenter.updateCollect(uid, viewData.getBbcId() + "", "Iyuba",
                                    viewData.getTiming(), 1, Constant.APPID, "del", Constant.CATEGORY_TYPE,
                                    "json");
                        }
                    }
                    break;
            }
            evaluateAdapter.setDataList(dataList);
        }
    };

    private void showNavigate() {
        View bottomView = View.inflate(mContext, R.layout.share_menu, null);
        LinearLayout lyQQ = bottomView.findViewById(R.id.share_ly_qq);
        LinearLayout lyQQSpace = bottomView.findViewById(R.id.share_ly_qq_space);

        if (BuildConfig.APPLICATION_ID.equals("com.ai.bbcpro")) {

            lyQQ.setVisibility(View.GONE);
            lyQQSpace.setVisibility(View.GONE);
        }


        LinearLayout lyWx = bottomView.findViewById(R.id.share_ly_wx);
        LinearLayout lyPyq = bottomView.findViewById(R.id.share_ly_pyq);
        PopupWindow window = new PopupWindow(bottomView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setFocusable(true);
        Window screen = getActivity().getWindow();
        WindowManager.LayoutParams attributes = screen.getAttributes();
        attributes.alpha = 0.5f;
        screen.setAttributes(attributes);
        window.setOnDismissListener(() -> {
            attributes.alpha = 1f;
            screen.setAttributes(attributes);
        });
        window.setAnimationStyle(R.style.main_menu_photo_anim);
        window.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 10);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.share_ly_qq:
//                        shareToPlatform(ShareSDK.getPlatform(QQ.NAME).getName());
//                        downPic(pic, ShareSDK.getPlatform(QQ.NAME).getName());

                        requestWrite(pic, ShareSDK.getPlatform(QQ.NAME).getName());
                        break;
                    case R.id.share_ly_qq_space:
                        shareToPlatform(ShareSDK.getPlatform(QZone.NAME).getName());
                        break;
                    case R.id.share_ly_wx:
                        shareToPlatform(ShareSDK.getPlatform(Wechat.NAME).getName());
                        break;
                    case R.id.share_ly_pyq:
                        shareToPlatform(ShareSDK.getPlatform(WechatMoments.NAME).getName());
                        break;
                }
                window.dismiss();
            }
        };
        lyQQ.setOnClickListener(clickListener);
        lyQQSpace.setOnClickListener(clickListener);
        lyWx.setOnClickListener(clickListener);
        lyPyq.setOnClickListener(clickListener);

    }

    @SuppressLint("CheckResult")
    private void requestWrite(String pic, String platform) {

        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(requireActivity());
        }

        if (rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            downPic(pic, platform);
        } else {

            int record = pSP.getInt(Constant.SP_KEY_RECORD, 0);
            if (record == 0) {

                rxPermissions
                        .request(Manifest.permission.RECORD_AUDIO)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) throws Throwable {

                                if (aBoolean) {

                                    downPic(pic, platform);
                                } else {

                                    pSP.edit().putInt(Constant.SP_KEY_RECORD, 1).apply();
                                    toast("您拒绝了存储权限，请在权限管理中打开权限！");
                                }
                            }
                        });
            } else {

                toast("请在应用权限管理中打开存储权限！");
            }
        }
    }

    /**
     * 下载图片，来处理QQ好友分享不能显示图片的问题
     *
     * @param pic
     */
    private void downPic(String pic, String platform) {

        Glide.with(MainApplication.getApplication()).asBitmap().load(pic).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                String picDir = MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                String picPath = picDir + "/share/";
                File pathFile = new File(picPath);
                if (!pathFile.exists()) {
                    pathFile.mkdirs();
                }
                try {

                    FileOutputStream fileOutputStream = new FileOutputStream(new File(picPath, dataList.get(currentPosition).getBbcId() + ".jpg"));
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    resource.recycle();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                shareToPlatform(platform);
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }
        });
    }

    private void shareToPlatform(String platform) {
        if (platform != null) {
            oks.setPlatform(platform);
        }

        String name = ConfigManager.Instance().loadString("userName", "");

        oks.setTitle(name + "在" + Constant.AppName + "语音测评中获得了" + dataList.get(currentPosition).getScore() + "分");
//        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        String ArticleShareUrl = "http://voa." + CommonConstant.domain + "/voa/play.jsp?id=" + dataList.get(currentPosition).getShuoshuoId()
                + "&appid=" + Constant.APPID
                + "&apptype=" + "bbc" + "&addr=" + dataList.get(currentPosition).getAudioUrl();
        oks.setTitleUrl(ArticleShareUrl);
//        // text是分享文本，所有平台都需要这个字段
        oks.setText(dataList.get(currentPosition).getSentence());
        oks.setImageUrl(pic);

        //QQ好友分享需要使用的设置
        String picPath = MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + "/share/"
                + dataList.get(currentPosition).getBbcId() + ".jpg";
        oks.setImagePath(picPath);
        oks.setUrl(ArticleShareUrl);
        //分享回调
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                // 分享成功回调
                ToastUtil.showToast(mContext, "分享成功");
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                // 分享失败回调
                // 失败的回调，arg:平台对象，arg1:表示当前的动作(9表示分享)，arg2:异常信息
                ToastUtil.showToast(mContext, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                // 分享取消回调
                ToastUtil.showToast(mContext, "分享取消");
            }
        });
        // 启动分享
        oks.show(MobSDK.getContext());

    }

    void upLoadEval(int position) {

        String nickName = ConfigManager.Instance().loadString("userName");

        uid = ConfigManager.Instance().loadString("userId", "0");

        dialog = null;
        showLoadingOld("上传中...");
        RecycleViewData data = dataList.get(position);
        String uploadUrl = "http://voa." + CommonConstant.domain + "/voa/UnicomApi?" +
                "platform=android" +
                "&format=json" +
                "&protocol=60003" +
                "&topic=bbc" +
                "&shuoshuotype=2" +
                "&userid=" + uid +
                "&voaid=" + bbcId +
                "&idIndex=" + data.getIdIndex() +
                "&paraid=" + data.getParaId() +
                "&username=" + nickName +
                "&score=" + data.getScore() +
                "&content=" + data.getAudioUrl();
        Log.e(TAG, "upLoadEval: uploadUrl：" + uploadUrl);
        // System.out.println("upload single---------------->" + uploadUrl);
        Request request = new Request.Builder()
                .get()
                .url(uploadUrl)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Message msg = Message.obtain();
                msg.what = UPLOAD_SINGLE_FAILED;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    ResponseBody responseBody = response.body();
                    if (responseBody != null) {

                        String s = responseBody.string();
                        JSONObject jo = JSONObject.parseObject(s);
                        Message msg = new Message();
                        Log.e(TAG, "onResponse: " + s);
                        if ("501".equals(jo.getString("ResultCode"))) {//成功

                            System.out.println("------------>单句评测上传成功");
                            dataList.get(position).setShuoshuoId(jo.getString("ShuoshuoId"));
                            // Objects.requireNonNull(getActivity()).runOnUiThread(() -> ToastUtil.showToast(getContext(), "上传成功"));
                            msg.what = UPLOAD_SINGLE_SUCCESS;
                            EventBus.getDefault().post(new RefreshRankEvent());
                        } else {
                            msg.what = UPLOAD_SINGLE_FAILED;
                            // Objects.requireNonNull(getActivity()).runOnUiThread(() -> ToastUtil.showToast(getContext(), "上传失败，请稍后重试"));
                        }
                        mHandler.sendMessage(msg);
                    }
                }
            }
        });
    }


    private void goOnRecPlay(int position) {
        recPlayer.start();
        Message message = Message.obtain();
        message.what = CONTINUE_REC_PLAY;
        message.arg1 = position;
        mHandler.sendMessage(message);
    }

    public void playRec(int position) {
        RecycleViewData data = dataList.get(position);
        String audioUrl = data.getAudioUrl();
        Log.e(TAG, "playRec: " + audioUrl);
        if (player != null && player.isPlaying()) {
            pauseSrc();
        }

        resetBtn1();
        resetBtn2();
        if (recPlayer != null) {
            recPlayer.release();
            recPlayer = null;
        }
        try {
            recPlayer = new MediaPlayer();
            recPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (!"".equals(data.getAudioUrl())) {
                recPlayer.setDataSource(CommonConstant.HTTP_SPEECH_ALL + "/voa/" + data.getAudioUrl());
            } else {
                recPlayer.setDataSource(recFilePath);
            }
            recPlayer.prepare();
            recPlayer.start();
            recPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Message message = Message.obtain();
                    message.what = PLAY_REC_OVER;
                    message.arg1 = position;
                    mHandler.sendMessage(message);
                }
            });
        } catch (Exception e) {

        }
    }

    private void pausePlayRec(int position) {
        resetAll();
        Message message = Message.obtain();
        message.what = PAUSE_REC;
        message.arg1 = position;
        mHandler.sendMessage(message);
        if (recPlayer != null && recPlayer.isPlaying())
            recPlayer.pause();
    }

    void pauseSrc() {
        currentPlayTime = player.getCurrentPosition();
        Log.e(TAG, "pauseSrc: " + currentPlayTime);
        if (player != null && player.isPlaying()) {
            player.pause();
        }
        if (timer != null)
            timer.cancel();
    }

    void resetAll() {
        resetBtn1();
        resetBtn2();
        resetBtn3();
    }

    // 全部“播放原文音频”变回播放图标
    void resetBtn1() {
        for (RecycleViewData data : dataList) {
            data.setPlayOri(false);
        }
    }

    // 全部“录音”变回播放图标
    void resetBtn2() {
        for (RecycleViewData data : dataList) {
            data.setRecording(false);
        }
    }

    // 全部“播放录音”变回播放图标
    void resetBtn3() {
        for (RecycleViewData data : dataList) {
            data.setPlayRec(false);
        }
    }

    // 初始化录音组件
    void initRecManager() {
        AudioRecorderUtils.OnAudioStatusUpdateListener listener = new AudioRecorderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
                if (isShowDB) {
                    Message message = Message.obtain();
                    message.what = CALLBACK_RECORD_DB;
                    message.arg1 = currentPosition;
                    message.arg2 = (int) db;
                    mHandler.sendMessage(message);
                }

            }

            @Override
            public void onStop(String filePath) {
                Log.e(TAG, "onStop: " + filePath);
                recFilePath = filePath;

            }
        };
        recorderManager = AudioRecorderUtils.getInstance();
        recorderManager.setOnAudioStatusUpdateListener(listener);
    }

    // 停止录音
    void stopRec(int position) {
        isShowDB = false;
        RecycleViewData data = dataList.get(position);
        resetAll();
        recorderManager.stopRecord();
        if (!"".equals(recFilePath)) {
            // 成功录音则调用评测接口
            evaluate(data);
            if (!accountManager.isVip()) {
                freeEvalTimes++;
                dataManager.saveFreeTimes(bbcId, freeEvalTimes);
            }

        }
    }

    // 开始录音
    void startRec(int position) {
        pauseSrc();
        resetAll();
        Message message = Message.obtain();
        message.what = RECORDING;
        message.arg1 = position;
        mHandler.sendMessage(message);
        isShowDB = true;
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)) {
            ToastUtil.showToast(getContext(), "请打开录音权限继续");
        } else {
            ToastUtil.showToast(getContext(), "开始录音，再次点击结束录音并打分");
            dataList.get(position).setRecording(true);
            recorderManager.startRecord();
        }
    }

    public void evaluate(RecycleViewData data) {
        uid = ConfigManager.Instance().loadString("userId", "0");
        Log.e(TAG, "evaluate: " + uid);
        dialog = null;
        showLoadingOld("评测中...");
        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
        File file = new File(recFilePath);
        RequestBody fileBody = RequestBody.create(type, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("IdIndex", data.getIdIndex())
                .addFormDataPart("appId", Constant.APPID)
                .addFormDataPart("flg", "0")
                .addFormDataPart("newsId", String.valueOf(data.getBbcId()))
                .addFormDataPart("paraId", data.getParaId())
                .addFormDataPart("sentence", data.getSentence().replaceAll("\\+", "%20"))
                .addFormDataPart("type", "bbc")
                .addFormDataPart("userId", uid)
                .addFormDataPart("wordId", "0")
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(CommonConstant.HTTP_SPEECH_ALL + "/test/ai/")
                .post(requestBody)
                .build();
        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // ToastUtil.showToast(getContext(), "网络错误，请稍后重试。");
                Log.e(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("传送发生错误！" + response);
                } else {
                    EvalResponse evalResponse = new Gson().fromJson(response.body().string(), EvalResponse.class);
                    List<EvalWord> evalWords = evalResponse.getData().getWords();
                    String sentenceKey = new StringBuilder().append(String.valueOf(data.getBbcId())).
                            append(data.getParaId()).append(data.getIdIndex()).toString();
                    for (int i = 0; i < evalWords.size(); i++) {
                        evalWords.get(i).setBbcId(bbcId);
                        evalWords.get(i).setPosition(currentPosition);
                        evalWords.get(i).setPara_id(data.getParaId());
                        evalWords.get(i).setId_index(data.getIdIndex());
                        dataManager.saveEvalWords(evalWords.get(i));
                    }

                    Log.e(TAG, "onResponse: " + evalResponse.getData().getUrl());
                    dataManager.changeState(sentenceKey);
                    for (RecycleViewData tempData : dataList) {
                        if (tempData.getSentence().equals(evalResponse.getData().getSentence())) {
                            tempData.setWords(evalWords);
                            for (int i = 0; i < evalWords.size(); i++) {
                                if (evalWords.get(i).getScore() < 2) {
                                    tempData.setHasLowScoreWord(true);
                                    break;
                                }
                            }
                            tempData.setScore(evalResponse.getData().getScores());
                            tempData.setAudioUrl(evalResponse.getData().getUrl());
                            tempData.setEval(true);
                            tempData.setPosition(currentPosition);
                            tempData.setSentence(data.getSentence());
                            tempData.setSentenceCn(data.getSentenceCn());
                            tempData.setTiming(data.getTiming());
                            tempData.setEndTiming(data.getEndTiming());
                            tempData.setParaId(data.getParaId());
                            tempData.setIdIndex(data.getIdIndex());
                            tempData.setBbcId(Integer.parseInt(bbcId));
                            dataManager.saveEvalSentence(tempData);
                            Message msg = new Message();
                            msg.what = EVAL_OVER;
                            mHandler.sendMessage(msg);
                            return;
                        }
                    }
                }
            }
        });
    }


    public void playSrc(int position) {
        resetBtn2();
        resetBtn3();
        RecycleViewData data = dataList.get(position);
        int beginTime = (int) (Float.parseFloat(data.getTiming()) * 1000);
        int endTime = (int) (Float.parseFloat(data.getEndTiming()) * 1000);
        if (endTime == 0) {

            int np = position + 1;
            if (np < dataList.size()) {
                RecycleViewData recycleViewData = dataList.get(np);
                endTime = (int) (Float.parseFloat(recycleViewData.getTiming()) * 1000);
            }
        }


        if (recorderManager.getRecordStatus() == 1) {
            recorderManager.cancelRecord();
        }
        if (recPlayer != null && recPlayer.isPlaying())
            recPlayer.pause();
        if (dataList.get(position).isEval() && dataList.get(position).isPlayRec()) {
            Message message = Message.obtain();
            message.what = PAUSE_REC;
            message.arg1 = position;
            mHandler.sendMessage(message);
        }

        dataList.get(position).setPlayOri(true);
        if (player != null && isPreparedOri) {

            if (endTime == 0) {

                endTime = player.getDuration();
            }

            if (srcPlayCounts == 0) {
                player.seekTo(beginTime);
                player.start();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        player.pause();
                        timer.cancel();
                        Message msg = new Message();
                        msg.what = PLAY_ORI_OVER;
                        msg.arg1 = position;
                        mHandler.sendMessage(msg);
                    }
                }, endTime - beginTime);
            } else {
                player.seekTo(beginTime);
                player.start();
                timer = new Timer();
                Log.e(TAG, "begin:" + beginTime + " end:" + endTime + " current:" + currentPlayTime);

                int d = endTime - currentPlayTime;
                if (d < 0) {

                    d = 0;
                }
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        player.pause();
                        timer.cancel();
                        Message msg = new Message();
                        msg.what = PLAY_ORI_OVER;
                        msg.arg1 = position;
                        mHandler.sendMessage(msg);
                    }
                }, endTime - beginTime);
            }

        }
    }

    void hiddenAllBtn(int position) {
        for (int i = 0; i < dataList.size(); i++) {
            if (!dataList.get(i).equals(position)) {
                dataList.get(i).setShowBtn(false);
            }
        }
    }

    // 用户评测语音合成
    void mixRec() {
        String mixUrl = CommonConstant.HTTP_SPEECH_ALL + "/test/merge/";  // type audios
        StringBuilder audios = new StringBuilder();
        int evalNum = 0;
        int totalScore = 0;
        for (RecycleViewData data : dataList) {
            if (!"".equals(data.getAudioUrl()) && audios.length() == 0) {
                // audios = data.getAudioUrl();
                audios.append(data.getAudioUrl());
                evalNum++;
                totalScore += data.getScore();
            } else if (!"".equals(data.getAudioUrl()) && audios.length() != 0) {
                audios.append(",").append(data.getAudioUrl());
                evalNum++;
                totalScore += data.getScore();
            }
        }
        if (evalNum < 2) {
            Message message = Message.obtain();
            message.what = EVAL_COUNT_NOT_ENOUGH;
            mHandler.sendMessage(message);
        } else {
            resetPlayers();
            dialog = null;
            showLoadingOld("合成中...");
            if (evalNum != 0) {
                avrScore = Math.round(totalScore / evalNum);
            }
            FormBody formBody = new FormBody.Builder()
                    .add("type", "bbc")
                    .add("audios", audios.toString())
                    .build();
            Request request = new Request.Builder()
                    .url(mixUrl)
                    .post(formBody)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        MixRecBean mixRecBean = new Gson().fromJson(response.body().string(), MixRecBean.class);
                        mixRecBean.setAvrScore(avrScore);
                        dataManager.saveMix(mixRecBean, bbcId);
                        Message msg = new Message();
                        if (mixRecBean.getResult().equals("1")) {
                            mixAudio = mixRecBean.getUrl();
                            msg.what = MIX_SUCCESS;
                            meta = new MergeAudioMeta();
                            meta.bbcId = Integer.parseInt(bbcId);
                            meta.score = avrScore;

                        } else {
                            msg.what = MIX_FAIL;
                        }
                        mHandler.sendMessage(msg);
                    }
                }
            });
        }
    }


    @Override
    public void onClick(View v) {

    }

    void showLoadingOld(String content) {
        if (dialog == null) {
            View v = View.inflate(getContext(), R.layout.dialog_waiting, null);
            TextView tvContent = v.findViewById(R.id.waiting_content);
            tvContent.setText(content);
            dialog = new Dialog(getContext(), R.style.theme_dialog);
            dialog.setContentView(v);
            dialog.setCancelable(true);
        }
        dialog.show();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            if (player != null) {
                player.pause();
            }

        }
    }

    public void resetPlayers() {
        if (player != null) {
            player.pause();
        }
        if (recPlayer != null) {
            recPlayer.pause();
        }
        if (recorderManager != null) {
            recorderManager.stopRecord();
        }
    }

    public void showLoginDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        Window window = alertDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_diy);
            window.setGravity(Gravity.CENTER);

            TextView tvContent = window.findViewById(R.id.dialog_content);
            tvContent.setText("您还未登录，是否跳转登录界面？");

            TextView tvCancel = window.findViewById(R.id.dialog_cancel);
            TextView tvAgree = window.findViewById(R.id.dialog_agree);
            tvAgree.setText("跳转登录");

            tvCancel.setOnClickListener(v -> {
                alertDialog.cancel();
            });

            tvAgree.setOnClickListener(v -> {
                alertDialog.cancel();
                // 跳转登录界面
                EventBus.getDefault().post(new LoginEventbus());
            });
        }
    }

    /**
     * 切换数据
     */
    public void switchData() {

        Bundle bundle = getArguments();
        if (bundle != null) {

            bbcId = getArguments().getString("bbcid");
            sound = getArguments().getString("sound");
            pic = getArguments().getString("image");
        }
        //获取数据
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (contentPresenter != null) {

            contentPresenter.detachView();
        }
    }

    @Override
    public void getWord(ApiWordBean apiWordBean) {

        initApiWordPopup(apiWordBean);
    }

    @Override
    public void collectWord(WordCollectBean wordCollectBean) {

        toast("收藏成功");
    }

    @Override
    public void updateCollect(String type, String voaId, String timing) {

        List<RecycleViewData> recycleViewDataList = evaluateAdapter.getmDataList();
        //目标数据
        RecycleViewData tdata = null;
        for (int i = 0; i < recycleViewDataList.size(); i++) {

            RecycleViewData recycleViewData = recycleViewDataList.get(i);
            if (recycleViewData.getBbcId() == Integer.parseInt(voaId) && recycleViewData.getTiming().equals(timing)) {

                tdata = recycleViewData;
                break;
            }
        }


        if (type.equals("insert")) {

            dataManager.collectSentence(voaId + "", timing, 1);
            if (tdata != null) {

                tdata.setCollect(1);
                evaluateAdapter.notifyDataSetChanged();
            }
        } else {

            dataManager.collectSentence(voaId + "", timing, 0);
            if (tdata != null) {

                tdata.setCollect(0);
                evaluateAdapter.notifyDataSetChanged();
            }
        }
    }


    /**
     * 单词详情弹窗
     *
     * @param apiWordBean
     */
    private void initApiWordPopup(ApiWordBean apiWordBean) {

        if (apiWordPopup == null && getContext() != null) {

            apiWordPopup = new ApiWordPopup(getContext());
            apiWordPopup.setCallback(new ApiWordPopup.Callback() {
                @Override
                public void add() {


                    String uid = ConfigManager.Instance().loadString("userId", "0");
                    if (uid.equals("0")) {

                        EventBus.getDefault().post(new LoginEventbus());
                        ;
                        return;
                    }
                    ApiWordBean apiWordBean1 = apiWordPopup.getWord();
                    contentPresenter.updateWord("Iyuba", "insert", apiWordBean1.getKey(), uid, "json");
                }

                @Override
                public void cancel() {

                    apiWordPopup.dismiss();
                }
            });
        }
        apiWordPopup.setWord(apiWordBean);
        apiWordPopup.showPopupWindow();
    }

    /**
     * 更新数据
     */
    public void updateData() {

        if (evaluateAdapter == null) {

            return;
        }

        dataList = dataManager.loadDetail(Integer.parseInt(bbcId));
        for (int i = 0; i < dataList.size(); i++) {

            RecycleViewData data = dataList.get(i);
            if (data.isEval()) {
                RecycleViewData evalSentence = dataManager.loadEvalSentence(Integer.parseInt(bbcId), data.getParaId(),
                        data.getIdIndex());
                if (data.getCollect() == 1) {
                    evalSentence.setCollect(1);
                }
                Log.e(TAG, "onCreateView: " + evalSentence.isHasLowScoreWord());
                dataList.set(i, evalSentence);
            }
        }
        evaluateAdapter.setDataList(dataList);
    }
}
