package com.ai.bbcpro.word;


import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;

import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.ExeProtocol;
import com.ai.bbcpro.http.ProtocolResponse;
import com.ai.bbcpro.http.WordUpdateRequest;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.widget.BackPlayer;
import com.ai.common.CommonConstant;

import org.greenrobot.eventbus.EventBus;


/**
 * 类名
 *
 * @author 作者 <br/>
 *         实现的主要功能。 创建日期 修改者，修改日期，修改内容。
 */
public class Cet4WordContent extends AppCompatActivity implements
        OnCheckedChangeListener, OnClickListener {
    private Context mContext;
    private Button backBtn;
    private TextView position;
    private View left, right, study, review;
    private ImageView leftImage, rightImage;
    private SegmentedRadioGroup mode;
    private String vocabularyMode;
    private TextView word;
    private Button easy, difficult, know;
    private TextView word2, pron, def, example;
    private ImageView addWord, addWord2;
    private ImageView sound;
    private int pos, all;
    private Cet4Word cet4Word;
    private BackPlayer vv;
    private boolean fromTestDifficult;
    private WordOp wordOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocabulary_content);
        mContext = this;
//        CrashApplication.getInstance().addActivity(this);
        vocabularyMode = ConfigManager.Instance().loadString("vocabulary");
        pos = WordDataManager.Instance().pos;
        all = WordDataManager.Instance().words.size();
        fromTestDifficult = false;
        wordOp = new WordOp(mContext);
        init();
    }

    private void init() {

        backBtn = (Button) findViewById(R.id.button_back);
        backBtn.setOnClickListener(this);
        left = findViewById(R.id.left);
        left.setOnClickListener(this);
        right = findViewById(R.id.right);
        right.setOnClickListener(this);
        leftImage = (ImageView) findViewById(R.id.left_img);
        rightImage = (ImageView) findViewById(R.id.right_img);
        mode = (SegmentedRadioGroup) findViewById(R.id.mode);
        mode.setOnCheckedChangeListener(this);
        position = (TextView) findViewById(R.id.position);
        study = findViewById(R.id.study_content);
        review = findViewById(R.id.review_content);
        vv = BackgroundManager.Instace().bindService.getPlayer();
        initStudy();
        initReview();
        setPosText();
        controlVideo();
        if (vocabularyMode.equals("study")) {
            checkStudy();
        } else {
            checkReview();
        }
    }

    private void initReview() {

        word = (TextView) findViewById(R.id.word);
        easy = (Button) findViewById(R.id.so_easy);
        difficult = (Button) findViewById(R.id.difficult);
        addWord = (ImageView) findViewById(R.id.word_add2);
        easy.setOnClickListener(this);
        difficult.setOnClickListener(this);
        addWord.setOnClickListener(this);
    }

    /**
     *
     */
    private void initStudy() {

        word2 = (TextView) findViewById(R.id.word_key);
        def = (TextView) findViewById(R.id.word_def);
        pron = (TextView) findViewById(R.id.word_pron);
        sound = (ImageView) findViewById(R.id.word_speaker);
        example = (TextView) findViewById(R.id.example);
        example.setMovementMethod(ScrollingMovementMethod.getInstance());
        addWord2 = (ImageView) findViewById(R.id.word_add);
        know = (Button) findViewById(R.id.know);
        addWord2.setOnClickListener(this);
        sound.setOnClickListener(this);
        know.setOnClickListener(this);
    }

    private void checkStudy() {
        study.setVisibility(View.VISIBLE);
        review.setVisibility(View.GONE);
        mode.check(R.id.study);
    }

    private void checkReview() {
        study.setVisibility(View.GONE);
        review.setVisibility(View.VISIBLE);
        mode.check(R.id.review);
    }

    private void setPosText() {
        StringBuffer sb = new StringBuffer();
        sb.append(pos + 1).append('/').append(all);
        position.setText(sb.toString());
        if (pos == 0) {
            leftImage.setBackgroundResource(R.drawable.ic_left_arrow_grey_42dp);
            rightImage.setBackgroundResource(R.drawable.ic_right_arrow_light_42dp);
        } else if (pos == all - 1) {
            leftImage.setBackgroundResource(R.drawable.ic_left_arrow_light_42dp);
            rightImage.setBackgroundResource(R.drawable.ic_right_arrow_grey_42dp);
        } else {
            leftImage.setBackgroundResource(R.drawable.ic_left_arrow_light_42dp);
            rightImage.setBackgroundResource(R.drawable.ic_right_arrow_light_42dp);
        }
        fromTestDifficult = false;
        setContent();
    }

    private void setContent() {
        cet4Word = WordDataManager.Instance().words.get(pos);
        cet4Word.example = new EGDBOp(mContext).findData(cet4Word.word);
        if (mode.getCheckedRadioButtonId() == R.id.study) {
            word2.setText(cet4Word.word);
            example.setText(Html.fromHtml(cet4Word.example));
            StringBuffer sb = new StringBuffer();
            sb.append('[').append(cet4Word.pron).append(']');
            pron.setText(TextAttr.decode(sb.toString()));
            def.setText(cet4Word.def);
            if (fromTestDifficult) {
                know.setVisibility(View.VISIBLE);
            } else {
                know.setVisibility(View.GONE);
            }
            if (wordOp.findDataByName(cet4Word.word) != null) {
                addWord2.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord2.setBackgroundResource(R.drawable.word_add);
            }
        } else {
            word.setText(cet4Word.word);
            if (wordOp.findDataByName(cet4Word.word) != null) {
                addWord.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord.setBackgroundResource(R.drawable.word_add);
            }
        }
    }

    private void saveNewWords() {
        if (!AccountManager.Instance(mContext).checkUserLogin()) {
            EventBus.getDefault().post(new LoginEventbus());;
        } else {
            Word word = new Word();
            word.key = cet4Word.word;
            word.audioUrl = cet4Word.sound;
            word.pron = cet4Word.pron;
            word.def = cet4Word.def;
            word.examples = cet4Word.example;
            word.userid = AccountManager.Instance(mContext).userId;
            new WordOp(mContext).saveData(word);
            if (mode.getCheckedRadioButtonId() == R.id.study) {
                addWord2.setBackgroundResource(R.drawable.word_add_ok);
            } else {
                addWord.setBackgroundResource(R.drawable.word_add_ok);
            }
            CustomToast.showToast(mContext, R.string.play_ins_new_word_success);
            addNetwordWord(cet4Word.word);
        }
    }

    private void addNetwordWord(String wordTemp) {
        ExeProtocol.exe(new WordUpdateRequest(
                        AccountManager.Instance(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new ProtocolResponse() {

//					@Override
//					public void finish(BaseHttpResponse bhr) {
//
//					}

                    @Override
                    public void finish(BaseHttpResponse bhr) {

                    }

                    @Override
                    public void error() {


                    }
                });
    }

    private void playAudio() {
        String url = "http://staticvip2."+ CommonConstant.domain+"aiciaudio/" + cet4Word.word
                + ".mp3";
        vv.setVideoPath(url);
    }

    private void controlVideo() {
        vv.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer arg0) {
                vv.start();
            }
        });
    }

    private void toNextOne() {
        if (pos < all - 1) {
            pos++;
        } else {
            CustomToast.showToast(mContext, "The last one!");
        }
        setPosText();
        playAudio();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mode.getCheckedRadioButtonId() == R.id.study) {
            ConfigManager.Instance().putString("vocabulary", "study");
        } else {
            ConfigManager.Instance().putString("vocabulary", "review");
        }
        WordDataManager.Instance().number = pos;
    }

    @Override
    public void onClick(View arg0) {

        int grade;
        Cet4WordOp op;
        switch (arg0.getId()) {
            case R.id.button_back:
                onBackPressed();
                break;
            case R.id.left:
                if (pos > 0) {
                    pos--;
                } else {
                    CustomToast.showToast(mContext, "The first one!");
                }
                setPosText();
                playAudio();
                break;
            case R.id.right:
                toNextOne();
                break;
            case R.id.word_add:
            case R.id.word_add2:
                saveNewWords();
                break;
            case R.id.so_easy:
                vv.pause();
                grade = Integer.parseInt(cet4Word.star);
                grade += 3;
                cet4Word.star = String.valueOf(grade);
                op = new Cet4WordOp(mContext);
                op.updateStar(cet4Word);
                toNextOne();
                break;
            case R.id.difficult:
                vv.pause();
                grade = Integer.parseInt(cet4Word.star);
                grade -= 3;
                cet4Word.star = String.valueOf(grade);
                op = new Cet4WordOp(mContext);
                op.updateStar(cet4Word);
                fromTestDifficult = true;
                checkStudy();
                setContent();
                break;
            case R.id.word_speaker:
                playAudio();
                break;
            case R.id.know:
                fromTestDifficult = false;
                toNextOne();
                checkReview();
                setContent();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup arg0, int checkedId) {

        if (checkedId == R.id.study) {
            checkStudy();
        } else {
            checkReview();
        }
        setContent();
    }
}
