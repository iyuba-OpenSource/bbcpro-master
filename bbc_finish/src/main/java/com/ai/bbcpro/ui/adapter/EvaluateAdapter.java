package com.ai.bbcpro.ui.adapter;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.bean.CorrectPronBean;
import com.ai.bbcpro.ui.bean.EvalWordBean;
import com.ai.bbcpro.ui.fragment.content.EvaluateContentFragment;
import com.ai.bbcpro.ui.player.RoundProgressBar.RoundProgressBar;
import com.ai.bbcpro.ui.player.eval.EvalWord;
import com.ai.bbcpro.ui.player.eval.RecycleViewData;
import com.ai.bbcpro.ui.utils.AudioCorrectRecorderUtils;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.ui.widget.selectTextView.MyOnWordClickListener;
import com.ai.bbcpro.ui.widget.selectTextView.MySelectableTextView;
import com.ai.bbcpro.util.widget.SelectWordTextView;
import com.ai.common.CommonConstant;
import com.google.gson.Gson;
import com.iyuba.imooclib.ui.mobclass.MobClassActivity;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EvaluateAdapter extends RecyclerView.Adapter<EvaluateAdapter.ItemViewHolder> implements View.OnClickListener {
    private Context mContext;
    private TextView scoreTv, userPron, rightPron, recordTv, explain;
    private View scoreLy;
    private List<RecycleViewData> mDataList;
    private String recFilePath = "";
    private String evalWordAudioUrl = "";
    private AudioCorrectRecorderUtils recorderManager;
    private boolean isPermissionRequested;
    private int recordDB;
    private String TAG = "EvaluateAdapter";
    private static final int NOTIFY_DB = 0;
    private int errorWordNum = 0;
    private MediaPlayer wordPlayer;
    private MediaPlayer recPlayer;
    private boolean isRecording = false;
    private Dialog evalDialog;
    int clickNum = 0;

    /**
     * 选中的位置
     */
    private int cPosition = 0;


    /**
     * 选择单词item 位置
     */
    private int selectWordPos = -1;

    private WordCallback wordCallback;


    public WordCallback getWordCallback() {
        return wordCallback;
    }

    public void setWordCallback(WordCallback wordCallback) {
        this.wordCallback = wordCallback;
    }

    public int getSelectWordPos() {
        return selectWordPos;
    }

    public void setSelectWordPos(int selectWordPos) {
        this.selectWordPos = selectWordPos;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 0:
                    CorrectPronBean bean = (CorrectPronBean) message.obj;
                    String curWordDef = bean.getDef();
                    String curWordAudio = bean.getAudio();
                    explain.setText("单词释义: " + curWordDef);
                    // 设置单词音频
                    if (wordPlayer != null) {
                        wordPlayer.release();
                        wordPlayer = null;
                    }
                    wordPlayer = new MediaPlayer();
                    try {
                        if (curWordAudio != null) {
                            wordPlayer.setDataSource(curWordAudio);
                            wordPlayer.prepare();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                case 1:
                    return true;
                case 2:
                    evalDialog.dismiss();
                    EvalWordBean wordBean = (EvalWordBean) message.obj;
                    scoreLy.setVisibility(View.VISIBLE);
                    scoreTv.setText(String.valueOf(wordBean.getData().getScores()));
                    userPron.setText("你的发音: " + wordBean.getData().getWords().get(0).getUserPron2());
                    evalWordAudioUrl = CommonConstant.HTTP_SPEECH_ALL + "/voa/" + wordBean.getData().getUrl();
                    return true;
            }
            return false;
        }
    });
    private String currentWord;
    private int currentIndex;

    public EvaluateAdapter(Context mContext, List<RecycleViewData> mDataList, EvaluateContentFragment fragment) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        initRecManager();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_evaluate, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

    }


    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull EvaluateAdapter.ItemViewHolder holder, int position, List payloads) {

        RecycleViewData data = mDataList.get(position);

        holder.all.setTag(position);
        holder.play.setTag(position);
        holder.recContainer.setTag(position);
        holder.playingRec.setTag(position);
        holder.playRec.setTag(position);
        holder.send.setTag(position);
        holder.correct.setTag(position);
        holder.share.setTag(position);
        holder.eval_iv_collect.setTag(position);

        if (payloads.isEmpty()) {
            holder.rec.setProgress(0);
        } else {
            int type = (int) payloads.get(0);
            switch (type) {
                case NOTIFY_DB:
                    holder.rec.setProgress(data.getRecordDB());
                    break;
            }
        }

        //是否显示底部图标
        if (cPosition == position) {
            holder.functionTab.setVisibility(View.VISIBLE);
            clickNum++;
        } else {
            holder.functionTab.setVisibility(View.GONE);
            holder.notice.setVisibility(View.GONE);
        }

        if (data.isPlayOri()) {
            holder.play.setBackgroundResource(R.drawable.ic_play_line);
        } else {
            holder.play.setBackgroundResource(R.drawable.ic_pause_line);
        }

        if (data.isEval()) {

            holder.sen_read_button.setVisibility(View.VISIBLE);
            holder.score.setVisibility(View.VISIBLE);
            holder.playRec.setVisibility(View.VISIBLE);
            holder.send.setVisibility(View.VISIBLE);
            holder.ll_correct_pron.setVisibility(View.VISIBLE);

            Integer correctClickNum = 0;
            if (data.isUpload()) {
                holder.share.setVisibility(View.VISIBLE);
            }
            if (data.isHasLowScoreWord()) {
                holder.correct.setVisibility(View.VISIBLE);
                if (data.isShowBtn()) {
                    holder.notice.setVisibility(View.VISIBLE);
                } else {
                    holder.notice.setVisibility(View.GONE);
                }
                holder.correct.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.notice.setVisibility(View.GONE);
                        showCorrectCard(position);
                    }
                });
            }

            if (data.isPlayRec()) {
                holder.playRec.setVisibility(View.GONE);
                holder.playRec.setClickable(false);
                holder.playingRec.setVisibility(View.VISIBLE);

                if (data.isUnderRecPlay()) {
                    holder.playingRec.setBackgroundResource(R.drawable.ic_play_line);
                } else {
                    holder.playingRec.setBackgroundResource(R.drawable.ic_pause_line);
                }
            } else {
                holder.playingRec.setVisibility(View.GONE);
                holder.playRec.setVisibility(View.VISIBLE);
                holder.playRec.setClickable(true);
            }
        } else {
            holder.score.setVisibility(View.GONE);
            holder.playRec.setVisibility(View.GONE);
            holder.playingRec.setVisibility(View.GONE);
            holder.send.setVisibility(View.GONE);
            holder.correct.setVisibility(View.GONE);
            holder.notice.setVisibility(View.GONE);
            holder.sen_read_button.setVisibility(View.GONE);
            holder.ll_correct_pron.setVisibility(View.GONE);
            holder.ll_share_icon.setVisibility(View.GONE);

        }
        //是否上传
        if (data.isUpload()) {

            holder.ll_sen_read_send.setVisibility(View.GONE);
            holder.ll_share_icon.setVisibility(View.VISIBLE);
        } else {

            if (data.isEval()) {

                holder.ll_sen_read_send.setVisibility(View.VISIBLE);
                holder.ll_share_icon.setVisibility(View.GONE);
            } else {

                holder.ll_sen_read_send.setVisibility(View.GONE);
                holder.ll_share_icon.setVisibility(View.GONE);
            }
        }

        int beginIndex = 0;
        String baseString = mDataList.get(position).getSentence();
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(baseString);
        List<EvalWord> wordList = data.getWords();
        int count = 0;
        for (int i = 0; i < wordList.size(); i++) {
            if (beginIndex > data.getSentence().length()) return;
            EvalWord word = wordList.get(i);
            if (word.getScore() < 2 && word.getScore() != -1) {
                if (count == 0) {
                    holder.notice.setText(word.getContent().replaceAll("[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】；：”“’。，、？|-]", "").replace("‘", "'")
                            + "等单词发音有误点击纠正");
                }
                count++;
                int tempEndIndex = beginIndex + word.getLength();
                sb.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 0, 0)), beginIndex, tempEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // System.out.println("-------------------->设置红色" + beginIndex + "---" + tempEndIndex + "---" + word.getContent());
            } else if (word.getScore() > 3) {
                int tempEndIndex = beginIndex + word.getLength();
                sb.setSpan(new ForegroundColorSpan(Color.argb(255, 6, 142, 0)), beginIndex, tempEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            beginIndex += word.getLength() + 1;
        }
        holder.selectWordTextView.setText(sb);
        //点击单词的事件

//        holder.en.setLabelText(String.valueOf(position + 1));
        holder.zh.setText(mDataList.get(position).getSentenceCn());
        holder.score.setText(mDataList.get(position).getScore() + "分");

        //判断收藏的状态
        if (data.getCollect() == 1) {

            holder.eval_iv_collect.setImageResource(R.drawable.icon_collect);
        } else {

            holder.eval_iv_collect.setImageResource(R.drawable.icon_uncollect);
        }
    }


    public int getcPosition() {
        return cPosition;
    }

    public void setcPosition(int cPosition) {
        this.cPosition = cPosition;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setDBData(List<RecycleViewData> mDataList, int position) {
        this.mDataList = mDataList;
        notifyItemChanged(position, NOTIFY_DB);
    }

    public void setDataList(List<RecycleViewData> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (itemClickListener != null) {
            switch (v.getId()) {
                case R.id.item_self:
                    itemClickListener.onItemClick("showSelect", position);
                    break;
                case R.id.sen_play_common:
                    itemClickListener.onItemClick("play", position);
                    break;
                case R.id.rec_container:
//                    requestPermission();
                    itemClickListener.onItemClick("rec", position);
                    break;
                case R.id.sen_read_play:
                    itemClickListener.onItemClick("playRec", position);
                    break;
                case R.id.sen_read_playing_common:
                    itemClickListener.onItemClick("stopRec", position);
                    break;
                case R.id.sen_read_send:
                    itemClickListener.onItemClick("send", position);
                    break;
                case R.id.correct_pron:
                    showCorrectCard(position);
                    break;
                case R.id.share_icon:
                    itemClickListener.onItemClick("share", position);
                    break;
                case R.id.eval_iv_collect:
                    itemClickListener.onItemClick("collect_sentence", position);
                    break;
            }
        }

    }

    private void showCorrectCard(int position) {
        AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.setCancelable(false);

        Window window = dialog.getWindow();
        if (window != null) {
            window.setContentView(LayoutInflater.from(mContext).inflate(R.layout.dialog_correct, null));
            window.setGravity(Gravity.CENTER);
            String sentence = mDataList.get(position).getSentence();
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(sentence.replace("‘", "'"));
            List<EvalWord> wordList = mDataList.get(position).getWords();
            int beginIndex = 0;
            for (int i = 0; i < wordList.size(); i++) {
                if (beginIndex > sentence.length())
                    break;
                EvalWord word = wordList.get(i);
                if (word.getScore() < 2 && word.getScore() != -1) {
                    int tempEndIndex = beginIndex + word.getLength();
                    sb.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 0, 0)), beginIndex, tempEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                beginIndex += word.getLength() + 1;
            }

            int firstLow = 0;
            for (int i = 0; i < wordList.size(); i++) {
                if (wordList.get(i).getScore() < 2) {
                    firstLow = i;
                    break;
                }
            }
            requestWord(wordList.get(firstLow).getContent(), wordList.get(firstLow).getUserPron(), wordList.get(firstLow).getPron());
            currentWord = wordList.get(firstLow).getContent();
            currentIndex = firstLow;
            TextView curWord = window.findViewById(R.id.tv_correct_word);
            // SelectableTextView correctSen = window.findViewById(R.id.tv_correct_sen);
            MySelectableTextView correctSen = window.findViewById(R.id.tv_correct_sen);
            rightPron = window.findViewById(R.id.right_pron);
            userPron = window.findViewById(R.id.user_pron);
            explain = window.findViewById(R.id.correct_explain);

            recordTv = window.findViewById(R.id.correct_rec_tv);
            scoreTv = window.findViewById(R.id.correct_score);
            scoreLy = window.findViewById(R.id.correct_score_ly);

            ImageView correctClose = window.findViewById(R.id.correct_close);
            ImageView playWord = window.findViewById(R.id.correct_play_word);
            ImageView playOri = window.findViewById(R.id.correct_play_ori);
            ImageView record = window.findViewById(R.id.correct_rec);
            ImageView playRec = window.findViewById(R.id.correct_play_user);
            ImageView goToMic = window.findViewById(R.id.goto_micro);

            curWord.setText(wordList.get(firstLow).getContent().replaceAll("‘", "'"));
            rightPron.setText("正确发音: " + wordList.get(firstLow).getContent() + " [" + wordList.get(firstLow).getPron2() + "]");
            userPron.setText("你的发音: " + wordList.get(firstLow).getContent() + " [" + wordList.get(firstLow).getUserPron2() + "]");
            correctSen.setSelectTextBackColor(R.color.green);
            correctSen.setText(sb);
            correctSen.setOnWordClickListener(new MyOnWordClickListener() {
                @Override
                protected void onNoDoubleClick(String s) {

                    if (recorderManager.getRecordStatus() == 1) {
                        recordTv.setText("点击开始");
                        RecycleViewData data = mDataList.get(position);
                        recorderManager.stopRecord();
                    }

                    curWord.setText(s);
                    rightPron.setText("正确发音: " + s + " []");
                    userPron.setText("你的发音: " + s + " []");
                    for (EvalWord word : wordList) {
                        //[`~!@#$%^&*()+=|{}':;',\[\].<>/?~！@#￥%……&;*（）——+|{}【】‘；：”“’。，、？|-]
                        Log.d(TAG, s + " --- " + "onNoDoubleClick: ---------->" + word.getContent().replaceAll("[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?~！@#￥%……&;*（）——+|{}【】；：”“’。，、？|-]", "").replace("\"", ""));
                        if (s.equals(word.getContent().replaceAll("[`~!@#$%^&*()+=|{}':;,\\[\\].<>/?~！@#￥%……& ;*（）——+|{}【】；：”“’。，、？|-]", "").replace("‘", "'").replace("\"", ""))) {
                            rightPron.setText("正确发音: " + s + " [" + word.getPron2() + "]");
                            userPron.setText("你的发音: " + s + " [" + word.getUserPron2() + "]");
                            requestWord(s.toLowerCase(),
                                    word.getUserPron(), word.getPron());
                            currentWord = word.getContent();
                            currentIndex = word.getIndex();
                            break;
                        }
                    }
                }
            });

            correctClose.setOnClickListener(v -> dialog.cancel());
            // 设置播放单词音频监听
            playWord.setOnClickListener(view -> {
                if (wordPlayer != null)
                    wordPlayer.start();
            });
            playOri.setOnClickListener(view -> {
                if (wordPlayer != null)
                    wordPlayer.start();
            });
            record.setOnClickListener(view -> {
                if (recorderManager.getRecordStatus() == 1) {
                    stopCorRec(position);
                } else {
                    startCorRec();
                }
            });
            playRec.setOnClickListener(v -> {
                playWordRec();
            });
            goToMic.setOnClickListener(v -> {
                mContext.startActivity(MobClassActivity.buildIntent(mContext, 3, true, null));
            });


        }

    }

    private void playWordRec() {
        if (recPlayer != null) {
            recPlayer.release();
            recPlayer = null;
        }
        recPlayer = new MediaPlayer();
        try {
            if (!"".equals(evalWordAudioUrl)) {
                recPlayer.setDataSource(evalWordAudioUrl);
            } else {
                recPlayer.setDataSource(recFilePath);
            }
            recPlayer.prepare();
            System.out.println("===============录音长度" + recPlayer.getDuration());
            recPlayer.start();
        } catch (IOException e) {
        }
    }

    private void stopCorRec(int position) {
        recordTv.setText("点击开始");
        RecycleViewData data = mDataList.get(position);
        recorderManager.stopRecord();
        if (!"".equals(recFilePath)) {
            doEval(data);
        }
    }

    private void doEval(RecycleViewData data) {
        evalDialog = null;
        showLoading("评测中...");
        MediaType type = MediaType.parse("application/octet-stream");//"text/xml;charset=utf-8"
        File file = new File(recFilePath);
        RequestBody fileBody = RequestBody.create(type, file);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("flg", "2")
                .addFormDataPart("IdIndex", data.getIdIndex())
                .addFormDataPart("paraId", data.getParaId())
                .addFormDataPart("appId", Constant.APPID)
                .addFormDataPart("newsId", String.valueOf(data.getBbcId()))
                .addFormDataPart("userId", ConfigManager.Instance().loadString("userId"))
                .addFormDataPart("sentence", currentWord)
                .addFormDataPart("wordId", String.valueOf(currentIndex))
                .addFormDataPart("file", file.getName(), fileBody)
                .addFormDataPart("type", "bbc")
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...")
                .url(CommonConstant.HTTP_SPEECH_ALL + "/test/ai/")
                .post(requestBody)
                .build();
        Call call = new OkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                // ToastUtil.showToast(getContext(), "网络错误，请稍后重试。");
                System.out.println("评测接口调用失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    EvalWordBean wordBean = new Gson().fromJson(response.body().string(), EvalWordBean.class);
                    Message message = Message.obtain();
                    message.what = 2;
                    message.obj = wordBean;
                    handler.sendMessage(message);
                } else {
                }
            }
        });
    }

    private void startCorRec() {
        recordTv.setText("正在录制");
        ToastUtil.showToast(mContext, "正在录制，点击结束");
        recorderManager.startRecord();
    }

    private void requestWord(String q, String userPron, String oriPron) {
        String urlStr = "http://word." + CommonConstant.domain + "/words/apiWordAi.jsp?" +
                "q=" + q +
                "&user_pron=" + URLEncoder.encode(userPron) +
                "&ori_pron=" + URLEncoder.encode(oriPron);
        Log.d(TAG, "requestWord: " + urlStr.substring(7));
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(urlStr)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    CorrectPronBean pronBean = new Gson().fromJson(response.body().string(), CorrectPronBean.class);
                    Message message = Message.obtain();
                    message.what = 0;
                    message.obj = pronBean;
                    handler.sendMessage(message);
                }
            }
        });
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public SelectWordTextView selectWordTextView;
        public TextView zh, score, notice;
        public View all, functionTab, recContainer;
        public ImageView playRec, send, correct, share;
        public RoundProgressBar play, rec, playingRec;
        private ImageView eval_iv_collect;

        private LinearLayout ll_share_icon;

        private LinearLayout ll_sen_read_send;

        private FrameLayout sen_read_button;
        private LinearLayout ll_correct_pron;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            selectWordTextView = itemView.findViewById(R.id.sen_en);
            zh = itemView.findViewById(R.id.sen_zh);
            all = itemView.findViewById(R.id.item_self);
            functionTab = itemView.findViewById(R.id.function_tab);
            play = itemView.findViewById(R.id.sen_play_common);
            rec = itemView.findViewById(R.id.sen_i_read_common);
            score = itemView.findViewById(R.id.sen_read_result);
            playRec = itemView.findViewById(R.id.sen_read_play);
            playingRec = itemView.findViewById(R.id.sen_read_playing_common);
            recContainer = itemView.findViewById(R.id.rec_container);
            send = itemView.findViewById(R.id.sen_read_send);
            correct = itemView.findViewById(R.id.correct_pron);
            notice = itemView.findViewById(R.id.correct_notice);
            share = itemView.findViewById(R.id.share_icon);
            eval_iv_collect = itemView.findViewById(R.id.eval_iv_collect);
            ll_share_icon = itemView.findViewById(R.id.ll_share_icon);
            ll_sen_read_send = itemView.findViewById(R.id.ll_sen_read_send);
            sen_read_button = itemView.findViewById(R.id.sen_read_button);
            ll_correct_pron = itemView.findViewById(R.id.ll_correct_pron);

            all.setOnClickListener(EvaluateAdapter.this);
            play.setOnClickListener(EvaluateAdapter.this);
            recContainer.setOnClickListener(EvaluateAdapter.this);
            playRec.setOnClickListener(EvaluateAdapter.this);
            playingRec.setOnClickListener(EvaluateAdapter.this);
            send.setOnClickListener(EvaluateAdapter.this);
            correct.setOnClickListener(EvaluateAdapter.this);
            share.setOnClickListener(EvaluateAdapter.this);
            eval_iv_collect.setOnClickListener(EvaluateAdapter.this);
            selectWordTextView.setOnClickWordListener(new SelectWordTextView.OnClickWordListener() {
                @Override
                public void onClickWord(String word) {

                    if (wordCallback != null) {

                        if (notice.getVisibility() == View.VISIBLE) {

                            notice.setVisibility(View.GONE);
                        }
                        wordCallback.selectWord(word, getBindingAdapterPosition());
                    }
                }
            });
        }


    }

    public interface ItemClickListener {
        void onItemClick(String function, int position);
    }

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }



    void initRecManager() {
        AudioCorrectRecorderUtils.OnAudioStatusUpdateListener listener2 = new AudioCorrectRecorderUtils.OnAudioStatusUpdateListener() {
            @Override
            public void onUpdate(double db, long time) {
            }

            @Override
            public void onStop(String filePath) {
                recFilePath = filePath;
            }
        };
        recorderManager = AudioCorrectRecorderUtils.getInstance();
        recorderManager.setOnAudioStatusUpdateListener(listener2);
    }

    void showLoading(String content) {
        if (evalDialog == null) {
            View v = View.inflate(mContext, R.layout.dialog_waiting, null);
            TextView tvContent = v.findViewById(R.id.waiting_content);
            tvContent.setText(content);
            evalDialog = new Dialog(mContext, R.style.theme_dialog);
            evalDialog.setContentView(v);
            evalDialog.setCancelable(false);
        }
        evalDialog.show();
    }

    public List<RecycleViewData> getmDataList() {
        return mDataList;
    }


    public interface WordCallback {

        void selectWord(String word, int position);
    }
}
