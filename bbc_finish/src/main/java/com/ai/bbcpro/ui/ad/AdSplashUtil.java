package com.ai.bbcpro.ui.ad;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;

import com.ai.bbcpro.http.main.Http;
import com.ai.bbcpro.http.main.HttpCallback;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.activity.OldSplashActivity;
import com.ai.bbcpro.util.IyubaADBean;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.youdao.sdk.nativeads.NativeErrorCode;
import com.youdao.sdk.nativeads.NativeResponse;
import com.youdao.sdk.nativeads.RequestParameters;
import com.youdao.sdk.nativeads.YouDaoNative;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;
import timber.log.Timber;

public class AdSplashUtil {
    private YouDaoNative youdaoNative;
    private Context mContext;
    private ImageView adView;
    private RelativeLayout rr1;
    private boolean isAdClick = false;
    private boolean isRequestAdEnd = false;

    private ImageView base;


    private String webUrl; // 自己的web广告的连接地址
    private String webPicUrl; // 自己的web广告的图片连接地址

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    private String TAG = "initActivity";

    public AdSplashUtil(final Context context, ImageView view, ViewGroup viewGroup) {
        this.mContext = context;
        //安卓VOA常速启动页开屏广告_9755487e03c2ff683be4e2d3218a2f2b
        //安卓VOA慢速启动页开屏广告_ a710131df1638d888ff85698f0203b46
        youdaoNative = new YouDaoNative(context, "a710131df1638d888ff85698f0203b46", youDaoAdListener);
        rr1 = (RelativeLayout) viewGroup;
        adView = view;
    }

    public AdSplashUtil(final Context context, ImageView view, ViewGroup viewGroup, ImageView base) {
        this.mContext = context;
        //安卓VOA常速启动页开屏广告_9755487e03c2ff683be4e2d3218a2f2b
        //安卓VOA慢速启动页开屏广告_ a710131df1638d888ff85698f0203b46
        youdaoNative = new YouDaoNative(context, "a710131df1638d888ff85698f0203b46", youDaoAdListener);
        rr1 = (RelativeLayout) viewGroup;
        adView = view;
        this.base = base;
    }


    public void requestAd() {
        String userId = AccountManager.Instance(mContext.getApplicationContext()).getUserId();
        if (TextUtils.isEmpty(userId)) {
            userId = "0";
        }
        String url = "http://dev." + CommonConstant.domain + "/getAdEntryAll.jsp?uid=" + userId
                + "&appId=" + Constant.APPID //148
                + "&flag=1";
        Request request = new Request.Builder().url(url).build();
        if (myCallback != null) {
            myCallback.startTimer();
        }
        Timber.tag("diao:").e(url);
        Http.getOkHttpClient3().newCall(request).enqueue(new HttpCallback() {
            @Override
            public void onSucceed(Call call, String response) {
                isRequestAdEnd = true;
                List<IyubaADBean> list = null;
                try {
                    list = new Gson().fromJson(response, new TypeToken<List<IyubaADBean>>() {
                    }.getType());
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                if (list == null || list.size() < 1) {
                    showYDSplash();
                    return;
                }

                try {
                    IyubaADBean adBean = list.get(0);
                    if ("1".equals(adBean.getResult())) {

                        if ("web".equals(adBean.getData().getType())) {//web diao
                            setWebPicUrl("http://static3." + CommonConstant.domain + "/dev/" + adBean.getData().getStartuppic());
                            setWebUrl(adBean.getData().getStartuppic_Url());
                            if (myCallback != null) {
                                myCallback.loadLocal();
                            }
                        } else if ("youdao".equals(adBean.getData().getType())) {
                            Log.e(TAG, "enter youdao ");
                            showYDSplash();
                        } else {

                            if (myCallback != null) {

                                myCallback.showOtherAD(adBean);
                            }
                        }
                    } else {
                        showYDSplash();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showYDSplash();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                isRequestAdEnd = true;
                showYDSplash();
            }
        });
    }

    public void showYDSplash() {
        Log.e(TAG, "showYDSplash: ");
        Timber.tag("有道开屏广告加载").e("有道开屏广告加载");
//        RequestParameters requestParameters = RequestParameters.builder().build();
        RequestParameters requestParameters = RequestParameters.builder().build();
        youdaoNative.makeRequest(requestParameters);
    }

    public void destroy() {
        youdaoNative.destroy();
    }

    public void setCallback(SCallback callback) {
        this.myCallback = callback;
    }

    public boolean isRequestEnd() {
        return isRequestAdEnd;
    }

    public boolean isClick() {
        return isAdClick;
    }

    public String getWebPicUrl() {
        return webPicUrl;
    }

    public void setWebPicUrl(String webPicUrl) {
        this.webPicUrl = webPicUrl;
    }

    public interface SCallback {
        void loadLocal();

        void onAdClick();

        void startTimer();

        void dismissTimer();

        /**
         * 显示穿山甲、倍孜、百度、快手的广告
         *
         * @param adBean
         */
        void showOtherAD(IyubaADBean adBean);
    }

    private SCallback myCallback;

    private YouDaoNative.YouDaoNativeNetworkListener youDaoAdListener = new YouDaoNative.YouDaoNativeNetworkListener() {
        @Override
        public void onNativeLoad(final NativeResponse nativeResponse) {

            Log.d("有道广告加载成功", nativeResponse.toString());
            if (mContext == null) {
                return;
            }
            adView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nativeResponse.handleClick(adView);
                    isAdClick = true;
                    if (myCallback != null) {
                        myCallback.onAdClick();
                    }
                }
            });

            Glide.with(MainApplication.getApplication()).load(nativeResponse.getMainImageUrl()).into(adView);
            nativeResponse.recordImpression(adView);
            //显示底部
            base.setImageResource(R.mipmap.splash_bottom);
            base.setVisibility(View.VISIBLE);

            /*            ImageService.get(mContext, imageUrls, new ImageService.ImageServiceListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onSuccess(final Map<String, Bitmap> bitmaps) {
                    if (nativeResponse.getMainImageUrl() != null) {
                        Bitmap bitMap = bitmaps.get(nativeResponse.getMainImageUrl());
                        if (bitMap != null) {
                            adView.setImageBitmap(bitMap);
                            nativeResponse.recordImpression(adView);
                        }
                    }
                }

                @Override
                public void onFail() {
                }
            });*/
        }

        @Override
        public void onNativeFail(NativeErrorCode nativeErrorCode) {

            Log.d("有道广告加载失败", nativeErrorCode.name());
        }
    };

    public static boolean isNoAdTime() {
        return false;
    }


    public void next() {
        if (((OldSplashActivity) mContext).canJump) {
            ((OldSplashActivity) mContext).startMainActivity();
        } else {
            ((OldSplashActivity) mContext).canJump = true;
        }
    }


}

