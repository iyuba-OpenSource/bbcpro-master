package com.ai.bbcpro.ui.activity.home;

import static android.os.Build.VERSION_CODES.BASE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.home.SearchAdapter;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.ActivityMoreSentenceBinding;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.MoreSentencePresenter;
import com.ai.bbcpro.mvp.view.home.MoreSentenceContract;
import com.ai.bbcpro.MainApplication;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tbruyelle.rxpermissions3.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 更多句子
 */
public class MoreSentenceActivity extends AppCompatActivity implements MoreSentenceContract.MoreSentenceView {

    private ActivityMoreSentenceBinding activityMoreSentenceBinding;

    private MoreSentencePresenter moreSentencePresenter;

    private SearchAdapter searchAdapter;

    private MediaPlayer mediaPlayer;

    private MediaRecorder mediaRecorder;

    private int page = 1;
    //搜索的关键词
    private String key;

    private RxPermissions rxPermissions;

    private SharedPreferences pSP;

    Handler recordHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            if (searchAdapter.isRecord() && mediaRecorder != null) {

                double ratio = (double) mediaRecorder.getMaxAmplitude() / BASE;
                double db = 0;// 分贝
                if (ratio > 1) {

                    db = 20 * Math.log10(ratio);
                    searchAdapter.setDb(db);
                    searchAdapter.notifyItemChanged(searchAdapter.getPosition());
                }
                recordHandler.sendEmptyMessageDelayed(1, 200);
            }

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMoreSentenceBinding = ActivityMoreSentenceBinding.inflate(getLayoutInflater());
        setContentView(activityMoreSentenceBinding.getRoot());

        getBundle();

