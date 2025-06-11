package com.ai.bbcpro.ui.vip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.MoreInfoBean;
import com.ai.bbcpro.mvp.presenter.me.VipCenterPresenter;
import com.ai.bbcpro.mvp.view.me.VipCenterContract;
import com.ai.bbcpro.ui.activity.BuyIyubiActivity;
import com.ai.bbcpro.ui.widget.IyuButton;
import com.bumptech.glide.Glide;
import com.iyuba.module.toolbox.MD5;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 会员中心
 */
public class VipCenterActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener
        , VipCenterContract.VipCenterView {

    private TextView tv_iyubi;
    private CheckBox checkBox1, checkBox6, checkBox12, checkBox36, checkBoxG1, checkBoxG3, checkBoxG12, checkBoxG6;
    private View view1, view6, view12, view36, viewG1, viewG3, viewG6, viewG12;
    private CircleImageView userImage;
    private TextView userName, vipShow, iyubiCoin;
    private ImageView back;
    private IyuButton goToOrder, goToBuyIyuCoin;
    private String username;
    private String userImageUri;
    private Intent intent;
    private Date date;
    private int price = 25;
    private int month = 1;
    private Context mContext;
    private TabHost mTabHost;
    private int vipCate;
    private int productId;
    private String TAG = "VipCenter";
    private boolean toGold;

    private VipCenterPresenter vipCenterPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_vip);

        vipCenterPresenter = new VipCenterPresenter();
        vipCenterPresenter.attchView(this);

        date = new Date();
        mContext = this;
        toGold = getIntent().getBooleanExtra("toGold", false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        username = getIntent().getStringExtra("username");
        userImageUri = getIntent().getStringExtra("userimage");
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (vipCenterPresenter != null) {

            vipCenterPresenter.detachView();
        }
    }

    public static void start(Context context, boolean toGold) {

        Intent intent = new Intent(context, VipCenterActivity.class);
        intent.putExtra("toGold", toGold);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String userid = ConfigManager.Instance().loadString("userId", "");


        String expireTime = ConfigManager.Instance().loadString("expireTime", "");
        if (checkExpireDate(expireTime + "")) {

            if (expireTime.length() == 10) {

                expireTime = expireTime + "000";
            }
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
            simpleDateFormat.applyPattern("yyyy-MM-dd");
            String et = simpleDateFormat.format(new Date(Long.parseLong(expireTime)));
            vipShow.setText(et);
        } else {
            vipShow.setText("您还不是VIP");
        }

        if (!userid.equals("")) {

            String sign = MD5.getMD5ofStr("20001" + userid + "iyubaV2");
            vipCenterPresenter.getUserInfo("android", "json", Constant.APPID,
                    "20001", userid + "", userid + "", sign);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        date = new Date();
    }

    private void initView() {
        goToBuyIyuCoin = findViewById(R.id.btn_buyiyuba);
        iyubiCoin = findViewById(R.id.tv_iyucoin);
        mTabHost = findViewById(R.id.vip_tab_host);
        back = findViewById(R.id.iv_back);
        checkBox1 = findViewById(R.id.cb_local1);
        checkBoxG1 = findViewById(R.id.cb_local1_golden);
        checkBox6 = findViewById(R.id.cb_local6);
        checkBoxG6 = findViewById(R.id.cb_local6_golden);
        checkBox12 = findViewById(R.id.cb_local12);
        checkBox36 = findViewById(R.id.cb_local36);
        checkBoxG12 = findViewById(R.id.cb_local12_golden);
        checkBoxG3 = findViewById(R.id.cb_local3_golden);
        userImage = findViewById(R.id.user_icon);
        userName = findViewById(R.id.user_name);
        view1 = findViewById(R.id.rllocal1);
        view6 = findViewById(R.id.rllocal6);
        view12 = findViewById(R.id.rllocal12);
        view36 = findViewById(R.id.rllocal36);
        viewG1 = findViewById(R.id.rllocal1_golden);
        viewG3 = findViewById(R.id.rllocal3_golden);
        viewG6 = findViewById(R.id.rllocal6_golden);
        viewG12 = findViewById(R.id.rllocal12_golden);
        vipShow = findViewById(R.id.expiredate);
        goToOrder = findViewById(R.id.go_to_pay);
        tv_iyubi = findViewById(R.id.tv_iyubi);

        checkBox1.setOnCheckedChangeListener(this);
        checkBox6.setOnCheckedChangeListener(this);
        checkBox12.setOnCheckedChangeListener(this);
        checkBox36.setOnCheckedChangeListener(this);
        checkBoxG1.setOnCheckedChangeListener(this);
        checkBoxG6.setOnCheckedChangeListener(this);
        checkBoxG12.setOnCheckedChangeListener(this);
        checkBoxG3.setOnCheckedChangeListener(this);
        goToBuyIyuCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, BuyIyubiActivity.class));
            }
        });
        mTabHost.setup();
        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(composeLayout("本应用VIP", R.drawable.forever_vip)).setContent(R.id.ll_native_vip));
        mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator(composeLayout("黄金会员", R.drawable.gold_vip)).setContent(R.id.ll_golden_vip));
        updateTab(0);
        if (toGold) {
            mTabHost.setCurrentTab(1);
            updateTab(1);
            productId = 2;
            price = 98;
            month = 1;
        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Log.e(TAG, "onTabChanged: " + s);
                switch (s) {
                    case "tab1":
                        updateTab(0);
                        productId = 10;
                        vipCate = 0;
                        if (checkBox1.isChecked()) {
                            price = 25;
                            month = 1;
                        } else if (checkBox6.isChecked()) {
                            price = 69;
                            month = 6;
                        } else if (checkBox12.isChecked()) {
                            price = 99;
                            month = 12;
                        } else if (checkBox36.isChecked()) {
                            price = 199;
                            month = 36;
                        }
                        break;
                    case "tab3":
                        updateTab(1);
                        productId = 2;
                        vipCate = 2;
                        if (checkBoxG1.isChecked()) {
                            price = 98;
                            month = 1;
                        } else if (checkBoxG3.isChecked()) {
                            price = 198;
                            month = 3;
                        } else if (checkBoxG6.isChecked()) {
                            price = 299;
                            month = 6;
                        } else if (checkBoxG12.isChecked()) {
                            price = 399;
                            month = 12;
                        }
                        break;
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (username == null) {
            username = ConfigManager.Instance().loadString("userName");
        }
        userName.setText(username);
        if (userImageUri == null) {
            userImageUri = ConfigManager.Instance().loadString("imgUrl");
            if (!userImageUri.startsWith("http")) {

                userImageUri = "http://static1.iyuba.cn/uc_server/" + userImageUri;
            }
        }
        Glide.with(this).load(userImageUri).error(R.mipmap.noavatar_middle).into(userImage);

        String expireTime = ConfigManager.Instance().loadString("expireTime", "");
        if (!expireTime.equals("")) {

            if (checkExpireDate(expireTime)) {

                if (expireTime.length() == 10) {

                    expireTime = expireTime + "000";
                }
                SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                simpleDateFormat.applyPattern("yyyy-MM-dd");
                String et = simpleDateFormat.format(new Date(Long.parseLong(expireTime)));
                vipShow.setText(et);
            } else {
                vipShow.setText("您还不是VIP");
            }
        }
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox1.setChecked(true);
                price = 25;
                month = 1;
                vipCate = 0;
                productId = 10;
            }
        });
        view6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox6.setChecked(true);
                price = 69;
                month = 6;
                vipCate = 0;
                productId = 10;
            }
        });
        view12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox12.setChecked(true);
                price = 99;
                month = 12;
                vipCate = 0;
                productId = 10;
            }
        });
        view36.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox36.setChecked(true);
                price = 199;
                month = 36;
                vipCate = 0;
                productId = 10;
            }
        });
        viewG1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxG1.setChecked(true);
                price = 98;
                month = 1;
                vipCate = 2;
                productId = 2;

            }
        });
        viewG3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxG3.setChecked(true);
                price = 198;
                month = 3;
                vipCate = 2;
                productId = 2;
            }
        });
        viewG6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxG6.setChecked(true);
                price = 299;
                month = 6;
                vipCate = 2;
                productId = 2;
            }
        });
        viewG12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBoxG12.setChecked(true);
                price = 399;
                month = 12;
                vipCate = 2;
                productId = 2;
            }
        });

        goToOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPay(price, month);
            }
        });

    }

    private void updateTab(int i) {
        mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(0x00FFFFFF);
        mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(0x00FFFFFF);
        switch (i) {
            case 0:
                mTabHost.getTabWidget().getChildAt(0).setBackgroundColor(0xFFFDDA94);
                break;
            case 1:
                mTabHost.getTabWidget().getChildAt(1).setBackgroundColor(0xFFFDDA94);
                break;
        }
    }

    public boolean checkExpireDate(String date) {

        if (date.length() == 10) {
            date = date + "000";
        }
        long expireDate = Long.parseLong(date);

        return expireDate >= System.currentTimeMillis();

    }

    public View composeLayout(String s, int i) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        ImageView iv = new ImageView(this);
        iv.setImageResource(i);
        iv.setAdjustViewBounds(true);
        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, dp2px(mContext, 20), 0, 20);
        lp.gravity = Gravity.CENTER;
        layout.addView(iv, lp);
        TextView tv = new TextView(this);
        tv.setGravity(Gravity.CENTER);
        tv.setSingleLine(true);
        tv.setText(s);
        tv.setTextColor(0xFF598aad);
        tv.setTextSize(14);
        LinearLayout.LayoutParams lpo = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpo.setMargins(0, 0, 0, dp2px(mContext, 20));
        layout.addView(tv, lpo);
        return layout;
    }

    public static int dp2px(Context context, float dipValue) {
        return (int) (dipValue * context.getResources().getDisplayMetrics().density + 0.5f);
    }

    public void goToPay(int price, int month) {

        intent = new Intent(this, PayActivity.class);
        intent.putExtra("price", price + "");
        intent.putExtra("username", username);
        intent.putExtra("type", month);
        intent.putExtra("productId", productId);
        if (month == 0) {
            intent.putExtra("subject", Constant.APPName + "永久vip");
        } else if (month == 120) {
            intent.putExtra("subject", Constant.APPName + "一年vip");
        }

        String vipCateString[] = {"本应用", "全站", "黄金"};
        Log.e(TAG, "goToPay: " + productId + " " + vipCateString[vipCate] + " " + price + " " + month);
        intent.putExtra("body", "花费" + price + "元购买" + vipCateString[vipCate] + "vip");
        startActivity(intent);
    }

    private String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Log.e("check date", "date: " + date);
        String key = format.format(date);
        Random r = new Random();
        key = key + Math.abs(r.nextInt());
        key = key.substring(0, 15);
        return key;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_local1:
                if (checkBox1.isChecked()) {
                    checkBox6.setChecked(false);
                    checkBox12.setChecked(false);
                    checkBox36.setChecked(false);
                }
                break;
            case R.id.cb_local6:
                if (checkBox6.isChecked()) {
                    checkBox1.setChecked(false);
                    checkBox12.setChecked(false);
                    checkBox36.setChecked(false);
                }
                break;
            case R.id.cb_local12:
                if (checkBox12.isChecked()) {
                    checkBox6.setChecked(false);
                    checkBox1.setChecked(false);
                    checkBox36.setChecked(false);
                }
                break;
            case R.id.cb_local36:
                if (checkBox36.isChecked()) {
                    checkBox6.setChecked(false);
                    checkBox12.setChecked(false);
                    checkBox1.setChecked(false);
                }
                break;
            case R.id.cb_local1_golden:
                if (checkBoxG1.isChecked()) {
                    checkBoxG3.setChecked(false);
                    checkBoxG6.setChecked(false);
                    checkBoxG12.setChecked(false);
                }

                break;
            case R.id.cb_local3_golden:
                if (checkBoxG3.isChecked()) {
                    checkBoxG1.setChecked(false);
                    checkBoxG6.setChecked(false);
                    checkBoxG12.setChecked(false);
                }
                break;
            case R.id.cb_local6_golden:
                if (checkBoxG6.isChecked()) {
                    checkBoxG1.setChecked(false);
                    checkBoxG3.setChecked(false);
                    checkBoxG12.setChecked(false);
                }
                break;
            case R.id.cb_local12_golden:
                if (checkBoxG12.isChecked()) {
                    checkBoxG1.setChecked(false);
                    checkBoxG3.setChecked(false);
                    checkBoxG6.setChecked(false);
                }
                break;
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

    }

    @Override
    public void getUserInfo(MoreInfoBean moreInfoBean, String uid) {

        iyubiCoin.setText(moreInfoBean.getAmount() + "");
    }
}
