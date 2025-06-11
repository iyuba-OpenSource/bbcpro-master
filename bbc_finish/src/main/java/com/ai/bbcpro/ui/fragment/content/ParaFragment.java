package com.ai.bbcpro.ui.fragment.content;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.bbcpro.Constant;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.home.ParaAdapter;
import com.ai.bbcpro.databinding.FragmentParaBinding;
import com.ai.bbcpro.entity.Para;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.home.ReadSubmitBean;
import com.ai.bbcpro.mvp.presenter.home.ParaPresenter;
import com.ai.bbcpro.mvp.view.home.ParaContract;
import com.ai.bbcpro.ui.BaseFragment;
import com.ai.bbcpro.ui.activity.login.LoginActivity;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.util.decoration.LineItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 主页面——阅读
 */
public class ParaFragment extends BaseFragment<ParaContract.ParaView, ParaContract.ParaPresenter>
        implements ParaContract.ParaView {


    private int voaid;

    private FragmentParaBinding binding;

    private ParaAdapter paraAdapter;

    private LineItemDecoration lineItemDecoration;

    /**
     * 考试阅读的开始时间
     */
    private long startTime = 0;

    /**
     * 考试阅读的结束时间
     */
    private long endTime = 0;

    /**
     * 单词数量
     */
    private int wordCount = 0;

    private HeadlinesDataManager dataManager;

    public ParaFragment() {
    }


    public static ParaFragment newInstance(int voaid) {

        ParaFragment fragment = new ParaFragment();
        Bundle args = new Bundle();
        args.putInt("VOA_ID", voaid);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

            voaid = getArguments().getInt("VOA_ID");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();

        lineItemDecoration = new LineItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(view.getResources().getDrawable(R.drawable.space_10dp));
        if (binding.paraRv.getItemDecorationCount() == 0) {

            binding.paraRv.addItemDecoration(lineItemDecoration);
        }

        binding.paraRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        paraAdapter = new ParaAdapter(R.layout.item_para, new ArrayList<>());
        binding.paraRv.setAdapter(paraAdapter);

        getData();
    }

    @Override
    protected View initLayout(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentParaBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    protected ParaContract.ParaPresenter initPresenter() {
        return new ParaPresenter();
    }

    @Override
    public void onResume() {
        super.onResume();

        startTime = System.currentTimeMillis();
        EventBus.getDefault().post(new MediaPause());
    }

    /**
     * 点击事件
     */
    private void initOperation() {

        binding.paraIvEncn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (paraAdapter != null) {

                    if (paraAdapter.isShowEn()) {

                        paraAdapter.setShowEn(false);
                    } else {
                        paraAdapter.setShowEn(true);
                    }
                    paraAdapter.notifyDataSetChanged();
                }

            }
        });

        binding.paraButSubmit.setOnClickListener(v -> {

            String userId = ConfigManager.Instance().loadString("userId", "");

            if (userId.equals("")) {//是否登录

                new AlertDialog.Builder(requireContext())
                        .setTitle("信息")
                        .setMessage("您还未登录，去登录吗？")
                        .setPositiveButton("确定", (dialog, which) -> {

                           EventBus.getDefault().post(new LoginEventbus());
                        })
                        .setNegativeButton("取消", (dialog, which) -> {

                            dialog.dismiss();
                        })
                        .show();
                return;
            }


            endTime = System.currentTimeMillis();
            double time = 1.0 * (endTime - startTime);
            int readspeed = (int) (wordCount / (time / (1000 * 60)));

            if (readspeed > Constant.NORMAL_WPM) {

                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("阅读提醒")
                        .setMessage("你认真读完了这篇文章吗?请用正常速度阅读。")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {


                int m_used = (int) (time / (1000 * 60));
                int s_used = (int) ((time / 1000) % 60);
                final String timestr = m_used + "分" + s_used + "秒";

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder
                        .append("当前文章单词数量：").append(wordCount + "\n")
                        .append("当前的阅读时间：").append(timestr + "\n")
                        .append("当前的阅读速度：").append(readspeed + "单词/分钟");


                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setTitle("阅读提醒")
                        .setMessage(stringBuilder.toString())
                        .setPositiveButton("确定", (dialog, which) -> {

                            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
                            simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");

                            String beginTime = simpleDateFormat.format(new Date(startTime));
                            String endTimeStr = simpleDateFormat.format(new Date(endTime));
                            presenter.updateNewsStudyRecord("json", Integer.parseInt(userId), beginTime, endTimeStr, "bbc",
                                    "bbc", voaid, Integer.parseInt(Constant.APPID), "",
                                    "", 1, wordCount, 0, "android",
                                    1);
                        })
                        .show();
            }

        });
    }

    /**
     * 获取数据
     */
    private void getData() {

        new Thread(() -> {


            if (dataManager == null) {

                dataManager = HeadlinesDataManager.getInstance(requireContext());
            }
            List<BBCContentBean.DataBean> data = dataManager.loadSentence(voaid + "");
            List<Para> paraList = getParaList(data);
            new Handler(Looper.getMainLooper()).post(() -> paraAdapter.setNewData(paraList));

        }).start();
    }


    private List<Para> getParaList(List<BBCContentBean.DataBean> sentences) {

        List<Para> paraList = new ArrayList<>();

        StringBuilder eStringBuilder = null;
        StringBuilder cStringBuilder = null;
        //组装paraList
        for (int i = 0; i < sentences.size(); i++) {

            BBCContentBean.DataBean sentence = sentences.get(i);

            wordCount = wordCount + sentence.getSentence().split("\\s").length;
            if (i == 0) {//第一项

                eStringBuilder = new StringBuilder(sentence.getSentence());
                cStringBuilder = new StringBuilder(sentence.getSentenceCn());
            } else {

                BBCContentBean.DataBean pre = sentences.get(i - 1);
                if (sentence.getParaId().equals(pre.getParaId())) {//是同一个

                    eStringBuilder.append(sentence.getSentence());
                    cStringBuilder.append(sentence.getSentenceCn());
                } else {

                    Para para = new Para();
                    para.setEn(eStringBuilder.toString());
                    para.setCn(cStringBuilder.toString());
                    paraList.add(para);
                    //清空数据
                    eStringBuilder.delete(0, eStringBuilder.length());
                    cStringBuilder.delete(0, cStringBuilder.length());
                    eStringBuilder.append(sentence.getSentence());
                    cStringBuilder.append(sentence.getSentenceCn());
                }

                if (i == (sentences.size() - 1) && !eStringBuilder.toString().equals("")) {
                    Para para = new Para();
                    para.setEn(eStringBuilder.toString());
                    para.setCn(cStringBuilder.toString());
                    paraList.add(para);
                }
            }
        }
        return paraList;
    }

    /**
     * 通过顺序播放和随机播放来更新数据
     */
    public void updateData() {

        if (getArguments() != null) {

//            voaid = getArguments().getInt("VOAID");
//            chapterOrder = getArguments().getInt("CHAPTER_ORDER");
        }

        getData();
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
    public void submitRead(ReadSubmitBean readSubmitBean) {

        if (!readSubmitBean.getReward().equals("0") && !readSubmitBean.getJifen().equals("0")) {

            int reward = Integer.parseInt(readSubmitBean.getReward());
            double rewardDouble = reward / 100.0f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");


            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励，已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众号提现，继续学习领取更多红包奖励吧！")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();
        } else if (readSubmitBean.getReward().equals("0") && !readSubmitBean.getJifen().equals("0")) {

            int jifen = Integer.parseInt(readSubmitBean.getJifen());
            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + jifen + "积分奖励。")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();

        } else if (!readSubmitBean.getReward().equals("0") && readSubmitBean.getJifen().equals("0")) {

            int reward = Integer.parseInt(readSubmitBean.getReward());
            double rewardDouble = reward / 100.0f;
            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            new AlertDialog.Builder(requireContext())
                    .setTitle("恭喜您！")
                    .setMessage("本次学习获得" + decimalFormat.format(rewardDouble) + "元红包奖励，已自动存入您的钱包账户。\n红包可在【爱语吧】微信公众号提现，继续学习领取更多红包奖励吧！")
                    .setPositiveButton("好的", (dialog, which) -> {

                        dialog.dismiss();
                    })
                    .show();
        }
    }
}