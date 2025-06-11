package com.ai.bbcpro.word;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.http.BaseHttpRequest;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.ClientSession;
import com.ai.bbcpro.http.ExeProtocol;
import com.ai.bbcpro.http.IResponseReceiver;
import com.ai.bbcpro.http.ProtocolResponse;
import com.ai.bbcpro.http.WordUpdateRequest;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.home.WordCollectBean;
import com.ai.bbcpro.mvp.presenter.me.WordCollectionPresenter;
import com.ai.bbcpro.mvp.view.me.WordCollectionContract;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.ui.activity.me.WordExerciseActivity;
import com.ai.bbcpro.ui.activity.me.WordListenActivity;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.widget.ExeRefreshTime;
import com.ai.bbcpro.widget.HttpRequestFactory;
import com.ai.bbcpro.widget.WordPdfResponse;
import com.ai.bbcpro.widget.WordSynRequest;
import com.ai.bbcpro.widget.WordSynResponse;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * 单词本界面
 *
 * @author chentong
 * @version 1.0
 */

public class WordCollectionActivity extends AppCompatActivity implements
        PullToRefreshView.OnHeaderRefreshListener, PullToRefreshView.OnFooterRefreshListener, WordCollectionContract.WordCollectionView {
    private Context mContext;
    private ArrayList<Word> words;
    private WordOp wordOp;
    private PullToRefreshView refreshView;// 刷新列表
    private View no_login;
    private ListView wordLV;
    private WordListAdapter wordListAdapter;
    private boolean isDelStart = false;
    private Button back, delButton, pdf;
    private String userId;
    private int page = 1;
    private Boolean isLastPage = false;
    private Boolean isTopRefresh = false;
    private Boolean isFootRefresh = false;
    private CustomDialog wettingDialog;

    private LinearLayout word_ll_en2cn;

    private LinearLayout word_ll_cn2en;

    private LinearLayout word_ll_listen;


    private WordCollectionPresenter wordCollectionPresenter;

    private AlertDialog delAlertDialog;

    private void initView() {

        word_ll_en2cn = findViewById(R.id.word_ll_en2cn);
        word_ll_cn2en = findViewById(R.id.word_ll_cn2en);
        word_ll_listen = findViewById(R.id.word_ll_listen);

        refreshView = findViewById(R.id.listview);
        no_login = findViewById(R.id.noLogin);

        delButton = findViewById(R.id.button_delete);
        pdf = findViewById(R.id.button_pdf);

        wordLV = findViewById(R.id.list);
        back = findViewById(R.id.button_back);
    }

    private void initOperation() {

        word_ll_en2cn.setOnClickListener(v -> {

            if (wordListAdapter.getmList().size() >= 4) {

                WordExerciseActivity.startActivity(WordCollectionActivity.this, wordListAdapter.getmList(), 0);
            } else {
                Toast.makeText(MainApplication.getApplication(), "单词数量必须大于等于4", Toast.LENGTH_SHORT).show();
            }
        });
        word_ll_cn2en.setOnClickListener(v -> {

            if (wordListAdapter.getmList().size() >= 4) {

                WordExerciseActivity.startActivity(WordCollectionActivity.this, wordListAdapter.getmList(), 1);
            } else {

                Toast.makeText(MainApplication.getApplication(), "单词数量必须大于等于4", Toast.LENGTH_SHORT).show();
            }
        });
        word_ll_listen.setOnClickListener(v -> {

            if (wordListAdapter.getmList().size() >= 4) {

                WordListenActivity.startActivity(WordCollectionActivity.this, wordListAdapter.getmList(), 1);
            } else {

                Toast.makeText(MainApplication.getApplication(), "单词数量必须大于等于4", Toast.LENGTH_SHORT).show();
            }
        });

        refreshView.setOnHeaderRefreshListener(this);
        refreshView.setOnFooterRefreshListener(this);

        pdf.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startPdf();
            }
        });
        //删除逻辑
