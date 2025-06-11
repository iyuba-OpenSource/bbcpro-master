package com.ai.bbcpro.ui.activity.home;

import static android.os.Build.VERSION_CODES.BASE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.home.SearchAdapter;
import com.ai.bbcpro.adapter.home.SearchHotAdapter;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.ActivitySearchBinding;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.event.MediaPause;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.EvalBean;
import com.ai.bbcpro.model.bean.RecommendBean;
import com.ai.bbcpro.model.bean.SearchBean;
import com.ai.bbcpro.mvp.presenter.SearchPresenter;
import com.ai.bbcpro.mvp.view.home.SearchContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.activity.login.LoginActivity;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
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
 * 搜索
 */
public class SearchActivity extends AppCompatActivity implements SearchContract.SearchView {


    private ActivitySearchBinding activitySearchBinding;

    private SearchAdapter searchAdapter;
    private SearchHotAdapter searchHotAdapter;

    private SearchPresenter searchPresenter;

    private String userId;
    private FlexboxLayoutManager flexboxLayoutManager;

    private LinearLayoutManager linearLayoutManager;

    private MediaPlayer mediaPlayer;

    private MediaRecorder mediaRecorder;

    /**
     * 数据库    如果文章没有被存储则存储文章
     */
    private HeadlinesDataManager headlinesDataManager;

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
        activitySearchBinding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(activitySearchBinding.getRoot());

        pSP = getSharedPreferences(Constant.SP_PERMISSIONS, MODE_PRIVATE);

        searchPresenter = new SearchPresenter();
        searchPresenter.attchView(this);

        userId = ConfigManager.Instance().loadString("userId");

        headlinesDataManager = HeadlinesDataManager.getInstance(MainApplication.getApplication());

        initOperation();
        searchPresenter.recommend("bbc");

//        stopPlayHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();

        //页面看不见，就停止录音和播放音频
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

