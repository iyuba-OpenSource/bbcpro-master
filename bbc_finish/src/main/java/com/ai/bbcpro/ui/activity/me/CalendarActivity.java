package com.ai.bbcpro.ui.activity.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.ClockInLogBean;
import com.ai.bbcpro.mvp.presenter.CalendarPresenter;
import com.ai.bbcpro.mvp.view.CalendarContract;
import com.ai.bbcpro.MainApplication;
import com.othershe.calendarview.bean.DateBean;
import com.othershe.calendarview.listener.OnPagerChangeListener;
import com.othershe.calendarview.listener.OnSingleChooseListener;
import com.othershe.calendarview.utils.CalendarUtil;
import com.othershe.calendarview.weiget.CalendarView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * 打卡-打卡日历
 */
public class CalendarActivity extends AppCompatActivity implements CalendarContract.CalendarView {
    private static final String TAG = CalendarActivity.class.getSimpleName();

    private Context mContext;
    private TextView chooseDate;
    private CalendarView calendarView;
    private ProgressDialog mLoadingDialog;
    private int[] cDate = CalendarUtil.getCurrentDate();
    private String curTime = "";
    private int flag = 0;

    /**
     * 年和月
     */
    private TextView time_title;

    /**
     * 返回
     */
    private ImageView calendar_iv_back;

    /**
     * presneter
     */
    private CalendarPresenter calendarPresenter;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mContext = this;

        calendarPresenter = new CalendarPresenter();
        calendarPresenter.attchView(this);
        initView();

        userId = ConfigManager.Instance().loadString("userId", "");
    }


    private void initView() {

        chooseDate = findViewById(R.id.choose_date);
        calendarView = findViewById(R.id.calendar);
        time_title = findViewById(R.id.time_title);

        calendarView = findViewById(R.id.calendar);
        calendar_iv_back = findViewById(R.id.calendar_iv_back);
        calendar_iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        calendarView
                .setStartEndDate("2016.1", "2028.12")
                .setDisableStartEndDate("2016.10.10", "2028.10.10")
                .setInitDate(cDate[0] + "." + cDate[1])
                .setSingleDate(cDate[0] + "." + cDate[1] + "." + cDate[2])
                .init();
        time_title.setText(cDate[0] + "年" + cDate[1] + "月");
        chooseDate.setText("今天的日期：" + cDate[0] + "年" + cDate[1] + "月" + cDate[2] + "日");

        calendarView.setOnPagerChangeListener(new OnPagerChangeListener() {
            @Override
            public void onPagerChanged(int[] date) {
                if (date == null) {
                    Log.e(TAG, "onPagerChanged data is null? ");
                    return;
                }
                time_title.setText(date[0] + "年" + date[1] + "月");
                Log.e(TAG, "onPagerChanged flag " + flag);
            }
        });

        calendarView.setOnSingleChooseListener(new OnSingleChooseListener() {
            @Override
            public void onSingleChoose(View view, DateBean date) {
                time_title.setText(date.getSolar()[0] + "年" + date.getSolar()[1] + "月");
                if (date.getType() == 1) {
                    chooseDate.setText("今天的日期：" + date.getSolar()[0] + "年" + date.getSolar()[1] + "月" + date.getSolar()[2] + "日");
                }
            }
        });
        getCalendar(cDate);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (calendarPresenter != null) {

            calendarPresenter.detachView();
        }
    }

    private void getCalendar(int[] curDate) {

        if (curDate[1] < 10) {
            curTime = curDate[0] + "0" + curDate[1];
        } else {
            curTime = curDate[0] + "" + curDate[1];
        }

        calendarPresenter.getShareInfoShow(userId, Integer.parseInt(Constant.APPID), curTime);
    }

    public void showRankings(List<ClockInLogBean.RecordDTO> ranking) {


        HashMap<String, String> map = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        for (ClockInLogBean.RecordDTO record : ranking) {

            try {
                sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(record.getCreatetime());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                String item = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
                if (Integer.parseInt(record.getScan()) > 1) {
                    map.put(item, record.getScan());
                } else {
                    map.put(item, "true");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        calendarView.setClockInStatus(map);

        Iterator<String> iterator = map.keySet().iterator();
        Calendar calendar = Calendar.getInstance();
        String today = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
        boolean isToday = false;//展示的是否是当天
        while (iterator.hasNext()) {

            String dateStr = iterator.next();
            if (today.equals(dateStr)) {

                String initDate = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1);
                String SingleDate = calendar.get(Calendar.YEAR) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.DAY_OF_MONTH);
                calendarView
                        .setStartEndDate("2016.1", "2028.12")
                        .setDisableStartEndDate("2016.10.10", "2028.10.10")
                        .setInitDate(initDate)
                        .setSingleDate(SingleDate)
                        .init();
                isToday = true;
                break;
            }
        }

        if(!isToday){

            if (flag > 0) {
                calendarView.nextMonth();
            } else if (flag < 0) {
                calendarView.lastMonth();
            }
        }
    }

    public void lastMonth(View view) {
        flag = -1;
        showLoadingLayout();
        if (cDate[1] > 0) {
            cDate[1]--;
        } else {
            cDate[0]--;
            cDate[1] = 12;
        }

        getCalendar(cDate);
    }

    public void nextMonth(View view) {

        flag = 1;
        showLoadingLayout();
        if (cDate[1] < 12) {
            cDate[1]++;
        } else {
            cDate[0]++;
            cDate[1] = 1;
        }
        getCalendar(cDate);
    }

    public void showLoadingLayout() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new ProgressDialog(mContext);
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingLayout() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.dismiss();
            }
        }
    }

    public void showToast(int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getLogComplete(ClockInLogBean clockInLogBean) {

        dismissLoadingLayout();
        List<ClockInLogBean.RecordDTO> rankingList = clockInLogBean.getRecord();
        if (rankingList == null || rankingList.size() < 1) {

            showRankings(new ArrayList<>());
        } else {
            showRankings(rankingList);
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
}