/*        delButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDelStart) {
                    //如果为true,表示不删除
                    wettingDialog.show();
                    delButton.setBackgroundResource(R.drawable.button_edit);
                    StringBuffer ids = new StringBuffer();
                    Iterator<Word> iteratorVoa = null;
                    try {
                        iteratorVoa = words.iterator();
                        ArrayList<Word> delWordTemp = new ArrayList<Word>();
                        while (iteratorVoa.hasNext()) {
                            Word word = iteratorVoa.next();
                            if (word.isDelete) {
                                delWordTemp.add(word);
                                ids.append(",").append("\'" + word.key + "\'");
                                iteratorVoa.remove();
                            }
                        }
                        if (ids.toString() != null
                                && ids.toString().length() != 0) {
                            delNetwordWord(delWordTemp);
                            wordOp.tryToDeleteItemWord(ids.toString().substring(1),
                                    userId);
                        } else {
                            CustomToast.showToast(mContext,
                                    R.string.newword_please_select_word, 1000);
                        }
                    } catch (Exception e) {// 当初始化单词表中尚无单词是出现异常处理
                        CustomToast.showToast(mContext, R.string.no_new_word,
                                1000);
                    }
                    cancelDelete();
                    isDelStart = false;
                } else {
                    delButton.setBackgroundResource(R.drawable.button_edit_finished);
                    isDelStart = true;
                }
                changeItemDeleteStart();
            }
        });*/


        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                onBackPressed();
            }
        });

        wordLV.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                if (isDelStart) {
                    words.get(arg2).isDelete = !words.get(arg2).isDelete;
                    handler.sendEmptyMessage(0);
                } else {
                    Intent intent = new Intent(mContext, WordContentActivity.class);
                    intent.putExtra("word", words.get(arg2).key);
                    SettingConfig.Instance().setIsCaChe(true);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_collection_list);
