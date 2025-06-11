package com.ai.bbcpro.ui.activity.me;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.me.WordAnswerAdapter;
import com.ai.bbcpro.databinding.ActivityWordAnswerBinding;
import com.ai.bbcpro.entity.WordQuestion;
import com.ai.bbcpro.mvp.activity.BaseActivity;
import com.ai.bbcpro.mvp.presenter.me.WordAnswerPresenter;
import com.ai.bbcpro.mvp.view.WordAnswerContract;
import com.ai.bbcpro.util.decoration.LineItemDecoration;
import com.ai.bbcpro.util.popup.AnalysisPopup;
import com.ai.bbcpro.word.Word;
import com.ai.bbcpro.word.WordOp;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 单词训练页面
 */
public class WordExerciseActivity extends BaseActivity<WordAnswerContract.WordAnswerView, WordAnswerContract.WordAnswerPresenter>
        implements WordAnswerContract.WordAnswerView {

    private ActivityWordAnswerBinding activityWordAnswerBinding;

    private List<Word> jpConceptWordList;

    private List<WordQuestion> wordQuestions;

    private Random random;
    private int position;

    private WordAnswerAdapter answerAdapter;

    private AnalysisPopup analysisPopup;

    private DecimalFormat decimalFormat;

    private LineItemDecoration lineItemDecoration;

    /**
     * type
     * 0：中文
     * 1：英文
     */
    private int questionType = 0;

    private WordOp wordOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = 0;
        random = new Random();
        decimalFormat = new DecimalFormat("##.0");
        getBundle();
        activityWordAnswerBinding.toolbar.toolbarIvRight.setVisibility(View.GONE);
        wordOp = new WordOp(this);

        initOperation();
        initData();
    }

    @Override
    public View initLayout() {

        activityWordAnswerBinding = ActivityWordAnswerBinding.inflate(getLayoutInflater());
        return activityWordAnswerBinding.getRoot();
    }

    @Override
    public WordAnswerContract.WordAnswerPresenter initPresenter() {
        return new WordAnswerPresenter();
    }

    private void initOperation() {

        activityWordAnswerBinding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        activityWordAnswerBinding.toolbar.toolbarTvTitle.setText("单词训练");
        activityWordAnswerBinding.toolbar.toolbarIvRight.setVisibility(View.INVISIBLE);

        //进度
        activityWordAnswerBinding.waPbIndex.setMax(jpConceptWordList.size());
//        activityWordAnswerBinding.waPbIndex.setProgress(1);
//        activityWordAnswerBinding.waTvIndex.setText((position + 1) + "/" + jpConceptWordList.size());


        lineItemDecoration = new LineItemDecoration(WordExerciseActivity.this, LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(getResources().getDrawable(R.drawable.space_10dp));

        if (activityWordAnswerBinding.waRvAnswers.getItemDecorationCount() == 0) {

            activityWordAnswerBinding.waRvAnswers.addItemDecoration(lineItemDecoration);
        }

        activityWordAnswerBinding.waRvAnswers.setLayoutManager(new LinearLayoutManager(this));
        answerAdapter = new WordAnswerAdapter(R.layout.item_answer, new ArrayList<>());
        activityWordAnswerBinding.waRvAnswers.setAdapter(answerAdapter);
        answerAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int listPosition) {

                //选择了就不能再选了
                if (answerAdapter.getChoosePosition() != -1 && answerAdapter.gettPosition() != -1) {

                    return;
                }

                WordQuestion wordQuestion = wordQuestions.get(position);
                wordQuestion.setChoosePosition(listPosition);//记录选择的位置
                wordQuestion.setTestTime(getCurrentTime());//记录作答的时间
                answerAdapter.setChoosePosition(listPosition);//适配器记录选择的位置
                answerAdapter.settPosition(wordQuestion.gettPosition());//适配器记录正确答案的位置
                answerAdapter.notifyDataSetChanged();

                //显示下一个按钮
                activityWordAnswerBinding.waLlButton.setVisibility(View.VISIBLE);
                Word word = wordQuestion.getWord();
                if (listPosition == wordQuestion.gettPosition()) {//选中的是正确的

                    word.setAnswer_status(1);
                } else {

                    word.setAnswer_status(2);
                }
            }
        });
        //下一个
        activityWordAnswerBinding.waButNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == (wordQuestions.size() - 1)) {

                    showResultAlert();
                    return;
                }

                position++;
                //设置答题开始时间
//                wordQuestions.get(position).setBeginTime(getCurrentTime());

