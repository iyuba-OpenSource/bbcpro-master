package com.ai.bbcpro.ui.player;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.event.MediaCompleteEventbus;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.mvp.presenter.home.AudioContentPresenter;
import com.ai.bbcpro.mvp.view.home.AudioContentContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.adapter.AudioViewPagerAdapter2;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.fragment.content.AudioContentFragment;
import com.ai.bbcpro.ui.fragment.content.EvaluateContentFragment;
import com.ai.bbcpro.ui.fragment.content.ParaFragment;
import com.ai.bbcpro.ui.fragment.content.QuestionFragment;
import com.ai.bbcpro.ui.fragment.content.RankContentFragment;
import com.ai.bbcpro.ui.helper.GoToVipHelper;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.bean.PDFExportBean;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.PdfService;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.tabs.TabLayout;
import com.mob.MobSDK;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.yd.saas.base.interfaces.AdViewInterstitialListener;
import com.yd.saas.config.exception.YdError;
import com.yd.saas.ydsdk.YdInterstitial;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * 首页-item的详情页面  新闻详情主界面
 */
public class AudioContentActivity extends AppCompatActivity implements AudioContentContract.AudioContentView {

    String TAG = "AudioContentActivity";
    private Context mContext;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AudioViewPagerAdapter2 adapter;
    private AudioContentFragment audioContentFragment;
    private EvaluateContentFragment evaluateContentFragment;
    private RankContentFragment rankFragment;
    private QuestionFragment questionFragment;

    private ParaFragment paraFragment;

    private List<String> titleList = new ArrayList<>();
    private List<Fragment> fragmentList = new ArrayList<>();
    private ImageView more, goBack;
    HeadlineMarqueeTextView title;
    private AccountManager accountManager;
    private int isEnglish;
    private String[] strings = {"导出英文", "导出中英双语"};
    private final OnekeyShare oks = new OnekeyShare();
    private String uid;
    private PopupMenu popupMenu;
    private SyncDataHelper syncDataHelper;

    //接收数据,文章题目信息
    private SumBean.DataDTO sumData;
    private int vPage = 0;//显示第几页

    private AudioContentPresenter audioContentPresenter;

    private RxPermissions rxPermissions;

    /**
     * 权限
     */
    private SharedPreferences pSP;

    private YdInterstitial ydInterstitial;
    private int interstitialAdPos = 0;

    private int parentID;

    public static void startActivity(Activity activity, SumBean.DataDTO sumData, int parentID, int vpage) {

        Intent intent = new Intent(activity, AudioContentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATA", sumData);
        bundle.putInt("PARENT_ID", parentID);
        bundle.putInt("VPAGE", vpage);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //处理点击通知打开mainactivity
        if (intent != null) {

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

/*                String action = bundle.getString("MY_ACTION");
                List<SumBean.DataDTO> itemList = (List<SumBean.DataDTO>) bundle.getSerializable("DATA");
                int position = bundle.getInt("POSITION");
                if (action.equals("OPEN")) {

//                    com.ai.bbcpro.ui.player.AudioContentActivity.startActivity2(this, itemList, position, 0);
                }*/
            }
        }
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            sumData = (SumBean.DataDTO) bundle.getSerializable("DATA");
            vPage = bundle.getInt("VPAGE", 0);
            parentID = bundle.getInt("PARENT_ID", 0);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_total);
        EventBus.getDefault().register(this);

        pSP = getSharedPreferences(Constant.SP_PERMISSIONS, Context.MODE_PRIVATE);

        getBundle();
        SumBean.DataDTO dto = sumData;

        audioContentPresenter = new AudioContentPresenter();
        audioContentPresenter.attchView(this);

