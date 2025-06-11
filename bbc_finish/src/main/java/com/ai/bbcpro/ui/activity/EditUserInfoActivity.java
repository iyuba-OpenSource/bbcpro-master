package com.ai.bbcpro.ui.activity;


/**
 * 编辑个人信息界面
 *
 * @author chentong
 * @version 1.0
 */

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ai.bbcpro.R;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.CustomToast;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.http.BaseHttpResponse;
import com.ai.bbcpro.http.ExeProtocol;
import com.ai.bbcpro.http.ProtocolResponse;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.setting.SettingConfig;
import com.ai.bbcpro.sqlite.bean.UserInfo;
import com.ai.bbcpro.ui.BasisActivity;
import com.ai.bbcpro.ui.event.SecVerifyLoginSuccessEvent;
import com.ai.bbcpro.ui.register.EditUserInfo;
import com.ai.bbcpro.ui.register.GitHubImageLoader;
import com.ai.bbcpro.ui.register.JudgeZodicaAndConstellation;
import com.ai.bbcpro.ui.register.LocationRequest;
import com.ai.bbcpro.ui.register.LocationResponse;
import com.ai.bbcpro.ui.register.ModifyUserNameRequest;
import com.ai.bbcpro.ui.register.ModifyUserNameResponse;
import com.ai.bbcpro.ui.register.RequestEditUserInfo;
import com.ai.bbcpro.ui.register.RequestUserDetailInfo;
import com.ai.bbcpro.ui.register.ResponseEditUserInfo;
import com.ai.bbcpro.ui.register.ResponseUserDetailInfo;
import com.ai.bbcpro.ui.register.School;
import com.ai.bbcpro.ui.register.SchoolListAdapter;
import com.ai.bbcpro.ui.register.SchoolOp;
import com.ai.bbcpro.ui.register.UpLoadImageActivity;
import com.ai.bbcpro.util.GetLocation;
import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class EditUserInfoActivity extends BasisActivity {
    private Context mContext;
    private TextView gender, birthday, zodiac, constellation, tourname, school;
    private EditText location;
    private LinearLayout changeImageLayout;
    private ImageView userImage;
    private Button back, save;
    private final static int GENDER_DIALOG = 1;// 性别选择
    private final static int DATE_DIALOG = 2;// 日期选择
    private final static int SCHOOL_DIALOG = 3;// 学校选择
    private Calendar calendar = null;
    private EditUserInfo editUserInfo = new EditUserInfo();
    private CustomDialog waitingDialog;
    private String cityName;
    // school
    private View schoolDialog;
    private EditText searchText;
    private Button sure;
    private View clear;
    private ListView schoolList;
    private ArrayList<School> schools = new ArrayList<School>();
    private SchoolListAdapter schoolListAdapter;
    private StringBuffer tempSchool;
    private EditText editName;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edituserinfo);
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary), 0);
        mContext = this;
