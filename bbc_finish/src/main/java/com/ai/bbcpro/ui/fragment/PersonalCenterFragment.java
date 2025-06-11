package com.ai.bbcpro.ui.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.OkHttpClient;
import personal.iyuba.personalhomelibrary.ui.message.MessageActivity;
import personal.iyuba.personalhomelibrary.ui.my.MySpeechActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryActivity;
import personal.iyuba.personalhomelibrary.ui.studySummary.SummaryType;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.mvp.presenter.me.PersonalCenterPresenter;
import com.ai.bbcpro.mvp.view.me.PersonalCenterContract;
import com.ai.bbcpro.newSection.BaseFragment;
import com.ai.bbcpro.sqlite.bean.UserInfo;
import com.ai.bbcpro.ui.activity.DownloadListActivity;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.activity.login.LoginActivity;
import com.ai.bbcpro.ui.activity.me.MyCollectActivity;
import com.ai.bbcpro.ui.activity.me.MyWalletActivity;
import com.ai.bbcpro.ui.activity.me.RankingListActivity;
import com.ai.bbcpro.ui.activity.me.SettingActivity;
import com.ai.bbcpro.ui.activity.me.SignActivity;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.event.RefreshPersonalEvent;
import com.ai.bbcpro.ui.logout.ClearUserResponse;
import com.ai.bbcpro.ui.logout.PwdInputDialog;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.ui.vip.VipCenterActivity;
import com.ai.bbcpro.word.SearchWordActivity;
import com.ai.bbcpro.word.WordCollectionActivity;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.imooclib.ui.record.PurchaseRecordActivity;
import com.iyuba.module.headlinesearch.ui.MSearchActivity;
import com.sd.iyu.training_camp.TCApplication;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 个人中心
 */
public class PersonalCenterFragment extends BaseFragment implements PersonalCenterContract.PersonalCenterView {

    Context mContext;
    private AccountManager accountManager;
    private Button loginBtn;
    private View noLogin, meTop, personalHome, keyWord, wordBook, searchWord,
            exit, vipCenter, collection, download;
    private ImageView photo, setting;
    private UserInfo userInfo;
    private TextView meName, vipStatus;
    private static final String TAG = "PersonalCenterFragment";

    private PwdInputDialog dialogTip;

    private RelativeLayout pc_rl_study_report;

    private RelativeLayout pc_rl_clock_in;


    private RelativeLayout pc_rl_ranking;

    /**
     * 口语圈
     */
    private RelativeLayout pc_rl_speech;

    private RelativeLayout pc_rl_sys;

    private RelativeLayout pc_rl_buylog;

    private RelativeLayout pc_rl_msg;

    private RelativeLayout pc_rl_wallet;

    private TextView pc_tv_wallet;

    private PersonalCenterPresenter personalCenterPresenter;

    private RelativeLayout pc_rl_setting;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(PersonalCenterFragment.this);

