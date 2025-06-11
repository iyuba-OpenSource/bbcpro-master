package com.ai.bbcpro.ui.activity.me;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.MyTimeBean;
import com.ai.bbcpro.model.bean.ScoreBean;
import com.ai.bbcpro.mvp.presenter.MyTimePresenter;
import com.ai.bbcpro.mvp.view.MyTimeContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.util.DateUtil;
import com.ai.bbcpro.util.NumberUtil;
import com.ai.bbcpro.util.PackageUtil;
import com.ai.bbcpro.util.QRCodeEncoder;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.iyuba.module.toolbox.DensityUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 我的-打卡签到页面
 */
@SuppressLint({"SimpleDateFormat", "SetTextI18n"})
public class SignActivity extends AppCompatActivity implements MyTimeContract.MyTimeView {

    private static final String TAG_INTENT_SHAREEDID = "shareid";

    private int[] bgDrawables = new int[]{R.mipmap.bg_share1, R.mipmap.bg_share2, R.mipmap.bg_share3,
            R.mipmap.bg_share4, R.mipmap.bg_share5, R.mipmap.bg_share6};

    private ImageView imageView;
    private ImageView qrImage;
    private TextView tv1, tv2, tv3;
    private TextView sign;
    private ImageView userIcon;
    private TextView tvShareMsg;
    private TextView mSignHistory;
    /**
     * 时间
     */
    private int signStudyTime = 3 * 60;

    String shareTxt;
    LinearLayout ll;
    CustomDialog mWaittingDialog;

    private TextView tvUserName;
    private TextView tvAppName;

    String qrIconUrl = "";

    private String mStoragePath = "";

    private MyTimePresenter myTimePresenter;
    //数据源
    private MyTimeBean myTimeBean;
    /**
     * 用户id
     */
    private String userId;

    /**
     * app的名称
     */
    private String appName;

    public static Intent buildIntent(Context context, String shareId) {
        Intent intent = new Intent(context, SignActivity.class);
        intent.putExtra(TAG_INTENT_SHAREEDID, shareId);
        return intent;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appName = PackageUtil.getAppName(MainApplication.getApplication());

        myTimePresenter = new MyTimePresenter();
        myTimePresenter.attchView(this);

        initWindow();
        setContentView(R.layout.activity_sign);

        userId = ConfigManager.Instance().loadString("userId", "");
        initView();
        initData();
        initBackGround();
    }