//        CrashApplication.getInstance().addActivity(this);


        wordCollectionPresenter = new WordCollectionPresenter();
        wordCollectionPresenter.attchView(this);

        wordOp = new WordOp(this);
        mContext = this;
        wettingDialog = WaittingDialog.showDialog(mContext);

        initView();
        initOperation();

        wordListAdapter = new WordListAdapter(this);
        wordListAdapter.setClickCallback(new WordListAdapter.ClickCallback() {
            @Override
            public void delClick(Word word) {


                delAlertDialog = new AlertDialog.Builder(WordCollectionActivity.this)
                        .setTitle("删除提示")
                        .setMessage("确定要删除" + "'" + word.getKey() + "'")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String uid = ConfigManager.Instance().loadString("userId", "0");
                                wordCollectionPresenter.updateWord("Iyuba", "delete", word.getKey(), uid, "json");
                                // contentPresenter.updateWord("Iyuba", "delete", apiWordBean1.getKey(), uid, "json");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        wordLV.setAdapter(wordListAdapter);
    }

    @SuppressLint("CheckResult")
    private void startPdf() {
        String u = String.valueOf(AccountManager.Instance(this).getUserId());
        String pageNumber = "1";
        String pageCount = "1000";
        HttpRequestFactory.getWordPdfApi().getPdf(u, pageNumber, pageCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WordPdfResponse>() {
                    @Override
                    public void accept(WordPdfResponse response) throws Exception {
                        if (response.getResult() == 1) showSuccessDialog(response);
                        else ToastUtil.showToast(WordCollectionActivity.this, "pdf导出失败！");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    private void showSuccessDialog(WordPdfResponse response) {
        final String filePath = response.getFilePath();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您的单词pdf已经导出，下载地址：\n\n" +
                        filePath + "\n\n已经复制到剪切板")
                .setPositiveButton("好的，我知道了", null)
                .setNegativeButton("去下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(filePath));
                        startActivity(intent);
                    }
                })
                .show();
        android.text.ClipboardManager cm = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(filePath);
    }

    public void changeItemDeleteStart() {
        if (wordListAdapter != null) {
            wordListAdapter.modeDelete = isDelStart;
            handler.sendEmptyMessage(0);
        }
    }

    public void cancelDelete() {
        if (words != null && words.size() != 0) {
            int size = words.size();
            for (int i = 0; i < size; i++) {
                words.get(i).isDelete = false;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccountManager.Instance(mContext).checkUserLogin()) {
            no_login.setVisibility(View.GONE);
            refreshView.setVisibility(View.VISIBLE);
            userId = AccountManager.Instance(mContext).userId;
            //从数据库获取数据
            words = (ArrayList<Word>) wordOp.findDataByAll(userId);

            if (words != null) {
                wordListAdapter.setData(words);
                handler.sendEmptyMessage(0);
            } else {
                words = new ArrayList<>();
                onHeaderRefresh(refreshView);
            }
        } else {
            no_login.setVisibility(View.VISIBLE);
            refreshView.setVisibility(View.GONE);
            findViewById(R.id.button_to_login).setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(new LoginEventbus());;
                        }
                    });
        }

//        isDelStart = false;
//        if (nla != null) {
//            cancelDelete();
//            isDelStart = false;
//            changeItemDeleteStart();
//        }

//        tryToDeleteWords = (ArrayList<Word>) wo.findDataByDelete(userId);
//        if (tryToDeleteWords != null
//                && NetWorkState.isConnectingToInternet()) {
//            delNetwordWord(tryToDeleteWords);
//        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (wordCollectionPresenter != null) {

            wordCollectionPresenter.detachView();
        }
    }

    private final Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case 0:
                    wordListAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    wettingDialog.show();
                    break;
                case 2:
                    wettingDialog.dismiss();
                    break;
                case 3:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 4:
                    page = 1;
                    handler.sendEmptyMessage(5);
                    handler.sendEmptyMessage(1);
                    break;
                case 5:
                    ExeProtocol.exe(new WordSynRequest(userId, userId, page),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    if (page == 1) {
                                        words.clear();
                                    }
                                    WordSynResponse wsr = (WordSynResponse) bhr;
                                    words.addAll(wsr.wordList);
                                    if (words.size() > 0) {
                                        if (wsr.firstPage == wsr.nextPage) {
                                            isLastPage = true;
                                        } else {
                                            page++;
                                            isLastPage = false;
                                        }

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                wordListAdapter.setData(words);
                                            }
                                        });
                                        //存储单词
                                        wordOp.saveData(wsr.wordList);
                                        handler.sendEmptyMessage(0);
                                        handler.sendEmptyMessage(6);
                                    } else {
                                        handler.sendEmptyMessage(2);
                                        handler.sendEmptyMessage(7);
                                    }
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(3);
                                    handler.sendEmptyMessage(6);
                                }
                            });
                    break;
                case 6:
                    handler.sendEmptyMessage(2);
                    if (isTopRefresh) {
                        isTopRefresh = false;
                        refreshView.onHeaderRefreshComplete();
                    } else if (isFootRefresh) {
                        isFootRefresh = false;
                        refreshView.onFooterRefreshComplete();
                    }
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.word_no_data);
                    break;
                case 8:
                    CustomToast.showToast(mContext, R.string.word_add_all);
                    break;
                case 9:
                    ClientSession.getInstance().asynGetResponse(
                            new WordUpdateRequest(userId,
                                    WordUpdateRequest.MODE_DELETE,
                                    msg.obj.toString()), new IResponseReceiver() {

                                @Override
                                public void onResponse(BaseHttpResponse response, BaseHttpRequest request, int rspCookie) {
                                    wordOp.deleteItemWord(userId);
                                    wettingDialog.dismiss();
                                }

//							@Override
//							public void onResponse(BaseHttpResponse response,
//									BaseHttpRequest request, int rspCookie) {
//								wo.deleteItemWord(userId);
//							}
                            });
                    break;
            }
            return false;
        }
    });

    private void delNetwordWord(ArrayList<Word> wordss) {
        int size = wordss.size();
        Message message;
        for (int i = 0; i < size; i++) {
            message = new Message();
            message.what = 9;
            message.obj = wordss.get(i).key;
            handler.sendMessageDelayed(message, 1500);
        }
    }

    @Override
    public void onFooterRefresh(PullToRefreshView view) {
        if (!isLastPage) {
            handler.sendEmptyMessage(5);
            isFootRefresh = true;
        } else {
            handler.sendEmptyMessage(8);
            refreshView.onFooterRefreshComplete();
        }
    }


    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        if (AccountManager.Instance(mContext).checkUserLogin()) {
            handler.sendEmptyMessage(4);
            refreshView.setLastUpdated(ExeRefreshTime.lastRefreshTime("Word"));
            isTopRefresh = true;
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
    public void collectWord(WordCollectBean wordCollectBean) {

//        toast("删除成功");
        List<Word> wordList = wordListAdapter.getmList();
        for (int i = 0; i < wordList.size(); i++) {

            Word word = wordList.get(i);
            if (word.getKey().equalsIgnoreCase(wordCollectBean.getStrWord())) {

                wordList.remove(word);
                wordListAdapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
