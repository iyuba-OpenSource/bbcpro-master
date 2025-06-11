package com.ai.bbcpro.ui.helper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.sqlite.db.DatabaseService;
import com.ai.bbcpro.ui.bean.CollectionBean;
import com.ai.bbcpro.ui.bean.RefreshInfoBean;
import com.ai.bbcpro.util.MD5;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyuba.module.user.IyuUserManager;
import com.iyuba.module.user.User;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SyncDataHelper extends DatabaseService {
    private String TAG = "SyncDataHelper";
    public static SyncDataHelper instance;
    public String expireTime;
    private Dialog dialog;
    private static Context mContext;
    private View v;

    protected SyncDataHelper() {
        super(mContext);
    }

    public static SyncDataHelper getInstance(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new SyncDataHelper();
        }
        return instance;
    }

    public void refreshUserInfo() {
        showLoading("同步中");
        String uid = ConfigManager.Instance().loadString("userId");
        String signStr = "20001" + uid + "iyubaV2";
        String sign = MD5.getMD5ofStr(signStr);
        String url = "http://api." + CommonConstant.domainLong + "/v2/api.iyuba?" +
                "platform=" + "android" +
                "&format=" + "json" +
                "&protocol=" + "20001" +
                "&id=" + uid +
                "&myid=" + uid +
                "&appid=" + Constant.APPID +
                "&sign=" + sign;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    RefreshInfoBean rr = new Gson().fromJson(response.body().string(), RefreshInfoBean.class);
                    User user = new User();
                    user.vipStatus = rr.getVipStatus();
                    user.uid = Integer.parseInt(uid);
                    user.name = rr.getUsername();
                    IyuUserManager.getInstance().updateUser(user);
                    int endTime = rr.getExpireTime();
                    ConfigManager.Instance().putString("expireTime", endTime + "");
                    ConfigManager.Instance().putInt("isvip2", Integer.parseInt(rr.getVipStatus()));
                    dialog.dismiss();
                }
            }
        });
    }

    void showLoading(String content) {
        View v = View.inflate(mContext, R.layout.dialog_waiting, null);
        TextView tvContent = v.findViewById(R.id.waiting_content);
        tvContent.setText(content);
        dialog = new Dialog(mContext, R.style.theme_dialog);
        dialog.setContentView(v);
        dialog.setCancelable(false);
        dialog.show();
    }

    public void loadCollectionData() {
        String uid = ConfigManager.Instance().loadString("userId", "");
        if (!uid.equals("")) {
            java.util.Date date = new java.util.Date();
            long unixTimestamp = date.getTime() / 1000 + 3600 * 8; //东八区;
            long days = unixTimestamp / 86400;
            String signStr = "iyuba + " + uid + "bbc" + "279" + days;
            String sign = MD5.getMD5ofStr(signStr);
            String url = "http://cms." + CommonConstant.domain + "/dataapi/jsp/getCollect.jsp?" +
                    "userId=" + uid +
                    "&topic=bbc" +
                    "&appid=" + Constant.APPID +
                    "&sentenceFlg=0" +
                    "&format=json" +
                    "&sign=" + sign;
            System.out.println("---------------" + url);
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .get()
                    .url(url)
                    .build();
            okhttp3.Call call = client.newCall(request);
            call.enqueue(new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    System.out.println("=================" + e.toString());
                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    if (response.body() != null) {
                        CollectionBean bean = new Gson().fromJson(response.body().string(), CollectionBean.class);
                        List<CollectionBean.DataBean> data = bean.getData();
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            list.add(data.get(i).getVoaid());
                        }
                        saveCollection(list);
                    }
                }
            });
        }
    }

    public void saveCollection(List<String> list) {
        Gson gson = new Gson();
        String data = gson.toJson(list);
        ConfigManager.Instance().putString("listStr", data);
    }

    public void addDownload(String bbcId) {
        List<String> list = loadDownload();
        list.add(bbcId);
        Gson gson = new Gson();
        String data = gson.toJson(list);
        ConfigManager.Instance().putString("downloadListStr", data);
    }

    public void deleteDownload(int position) {
        List<String> list = loadDownload();
        list.remove(position);
        Gson gson = new Gson();
        String data = gson.toJson(list);
        ConfigManager.Instance().putString("downloadListStr", data);
    }

    public List<String> loadDownload() {
        String data = ConfigManager.Instance().loadString("downloadListStr", "");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = gson.fromJson(data, listType);
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    public List<String> loadCollection() {
        String data = ConfigManager.Instance().loadString("listStr", "");
        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> list = gson.fromJson(data, listType);
        return list;
    }
}