        pSP = getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);

        activityMoreSentenceBinding.toolbarTvTitle.setText("更多句子");
        activityMoreSentenceBinding.toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        moreSentencePresenter = new MoreSentencePresenter();
        moreSentencePresenter.attchView(this);


        activityMoreSentenceBinding.msRv.setLayoutManager(new LinearLayoutManager(this));
        searchAdapter = new SearchAdapter(R.layout.item_search, new ArrayList<>());
        activityMoreSentenceBinding.msRv.setAdapter(searchAdapter);
        searchAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                page++;
                String userId = ConfigManager.Instance().loadString("userId");
                moreSentencePresenter.searchApiNew("json", key, page, 10, 0,
                        "bbc", 2, userId, Constant.APPID);
            }
        }, activityMoreSentenceBinding.msRv);
        searchAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                int id = view.getId();
                if (id == R.id.search_iv_play) {//播放

                    EventBus.getDefault().post(new MediaPause());
                    if (!searchAdapter.isPlay() && !searchAdapter.isRecord()) {//没有录音则可执行

                        SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                        initMediaPlayer(dataDTO, i);
                    } else if (searchAdapter.isPlay()) {

                        if (i == searchAdapter.getPosition()) {//操作的数据正在播放

                            searchAdapter.setPlay(false);
                            mediaPlayer.stop();
                            searchAdapter.notifyItemChanged(i);
                        } else {//操作的数据没有播放，其他数据在播放
                            SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                            int oPosition = searchAdapter.getPosition();
                            searchAdapter.setPlay(false);
                            mediaPlayer.stop();
                            searchAdapter.notifyItemChanged(oPosition);
                            initMediaPlayer(dataDTO, i);
                        }
                    } else if (searchAdapter.isRecord()) {

                        searchAdapter.setRecord(false);
                        mediaRecorder.stop();
                        searchAdapter.notifyItemChanged(searchAdapter.getPosition());
                        SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                        initMediaPlayer(dataDTO, i);
                    }
                } else if (id == R.id.search_iv_record) {//录音

                    requestRecord(i);
                }
            }
        });

        String userId = ConfigManager.Instance().loadString("userId");
        moreSentencePresenter.searchApiNew("json", key, page, 10, 0,
                "bbc", 2, userId, Constant.APPID);
    }


    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            key = bundle.getString("KEY", "");
        }
    }

    public static void startActivity(Activity activity, String key) {

        Intent intent = new Intent(activity, MoreSentenceActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("KEY", key);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    private void test(SearchBean.TitleDataDTO dataDTO) {

        String uid = ConfigManager.Instance().loadString("userId", "0");

        MediaType type = MediaType.parse("application/octet-stream");
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + dataDTO.getBbcId() + ".mp3");
        RequestBody fileBody = RequestBody.create(type, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("type", "bbc")
                .addFormDataPart("userId", uid)
                .addFormDataPart("newsId", dataDTO.getBbcId() + "")
                .addFormDataPart("paraId", dataDTO.getParaId())
                .addFormDataPart("IdIndex", dataDTO.getIdIndex())
                .addFormDataPart("sentence", dataDTO.getSentence())
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("wordId", "0")
                .addFormDataPart("flg", "0")
                .addFormDataPart("appId", Constant.APPID + "")
                .build();
        moreSentencePresenter.test(requestBody);
    }

    @Override
    protected void onStop() {
        super.onStop();

        searchAdapter.setPlay(false);
        searchAdapter.setRecord(false);
        searchAdapter.notifyItemChanged(searchAdapter.getPosition());

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {

            mediaPlayer.stop();
        }
        if (mediaRecorder != null) {
            mediaRecorder.reset();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {

            mediaPlayer.release();
        }
        if (mediaRecorder != null) {

            mediaRecorder.release();
        }
        if (moreSentencePresenter != null) {

            moreSentencePresenter.detachView();
        }
    }


    /**
     * 请求录音权限
     *
     * @param position
     */
    private void requestRecord(int position) {


        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(MoreSentenceActivity.this);
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
                            public void accept(Boolean aBoolean) throws Throwable {

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

        EventBus.getDefault().post(new MediaPause());
        if (!searchAdapter.isPlay() && !searchAdapter.isRecord()) {//没有录音则可执行

            SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
            initRecord(dataDTO, i);
        } else if (searchAdapter.isRecord()) {

            if (i == searchAdapter.getPosition()) {//操作的数据正在播放

                searchAdapter.setRecord(false);
                searchAdapter.setPlay(false);
                mediaRecorder.stop();
                SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                test(dataDTO);
            } else {//操作的数据没有播放，其他数据在播放

                SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                int oPosition = searchAdapter.getPosition();
                searchAdapter.setRecord(false);
                searchAdapter.setPlay(false);
                mediaRecorder.stop();
                searchAdapter.notifyItemChanged(oPosition);
                initRecord(dataDTO, i);
            }
        } else if (searchAdapter.isPlay()) {//在音频播放的情况下，录音

            searchAdapter.setPlay(false);
            mediaPlayer.stop();
            searchAdapter.notifyItemChanged(searchAdapter.getPosition());
            SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
            initRecord(dataDTO, i);
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
    public void searchApiNewComplete(SearchBean searchBean) {

        List<SearchBean.TitleDataDTO> textDataDTOList = searchBean.getTextData();
        for (int i = 0; i < textDataDTOList.size(); i++) {

            SearchBean.TitleDataDTO titleDataDTO = textDataDTOList.get(i);
            titleDataDTO.setFlag(2);
        }

        searchAdapter.setNewData(textDataDTOList);
    }

    @Override
    public void loadmore(SearchBean searchBean) {

        if (searchBean == null) {

            searchAdapter.loadMoreFail();
        } else {

            List<SearchBean.TitleDataDTO> textDataDTOList = searchBean.getTextData();
            for (int i = 0; i < textDataDTOList.size(); i++) {

                SearchBean.TitleDataDTO titleDataDTO = textDataDTOList.get(i);
                titleDataDTO.setFlag(2);
            }

            if (searchBean.getTitleData().size() == 10) {

                searchAdapter.addData(searchBean.getTextData());
                searchAdapter.loadMoreComplete();
            } else {

                searchAdapter.addData(searchBean.getTextData());
                searchAdapter.loadMoreEnd();
            }
        }
    }

    @Override
    public void testComplete(EvalBean evalBean) {

        List<SearchBean.TitleDataDTO> dataDTOList = searchAdapter.getData();
        for (int i = 0; i < dataDTOList.size(); i++) {

            SearchBean.TitleDataDTO dataDTO = dataDTOList.get(i);
            if (dataDTO.getFlag() == 2) {

                if (dataDTO.getSentence().equals(evalBean.getData().getSentence())) {

                    dataDTO.setScore(evalBean.getData().getScores());
                    searchAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }


    /**
     * 播放句子
     *
     * @param titleDataDTO
     */
    private void initMediaPlayer(SearchBean.TitleDataDTO titleDataDTO, int p) {

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
                    searchAdapter.setPlay(false);
                    searchAdapter.notifyItemChanged(searchAdapter.getPosition());
                }
            });
        }

        //记录播放的数据的位置
        if (searchAdapter != null) {
            searchAdapter.setPlay(true);
            searchAdapter.setPosition(p);
            searchAdapter.notifyItemChanged(p);
        }

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(titleDataDTO.getSoundText());
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 录音
     */
    private void initRecord(SearchBean.TitleDataDTO titleDataDTO, int p) {

        if (mediaRecorder == null) {

            mediaRecorder = new MediaRecorder();
        }
        mediaRecorder.reset();

        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setAudioChannels(1);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/"
                + titleDataDTO.getBbcId() + ".mp3");
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (searchAdapter != null) {

            searchAdapter.setRecord(true);
            searchAdapter.setPosition(p);
            searchAdapter.notifyItemChanged(p);
        }
        toast("开始录音，再次点击录音结束");
        recordHandler.sendEmptyMessage(1);
    }
}