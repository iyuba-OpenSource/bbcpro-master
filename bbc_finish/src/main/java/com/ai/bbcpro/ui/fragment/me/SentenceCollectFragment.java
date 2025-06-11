package com.ai.bbcpro.ui.fragment.me;

import static android.os.Build.VERSION_CODES.BASE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.me.SentenceCollectAdapter;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.FragmentSentenceCollectBinding;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.me.SentenceCollectBean;
import com.ai.bbcpro.mvp.presenter.me.SentenceCollectPresenter;
import com.ai.bbcpro.mvp.view.me.SentenceCollectContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.player.eval.RecycleViewData;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.iyuba.module.toolbox.MD5;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 句子收藏fragment
 */
public class SentenceCollectFragment extends Fragment implements SentenceCollectContract.SentenceCollectView {


    private SentenceCollectPresenter sentenceCollectPresenter;

    private SentenceCollectAdapter sentenceCollectAdapter;

    private FragmentSentenceCollectBinding binding;

    private MediaPlayer mediaPlayer;

    private RxPermissions rxPermissions;

    private SharedPreferences pSP;

    private MediaRecorder mediaRecorder;

    /**
     * 数据库，存储新闻数据
     */
    private HeadlinesDataManager headlinesDataManager;

    public SentenceCollectFragment() {
        // Required empty public constructor
    }


