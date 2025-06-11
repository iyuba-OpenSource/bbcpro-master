package com.ai.bbcpro.word;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.DictResponse;
import com.ai.bbcpro.http.ExeProtocol;
import com.ai.bbcpro.http.ProtocolResponse;
import com.ai.bbcpro.http.WordUpdateRequest;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.player.Player;
import com.ai.bbcpro.setting.SettingConfig;

import org.greenrobot.eventbus.EventBus;


/**
 * 单词内容界面
 *
 * @author chentong
 * @version 1.0
 * @para "word"传入单词值
 */
public class WordContentActivity extends AppCompatActivity {
    private Context mContext;
    private Button backBtn;
    private String appointWord;
    private TextView key, pron, def, example;
    private ImageView saveBtn, addok;
    private Word curWord;
    private ImageView speaker;
    private boolean fromHtml;
    private CustomDialog waittingDialog;

    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word);
        mContext = this;
//        CrashApplication.getInstance().addActivity(this);
        waittingDialog = WaittingDialog.showDialog(mContext);
        backBtn = findViewById(R.id.button_back);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onBackPressed();
            }
        });
        saveBtn = findViewById(R.id.word_add);
        addok = findViewById(R.id.word_add_ok);
        //如果isme为true表示已经添加到单词本了
        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                saveNewWords();
            }
        });
        appointWord = this.getIntent().getStringExtra("word");
        //判断图片显示的样子
        if (SettingConfig.Instance().getIsCaChe()) {
            saveBtn.setVisibility(View.GONE);
            addok.setVisibility(View.VISIBLE);
        } else {
            saveBtn.setVisibility(View.VISIBLE);
            addok.setVisibility(View.GONE);
        }
        initGetWordMenu();
        handler.sendEmptyMessage(0);
    }

    private void initGetWordMenu() {
        key = findViewById(R.id.word_key);
        pron = findViewById(R.id.word_pron);
        def = findViewById(R.id.word_def);
        example = findViewById(R.id.example);
        speaker = findViewById(R.id.word_speaker);
        curWord = new WordDBOp(mContext).findDataByKey(appointWord);
        if (curWord != null) {
            curWord.examples = new EGDBOp(mContext).findData(curWord.key);
            handler.sendEmptyMessage(2);
        }
        getNetworkInterpretation();
    }

    /**
     * 获取单词释义
     */
    private void getNetworkInterpretation() {
        if (appointWord != null && appointWord.length() != 0) {
            ExeProtocol.exe(new DictRequest(appointWord),
                    new ProtocolResponse() {
                        //						@Override
//						public void finish(BaseHttpResponse bhr) {
//							// TODO Auto-generated method stub
//							DictResponse dictResponse = (DictResponse) bhr;
//							fromHtml = true;
//							curWord = dictResponse.word;
//							handler.sendEmptyMessage(2);
//						}
                        @Override
                        public void finish(BaseHttpResponse bhr) {
                            // TODO Auto-generated method stub
                            DictResponse dictResponse = (DictResponse) bhr;
                            fromHtml = true;
                            curWord = dictResponse.word;
                            handler.sendEmptyMessage(2);
                        }

                        @Override
                        public void error() {
                            // TODO Auto-generated method stub
                            handler.sendEmptyMessage(5);
                        }
                    });
        } else {
            handler.sendEmptyMessage(4);
        }
    }

    /**
     * 显示单词释义
     */
    private void showWordDefInfo() {
        key.setText(curWord.key);
        if (curWord.pron != null && curWord.pron.length() != 0) {
            if (fromHtml) {
                pron.setText(Html.fromHtml("[" + curWord.pron + "]"));
            } else {
                pron.setText(TextAttr.decode("[" + curWord.pron + "]"));
            }
        }
        def.setText(curWord.def);
        if (curWord.examples != null && curWord.examples.length() != 0) {
            example.setText(Html.fromHtml(curWord.examples));
        } else {
            example.setText(R.string.no_word_example);
        }
        if (curWord.audioUrl != null && curWord.audioUrl.length() != 0) {
            speaker.setVisibility(View.VISIBLE);
        } else {
            speaker.setVisibility(View.GONE);
        }
        speaker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (player == null) {

                    player = new Player(mContext, null);
                }
                String url = curWord.audioUrl;
                player.reset();
                player.playUrl(url);
            }
        });
        handler.sendEmptyMessage(1);
    }

    /**
     * 保存单词值单词本首相判断是否登录如果登陆了,就保存单词到本地
     */
    private void saveNewWords() {
        if (AccountManager.Instance(getApplicationContext()).checkUserLogin()) {
            curWord.userid = AccountManager.Instance(getApplicationContext()).userId;
            handler.sendEmptyMessage(3);
        } else if (SettingConfig.Instance().getIstour()) {
            Toast.makeText(mContext, "请登录正式用户", Toast.LENGTH_SHORT).show();
        } else {
            EventBus.getDefault().post(new LoginEventbus());;
        }
    }

    /**
     * 上传网络生词库
     */
    private void addNetwordWord(String wordTemp) {
        ExeProtocol.exe(new WordUpdateRequest(
                        AccountManager.Instance(mContext).userId,
                        WordUpdateRequest.MODE_INSERT, wordTemp),
                new ProtocolResponse() {
                    //					@Override
//					public void finish(BaseHttpResponse bhr) {
//						// TODO Auto-generated method stub
//						onBackPressed();
//					}
                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        //onBackPressed();
                        Log.d("测试", "finish: 我是br" + bhr);
                        handler.sendEmptyMessage(6);
                    }

                    @Override
                    public void error() {
                        // TODO Auto-generated method stub
                        Log.d("测试", "finish: 我是br上传失败");
                        handler.sendEmptyMessage(5);
                    }
                });
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waittingDialog.show();
                    break;
                case 1:
                    waittingDialog.dismiss();
                    break;
                case 2:
                    showWordDefInfo();
                    break;
                case 3:
                    //保存到本地数据库
                    new WordOp(mContext).saveData(curWord);
                    saveBtn.setVisibility(View.GONE);
                    addok.setVisibility(View.VISIBLE);
                    //上传到网络
                    addNetwordWord(curWord.key);
                    CustomToast.showToast(mContext, R.string.play_ins_new_word_success);
                    break;
                case 4:
                    CustomToast.showToast(mContext,
                            R.string.play_please_take_the_word);
                    break;
                case 5:
                    handler.sendEmptyMessage(1);
                    CustomToast.showToast(mContext,
                            mContext.getString(R.string.action_fail) + "\n"
                                    + mContext.getString(R.string.check_network));
                    break;
                case 6:
                    Toast.makeText(mContext, "上传网络成功单词成功", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        fromHtml = false;
    }
}