//        CrashApplication.getInstance().addActivity(this);
        waitingDialog = WaittingDialog.showDialog(mContext);
        initWidget();
        LoadInfo();
        setListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewChange();
    }

    private void viewChange() {

        String userName = ConfigManager.Instance().loadString("userName", "");
        if (userName != null) {
            editName.setVisibility(View.VISIBLE);
            editName.setText(userName);
            tourname.setVisibility(View.GONE);
        } else {
            tourname.setVisibility(View.VISIBLE);
            editName.setVisibility(View.GONE);
            tourname.setText(AccountManager.Instance(this).getUserId());
        }
    }

    private void initWidget() {
        // TODO Auto-generated method stub
        tourname = (TextView) findViewById(R.id.tourName);
        editName = (EditText) findViewById(R.id.editName);
        userImage = (ImageView) findViewById(R.id.iveditPortrait);
        gender = (TextView) findViewById(R.id.editGender);
        birthday = (TextView) findViewById(R.id.editBirthday);
        location = (EditText) findViewById(R.id.editResideLocation);
        zodiac = (TextView) findViewById(R.id.editZodiac);
        constellation = (TextView) findViewById(R.id.editConstellation);
        changeImageLayout = (LinearLayout) findViewById(R.id.editPortrait);
        back = (Button) findViewById(R.id.button_back);
        save = (Button) findViewById(R.id.editinfo_save_btn);
        school = (TextView) findViewById(R.id.editSchool);

    }

    private void setText() {
        // TODO Auto-generated method stub
        if (editUserInfo.getEdGender() != null) {
            if (editUserInfo.getEdGender().equals("1")) {
                gender.setText(getResources().getStringArray(R.array.gender)[0]);
            } else if (editUserInfo.getEdGender().equals("2")) {
                gender.setText(getResources().getStringArray(R.array.gender)[1]);
            }
        } else {
            gender.setText(getResources().getStringArray(R.array.gender)[0]);
        }
        birthday.setText(editUserInfo.getBirthday());
        zodiac.setText(editUserInfo.getEdZodiac());
        constellation.setText(editUserInfo.getEdConstellation());
        location.setText(editUserInfo.getEdResideCity());
        school.setText(editUserInfo.getUniversity());
        GitHubImageLoader.Instace(mContext).setCirclePic(
                AccountManager.Instance(mContext).userId, userImage);
        // editName.setText(AccountManager.Instance(mContext).userName);
    }

    private void LoadInfo() {
        // TODO Auto-generated method stub
        handler.sendEmptyMessage(0);
    }

    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    waitingDialog.show();
                    GetAddr();
                    handler.sendEmptyMessage(1);
                    break;
                case 1:
                    ExeProtocol.exe(
                            new RequestUserDetailInfo(AccountManager
                                    .Instance(mContext).getUserId()),
                            new ProtocolResponse() {

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ResponseUserDetailInfo responseUserDetailInfo = (ResponseUserDetailInfo) bhr;
                                    if (responseUserDetailInfo.result.equals("211")) {
                                        editUserInfo = responseUserDetailInfo.editUserInfo;
                                    }
                                    handler.sendEmptyMessage(2);
                                }

                                @Override
                                public void error() {
                                    handler.sendEmptyMessage(2);
                                    handler.sendEmptyMessage(6);
                                }
                            });
                    break;
                case 2:
                    waitingDialog.dismiss();
                    setText();
                    break;
                case 3:
                    save.setClickable(true);
                    CustomToast.showToast(mContext, R.string.person_info_success);
                    EventBus.getDefault().post(new SecVerifyLoginSuccessEvent());
                    setResult(RESULT_OK);
                    finish();
                    break;
                case 4:
                    save.setClickable(true);
                    CustomToast.showToast(mContext, R.string.person_info_fail);
                    break;
                case 44:
                    save.setClickable(true);
                    CustomToast.showToast(mContext, "修改失败，请将信息填写完成后再进行提交");
                    break;
                case 5:
                    setText();
                    break;
                case 6:
                    CustomToast.showToast(mContext, R.string.check_network);
                    break;
                case 7:
                    CustomToast.showToast(mContext, R.string.check_gps);
                    break;
                case 8:
                    schools = new SchoolOp(mContext).findDataByFuzzy(tempSchool
                            .toString());
                    schoolListAdapter = new SchoolListAdapter(mContext, schools);
                    schoolList.setAdapter(schoolListAdapter);
                    schoolListAdapter.notifyDataSetChanged();
                    break;
                case 9:
                    schools = new SchoolOp(mContext).findDataByFuzzy(tempSchool
                            .toString());
                    schoolListAdapter.setData(schools);
                    schoolListAdapter.notifyDataSetChanged();
                    break;
                case 10:
                    location.setText(cityName);
                    break;
                case 11:
                    waitingDialog.show();
                    String id = AccountManager.Instance(mContext).userId;
                    String userName2 = ConfigManager.Instance().loadString("userName", "");

                    ExeProtocol.exe(new ModifyUserNameRequest(id, userName2, userName),
                            new ProtocolResponse() {

//                                @Override
//                                public void finish(BaseHttpResponse bhr) {
//                                    // TODO Auto-generated method stub
//                                    ModifyUserNameResponse response = (ModifyUserNameResponse) bhr;
//                                    Log.e("result", response.result);
//                                    if (response.result.equals("121")) {
//                                        handler.sendEmptyMessage(12);
//                                    } else if (response.result.equals("000")) {
//                                        handler.sendEmptyMessage(13);
//                                    } else {
//                                        handler.sendEmptyMessage(14);
//                                    }
//                                }

                                @Override
                                public void finish(BaseHttpResponse bhr) {
                                    ModifyUserNameResponse response = (ModifyUserNameResponse) bhr;
                                    Log.e("result", response.result);
                                    if (response.result.equals("121")) {
                                        handler.sendEmptyMessage(12);
                                    } else if (response.result.equals("000")) {
                                        handler.sendEmptyMessage(13);
                                    } else {
                                        handler.sendEmptyMessage(14);
                                    }
                                }

                                @Override
                                public void error() {
                                }
                            });
                    break;
                case 12:
                    Toast.makeText(mContext, "修改成功！", Toast.LENGTH_SHORT).show();