    private void initWindow() {

        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void initData() {
        //初始化存储路径 start
        if (TextUtils.isEmpty(getExternalFilesDir("").getAbsolutePath())) {
            // 没有外部存储
            mStoragePath = getFilesDir().getAbsolutePath();
        } else {
            // 有外部存储
            mStoragePath = getExternalFilesDir("").getAbsolutePath();
        }
        //初始化存储路径 end

        mWaittingDialog = WaittingDialog.showDialog(this);
        mWaittingDialog.show();
        //请求打卡数据
        String userId = ConfigManager.Instance().loadString("userId", "");
        myTimePresenter.getMyTime(userId, (int) DateUtil.getDays(), 1);
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

        if (null != mWaittingDialog) {
            if (mWaittingDialog.isShowing()) {
                mWaittingDialog.dismiss();
            }
        }
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    private void initView() {

        imageView = findViewById(R.id.iv);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);

        sign = findViewById(R.id.tv_sign);
        ll = findViewById(R.id.ll);
        qrImage = findViewById(R.id.tv_qrcode);
        userIcon = findViewById(R.id.iv_userimg);
        tvUserName = findViewById(R.id.tv_username);
        tvAppName = findViewById(R.id.tv_appname);
        tvShareMsg = findViewById(R.id.tv_sharemsg);
        mSignHistory = findViewById(R.id.tv_sign_history);

        //签到
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myTimeBean == null) {

                    return;
                }
                int time = Integer.parseInt(myTimeBean.getTotalTime());

                //生成二位码并显示
                qrIconUrl = CommonConstant.URL_APP + "/share.jsp?uid=" + userId
                        + "&appId=" + Constant.APPID + "&shareId=" + myTimeBean.getShareId();
                Bitmap qr_bitmap = QRCodeEncoder.
                        syncEncodeQRCode(qrIconUrl, DensityUtil.dp2px(MainApplication.getApplication(), 65)
                                , Color.BLACK, Color.WHITE, BitmapFactory.decodeResource(getResources()
                                        , R.drawable.bbc));
                qrImage.setImageBitmap(qr_bitmap);
                if (time < signStudyTime) {

                    ToastUtil.showToast(SignActivity.this, String.format(Locale.CHINA, "打卡失败，当前已学习%d秒，\n满%d分钟可打卡", time, signStudyTime / 60));
                } else {

                    qrImage.setVisibility(View.VISIBLE);
                    sign.setVisibility(View.GONE);
                    tvShareMsg.setVisibility(View.VISIBLE);
                    findViewById(R.id.tv_close).setVisibility(View.INVISIBLE);
                    tvShareMsg.setText("长按图片识别二维码");
                    tvShareMsg.setGravity(Gravity.CENTER_HORIZONTAL);
                    findViewById(R.id.tv_desc).setVisibility(View.VISIBLE);
                    tvShareMsg.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.sign_bg_yellow));
                    writeBitmapToFile();
                    findViewById(R.id.tv_desc).setVisibility(View.INVISIBLE);
                    showShareOnMoment(MainApplication.getApplication(), userId + "",
                            Constant.APPID + "");
                }
            }
        });

        findViewById(R.id.tv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCloseAlert();
            }
        });

        mSignHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SignActivity.this, CalendarActivity.class));
            }
        });
    }

    private void showCloseAlert() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(SignActivity.this);
        dialog.setTitle("温馨提示");
        dialog.setMessage("点击下面的打卡按钮,成功分享至微信朋友圈,可以领取红包哦!您确定要退出吗?");
        dialog.setPositiveButton("留下打卡", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("去意已决", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        dialog.show();
    }

    private void initBackGround() {
        //背景
        Random random = new Random();
        int rInt = random.nextInt(6);
        imageView.setImageResource(bgDrawables[rInt]);

        //id
        String userId = ConfigManager.Instance().loadString("userId", "");
        //username
        String userName = ConfigManager.Instance().loadString("userName", "");

        //头像
        String userIconUrl = CommonConstant.URL_API_COM_CN + "/v2/api.iyuba?protocol=10005&uid="
                + userId + "&size=middle";
        Glide.with(getApplication()).load(userIconUrl).placeholder(R.mipmap.ic_elvator).dontAnimate().into(userIcon);
        tvUserName.setText(userName);
        tvAppName.setText(appName + "\n练习听力的好帮手!");
    }


    public void writeBitmapToFile() {

        View view = getWindow().getDecorView();
        ((TextView) findViewById(R.id.tv_desc)).setText("刚刚在 「" + appName + "」上完成了打卡");
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap == null) {
            return;
        }
        bitmap.setHasAlpha(false);
        bitmap.prepareToDraw();
        File newpngfile = new File(mStoragePath, "aaa.png");

        if (newpngfile.exists()) {
            newpngfile.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(newpngfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @author
     * @time
     * @describe 启动获取积分(红包的接口)
     */

    private void startInterfaceADDScore(String userID, String appid) {

        Date currentTime = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(currentTime);
        final String time = Base64.encodeToString(dateString.getBytes(), Base64.DEFAULT);

        myTimePresenter.updateScore(81, 1, time, userID, Integer.parseInt(appid));
    }


    public void showShareOnMoment(Context context, final String userID, final String AppId) {

        share(context,
                mStoragePath + "/aaa.png",
                null,
                null,
                "学英语的朋友们快扫个码！自从用上了爱语吧的" +
                        appName +
                        "app，我的英语听说水平明显提高！我们在这可以零起点学英语，练英语听力和口语、人工智能学英语！高效学英语！墙裂推荐~"
                , "每日学习打卡~",
                userID,
                AppId);
    }

    public void share(Context context, String imagePath, String imageUrl, String url,
                      String text, String comment, String userID, String AppId) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", text);
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData);
        Toast.makeText(MainApplication.getApplication(), "推荐语录已经复制到粘贴板", Toast.LENGTH_SHORT).show();

        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_IMAGE);
        sp.setTitle(text);
//        sp.setText(text);
//        sp.setImageUrl(imageUrl);
        sp.setImagePath(imagePath);
//        sp.setUrl(url);
        sp.setTitleUrl(qrIconUrl);


        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
        wechatMoments.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                startInterfaceADDScore(userID, AppId);
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.e("--分享失败=== onError", throwable.toString());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.e("okCallbackonError", "onCancel");
                Log.e("--分享取消=== onCancel", "....");
            }
        });
        wechatMoments.share(sp);
    }

    @Override
    public void getMyTimeComplete(MyTimeBean myTimeBean) {

        this.myTimeBean = myTimeBean;


        int totalDays = Integer.parseInt(myTimeBean.getTotalDays());
        //String url = CommonConstant.STATIC_URL + "/images/mobile/" + (totalDays % 30) + ".jpg";
        //Glide.with(mContext).load(url).placeholder(R.drawable.place).dontAnimate().into(imageView);
        tv1.setText("学习天数:\n" + myTimeBean.getTotalDays() + "天");
        //tv2.setText("总单词:\n" + mWordDao.getAnsweredWordSum());
        int rankRate = 100 - Integer.parseInt(myTimeBean.getRanking()) * 100 / Integer.parseInt(myTimeBean.getTotalUser());
        tv3.setText("超越了：\n" + rankRate + "%同学");
        shareTxt = myTimeBean.getSentence() + "我在" + getResources().getString(R.string.app_name) + "坚持学习了" + myTimeBean.getTotalDays() + "天"
                + "单词如下";

    }

    @Override
    public void updateScore(ScoreBean scoreBean) {

        String money = scoreBean.getMoney();//本次打卡获得的钱数 ，单位 分
        String totalCredit = scoreBean.getTotalcredit();//总积分
        String days = scoreBean.getDays();
        String addCredit = scoreBean.getAddcredit();

        float moneyThisTime = Float.parseFloat(money);
        if (moneyThisTime > 0) {
            float moneyTotal = Float.parseFloat(totalCredit);
            Toast.makeText(MainApplication.getApplication(), "打卡成功," + "您已连续打卡" + days + "天,  获得" + NumberUtil.getFormatDouble(moneyThisTime * 0.01) + "元,总计: " + NumberUtil.getFormatDouble(moneyTotal * 0.01) + "元," + "满10元可在\"爱语吧\"公众号提现", Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(MainApplication.getApplication(), "打卡成功，连续打卡" + days + "天,获得" + addCredit + "积分，总积分: " + totalCredit, Toast.LENGTH_LONG).show();
            finish();
        }
    }
}