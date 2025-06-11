package com.ai.bbcpro.ui.fragment.content;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.bbcpro.R;
import com.ai.bbcpro.adapter.ContentQuestionAdapter;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.databinding.FragmentContentQuestionBinding;
import com.ai.bbcpro.entity.Exercises;
import com.ai.bbcpro.entity.WxLoginEventbus;
import com.ai.bbcpro.event.LoginEventbus;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.model.bean.UpdateTestRecordBean;
import com.ai.bbcpro.mvp.presenter.ContentQuestionPresenter;
import com.ai.bbcpro.mvp.view.ContentQuestionContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.util.MD5;
import com.ai.bbcpro.util.decoration.LineItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评测主页面-问题fragmnt
 */
public class QuestionFragment extends Fragment implements ContentQuestionContract.ContentQuestionView {


    private static final String BBCID = "BBCID";

    /**
     * 新闻id
     */
    private int bbcid;

    //    private NewsListDao newsListDao;
    private HeadlinesDataManager headlinesDataManager;

    private FragmentContentQuestionBinding fragmentContentQuestionBinding;

    //选项适配器
    private ContentQuestionAdapter contentQuestionAdapter;

    private List<QuestionBean> questionBeanList;

    //当前数据的位置
    private int position = 0;

    private ContentQuestionPresenter contentQuestionPresenter;

    private SharedPreferences sharedPreferences;

    private SimpleDateFormat simpleDateFormat;

    private LineItemDecoration lineItemDecoration;

    public QuestionFragment() {

//        newsListDao = new NewsListDaoImpl(MainApplication.getApplication(), "news_list");
        headlinesDataManager = HeadlinesDataManager.getInstance(MainApplication.getApplication());
        sharedPreferences = MainApplication.getApplication().getSharedPreferences(ConfigManager.CONFIG_NAME, 0);
        simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
    }

    /**
     * @param flag 1日期+时间 0时间
     * @return
     */

    private String getTime(int flag) {

        if (flag == 1) {

            simpleDateFormat.applyPattern("yyyy-MM-dd HH:mm:ss");
        } else {

            simpleDateFormat.applyPattern("yyyy-MM-dd");
        }
        return simpleDateFormat.format(new Date());
    }