        mContext = this;
        more = findViewById(R.id.more_func);
        title = findViewById(R.id.marquee1);
        tabLayout = findViewById(R.id.tab_content);
        viewPager = findViewById(R.id.viewpager_content);
        goBack = findViewById(R.id.go_back);
        uid = ConfigManager.Instance().loadString("userId", "0");
        syncDataHelper = SyncDataHelper.getInstance(mContext);
        audioContentFragment = AudioContentFragment.newInstance(dto, parentID);
        evaluateContentFragment = EvaluateContentFragment.getInstance(dto.getBbcId(), dto.getSound(), dto.getPic());
        questionFragment = QuestionFragment.newInstance(Integer.parseInt(dto.getBbcId()));

        rankFragment = new RankContentFragment();

        paraFragment = ParaFragment.newInstance(Integer.parseInt(dto.getBbcId()));

        fragmentList.add(audioContentFragment);
        fragmentList.add(paraFragment);
        fragmentList.add(evaluateContentFragment);
        fragmentList.add(rankFragment);
        fragmentList.add(questionFragment);
        titleList.add("原文");
        titleList.add("阅读");
        titleList.add("测评");
        titleList.add("排行");
        titleList.add("问题");

        adapter = new AudioViewPagerAdapter2(getSupportFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        //显示哪页
        viewPager.setCurrentItem(vPage);


        accountManager = AccountManager.Instance(mContext);

        Bundle bundle = new Bundle();
        bundle.putString("sound", dto.getSound());
        bundle.putString("bbcid", dto.getBbcId());
        bundle.putString("image", dto.getPic());
        bundle.putSerializable("DATA", sumData);
//        audioContentFragment.setArguments(bundle);
        evaluateContentFragment.setArguments(bundle);
        rankFragment.setArguments(bundle);
        title.setText(dto.getTitle());


        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);

            }
        });

    }

    private void showMenu(View v) {

        popupMenu = new PopupMenu(this, v);
        //获取收藏状态
        int collect = HeadlinesDataManager.getInstance(this).getTextCollect(sumData.getBbcId());
        if (collect == 1) {
            popupMenu.inflate(R.menu.popupmenu2);
        } else {
            popupMenu.inflate(R.menu.popupmenu);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.pdf_out:
                        exportPDF();
                        break;
                    case R.id.title_share:
                        if (accountManager.checkUserLogin()) {
                            showNavigate();
                        } else {
                            GoToVipHelper.getInstance().goToLogin(AudioContentActivity.this);
                        }
                        break;
                    case R.id.collect_article:
                        if (accountManager.checkUserLogin()) {
                            //收藏
                            String userid = ConfigManager.Instance().loadString("userId", "");
                            audioContentPresenter.updateCollect(userid, sumData.getBbcId(), "Iyuba",
                                    0 + "", 0, Constant.APPID, "insert", "bbc", "json");
                        } else {
                            GoToVipHelper.getInstance().goToLogin(AudioContentActivity.this);
                        }

                        break;
                    case R.id.remove_article:
//                        cancelCollect();
                        //取消收藏
                        String userid = ConfigManager.Instance().loadString("userId", "");
                        audioContentPresenter.updateCollect(userid, sumData.getBbcId(), "Iyuba",
                                0 + "", 0, Constant.APPID, "del", "bbc", "json");
                        break;
                    case R.id.menu_update://更新

                        audioContentPresenter.textAllApi("json", sumData.getBbcId());
                        break;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void showNavigate() {
        View bottomView = View.inflate(this, R.layout.share_menu, null);
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
        Window screen = getWindow();
        WindowManager.LayoutParams attributes = screen.getAttributes();
        attributes.alpha = 0.5f;
        screen.setAttributes(attributes);
        window.setOnDismissListener(() -> {
            attributes.alpha = 1f;
            screen.setAttributes(attributes);
        });
        window.setAnimationStyle(R.style.main_menu_photo_anim);
        window.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 10);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.share_ly_qq:
//                        shareToPlatform(ShareSDK.getPlatform(QQ.NAME).getName());

                        requestWrite(sumData.getPic(), ShareSDK.getPlatform(QQ.NAME).getName());
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

            rxPermissions = new RxPermissions(this);
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

                    FileOutputStream fileOutputStream = new FileOutputStream(new File(picPath, sumData.getBbcId() + ".jpg"));
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
        oks.setTitle(sumData.getTitle_cn());
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://m.iyuba.cn/news.html?id=" + sumData.getBbcId() + "&type=bbc");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我在" + Constant.AppName + "练习听说，快来和我一起学习吧！");
        oks.setImageUrl(sumData.getPic());


        //QQ好友分享需要使用的设置
        String picPath = MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                + "/share/"
                + sumData.getBbcId() + ".jpg";
        oks.setImagePath(picPath);

        oks.setUrl("http://m.iyuba.cn/news.html?id=" + sumData.getBbcId() + "&type=bbc");
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

    private void exportPDF() {
        if (accountManager.checkUserLogin()) {
            if (AccountManager.isVip()) {
                AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).setTitle("请选择需要导出的PDF形式").setItems(strings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            isEnglish = 1;
                        } else {
                            isEnglish = 3;
                        }

                        dialog.dismiss();
                        Retrofit retrofit = new Retrofit.Builder().baseUrl(PdfService.BASE_URL).
                                addConverterFactory(GsonConverterFactory.create()).
                                client(new OkHttpClient()).
                                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                                build();
                        retrofit.create(PdfService.class)
                                .post("bbc", sumData.getBbcId(), isEnglish)
                                .enqueue(new Callback<PDFExportBean>() {
                                    @Override
                                    public void onResponse(Call<PDFExportBean> call, Response<PDFExportBean> response) {
                                        if (response.body().getExists()) {
                                            String path = "http://apps." + CommonConstant.domain + "/minutes/" + response.body().getPath();
                                            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            cm.setText(path);
                                            AlertDialog alertDialog = (new AlertDialog.Builder(mContext)).setTitle("PDF链接生成成功!").setMessage(path + "\t(链接已复制)").setNegativeButton("下载", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent intent = new Intent();
                                                    intent.setAction("android.intent.action.VIEW");
                                                    Uri uri = Uri.parse(path);
                                                    intent.setData(uri);
                                                    startActivity(intent);
                                                }
                                            }).setPositiveButton("返回", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    return;
                                                }
                                            }).create();
                                            alertDialog.show();
                                        } else {
                                            Toast.makeText(AudioContentActivity.this, "该PDF文件不存在", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PDFExportBean> call, Throwable t) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                toast("pdf获取超时，请重试");
                                            }
                                        });
                                    }
                                });

                    }
                }).create();
                alertDialog.show();
            } else {
                GoToVipHelper.getInstance().takeYourMoney(mContext);
            }
        } else {
            GoToVipHelper.getInstance().goToLogin(AudioContentActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == 2) {
                    ToastUtil.showToast(mContext, "您尚未开通VIP,暂时无法使用此功能");
                }
                break;
        }
    }

    /**
     * 播放完成，显示广告
     *
     * @param mediaCompleteEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MediaCompleteEventbus mediaCompleteEventbus) {

        requestAd();
    }


    /**
     * 请求插屏广告
     */
    private void requestAd() {


        if (AccountManager.isVip()) {

            return;
        }

        if (ydInterstitial != null) {

            ydInterstitial.destroy();
        }
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        String type;
        if (interstitialAdPos == 0) {

            type = BuildConfig.INTERSTITIAL_AD_KEY_CSJ;
        } else if (interstitialAdPos == 1) {

            type = BuildConfig.INTERSTITIAL_AD_KEY_YLH;
        } else {

            type = BuildConfig.INTERSTITIAL_AD_KEY_BD;
        }

        ydInterstitial = new YdInterstitial.Builder(AudioContentActivity.this)
                .setKey(type)
                .setWidth((int) (displayMetrics.widthPixels * 0.8))
                .setHeight((int) (displayMetrics.heightPixels * 0.5))
                .setInterstitialListener(new AdViewInterstitialListener() {
                    @Override
                    public void onAdReady() {

                        Timber.d("onAdReady");
                        ydInterstitial.show();
                    }

                    @Override
                    public void onAdDisplay() {

                        Timber.d("onAdDisplay");
                    }

                    @Override
                    public void onAdClick(String s) {

                        Timber.d("onAdClick");
                    }

                    @Override
                    public void onAdClosed() {

                        Timber.d("onAdClosed");
                    }

                    @Override
                    public void onAdFailed(YdError ydError) {

                        if (interstitialAdPos == 0) {

                            interstitialAdPos = 1;
                            requestAd();
                        } else if (interstitialAdPos == 1) {

                            interstitialAdPos = 2;
                            requestAd();
                        } else {

                            Timber.d(ydError.getMsg());
                        }
                    }
                })
                .build();
        ydInterstitial.requestInterstitial();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);

        if (ydInterstitial != null) {

            ydInterstitial.destroy();
        }

        if (audioContentPresenter != null) {

            audioContentPresenter.detachView();
        }
    }

    /**
     * 切换数据
     *
     * @param dataDTO
     * @param position
     */
    public void setNewData(SumBean.DataDTO dataDTO, int position) {

        //本页的标题栏
        title.setText(dataDTO.getTitle());
        //新闻文字页面
        if (audioContentFragment != null) {

            Bundle bundle = audioContentFragment.getArguments();
            if (bundle != null) {

                bundle.putSerializable("DATA", dataDTO);
            }
            audioContentFragment.switchData();
        }
        //评测
        if (evaluateContentFragment != null) {

            Bundle bundle = evaluateContentFragment.getArguments();
            if (bundle != null) {

                bundle.putString("bbcid", dataDTO.getBbcId());
                bundle.putString("sound", dataDTO.getSound());
                bundle.putString("image", dataDTO.getPic());
            }
            evaluateContentFragment.switchData();
        }
        //排行榜
        if (rankFragment != null) {

            Bundle bundle = rankFragment.getArguments();
            if (bundle != null) {

                bundle.putString("bbcid", dataDTO.getBbcId());
            }
            rankFragment.switchData();
        }
        //练习题
        if (questionFragment != null) {

            Bundle bundle = questionFragment.getArguments();
            if (bundle != null) {

                bundle.putString("bbcid", dataDTO.getBbcId());
            }
            questionFragment.switchData();
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
    public void textAllApi(BBCContentBean bbcContentBean) {

        if (!bbcContentBean.getTotal().equals("0")) {

            HeadlinesDataManager.getInstance(getApplicationContext()).saveAndUpdateDetail(bbcContentBean.getData());

            //存储问题到数据库
            List<QuestionBean> questionBeanList = bbcContentBean.getDataQuestion();
            for (int i = 0; i < questionBeanList.size(); i++) {

                QuestionBean questionBean = questionBeanList.get(i);
                long count = HeadlinesDataManager.getInstance(getApplicationContext()).hasQuestionBean(questionBean.getBbcId(), questionBean.getIndexId());
                if (count > 0) {//有数据

                    HeadlinesDataManager.getInstance(getApplicationContext()).updateQuestion(questionBean);
                } else {

                    HeadlinesDataManager.getInstance(getApplicationContext()).addQuestion(questionBean);
                }
            }
            if (audioContentFragment != null) {

                audioContentFragment.updateData();
            }
            if (evaluateContentFragment != null) {

                evaluateContentFragment.updateData();
            }
            if (questionFragment != null) {

                questionFragment.updateData();
            }
            Toast.makeText(MainApplication.getApplication(), "完成更新", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(MainApplication.getApplication(), "暂无数据", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void updateCollect(String type, String voaId) {

        if (type.equals("insert")) {

            HeadlinesDataManager.getInstance(this).collectText(voaId, 1);
            toast("收藏成功");
        } else {

            HeadlinesDataManager.getInstance(this).collectText(voaId, 0);
            toast("取消收藏成功");
        }
    }
}