        if (searchPresenter != null) {

            searchPresenter.detachView();
        }
    }


    /**
     * 请求录音权限
     *
     * @param position
     */
    private void requestRecord(int position) {


        if (rxPermissions == null) {

            rxPermissions = new RxPermissions(SearchActivity.this);
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
                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(searchAdapter.getPosition());
                    if (dataDTO != null && dataDTO.getFlag() != 1) {

                        searchAdapter.setPlay(false);
                        searchAdapter.notifyItemChanged(searchAdapter.getPosition());
                    }
                }
            });
        }

        //记录播放的数据的位置
        if (searchAdapter != null && titleDataDTO.getFlag() != 1) {//处理单词播放
            searchAdapter.setPlay(true);
            searchAdapter.setPosition(p);
            searchAdapter.notifyItemChanged(p);
        } else if (searchAdapter != null) {
            searchAdapter.setPosition(p);//记录操作单词的数据位置
        }

        mediaPlayer.reset();
        try {
            String url;
            if (titleDataDTO.getFlag() == 1) {//单词

                url = titleDataDTO.getPhAmMp3();
            } else {
                url = titleDataDTO.getSoundText();
            }
            mediaPlayer.setDataSource(url);
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

    private void initOperation() {

        flexboxLayoutManager = new FlexboxLayoutManager(this);
        //flexDirection 属性决定主轴的方向（即项目的排列方向）。类似 LinearLayout 的 vertical 和 horizontal。
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW); //主轴为水平方向，起点在左端。
        //flexWrap 默认情况下 Flex 跟 LinearLayout 一样，都是不带换行排列的，但是flexWrap属性可以支持换行排列。
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP); //按正常方向换行
        //justifyContent 属性定义了项目在主轴上的对齐方式。
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START); //交叉轴的起点对齐。

        linearLayoutManager = new LinearLayoutManager(this);
        searchAdapter = new SearchAdapter(R.layout.item_search, new ArrayList<>());
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

                } else if (id == R.id.search_ll_essay) {//文章

                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);

                    long count = headlinesDataManager.hasQuestionBean(dataDTO.getBbcId());
                    if (count > 0) {//含有数据

                        saveNewContentComplete(dataDTO.getBbcId());
                    } else {//没有数据

                        searchPresenter.textAllApi("json", dataDTO.getBbcId());
                    }
                } else if (id == R.id.search_tv_more) {

                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                    if (dataDTO.getFlag() == 103) {//文章

                        MoreEssayActivity.startActivity(SearchActivity.this, activitySearchBinding.searchEtKey.getText().toString());
                    } else {//句子

                        MoreSentenceActivity.startActivity(SearchActivity.this, activitySearchBinding.searchEtKey.getText().toString());
                    }
                } else if (id == R.id.search_iv_sound) {

                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                    initMediaPlayer(dataDTO, i);
                } else if (id == R.id.search_iv_collect) {

                    //检测登录
                    String userId = ConfigManager.Instance().loadString("userId", "");
                    if (userId.equals("")) {

                        EventBus.getDefault().post(new LoginEventbus());
                        return;
                    }

                    SearchBean.TitleDataDTO dataDTO = searchAdapter.getItem(i);
                    if (dataDTO.getCollect() == 1) {

                        searchPresenter.updateCollect(userId, dataDTO.getBbcId() + "", "Iyuba",
                                dataDTO.getTiming(), 1, Constant.APPID, "del", Constant.CATEGORY_TYPE,
                                "json");
                    } else {

                        searchPresenter.updateCollect(userId, dataDTO.getBbcId() + "", "Iyuba",
                                dataDTO.getTiming(), 1, Constant.APPID, "insert", Constant.CATEGORY_TYPE,
                                "json");
                    }
                }
            }
        });

        searchHotAdapter = new SearchHotAdapter(R.layout.item_search_hot, new ArrayList<>());
        activitySearchBinding.searchRv.setAdapter(searchHotAdapter);
        searchHotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {

                String key = searchHotAdapter.getItem(i);
                activitySearchBinding.searchEtKey.setText(key);
            }
        });


        activitySearchBinding.searchEtKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 0) {

                    searchPresenter.recommend("bbc");
                } else {

                    searchPresenter.searchApiNew("json", s.toString(), 1, 3, 0,
                            "bbc", 0, userId, Constant.APPID);
                }
            }
        });
        activitySearchBinding.searchIvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activitySearchBinding.searchEtKey.setText("");
            }
        });
        activitySearchBinding.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        searchPresenter.test(requestBody);
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
    public void recommendComplete(RecommendBean recommendBean) {

        activitySearchBinding.searchRv.setLayoutManager(flexboxLayoutManager);
        searchHotAdapter.setNewData(recommendBean.getData());
        activitySearchBinding.searchRv.setAdapter(searchHotAdapter);
    }

    @Override
    public void searchApiNewComplete(SearchBean searchBean) {

        activitySearchBinding.searchRv.setLayoutManager(linearLayoutManager);

        List<SearchBean.TitleDataDTO> dataDTOList = new ArrayList<>();

        if (searchBean.getWord() != null) {//防止没有单词崩溃的情况

            SearchBean.TitleDataDTO nameDto = new SearchBean.TitleDataDTO();//标题
            nameDto.setFlag(101);
            dataDTOList.add(nameDto);

            SearchBean.TitleDataDTO wordDto = new SearchBean.TitleDataDTO();
            wordDto.setWord(searchBean.getWord());
            wordDto.setDef(searchBean.getDef());
            wordDto.setPh_en(searchBean.getPhEn());
            wordDto.setPhAmMp3(searchBean.getPhAmMp3());
            wordDto.setFlag(1);
            dataDTOList.add(wordDto);
        }

        List<SearchBean.TitleDataDTO> essaySearchBeanList = searchBean.getTitleData();
        for (int i = 0; i < essaySearchBeanList.size(); i++) {

            if (i == 0) {

                SearchBean.TitleDataDTO nameDto = new SearchBean.TitleDataDTO();
                nameDto.setFlag(103);
                dataDTOList.add(nameDto);
            }
            SearchBean.TitleDataDTO dataDTO = essaySearchBeanList.get(i);
            dataDTO.setFlag(3);
            dataDTOList.add(dataDTO);
        }
        List<SearchBean.TitleDataDTO> textDTOList = searchBean.getTextData();
        for (int i = 0; i < textDTOList.size(); i++) {

            if (i == 0) {

                SearchBean.TitleDataDTO nameDto = new SearchBean.TitleDataDTO();
                nameDto.setFlag(102);
                dataDTOList.add(nameDto);
            }
            SearchBean.TitleDataDTO dataDTO = textDTOList.get(i);
            dataDTO.setFlag(2);
            dataDTOList.add(dataDTO);
        }

        searchAdapter.setNewData(dataDTOList);
        activitySearchBinding.searchRv.setAdapter(searchAdapter);
        activitySearchBinding.searchRv.setAnimation(null);
        activitySearchBinding.searchRv.setItemAnimator(null);
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

    @Override
    public void saveNewContentComplete(int bbcid) {

        List<SearchBean.TitleDataDTO> dataDTOList = searchAdapter.getData();
        SearchBean.TitleDataDTO dataDTO = null;//获取bbcid的对象
        for (int i = 0; i < dataDTOList.size(); i++) {

            SearchBean.TitleDataDTO titleDataDTO = dataDTOList.get(i);
            if (titleDataDTO.getFlag() == 3 && titleDataDTO.getBbcId() == bbcid) {
                dataDTO = titleDataDTO;
                break;
            }
        }

        if (dataDTO != null) {

            SumBean.DataDTO sDto = new SumBean.DataDTO();
            sDto.setSound(dataDTO.getSound());
            sDto.setBbcId(dataDTO.getBbcId() + "");
            sDto.setPic(dataDTO.getPic());
            sDto.setTitle(dataDTO.getTitle());
            AudioContentActivity.startActivity(SearchActivity.this, sDto, 0, 0);
        }
    }

    @Override
    public void updateCollect(String type, String voaId, String timing) {

        List<SearchBean.TitleDataDTO> titleDataDTOS = searchAdapter.getData();
        //目标数据
        SearchBean.TitleDataDTO tdata = null;
        for (int i = 0; i < titleDataDTOS.size(); i++) {

            SearchBean.TitleDataDTO dto = titleDataDTOS.get(i);
            if (dto.getTiming() != null && dto.getBbcId() == Integer.parseInt(voaId) && dto.getTiming().equals(timing)) {

                tdata = dto;
                break;
            }
        }


        if (type.equals("insert")) {

            if (tdata != null) {

                tdata.setCollect(1);
                searchAdapter.notifyDataSetChanged();
            }
        } else {

            if (tdata != null) {

                tdata.setCollect(0);
                searchAdapter.notifyDataSetChanged();
            }
        }
    }
}