package com.ai.bbcpro;

import android.os.Environment;

import com.ai.common.CommonConstant;

public class Constant {

    public static boolean sPreVerifySuccess;


    /**
     * 隐私政策
     */
    public static String PROTOCOLPRI;

    /**
     * 使用协议
     */
    public static String PROTOCOLUSE;

    public static final String CATEGORY_TYPE = "bbc";

    public static String courseTypeId = "9";
    //友盟
    public static String UMENGKEY = "5c75fd04f1f556685e000ba0";

    public static String APPName = "BBC";// 应用名称
    public static final String APP_DATA_PATH = Environment.getExternalStorageDirectory() + "/iyuba/" + APPName + "/";
    // mob
    public static final String WECHAT_APP_KEY = "wxcfa82a25eb43c928";
    public static final String WECHAT_APP_SECRET = "38eb9549a46868dc46d8f434e27a47d4";
    public static final String SINA_APP_KEY = "3522397949";
    public static final String SINA_APP_SECRET = "5d7bbc0dc4f5c22dba5f6033654563a2";
    public static int lastVersion = 20;
    //更新23：文本时间支持小数
    public static int currentVersion = 23;
    public static String mListen = "cet4";
    public static String word = "cet4word";
    public static String level = "4";
    public static final String DB_NAME_CET = "cet_13.sqlite";

    //oppo 应用宝  小米
    public static String AppName = "BBE英语";
    public static int AppIcon = R.drawable.bbe;
    public static String BaseUrl1 = CommonConstant.HTTP_SPEECH_ALL + "/api/protocolpri.jsp?apptype=";
    public static String BaseUrl2 = CommonConstant.HTTP_SPEECH_ALL + "/api/protocoluse666.jsp?apptype=";
    public static int Company = 3;
    public static final String PACKAGE_NAME = "com.ai.bbcpro";

    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;
    public static String APPID = "281";// 走遍美国确认为230 爱语吧id 219 华为应用市场21.10.25更为200

    public static String ADAPPID = "2811";
    public static String append = ".mp3";// 文件append

    //发短信
    public final static String SMSAPPID = "m34ea34920ee6a";
    public final static String SMSAPPSECRET = "f944a37bd66cd344764beb3b208f37ce";

    public static int textColor = 0xff2983c1;
    public static int textSize = 18;

    public static int mode;// 播放模式
    public static int type;// 听歌播放模式
    public static int download;// 是否下载

    /**
     * 权限
     */
    public final static String SP_PERMISSIONS = "PERMISSIONS";

    /**
     * 录音
     * 1 拒绝
     * 0 未申请过此权限
     */
    public final static String SP_KEY_RECORD = "RECORD";

    /**
     * 获取名人读音的列表
     */
    public static String URL_GET_BROADCAST_OPTIONS = CommonConstant.IUSERSPEECH_URL + "/voice/getBroadcastOptions";


    /**
     * 获取是否要隐藏新闻类型
     */
    public static String URL_DO_CHECK_IPBBC = CommonConstant.URL_APPS + "/minutes/doCheckIPBBC.jsp";
    /**
     * 搜索
     */
    public static String URL_SEARCH_BBC_API = CommonConstant.URL_APPS + "/minutes/searchBbcApi.jsp";
    /**
     * 获取热搜
     */
    public static String URL_RECOMMEND = CommonConstant.URL_APPS + "/voa/recommend.jsp";
    /**
     * 获取新闻的详情
     */
    public static String URL_TEXT_ALL_API = CommonConstant.URL_APPS + "/minutes/textAllApi.jsp";
    /**
     * 获取广告
     */
    public static String URL_GET_AD_ENTRY_ALL = CommonConstant.URL_DEV + "/getAdEntryAll.jsp";
    /**
     * 评测
     */
    public static String URL_EVAL = CommonConstant.IUSERSPEECH_URL + "/test/ai/";

    /**
     * 获取打卡记录
     */
    public static String URL_GET_SHARE_INFO_SHOW = CommonConstant.URL_APPS + "/getShareInfoShow.jsp";

    /**
     * 获取名人配音音频及句子
     */
    public static String URL_ANNOUNCER = CommonConstant.IUSERSPEECH_URL + "/voice/announcer";

    /**
     * 支付宝支付
     */
    public static String URL_ALIPAY = CommonConstant.URL_VIP + "/alipay.jsp";

    /**
     * 获取微信小程序的token
     * 根据userid获取用户数据
     */
    public static String URL_GET_WX_APPLET_TOKEN = CommonConstant.URL_API_COM_CN + "/v2/api.iyuba";

    /**
     * 获取单词信息
     */
    public static String URL_API_WORD = CommonConstant.URL_WORD + "/words/apiWord.jsp";


    /**
     * 收藏单词
     */
    public static String URL_UPDATE_WORD = CommonConstant.URL_WORD + "/words/updateWord.jsp";


    /**
     * 收藏或者取消收藏  句子和文章
     */
    public static String URL_UPDATE_COLLECT = CommonConstant.URL_APPS + "/iyuba/updateCollect.jsp";

    /**
     * 获取收藏的课本或者句子
     */
    public static String URL_COLLECT = CommonConstant.URL_CMS + "/dataapi/jsp/getCollect.jsp";


    /**
     * 上传阅读记录（学习）
     */
    public static String UPDATE_NEWS_STUDY_RECORD = CommonConstant.URL_DAXUE + "/ecollege/updateNewsStudyRecord.jsp";

    /**
     * 我的钱包记录
     */
    public static String GET_USER_ACTION_RECORD = CommonConstant.URL_API_CN + "/credits/getuseractionrecord.jsp";


    /**
     *练习题提交接口
     */
    public static String URL_UPDATE_TEST_RECORD_NEW = CommonConstant.URL_DAXUE + "/ecollege/updateTestRecordNew.jsp";

    /**
     *上传学习记录（语音）
     */
    public static String URL_UPDATE_STUDY_RECORD_NEW = CommonConstant.URL_DAXUE + "/ecollege/updateStudyRecordNew.jsp";

    /**
     * 获取打卡界面的信息
     */
    public static String URL_GET_MY_TIME = CommonConstant.URL_DAXUE + "/ecollege/getMyTime.jsp";

    /**
     * 获取学习排行榜 同听力排行
     */
    public static String URL_GET_STUDY_RANKING = CommonConstant.URL_DAXUE + "/ecollege/getStudyRanking.jsp";

    /**
     * 获取口语排行榜
     */
    public static String URL_GET_TOPIC_RANKING = CommonConstant.URL_DAXUE + "/ecollege/getTopicRanking.jsp";


    /**
     * 获取测试排行榜
     */
    public static String URL_GET_TEST_RANKING = CommonConstant.URL_DAXUE + "/ecollege/getTestRanking.jsp";



    /**
     * 状态栏的高度
     */
    public static int STATUSBAR_HEIGHT;


    /**
     * 阅读速度
     */
    public final static int NORMAL_WPM = 600;


    //广告
    public static final String AD_ADS1 = "ads1";//倍孜
    public static final String AD_ADS2 = "ads2";//创见
    public static final String AD_ADS3 = "ads3";//头条穿山甲
    public static final String AD_ADS4 = "ads4";//广点通优量汇
    public static final String AD_ADS5 = "ads5";//快手
}