    public static SentenceCollectFragment newInstance() {
        return new SentenceCollectFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pSP = requireContext().getSharedPreferences(Constant.SP_PERMISSIONS, Context.MODE_PRIVATE);


        headlinesDataManager = HeadlinesDataManager.getInstance(requireContext());

        sentenceCollectPresenter = new SentenceCollectPresenter();
        sentenceCollectPresenter.attchView(this);


        sentenceCollectAdapter = new SentenceCollectAdapter(R.layout.item_sentence_collect, new ArrayList<>());
        sentenceCollectAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {


                int id = view.getId();
                if (id == R.id.search_iv_play) {//播放

//                    EventBus.getDefault().post(new MediaPause());
                    if (!sentenceCollectAdapter.isPlay() && !sentenceCollectAdapter.isRecord()) {//没有录音则可执行

                        SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                        initMediaPlayer(dataDTO, i);
                    } else if (sentenceCollectAdapter.isPlay()) {

                        if (i == sentenceCollectAdapter.getPosition()) {//操作的数据正在播放

                            sentenceCollectAdapter.setPlay(false);
                            mediaPlayer.stop();
                            sentenceCollectAdapter.notifyItemChanged(i);
                        } else {//操作的数据没有播放，其他数据在播放
                            SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                            int oPosition = sentenceCollectAdapter.getPosition();
                            sentenceCollectAdapter.setPlay(false);
                            mediaPlayer.stop();
                            sentenceCollectAdapter.notifyItemChanged(oPosition);
                            initMediaPlayer(dataDTO, i);
                        }
                    } else if (sentenceCollectAdapter.isRecord()) {

                        sentenceCollectAdapter.setRecord(false);
                        mediaRecorder.stop();
                        sentenceCollectAdapter.notifyItemChanged(sentenceCollectAdapter.getPosition());
                        SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                        initMediaPlayer(dataDTO, i);
                    }
                } else if (id == R.id.search_iv_record) {//录音

                    SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                    sentenceCollectPresenter.textAllApi("json", dataDTO.getVoaid(), i);
                } else if (id == R.id.search_tv_collect) {//取消收藏


                    showCancelCollect(i);
                }
            }
        });
    }


    /**
     * 取消收藏的提示
     *
     * @param position
     */
    private void showCancelCollect(int position) {

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setTitle("收藏")
                .setMessage("确定要取消收藏吗?")
                .setPositiveButton("取消收藏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                        String uid = ConfigManager.Instance().loadString("userId", "0");
                        SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(position);

                        sentenceCollectPresenter.updateCollect(uid, dataDTO.getVoaid(), "Iyuba", dataDTO.getSentenceId(), 1,
                                Constant.APPID, "del", Constant.CATEGORY_TYPE, "json");
                    }
                })
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (sentenceCollectPresenter != null) {

            sentenceCollectPresenter.detachView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSentenceCollectBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initOperation();
        binding.scRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.scRv.setAdapter(sentenceCollectAdapter);

        String uid = ConfigManager.Instance().loadString("userId", "0");
        //请求数据
        long curTime = new Date().getTime() / 1000 + 3600 * 8;
        long days = curTime / 86400;
        String signStr = MD5.getMD5ofStr("iyuba" + uid + Constant.CATEGORY_TYPE + Constant.APPID + days);
        sentenceCollectPresenter.getCollect(signStr, Constant.CATEGORY_TYPE, Constant.APPID, "1",
                uid, "json");
    }

    private void initOperation() {

        binding.scWrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                String uid = ConfigManager.Instance().loadString("userId", "0");
                //请求数据
                long curTime = new Date().getTime() / 1000 + 3600 * 8;
                long days = curTime / 86400;
                String signStr = MD5.getMD5ofStr("iyuba" + uid + Constant.CATEGORY_TYPE + Constant.APPID + days);
                sentenceCollectPresenter.getCollect(signStr, Constant.CATEGORY_TYPE, Constant.APPID, "1",
                        uid, "json");
            }
        });
    }


    @Override
    public void showLoading(String msg) {

        binding.scPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        binding.scPb.setVisibility(View.GONE);
    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getCollect(SentenceCollectBean sentenceCollectBean) {

        if (binding.scWrl.isRefreshing()) {

            binding.scWrl.setRefreshing(false);
        }

        //更新数据库 收藏状态
        List<SentenceCollectBean.DataDTO> dataDTOList = sentenceCollectBean.getData();
        for (int i = 0; i < dataDTOList.size(); i++) {

            SentenceCollectBean.DataDTO dataDTO = dataDTOList.get(i);
            headlinesDataManager.collectSentence(dataDTO.getVoaid(), dataDTO.getSentenceId(), 1);
        }

        sentenceCollectAdapter.setNewData(sentenceCollectBean.getData());
    }

    @Override
    public void textAllApi(BBCContentBean newsTextBean, int position) {


        //保存或者更新数据
        headlinesDataManager.saveDetail(newsTextBean.getData());
        //查找数据
        SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(position);

        //更新数据库的收藏状态
        headlinesDataManager.collectSentence(dataDTO.getVoaid(), dataDTO.getSentenceId(), 1);
        RecycleViewData sentence = headlinesDataManager.getSingleEvalSentence(Integer.parseInt(dataDTO.getVoaid()), dataDTO.getSentenceId());
        if (sentence != null) {//设置Para_id和Id_index

            dataDTO.setPara_id(sentence.getParaId());
            dataDTO.setId_index(sentence.getIdIndex());
            requestRecord(position);
        }
    }

    @Override
    public void test(EvalBean evalBean, String voaid, String timing) {

        List<SentenceCollectBean.DataDTO> dataDTOList = sentenceCollectAdapter.getData();

        SentenceCollectBean.DataDTO dataDTO = null;
        int index = 0;
        for (int i = 0; i < dataDTOList.size(); i++) {

            dataDTO = dataDTOList.get(i);
            if (dataDTO.getVoaid().equals(voaid) && dataDTO.getSentenceId().equals(timing)) {

                index = i;
                break;
            }
        }
        if (dataDTO != null) {

            dataDTO.setScore(evalBean.getData().getScores());
            sentenceCollectAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void updateCollect(String type, String voaId, String timing) {

        //del
        headlinesDataManager.collectSentence(voaId + "", timing, 0);

        //寻找第几个
        int index = -1;
        List<SentenceCollectBean.DataDTO> dataDTOList = sentenceCollectAdapter.getData();
        for (int i = 0; i < dataDTOList.size(); i++) {

            SentenceCollectBean.DataDTO dataDTO = dataDTOList.get(i);
            if (dataDTO.getVoaid().equals(voaId) && dataDTO.getSentenceId().equals(timing)) {

                index = i;
                break;
            }
        }

        if (index != -1) {

            dataDTOList.remove(index);
            sentenceCollectAdapter.notifyItemRemoved(index);
        }
    }


    /**
     * 播放句子
     *
     * @param titleDataDTO
     */
    private void initMediaPlayer(SentenceCollectBean.DataDTO titleDataDTO, int p) {

        if (mediaPlayer == null) {

            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {

                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    //完成则更改播放状态
                    sentenceCollectAdapter.setPlay(false);
                    sentenceCollectAdapter.notifyItemChanged(sentenceCollectAdapter.getPosition());
                }
            });
        }

        //记录播放的数据的位置
        if (sentenceCollectAdapter != null) {
            sentenceCollectAdapter.setPlay(true);
            sentenceCollectAdapter.setPosition(p);
            sentenceCollectAdapter.notifyItemChanged(p);
        }

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(titleDataDTO.getSoundsentence());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求录音权限
     *
     * @param position
     */
    @SuppressLint("CheckResult")
    private void requestRecord(int position) {


        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(requireActivity());
        }

        if (rxPermissions.isGranted(Manifest.permission.RECORD_AUDIO)) {

            record(position);
        } else {

            int record = pSP.getInt(Constant.SP_KEY_RECORD, 0);
            if (record == 0) {

                rxPermissions
                        .request(Manifest.permission.RECORD_AUDIO)
                        .subscribe(new Consumer<Boolean>() {
                            @Override
                            public void accept(Boolean aBoolean) {

                                if (aBoolean) {

                                    record(position);
                                } else {

                                    pSP.edit().putInt(Constant.SP_KEY_RECORD, 1).apply();
                                    toast("您拒绝了录音权限，请在权限管理中打开权限！");
                                }
                            }
                        });
            } else {

                toast("请在应用权限管理中打开录音权限！");
            }
        }
    }


    /**
     * 录音评测
     *
     * @param i
     */
    private void record(int i) {

//        EventBus.getDefault().post(new MediaPause());
        if (!sentenceCollectAdapter.isPlay() && !sentenceCollectAdapter.isRecord()) {//没有录音则可执行

            SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
            initRecord(dataDTO, i);
        } else if (sentenceCollectAdapter.isRecord()) {

            if (i == sentenceCollectAdapter.getPosition()) {//操作的数据正在播放

                sentenceCollectAdapter.setRecord(false);
                sentenceCollectAdapter.setPlay(false);
                mediaRecorder.stop();
                SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                test(dataDTO);
            } else {//操作的数据没有播放，其他数据在播放

                SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
                int oPosition = sentenceCollectAdapter.getPosition();
                sentenceCollectAdapter.setRecord(false);
                sentenceCollectAdapter.setPlay(false);
                mediaRecorder.stop();
                sentenceCollectAdapter.notifyItemChanged(oPosition);
                initRecord(dataDTO, i);
            }
        } else if (sentenceCollectAdapter.isPlay()) {//在音频播放的情况下，录音

            sentenceCollectAdapter.setPlay(false);
            mediaPlayer.stop();
            sentenceCollectAdapter.notifyItemChanged(sentenceCollectAdapter.getPosition());
            SentenceCollectBean.DataDTO dataDTO = sentenceCollectAdapter.getItem(i);
            initRecord(dataDTO, i);
        }
    }


    /**
     * 录音
     */
    private void initRecord(SentenceCollectBean.DataDTO titleDataDTO, int p) {

        if (mediaRecorder == null) {

            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/"
                + titleDataDTO.getVoaid() + ".mp3");
        //todo 有问题
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sentenceCollectAdapter != null) {

            sentenceCollectAdapter.setRecord(true);
            sentenceCollectAdapter.setPosition(p);
            sentenceCollectAdapter.notifyItemChanged(p);
        }
        toast("开始录音，再次点击录音结束");
        recordHandler.sendEmptyMessage(1);
    }


    Handler recordHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (sentenceCollectAdapter.isRecord() && mediaRecorder != null) {

                double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
                double db = 0;// 分贝
                if (ratio > 1) {

                    db = 20 * Math.log10(ratio);
                    sentenceCollectAdapter.setDb(db);
                    sentenceCollectAdapter.notifyItemChanged(sentenceCollectAdapter.getPosition());
                }
                recordHandler.sendEmptyMessageDelayed(1, 200);
            }

            return false;
        }
    });


    //评测
    private void test(SentenceCollectBean.DataDTO dataDTO) {

        String uid = ConfigManager.Instance().loadString("userId", "0");

        MediaType type = MediaType.parse("application/octet-stream");
        File file = new File(requireContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + dataDTO.getVoaid() + ".mp3");
        RequestBody fileBody = RequestBody.create(type, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "bbc")
                .addFormDataPart("userId", uid)
                .addFormDataPart("newsId", dataDTO.getVoaid() + "")
                .addFormDataPart("paraId", dataDTO.getPara_id())
                .addFormDataPart("IdIndex", dataDTO.getId_index())
                .addFormDataPart("sentence", dataDTO.getSentence())
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "0")
                .addFormDataPart("appId", Constant.APPID + "")
                .build();
        sentenceCollectPresenter.test(requestBody, dataDTO.getVoaid(), dataDTO.getSentenceId());
    }
}