//                activityWordAnswerBinding.toolbar.toolbarIvTitle.setText((position + 1) + "/" + jpConceptWordList.size());
                answerAdapter.settPosition(-1);
                answerAdapter.setChoosePosition(-1);
                showData();
            }
        });
        activityWordAnswerBinding.waButAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initAnalysisPopup(wordQuestions.get(position).getWord());
            }
        });
    }

    /**
     * 检测闯关是否失败
     */
    private void showResultAlert() {

        int totalDoQuestion = 0;//共做多少道题
        int tNUM = 0;//已做题正确数量
        int fNum = 0;//已做题错误数量

        for (int i = 0; i < wordQuestions.size(); i++) {

            WordQuestion wq = wordQuestions.get(i);
            if (wq.getChoosePosition() == -1) {
                break;
            } else {
                totalDoQuestion++;
                if (wq.gettPosition() == wq.getChoosePosition()) {
                    tNUM++;
                } else {
                    fNum++;
                }
            }
        }
//        double fPercentage = 100.0 * fNum / wordQuestions.size();//总的错误率
//        double questionPercentage = 100.0 * totalDoQuestion / wordQuestions.size();//总做题进度

        double tPercentage = 100.0 * tNUM / totalDoQuestion;

        AlertDialog alertDialog = new AlertDialog.Builder(WordExerciseActivity.this)
                .setTitle("训练结果")
                .setMessage("共做：" + totalDoQuestion + "题，\n做对：" + tNUM + "题，\n正确比例" + decimalFormat.format(tPercentage) + "%")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        finish();
                    }
                })
                .show();
    }


    /**
     * 解释、解析弹窗
     *
     * @param conceptWord
     */
    private void initAnalysisPopup(Word conceptWord) {

        if (analysisPopup == null) {

            analysisPopup = new AnalysisPopup(this);
        }

        analysisPopup.setJpWord(conceptWord);
        analysisPopup.showPopupWindow();
    }

    public String getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String strCurrTime = formatter.format(curDate);
        return strCurrTime;
    }

    private void initData() {


        new Thread(() -> {


            wordQuestions = new ArrayList<>();
            for (int i = 0; i < jpConceptWordList.size(); i++) {

                Word conceptWord = jpConceptWordList.get(i);
                WordQuestion wordQuestion = new WordQuestion();
                wordQuestion.setWord(conceptWord);
                wordQuestion.setAnswerList(getAnswerData(conceptWord));
                int tPosition = random.nextInt(4);
                wordQuestion.getAnswerList().add(tPosition, conceptWord);
                wordQuestion.settPosition(tPosition);
                wordQuestion.setType(questionType);
                wordQuestions.add(wordQuestion);
            }

            runOnUiThread(this::showData);
            //设置开始时间
//            wordQuestions.get(0).setBeginTime(getCurrentTime());
        }).start();
    }


    private void showData() {

        //index
        activityWordAnswerBinding.waPbIndex.setProgress((position + 1));
        activityWordAnswerBinding.waTvIndex.setText((position + 1) + "/" + jpConceptWordList.size());
        //隐藏按钮
        activityWordAnswerBinding.waLlButton.setVisibility(View.INVISIBLE);

        WordQuestion wordQuestion = wordQuestions.get(position);
        if (wordQuestion.getType() == 0) {

            activityWordAnswerBinding.waTvQuestion.setText(wordQuestion.getWord().getDef());
        } else {

            activityWordAnswerBinding.waTvQuestion.setText(wordQuestion.getWord().getKey());
        }
        answerAdapter.setType(wordQuestion.getType());
        answerAdapter.setNewData(wordQuestion.getAnswerList());

        //设置
        if ((position + 1) == jpConceptWordList.size()) {

            activityWordAnswerBinding.waButNext.setText("完成");
        }
    }

    /**
     * 创建答题项
     *
     * @param collectionWord
     * @return
     */
    private List<Word> getAnswerData(Word collectionWord) {

        List<Word> answerList = new ArrayList<>();
        do {

            Word addConceptWord = wordOp.getRandom();
            if (addConceptWord == null) {

                break;
            }
            boolean isAdd = true;//添加不重复的选项
            for (int i = 0; i < answerList.size(); i++) {

                Word jp = answerList.get(i);
                if (jp.getKey().equalsIgnoreCase(addConceptWord.getKey()) && !jp.getKey().equalsIgnoreCase(collectionWord.getKey())) {

                    isAdd = false;
                }
            }
            if (isAdd && !addConceptWord.getKey().equalsIgnoreCase(collectionWord.getKey())) {

                answerList.add(addConceptWord);
            }
        } while (answerList.size() != 3);
        return answerList;
    }

    private void getBundle() {

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            jpConceptWordList = (List<Word>) bundle.getSerializable("DATAS");
            questionType = bundle.getInt("QUESTION_TYPE");
        }
    }

    public static void startActivity(Activity activity, List<Word> jpConceptWordList, int questionType) {

        Intent intent = new Intent(activity, WordExerciseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("DATAS", (Serializable) jpConceptWordList);
        bundle.putInt("QUESTION_TYPE", questionType);
        intent.putExtras(bundle);
        activity.startActivity(intent);
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
    protected void onDestroy() {
        super.onDestroy();

    }
}