        personalCenterPresenter = new PersonalCenterPresenter();
        personalCenterPresenter.attchView(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (personalCenterPresenter != null) {

            personalCenterPresenter.detachView();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal_center, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        accountManager = AccountManager.Instance(mContext);

        initView(view);
        initOperation();

        setTextViewContent();
    }

    private void initView(View rootView) {

        keyWord = rootView.findViewById(R.id.main_word);
        wordBook = rootView.findViewById(R.id.collect_word);
        searchWord = rootView.findViewById(R.id.search_word);
        loginBtn = rootView.findViewById(R.id.button_to_login);
        noLogin = rootView.findViewById(R.id.noLogin);
        meTop = rootView.findViewById(R.id.me_top);
        photo = rootView.findViewById(R.id.me_pic);
        meName = rootView.findViewById(R.id.me_name);
        vipStatus = rootView.findViewById(R.id.tv_vipstate);
        personalHome = rootView.findViewById(R.id.personalhome);
        exit = rootView.findViewById(R.id.exit);
        vipCenter = rootView.findViewById(R.id.vip_center);
        collection = rootView.findViewById(R.id.collection);
        download = rootView.findViewById(R.id.download);
        pc_rl_study_report = rootView.findViewById(R.id.pc_rl_study_report);
        pc_rl_clock_in = rootView.findViewById(R.id.pc_rl_clock_in);

        pc_rl_ranking = rootView.findViewById(R.id.pc_rl_ranking);

        pc_rl_speech = rootView.findViewById(R.id.pc_rl_speech);

        pc_rl_sys = rootView.findViewById(R.id.pc_rl_sys);

        pc_rl_buylog = rootView.findViewById(R.id.pc_rl_buylog);

        pc_rl_msg = rootView.findViewById(R.id.pc_rl_msg);

        pc_rl_wallet = rootView.findViewById(R.id.pc_rl_wallet);

        pc_tv_wallet = rootView.findViewById(R.id.pc_tv_wallet);
        pc_rl_setting = rootView.findViewById(R.id.pc_rl_setting);
    }


    private void initOperation() {

        pc_rl_setting.setOnClickListener(v -> startActivity(new Intent(requireActivity(), SettingActivity.class)));
        //我的钱包
        pc_rl_wallet.setOnClickListener(v -> {

            String userId = ConfigManager.Instance().loadString("userId", "");
            if (userId.equals("")) {

                EventBus.getDefault().post(new LoginEventbus());
            } else {

                startActivity(new Intent(requireActivity(), MyWalletActivity.class));
            }
        });
        //消息中心
        pc_rl_msg.setOnClickListener(v -> {

            String userId = ConfigManager.Instance().loadString("userId", "");
            if (userId.equals("")) {

                EventBus.getDefault().post(new LoginEventbus());
            } else {

                startActivity(new Intent(requireActivity(), MessageActivity.class));
            }

        });

        pc_rl_buylog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = ConfigManager.Instance().loadString("userId", "");
                if (!userId.equals("")) {

                    startActivity(PurchaseRecordActivity.buildIntent(requireActivity()));
                } else {

                    Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });
        download.setOnClickListener(view1 -> {
            mContext.startActivity(new Intent(mContext, DownloadListActivity.class));
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  EventBus.getDefault().post(new LoginEventbus());
            }
        });
        keyWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(mContext, Cet4WordList.class));
            }
        });
        wordBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = ConfigManager.Instance().loadString("userId", "0");

                if (userId.equals("0") || userId.trim().equals("")) {

                      EventBus.getDefault().post(new LoginEventbus());
                } else {
                    startActivity(new Intent(mContext, WordCollectionActivity.class));
                }
            }
        });
        searchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SearchWordActivity.class));
            }
        });
        collection.setOnClickListener((v) -> {
//            startActivity(new Intent(mContext, CollectionActivity.class));

            String userId = ConfigManager.Instance().loadString("userId", "");
            if (!userId.equals("")) {
//                startActivity(BasicFavorActivity.buildIntent(getContext()));
                startActivity(new Intent(requireContext(), MyCollectActivity.class));
            } else {

                Toast.makeText(getActivity(), "请登录", Toast.LENGTH_SHORT).show();
            }
        });

        personalHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountManager.checkUserLogin()) {

                    String userId = ConfigManager.Instance().loadString("userId", "");
                    String username = ConfigManager.Instance().loadString("userName", "");
//                    startActivity(PersonalHomeActivity.buildIntent(getContext(), Integer.parseInt(userId), username, 0));
                } else {
                    ToastUtil.showToast(mContext, "请先登录");
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(getResources().getString(
                                R.string.alert_title))
                        .setMessage(getResources().getString(
                                R.string.logout_alert))
                        .setPositiveButton(getResources().getString(
                                        R.string.alert_btn_ok),
                                (dialog11, whichButton) -> handler.sendEmptyMessage(4))

                        .setNeutralButton(getResources().getString(
                                        R.string.alert_btn_cancel),
                                (dialog1, which) -> {
                                }).create();
                dialog.show();
            }
        });

        vipCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = ConfigManager.Instance().loadString("userId", "");
                String username = ConfigManager.Instance().loadString("userName", null);

                String imgUrl = ConfigManager.Instance().loadString("imgUrl");
                if (imgUrl != null && !imgUrl.startsWith("http")) {

                    imgUrl = CommonConstant.URL_STATIC1 + "/uc_server/" + imgUrl;
                }

                Intent intent = new Intent(mContext, VipCenterActivity.class);
                intent.putExtra("username", username);
                intent.putExtra("userimage", imgUrl);
                startActivity(intent);
            }
        });
        pc_rl_study_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = ConfigManager.Instance().loadString("userId", "0");
                if (userID.equals("0")) {

                    Toast.makeText(MainApplication.getApplication(), "请登录", Toast.LENGTH_SHORT).show();
                      EventBus.getDefault().post(new LoginEventbus());
                    return;
                }

                String[] types = new String[]{
                        SummaryType.LISTEN,
                        SummaryType.EVALUATE,
                        //SummaryType.WORD,
                        SummaryType.TEST,
                        //SummaryType.MOOC,
//                    SummaryType.READ
                };
                startActivity(SummaryActivity.getIntent(getContext(), "bbc", types, 0));//10 PersonalType.NEWS
            }
        });
        //打卡签到
        pc_rl_clock_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = ConfigManager.Instance().loadString("userId", "");
                if (userID.equals("0") || userID.equals("")) {

                    Toast.makeText(MainApplication.getApplication(), "请登录", Toast.LENGTH_SHORT).show();
                      EventBus.getDefault().post(new LoginEventbus());
                    return;
                }
                startActivity(new Intent(getActivity(), SignActivity.class));
            }
        });
        pc_rl_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), RankingListActivity.class));
            }
        });

        //口语圈
        pc_rl_speech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = ConfigManager.Instance().loadString("userId", "");

                if (userID.equals("0") || userID.equals("")) {

                    Toast.makeText(MainApplication.getApplication(), "请登录", Toast.LENGTH_SHORT).show();
                      EventBus.getDefault().post(new LoginEventbus());
                } else {

                    startActivity(MySpeechActivity.buildIntent(getContext()));
                }
            }
        });
        pc_rl_sys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] types = new String[]{HeadlineType.NEWS, HeadlineType.SONG, HeadlineType.VOA,
                        HeadlineType.BBC, HeadlineType.TED, HeadlineType.VIDEO};
                startActivity(MSearchActivity.buildIntent(getContext(), types));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(RefreshPersonalEvent event) {
        Log.e(TAG, "onLoginEvent: wo diao le");
        onResume();
    }

    public static void startQQGroup(Context context, String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?" +
                "url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "您的设备尚未安装QQ客户端", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        viewChange();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (!hidden) {

            viewChange();
        }
    }

    private void setTextViewContent() {

        String userId = ConfigManager.Instance().loadString("userId", "");
        if (!userId.equals("")) {//登录

            String imgUrl = ConfigManager.Instance().loadString("imgUrl");
            String expireTime = ConfigManager.Instance().loadString("expireTime");
            String username = ConfigManager.Instance().loadString("userName", null);
            int vipStatusStr = AccountManager.getVipStatus();

            if (imgUrl != null && imgUrl.startsWith("http")) {

                Glide.with(mContext).load(imgUrl).into(photo);
            } else {

                Glide.with(mContext).load(CommonConstant.URL_STATIC1 + "/uc_server/" + imgUrl).into(photo);
            }


            meName.setText(username);
            if (vipStatusStr > 0 && checkVipDate(expireTime)) {

                if (expireTime.length() == 10) {
                    expireTime = expireTime + "000";
                }
                String dateStr = null;
                Date date = new Date(Long.parseLong(expireTime));
                dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
                vipStatus.setText(dateStr);
                vipStatus.setVisibility(View.VISIBLE);
            } else {

                vipStatus.setVisibility(View.GONE);
            }

        } else {

            vipStatus.setVisibility(View.GONE);
            pc_tv_wallet.setText("我的钱包");
        }
    }

    public boolean checkVipDate(String date) {

        if (date.length() > 1) {
            if (date.length() == 10) {

                date = date + "000";
            }
            return Long.parseLong(date) >= System.currentTimeMillis();
        }
        return false;
    }

    public boolean checkExpireDate(String date) {
        if (!date.equals("您还不是VIP")) {
            String[] split = date.split("\\-");
            StringBuilder sb = new StringBuilder();
            sb.append(split[0]).append(split[1]).append(split[2]);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            Date currentDate = new Date(System.currentTimeMillis());
            String currentFormatDate = formatter.format(currentDate);
            Log.e(TAG, "reviseExpireDate: " + currentFormatDate + " " + sb.toString());
            return Integer.parseInt(sb.toString()) > Integer.parseInt(currentFormatDate);
        } else {
            return true;
        }

    }

    private void viewChange() {

        String userId = ConfigManager.Instance().loadString("userId", "");
        if (!userId.equals("")) {
            //获取用户数据
            String sign = com.iyuba.module.toolbox.MD5.getMD5ofStr("20001" + userId + "iyubaV2");
            personalCenterPresenter.getUserInfo("android", "json", Constant.APPID,
                    "20001", userId + "", userId + "", sign);
        }
        if (userId.equals("")) {
            vipCenter.setVisibility(View.GONE);
//            leave.setVisibility(View.GONE);
            noLogin.setVisibility(View.VISIBLE);
            meTop.setVisibility(View.GONE);
            collection.setVisibility(View.GONE);
            exit.setVisibility(View.GONE);
        } else {
            vipCenter.setVisibility(View.VISIBLE);
//            leave.setVisibility(View.VISIBLE);
            noLogin.setVisibility(View.GONE);
            meTop.setVisibility(View.VISIBLE);
            collection.setVisibility(View.VISIBLE);
            exit.setVisibility(View.VISIBLE);
        }
        setTextViewContent();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 1:
                    CustomToast.showToast(mContext, R.string.action_fail);
                    break;
                case 2:
//                    rootView.findViewById(R.id.newletter).setVisibility(View.VISIBLE);
                    break;
                case 3:
                    setTextViewContent();
                    break;
                case 4://退出登录
//                    TrainingManager.userId = "0";
                    accountManager.loginOut();
                    //重置训练营
                    TCApplication.clear();
                    EventBus.getDefault().post(new RefreshListEvent("refresh"));
                    CustomToast.showToast(mContext, R.string.loginout_success);
//                    SettingConfig.Instance().setHighSpeed(false);
                    viewChange();
//                    verifyUsrInfoAndTarget();
                    break;
                case 5:
//                    rootView.findViewById(R.id.newletter).setVisibility(View.GONE);
                    break;
                case 6:
                    Dialog dialog = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("提示")
                            .setMessage("亲,请先完善个人信息~")
                            .setPositiveButton("确定", null)
                            .create();
                    dialog.show();
                    break;
                case 7:
                    Dialog dialog2 = new AlertDialog.Builder(mContext)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("提示")
                            .setMessage("亲,请先补充您的学习目标~")
                            .setPositiveButton("确定", null)
                            .create();
                    dialog2.show();
                    break;
                default:
                    break;
            }
        }
    };

    private void verifyUsrInfoAndTarget() {
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
    public void getUserInfo(MoreInfoBean moreInfoBean, String uid) {

        ConfigManager.Instance().putString("imgUrl", moreInfoBean.getMiddleUrl());

        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        pc_tv_wallet.setText("我的钱包:" + decimalFormat.format(moreInfoBean.getMoney() / 100.0f) + "元");
        setTextViewContent();
    }

    public interface ApiComService {

        String ENDPOINT = "http://api." + CommonConstant.domainLong + "/";

        @GET("v2/api.iyuba")
        retrofit2.Call<ClearUserResponse> clearUserInfo(@Query("protocol") String protocol,
                                                        @Query("username") String username,
                                                        @Query("password") String password,
                                                        @Query("sign") String sign,
                                                        @Query("format") String format);


        class Creator {
            public static ApiComService createService(OkHttpClient client, GsonConverterFactory gsonFactory, RxJava2CallAdapterFactory rxJavaFactory) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ENDPOINT)
                        .client(client)
                        .addConverterFactory(gsonFactory)
                        .addCallAdapterFactory(rxJavaFactory)
                        .build();
                return retrofit.create(ApiComService.class);
            }
        }
    }
}