    public static QuestionFragment newInstance(int bbcid) {

        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(BBCID, bbcid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            bbcid = getArguments().getInt(BBCID);
        }
        contentQuestionPresenter = new ContentQuestionPresenter();
        contentQuestionPresenter.attchView(this);

        lineItemDecoration = new LineItemDecoration(MainApplication.getApplication(), LinearLayoutManager.VERTICAL);
        lineItemDecoration.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.space_10dp, null));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (contentQuestionPresenter != null) {

            contentQuestionPresenter.detachView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentContentQuestionBinding = FragmentContentQuestionBinding.inflate(inflater, container, false);
        return fragmentContentQuestionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contentQuestionAdapter = new ContentQuestionAdapter(R.layout.item_content_question, new ArrayList<>());
        fragmentContentQuestionBinding.cqRvQl.setLayoutManager(new LinearLayoutManager(MainApplication.getApplication()));
        int decCount = fragmentContentQuestionBinding.cqRvQl.getItemDecorationCount();
        if (decCount == 0) {

            fragmentContentQuestionBinding.cqRvQl.addItemDecoration(lineItemDecoration);
        }

        fragmentContentQuestionBinding.cqRvQl.setAdapter(contentQuestionAdapter);
        contentQuestionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {


                if (!contentQuestionAdapter.isCheck()) {//不能选择

                    return;
                }

                contentQuestionAdapter.setPostion(i);
                contentQuestionAdapter.notifyDataSetChanged();
                //记录用户选择的答案和位置
                QuestionBean questionBean = questionBeanList.get(position);
                questionBean.setCheckPosition(i);
                questionBean.setCheckAnswer(contentQuestionAdapter.getItem(i));
                questionBean.setTestTime(getTime(1));
            }
        });

        initOperation();

        showData();
    }


    /**
     * 获取数据显示数据
     */
    private void showData() {

        questionBeanList = headlinesDataManager.getQuestionList(bbcid);

        if (questionBeanList.size() > 0) {

            QuestionBean questionBean = questionBeanList.get(0);
            if (questionBean.getUpload() == 1) {//如果上传过了，则直接进入到对照答案状态

                contentQuestionAdapter.setCheck(false);
                position = 0;
                showQuestion();
            } else {

                questionBean.setBeginTime(getTime(1));
                position = 0;
                showQuestion();
            }
        }
    }

    /**
     * 切换数据
     */
    public void switchData() {

        if (getArguments() != null) {
            bbcid = getArguments().getInt(BBCID);
        }
        showData();
    }

    private void initOperation() {

        fragmentContentQuestionBinding.cqTvPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (position <= 0) {
                    position = 0;
                    if (!contentQuestionAdapter.isCheck()) {//重做

                        redoQuestion();
                    }
                    return;
                }

                position--;
                showQuestion();
            }
        });
        fragmentContentQuestionBinding.cqTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position >= (questionBeanList.size() - 1)) {


                    if (!contentQuestionAdapter.isCheck()) {//不能选择选线则是在对比答案状态

                        toast("已经是最后一页了");
                        return;
                    }

                    String uid = sharedPreferences.getString("userId", "0");
                    if (uid.equals("0") || uid.equals("")) {

                        EventBus.getDefault().post(new LoginEventbus());;
                        toast("请登录");
                        return;
                    }
                    boolean isComplete = true;
                    for (int i = 0; i < questionBeanList.size(); i++) {

                        QuestionBean questionBean = questionBeanList.get(i);
                        if (questionBean.getCheckPosition() == -1) {

                            isComplete = false;
                            break;
                        }
                    }

                    if (!isComplete) {//没有完成

                        Toast.makeText(MainApplication.getApplication(), "还有题没有作答", Toast.LENGTH_SHORT).show();
                    } else {//完成

                        submitQuestion();
                    }
                } else {

                    position++;
                    questionBeanList.get(position).setBeginTime(getTime(1));
                    showQuestion();
                }
            }
        });
    }


    /**
     * 重做练习题
     */
    private void redoQuestion() {


        for (int i = 0; i < questionBeanList.size(); i++) {

            QuestionBean questionBean = questionBeanList.get(i);
            questionBean.setCheckPosition(-1);
            questionBean.setCheckAnswer(null);
            if (i == 0) {
                questionBean.setBeginTime(getTime(1));
            }
        }

        contentQuestionAdapter.setCheck(true);
        showQuestion();
    }

    /**
     * 完成问题上传
     */
    private void submitQuestion() {

        String uid = sharedPreferences.getString("uid", "0");
        List<Exercises> exercisesList = new ArrayList<>();
        for (int i = 0; i < questionBeanList.size(); i++) {

            QuestionBean questionBean = questionBeanList.get(i);
            int position;//计算正确的答案的位置
            if (questionBean.getAnswer().equalsIgnoreCase("A")) {
                position = 0;
            } else if (questionBean.getAnswer().equalsIgnoreCase("B")) {
                position = 1;
            } else if (questionBean.getAnswer().equalsIgnoreCase("C")) {
                position = 2;
            } else {
                position = 3;
            }
            int AnswerResut = 0;//判断答题是否正确
            if (questionBean.getCheckPosition() == position) {

                AnswerResut = 1;
            } else {

                AnswerResut = 0;
            }
            String userAnswer;//要上传ABCD 转换一下
            if (questionBean.getCheckPosition() == 0) {

                userAnswer = "A";
            } else if (questionBean.getCheckPosition() == 1) {

                userAnswer = "B";
            } else if (questionBean.getCheckPosition() == 2) {

                userAnswer = "C";
            } else {

                userAnswer = "D";
            }

            Exercises exercises = new Exercises(AnswerResut, questionBean.getBeginTime(), bbcid, questionBean.getAnswer(),
                    questionBean.getIndexId(), questionBean.getTestTime(), userAnswer, Integer.parseInt(uid));
            exercisesList.add(exercises);
        }

        Map<String, String> stringStringMap = new HashMap<>();
        stringStringMap.put("datalist", new Gson().toJson(exercisesList));


        simpleDateFormat.applyPattern("yyyy-MM-dd");
        UMConfigure.getOaid(MainApplication.getApplication(), new OnGetOaidListener() {
            @Override
            public void onGetOaid(String s) {

                String jsonStr = null;
                try {
                    jsonStr = URLEncoder.encode(new Gson().toJson(stringStringMap), "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                String signStr = MD5.getMD5ofStr(uid + "iyubaTest" + simpleDateFormat.format(new Date()));
                contentQuestionPresenter.updateTestRecordNew("json", "BBC", signStr, uid,
                        Constant.APPID, 0, s, jsonStr);
            }
        });
    }

    /**
     * 显示数据
     */
    private void showQuestion() {


        QuestionBean questionBean = questionBeanList.get(position);

        //根据可选状态显示或者隐藏答案,及按钮的显示文字
        if (contentQuestionAdapter.isCheck()) {//可选时，不显示

            fragmentContentQuestionBinding.cqTvAnalysis.setVisibility(View.GONE);
            if ((position + 1) == questionBeanList.size()) {//最后一页按钮显示提交

                fragmentContentQuestionBinding.cqTvNext.setText("提交");
            } else {
                fragmentContentQuestionBinding.cqTvNext.setText("下一页");
            }
            fragmentContentQuestionBinding.cqTvPre.setText("上一页");
        } else {

            fragmentContentQuestionBinding.cqTvAnalysis.setText("答案：" + questionBean.getAnswer());
            fragmentContentQuestionBinding.cqTvAnalysis.setVisibility(View.VISIBLE);
            fragmentContentQuestionBinding.cqTvNext.setText("下一页");
            if (position == 0) {

                fragmentContentQuestionBinding.cqTvPre.setText("重做");
            } else {

                fragmentContentQuestionBinding.cqTvPre.setText("上一页");
            }
        }

        //题目
        fragmentContentQuestionBinding.cqTvQuestion.setText((position + 1) + "." + questionBean.getQuestion());
        //选项
        List<String> optionList = new ArrayList<>();
        optionList.add(questionBean.getAnswer1());
        optionList.add(questionBean.getAnswer2());
        optionList.add(questionBean.getAnswer3());
        if (questionBean.getAnswer4() != null && questionBean.getAnswer4().equals("")) {

            optionList.add(questionBean.getAnswer4());
        }
        //用户选择的位置
        contentQuestionAdapter.setPostion(questionBean.getCheckPosition());

        contentQuestionAdapter.setNewData(optionList);
        //题号
        fragmentContentQuestionBinding.cqTvNum.setText((position + 1) + "/" + questionBeanList.size());
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
    public void updateTestRecordComplete(UpdateTestRecordBean updateTestRecordBean) {

        //更新数据库upload
        for (int i = 0; i < questionBeanList.size(); i++) {

            QuestionBean questionBean = questionBeanList.get(i);
            headlinesDataManager.updateUpload(1, questionBean.getBbcId(), questionBean.getIndexId(), questionBean.getCheckPosition());
        }
        contentQuestionAdapter.setCheck(false);
        position = 0;
        showQuestion();
    }

    /**
     * 更新数据
     */
    public void updateData() {


        showData();
    }
}