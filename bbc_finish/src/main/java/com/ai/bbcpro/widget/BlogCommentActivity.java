package com.ai.bbcpro.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.word.DataManager;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import personal.iyuba.personalhomelibrary.utils.StatusBarUtil;
import timber.log.Timber;

@SuppressLint("BinaryOperationInTimber")
public class BlogCommentActivity extends AppCompatActivity {

    // 通用变量
    private boolean isConnected;
    private boolean isvip;
    private BackPlayer vv = null;
    private boolean isUpload = false;


    //新版评论列表用到的控件
    private ListView commentList;
    private BlogCommentAdapter commentAdapter;

    private View commentFooter;
    private LayoutInflater inflater;

    // 评论
    private String blogId;
    private int curCommentPage = 1;
    private Button expressButton;
    private EditText expressEditText;
    private String expressWord;
    private View commentLoadMoreLayout;
    private ListView listComment;
    private boolean commentAll = false;
    private boolean commentExist = false;
    private TextView commentLoadMoreTextView;
    private String jiFen;

    // 语音评论新加
    private int commentMode = 0;// 0是文字评论，1是语音评论

    private ArrayList<BlogComment> commentsList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_blogcomment);

        StatusBarUtil.setColor(this, ResourcesCompat.getColor(getResources(), R.color.colorPrimary, getTheme()), 0);
        isConnected = NetWorkState.isConnectingToInternet();

        initComment();
        setComment();

    }

    private void initComment() {

        //ImageLoader初始化
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext)
                .memoryCache(new UsingFreqLimitedMemoryCache(4 * 1024 * 1024))
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);

        blogId = DataManager.Instance().blogContent.blogid;

        inflater = getLayoutInflater();

        commentFooter = inflater.inflate(R.layout.activity_blogcomment_footer, null);

        expressButton = findViewById(R.id.blogcomment_button_express);
        expressEditText = findViewById(R.id.blogcomment_editText_express);

        commentList = findViewById(R.id.blogcomment_list);
        commentList.addFooterView(commentFooter);

        commentLoadMoreLayout = commentFooter.findViewById(R.id.blog_comment_loadmore);
        commentLoadMoreTextView = commentFooter
                .findViewById(R.id.blog_comment_loadmore_text);

        findViewById(R.id.blogComment_back_btn).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BlogCommentActivity.this.finish();
            }
        });

        expressButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AccountManager.Instance(mContext).checkUserLogin()) {
                    if (commentMode == 0) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(expressEditText.getWindowToken(), 0);
                        String expressionInput = expressEditText.getText().toString();
                        if (expressionInput.equals("")) {
                            CustomToast.showToast(mContext,
                                    R.string.study_input_comment);
                        } else {
                            expressWord = expressionInput;
                            Timber.tag("expressWord:").d(expressionInput);
                            handler_comment.sendEmptyMessage(1);
                            expressEditText.setText("");
                        }
                    }

                } else {
                    EventBus.getDefault().post(new LoginEventbus());;
                }
            }
        });

        commentLoadMoreLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isConnected) {
                    commentLoadMoreTextView.setText(getResources().getString(
                            R.string.check_network));
                } else if (!commentAll) {
                    handler_comment.sendEmptyMessage(0);
                }
            }
        });
    }

    private void setComment() {

        Timber.tag("setComment中 调用构造函数之前：").d(commentsList.size() + "");
        commentAdapter = new BlogCommentAdapter(mContext, commentsList, 0);
        Timber.tag("setComment中 调用构造函数之后：").d(commentsList.size() + "");
        commentList.setAdapter(commentAdapter);
        handler_comment.sendEmptyMessage(0);

    }

    @SuppressLint("HandlerLeak")
    private Handler handler_comment = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    new Thread(new commentThread()).start();
                    break;
                case 1:
                    String uid = AccountManager.Instance(mContext).userId;
                    String uName = ConfigManager.Instance().loadString("userName", "");
                    if (TextUtils.isEmpty(uName) || "null".equals(uName)) {
                        uName = uid;
                    }
                    ExeProtocol.exe(new BlogExpressionRequest(blogId, uid, uName, expressWord),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {

                                    BlogExpressionResponse response = (BlogExpressionResponse) bhr;
                                    jiFen = response.jiFen;
                                    handler_comment.sendEmptyMessage(4);
                                }

                                @Override
                                public void error() {
                                }
                            });

                    break;
                case 3:
                    handler_comment.sendEmptyMessage(0);
                    break;
                case 4:
                    curCommentPage = 1;
                    commentsList.clear();
                    if (jiFen != null && !jiFen.equals("") && Integer.parseInt(jiFen) > 0) {
                        CustomToast.showToast(mContext, "评论成功，+" + jiFen + "积分");
                    }
                    handler_comment.sendEmptyMessage(3);
                    break;
                case 5:
                    commentAdapter.notifyDataSetChanged();
                    break;
                case 6:
                    if (commentExist) {
                        commentLoadMoreTextView.setText(getResources().getString(
                                R.string.study_all_loaded));
                        commentAll = true;
                    } else if (commentsList == null) {
                        commentLoadMoreTextView.setText(getResources().getString(
                                R.string.check_network));
                    } else {
                        commentLoadMoreTextView.setText(getResources().getString(
                                R.string.study_no_comment));
                        commentAll = true;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private class commentThread implements Runnable {
        @Override
        public void run() {

            ExeProtocol.exe(new BlogCommentRequest(blogId),
                    new ProtocolResponse() {

                        @Override
                        public void finish(BaseHttpResponse bhr) {
//                            Looper.prepare();

                            BlogCommentResponse res = (BlogCommentResponse) bhr;

                            if (res.result.equals("261")) {
                                commentsList.addAll(res.Comments);
                                if (commentsList.size() != 0) {
                                    commentExist = true;
                                    handler_comment.sendEmptyMessage(5);
                                    handler_comment.sendEmptyMessage(6);
                                } else {
                                    handler_comment.sendEmptyMessage(6);
                                }
                            } else {
                                handler_comment.sendEmptyMessage(6);
                            }
//                            Looper.loop();
                        }

                        @Override
                        public void error() {
                        }

                    });
        }
    }

}