//                    AccountManager.Instance(mContext).userName = userName;
                    ConfigManager.Instance().putString("userName", userName);
                    waitingDialog.dismiss();
                    break;
                case 13:
                    Toast.makeText(mContext, "该用户名已经被使用！", Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                    break;
                case 14:
                    Toast.makeText(mContext, "修改失败", Toast.LENGTH_SHORT).show();
                    waitingDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    private void setListener() {
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeImageLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (SettingConfig.Instance().getIstour()) {
                    CustomToast.showToast(EditUserInfoActivity.this, "临时用户不可以修改信息,请注册正式用户");
                } else {
                    Intent intent = new Intent(mContext, UpLoadImageActivity.class);
                    startActivity(intent);
                }

            }
        });

        gender.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialog(GENDER_DIALOG);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createDialog(DATE_DIALOG);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                save.setClickable(false);
                String city = location.getText().toString();
                city = city.trim();
                String value = "", key = "";
                StringBuffer sb = new StringBuffer("");
                int i;
                if (city.contains(" ")) {
                    String[] area = city.split(" ");
                    sb.append(editUserInfo.getEdGender()).append(",");
                    sb.append(editUserInfo.getEdBirthYear()).append(",");
                    sb.append(editUserInfo.getEdBirthMonth()).append(",");
                    sb.append(editUserInfo.getEdBirthDay()).append(",");
                    sb.append(editUserInfo.getEdConstellation()).append(",");
                    sb.append(editUserInfo.getEdZodiac()).append(",");
                    sb.append(school.getText()).append(",");
                    for (i = 0; i < area.length; i++) {
                        sb.append(area[i]).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                    value = sb.toString();
                    if (i == 3) {
                        key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity,residedist";
                    } else {
                        key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,resideprovince,residecity";
                    }
                } else {
                    sb.append(editUserInfo.getEdGender()).append(",");
                    sb.append(editUserInfo.getEdBirthYear()).append(",");
                    sb.append(editUserInfo.getEdBirthMonth()).append(",");
                    sb.append(editUserInfo.getEdBirthDay()).append(",");
                    sb.append(editUserInfo.getEdConstellation()).append(",");
                    sb.append(editUserInfo.getEdZodiac()).append(",");
                    sb.append(school.getText()).append(",");
                    sb.append(city);
                    value = sb.toString();
                    key = "gender,birthyear,birthmonth,birthday,constellation,zodiac,graduateschool,residecity";
                }
                ExeProtocol.exe(
                        new RequestEditUserInfo(AccountManager
                                .Instance(mContext).userId, key, value),
                        new ProtocolResponse() {

                            @Override
                            public void finish(BaseHttpResponse bhr) {
                                // TODO Auto-generated method stub
                                ResponseEditUserInfo responseEditUserInfo = (ResponseEditUserInfo) bhr;
                                Log.e("editUser", "finish: " + responseEditUserInfo.toString());
                                if (responseEditUserInfo.result.equals("221")) {
                                    handler.sendEmptyMessage(3);
                                } else if (responseEditUserInfo.result.equals("222")) {
                                    handler.sendEmptyMessage(44);
                                } else {
                                    handler.sendEmptyMessage(4);
                                }
                            }

                            @Override
                            public void error() {
                                // TODO Auto-generated method stub
                                handler.sendEmptyMessage(6);
                            }
                        });
                userName = editName.getText().toString().trim();
/*                if (userInfo != null && !userName.equals(userInfo.username)) {
                    if (userName.length() < 4) {
                        Toast.makeText(mContext, "您的昵称太短啦", Toast.LENGTH_SHORT).show();
                    } else {
                        handler.sendEmptyMessage(11);
                    }
                }*/

            }
        });
        school.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                createDialog(SCHOOL_DIALOG);
            }
        });

    }

    private void initSchoolDialog(final Dialog dialog) {
        searchText = (EditText) schoolDialog.findViewById(R.id.search_text);
        sure = (Button) schoolDialog.findViewById(R.id.search);
        clear = schoolDialog.findViewById(R.id.clear);
        schoolList = (ListView) schoolDialog.findViewById(R.id.school_list);
        searchText.requestFocus();
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                tempSchool = new StringBuffer("");
                int size = arg0.length();
                for (int i = 0; i < size; i++) {
                    tempSchool.append(arg0.charAt(i));
                    tempSchool.append('%');
                }
                handler.sendEmptyMessage(9);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                searchText.setText("");
                schools.clear();
                tempSchool = new StringBuffer("");
                handler.sendEmptyMessage(8);
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                school.setText(searchText.getText().toString());
                dialog.dismiss();
            }
        });
        schoolList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                school.setText(schools.get(arg2).school_name);
                dialog.dismiss();
            }
        });
        tempSchool = new StringBuffer("");
        handler.sendEmptyMessage(8);
    }

    private void GetAddr() {
        String locationPos[] = GetLocation.getInstance(mContext).getLocation();
        String latitude = locationPos[0];
        String longitude = locationPos[1];
        if (latitude.equals("0.0") && longitude.equals("0.0")) {
            handler.sendEmptyMessage(7);
        }
        ExeProtocol.exe(new LocationRequest(latitude, longitude),
                new ProtocolResponse() {

//                    @Override
//                    public void finish(BaseHttpResponse bhr) {
//                        // TODO Auto-generated method stub
//
//                        LocationResponse lcr = (LocationResponse) bhr;
//                        StringBuffer builder = new StringBuffer();
//                        builder.append(lcr.province).append(" ");
//                        builder.append(lcr.locality).append(" ");
//                        builder.append(lcr.subLocality);
//                        cityName = builder.toString();
//                        handler.sendEmptyMessage(10);
//                    }

                    @Override
                    public void finish(BaseHttpResponse bhr) {
                        // TODO Auto-generated method stub

                        LocationResponse lcr = (LocationResponse) bhr;
                        StringBuffer builder = new StringBuffer();
                        builder.append(lcr.province).append(" ");
                        builder.append(lcr.locality).append(" ");
                        builder.append(lcr.subLocality);
                        cityName = builder.toString();
                        handler.sendEmptyMessage(10);
                    }

                    @Override
                    public void error() {
                        // TODO Auto-generated method stub

                    }
                });
    }

    private void createDialog(int id) {
        Dialog dialog = null;
        Builder builder = new AlertDialog.Builder(mContext);
        switch (id) {
            case GENDER_DIALOG:
                builder.setTitle(R.string.person_info_gender);
                builder.setSingleChoiceItems(R.array.gender, 0,
                        new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stud
                                if (which == 1) {
                                    editUserInfo.setEdGender("2");
                                } else if (which == 0) {
                                    editUserInfo.setEdGender("1");
                                }
                                handler.sendEmptyMessage(5);
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(R.string.alert_btn_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        });
                dialog = builder.create();
                break;
            case DATE_DIALOG:
                calendar = Calendar.getInstance();
                dialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker dp, int year,
                                                  int month, int dayOfMonth) {
                                editUserInfo.setEdBirthDay(dayOfMonth);
                                editUserInfo.setEdBirthYear(year);
                                editUserInfo.setEdBirthMonth(month + 1);
                                Calendar cal = new GregorianCalendar(year, month,
                                        dayOfMonth);
                                String constellation = JudgeZodicaAndConstellation
                                        .date2Constellation(cal);
                                String zodiac = JudgeZodicaAndConstellation
                                        .date2Zodica(cal);
                                editUserInfo.setEdZodiac(zodiac);
                                editUserInfo.setEdConstellation(constellation);
                                editUserInfo.setBirthday(year + "-" + (month + 1)
                                        + "-" + dayOfMonth);
                                handler.sendEmptyMessage(5);
                            }
                        }, calendar.get(Calendar.YEAR), // 传入年份
                        calendar.get(Calendar.MONTH), // 传入月份
                        calendar.get(Calendar.DAY_OF_MONTH) // 传入天数
                );
                dialog.setTitle(R.string.person_info_birth);
                break;
            case SCHOOL_DIALOG:
                builder.setTitle(R.string.person_info_school);
                LayoutInflater vi = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                schoolDialog = vi.inflate(R.layout.school_dialog, null);
                builder.setView(schoolDialog);
                builder.setNegativeButton(R.string.alert_btn_cancel,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                            }
                        });
                dialog = builder.create();
                dialog.setOnDismissListener(new OnDismissListener() {

                    @Override
                    public void onDismiss(DialogInterface arg0) {
                        // TODO Auto-generated method stub
                        schoolListAdapter = null;
                    }
                });
                initSchoolDialog(dialog);
                break;
            default:
                break;
        }
        dialog.show();
    }

}

