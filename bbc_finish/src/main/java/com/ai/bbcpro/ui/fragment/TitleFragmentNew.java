package com.ai.bbcpro.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.BuildConfig;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.dialog.CustomDialog;
import com.ai.bbcpro.dialog.WaittingDialog;
import com.ai.bbcpro.entity.DownloadRefresh;
import com.ai.bbcpro.entity.OpenTextEventbus;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.AdEntryBean;
import com.ai.bbcpro.model.bean.QuestionBean;
import com.ai.bbcpro.mvp.presenter.TitlePresenter;
import com.ai.bbcpro.mvp.view.TitleContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.adapter.DownloadNewsCallback;
import com.ai.bbcpro.ui.adapter.ILoadMoreData;
import com.ai.bbcpro.ui.adapter.NewHeadlineAdapter;
import com.ai.bbcpro.db.HeadlinesDataManager;
import com.ai.bbcpro.ui.event.RefreshListEvent;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.http.net.ApiService;
import com.ai.bbcpro.ui.http.net.ContentService;
import com.ai.bbcpro.ui.http.thread.DownloadImageThread;
import com.ai.bbcpro.ui.player.AudioContentActivity;
import com.iyuba.sdk.data.iyu.IyuNative;
import com.iyuba.sdk.data.ydsdk.YDSDKTemplateNative;
import com.iyuba.sdk.data.youdao.YDNative;
import com.iyuba.sdk.mixnative.MixAdRenderer;
import com.iyuba.sdk.mixnative.MixNative;
import com.iyuba.sdk.mixnative.MixViewBinder;
import com.iyuba.sdk.mixnative.PositionLoadWay;
import com.iyuba.sdk.mixnative.StreamType;
import com.iyuba.sdk.nativeads.NativeAdPositioning;
import com.iyuba.sdk.nativeads.NativeEventListener;
import com.iyuba.sdk.nativeads.NativeRecyclerAdapter;
import com.youdao.sdk.nativeads.RequestParameters;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 首页fragment-TitleFragment
 */
public class TitleFragmentNew extends Fragment implements ILoadMoreData
        , BGARefreshLayout.BGARefreshLayoutDelegate, DownloadNewsCallback, TitleContract.TitleView {

    private Context mContext;
    private int parentID;
    private int page = 1;
    private boolean isNeedRefresh;
    RecyclerView recyclerView;
    NewHeadlineAdapter newHeadlineAdapter;
    String TAG = "TitleFragmentNew";
    BGARefreshLayout refreshLayout;
    int count = 10;
    private HeadlinesDataManager dataManager;
    private CustomDialog waittingDialog;

    private TitlePresenter titlePresenter;


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case 1:
                    waittingDialog.show();
                    break;
                case 2:
                    waittingDialog.dismiss();
                    break;
            }
            return false;
        }
    });

    public static Bundle buildArguments(int pID) {
        Bundle bundle = new Bundle();
        bundle.putInt("parentID", pID);
        return bundle;
    }

    public static TitleFragmentNew newInstance(Bundle bundle) {
        TitleFragmentNew newFragment = new TitleFragmentNew();
        newFragment.setArguments(bundle);
        return newFragment;
    }

    public TitleFragmentNew() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            parentID = bundle.getInt("parentID");
        }

        dataManager = HeadlinesDataManager.getInstance(mContext);
        waittingDialog = WaittingDialog.showDialog(mContext);
        EventBus.getDefault().register(TitleFragmentNew.this);
        titlePresenter = new TitlePresenter();
        titlePresenter.attchView(this);
    }

/*    private void initYouDao() {


        int vipStatus = ConfigManager.Instance().loadInt("isvip2", 0);
        if (BuildConfig.examineTime < System.currentTimeMillis() && vipStatus == 0 && YoudaoSDK.hasInit()) {

            if (youDaoMultiNative == null) {

                youDaoMultiNative = new YouDaoMultiNative(requireActivity(), "3438bae206978fec8995b280c49dae1e", this);
            }
            RequestParameters mRequestParameters = new RequestParameters.RequestParametersBuilder()
                    .keywords("bbc")
                    .keywords("英语")
                    .keywords("雅思听力")
                    .keywords("雅思")
                    .keywords("托业")
                    .build();

            youDaoMultiNative.makeRequest(mRequestParameters, 9);
        }
    }*/


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        page = 1;
        List<SumBean.DataDTO> mList;
        if (parentID == 0) {
            mList = dataManager.loadHeadlines(page, count);
        } else {
            mList = dataManager.loadHeadlinesType(parentID, page, count);
        }

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setDelegate(TitleFragmentNew.this);
        BGARefreshViewHolder mBGARefreshViewHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        refreshLayout.setRefreshViewHolder(mBGARefreshViewHolder);
        recyclerView = view.findViewById(R.id.recyclerView);

        setNewHeadlineAdapter(mList);
        refreshTopNews();
    }

    private void setNewHeadlineAdapter(List<SumBean.DataDTO> mList) {

        newHeadlineAdapter = new NewHeadlineAdapter(mList, mContext, this, this);
        recyclerView.setAdapter(newHeadlineAdapter);
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        newHeadlineAdapter.setCallback(new NewHeadlineAdapter.Callback() {
            @Override
            public void exercises(int position) {

                jumpDetails(position, 4);
            }

            @Override
            public void test(int position) {

                jumpDetails(position, 2);
            }

            @Override
            public void clickAD(int position) {

                SumBean.DataDTO dataDTO = newHeadlineAdapter.getItem(position);
                if (dataDTO.getNativeResponse() != null) {

                    RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(position);
                    dataDTO.getNativeResponse().handleClick(holder.itemView);
                }
            }

            @Override
            public void clickEssay(int position) {

                NewHeadlineAdapter.TitleHolder titleHolder = (NewHeadlineAdapter.TitleHolder) recyclerView.findViewHolderForAdapterPosition(position);
                SumBean.DataDTO dto = titleHolder.getDataDTO();
                if (dto == null) {

                    return;
                }
                String bbcid = dto.getBbcId();

                long count = dataManager.getQuestionCountById(Integer.parseInt(bbcid));
                if (dataManager.loadDetail(Integer.parseInt(bbcid)).isEmpty() || count == 0) {

                    mHandler.sendEmptyMessage(1);
                    getDetailData(dto, bbcid, true, 0);
                } else {

                    AudioContentActivity.startActivity(requireActivity(), dto, parentID, 0);
                }
            }
        });
    }

    /**
     * 跳转到详情页面
     *
     * @param position
     * @param vPage    显示第几个fragment
     */
    private void jumpDetails(int position, int vPage) {


        if (position < newHeadlineAdapter.getDataDTOList().size()) {

            SumBean.DataDTO dto = newHeadlineAdapter.getDataDTOList().get(position);
            String bbcid = dto.getBbcId();

            long count = dataManager.getQuestionCountById(Integer.parseInt(bbcid));
            if (dataManager.loadDetail(Integer.parseInt(bbcid)).isEmpty() || count == 0) {

                mHandler.sendEmptyMessage(1);
                getDetailData(dto, bbcid, true, vPage);
            } else {

                AudioContentActivity.startActivity(requireActivity(), dto, parentID, vPage);
            }
        }
    }

    private void getAd() {


        //请求广告
        String userId = ConfigManager.Instance().loadString("userId", "");
        if (userId.trim().equals("")) {
            userId = "0";
        }
        int vipStatus = ConfigManager.Instance().loadInt("isvip2", 0);
        if (vipStatus == 0) {

            titlePresenter.getAdEntryAll(Constant.ADAPPID, 2, userId);
            isNeedRefresh = false;
        }
    }

    @Override
    public void loadMoreData() {

    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshUIEvent(RefreshListEvent event) {
        String message = event.getMessage();
        isNeedRefresh = true;
    }

    /**
     * 更新下载进度
     *
     * @param downloadRefresh
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadRefresh downloadRefresh) {

        String bbcid = downloadRefresh.getBbcid();
        List<SumBean.DataDTO> dataDTOList = newHeadlineAdapter.getDataDTOList();
        for (int i = 0; i < dataDTOList.size(); i++) {

            SumBean.DataDTO dataDTO = dataDTOList.get(i);
            if (dataDTO.getBbcId().equals(bbcid)) {

                newHeadlineAdapter.notifyItemChanged(i);
                break;
            }
        }
    }

    /**
     * 新闻下载的点击事件，防止没有数据的情况
     *
     * @param openTextEventbus
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OpenTextEventbus openTextEventbus) {

        String bbcidStr = openTextEventbus.getBbcid();

        SumBean.DataDTO dataDTO = dataManager.getHeadlineForBbcid(bbcidStr);
        if (dataDTO == null) {

            return;
        }

        List<SumBean.DataDTO> dataDTOList = new ArrayList<>();
        dataDTOList.add(dataDTO);

        long count = dataManager.getQuestionCountById(Integer.parseInt(bbcidStr));
        if (dataManager.loadDetail(Integer.parseInt(bbcidStr)).isEmpty() || count == 0) {

            mHandler.sendEmptyMessage(1);
            getDetailData(dataDTO, bbcidStr, true, 0);
        } else {

            AudioContentActivity.startActivity(requireActivity(), dataDTO, parentID, 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (titlePresenter != null) {

            titlePresenter.detachView();
        }
        EventBus.getDefault().unregister(TitleFragmentNew.this);
    }

    @Override
    public void myOnClick(String sound, String bbcid, String title, String imageUrl) {

    }

    @Override
    public void clickForPosition(int position) {


    }


    public Call<BBCContentBean> initDetailHttp(String bbcid) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ContentService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ContentService contentService = retrofit.create(ContentService.class);
        return contentService.post("json", bbcid);
    }

    public void getDetailData(SumBean.DataDTO dto, String bbcid, boolean isSkip, int vPage) {

        Observable.create((ObservableOnSubscribe<BBCContentBean>) emitter -> {

                    Response<BBCContentBean> execute = initDetailHttp(bbcid).execute();
                    BBCContentBean body = execute.body();
                    emitter.onNext(body);
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BBCContentBean>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {


                    }

                    @Override
                    public void onNext(@NonNull BBCContentBean dataBean) {

                        dataManager.saveDetail(dataBean.getData());

                        //存储问题到数据库
                        List<QuestionBean> questionBeanList = dataBean.getDataQuestion();
                        for (int i = 0; i < questionBeanList.size(); i++) {

                            QuestionBean questionBean = questionBeanList.get(i);
                            long count = dataManager.hasQuestionBean(questionBean.getBbcId(), questionBean.getIndexId());
                            if (count > 0) {//有数据

                                dataManager.updateQuestion(questionBean);
                            } else {

                                dataManager.addQuestion(questionBean);
                            }
                        }

                        if (isSkip) {
                            mHandler.sendEmptyMessage(2);

                            AudioContentActivity.startActivity(requireActivity(), dto, parentID, vPage);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                        mHandler.sendEmptyMessage(2);
                        toast("请求超时");
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    public Call<SumBean> initHttp(int parentID, int page) {
        int pageNum = 10;
        pageNum = page * 10;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiService.BASE_URL).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
        ApiService apiService = retrofit.create(ApiService.class);
        return apiService.post("android", "json", Integer.parseInt(Constant.APPID), 0, page, 10, parentID);
    }

    public void getData(int parentID, int page) {
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws IOException {
                Response<SumBean> execute = initHttp(parentID, page).execute();
                SumBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean SumBean) {

                if (SumBean.getData() != null) {

                    List<SumBean.DataDTO> netData = SumBean.data;
                    dataManager.saveHeadlines(netData);

                    newHeadlineAdapter.getDataDTOList().addAll(netData);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        refreshTopNews();
    }

    /**
     * 获取新闻列表
     */
    private void refreshTopNews() {
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws Exception {
                emitter.onNext(initHttp(parentID, 1).execute().body());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean sumBean) {

                List<SumBean.DataDTO> netData = sumBean.data;
                dataManager.saveHeadlines(netData);

                //更新TestNumber、更新做题数量
                List<SumBean.DataDTO> dataDTOList = sumBean.data;
                for (int i = 0; i < dataDTOList.size(); i++) {

                    SumBean.DataDTO dataDTO = dataDTOList.get(i);
                    int bbcid = Integer.parseInt(dataDTO.getBbcId());
                    int TestNumber = dataManager.getTestNumber(bbcid);
                    dataDTO.setTestNumber(TestNumber);
                    long exercises = dataManager.getExercises(bbcid);
                    dataDTO.setExercises((int) exercises);
                    long evalCount = dataManager.getEvalCount(bbcid);
                    dataDTO.setEvalCount((int) evalCount);
                }


//                adapter.getData().clear();
//                adapter.getData().addAll(dataDTOList);
//                mStrategyCode = -1;
                setNewHeadlineAdapter(dataDTOList);
//                adapter.notifyDataSetChanged();

                getAd();
                if (refreshLayout != null) {
                    refreshLayout.endRefreshing();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @SuppressLint({"StaticFieldLeak", "NotifyDataSetChanged"})
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {

        page++;
        Observable.create(new ObservableOnSubscribe<SumBean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<SumBean> emitter) throws IOException {
                Response<SumBean> execute = initHttp(parentID, page).execute();
                SumBean body = execute.body();
                emitter.onNext(body);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<SumBean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull SumBean sumBean) {

                if (sumBean.getData() != null) {

                    List<SumBean.DataDTO> netData = sumBean.data;

                    dataManager.saveHeadlines(netData);
                    newHeadlineAdapter.getDataDTOList().addAll(netData);
                    newHeadlineAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.e(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {
            }
        });
        return false;
    }

    @Override
    public void download(String bbcId, String imageUrl, String audioUrl, int position) {
        getDetailData(null, bbcId, false, 0);
//        currentPos = position;
        new DownloadImageThread(imageUrl, bbcId, mContext).start();
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
    public void getAdEntryAllComplete(AdEntryBean adEntryBean) {

        if (adEntryBean != null && adEntryBean.getData() != null) {

            setAdAdapter(adEntryBean.getData());
        }
    }


    /**
     * 设置
     *
     * @param dataBean
     */
    private void setAdAdapter(AdEntryBean.DataDTO dataBean) {

        OkHttpClient mClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.ICON_IMAGE,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);
        RequestParameters requestParameters = new RequestParameters.RequestParametersBuilder()
                .location(null)
                .keywords(null)
                .desiredAssets(desiredAssets)
                .build();
        YDNative ydNative = new YDNative(requireActivity(), "edbd2c39ce470cd72472c402cccfb586", requestParameters);

        IyuNative iyuNative = new IyuNative(requireActivity(), getString(R.string.appid), mClient);

        YDSDKTemplateNative csjTemplateNative = new YDSDKTemplateNative(requireActivity(), BuildConfig.TEMPLATE_SCREEN_AD_KEY_CSJ);
        YDSDKTemplateNative ylhTemplateNative = new YDSDKTemplateNative(requireActivity(), BuildConfig.TEMPLATE_SCREEN_AD_KEY_YLH);
//        YDSDKTemplateNative ksTemplateNative = new YDSDKTemplateNative(requireActivity(), BuildConfig.TEMPLATE_SCREEN_AD_KEY_KS);
        YDSDKTemplateNative bdTemplateNative = new YDSDKTemplateNative(requireActivity(), BuildConfig.TEMPLATE_SCREEN_AD_KEY_BD);

        //添加key
        HashMap<Integer, YDSDKTemplateNative> ydsdkMap = new HashMap<>();
        ydsdkMap.put(StreamType.TT, csjTemplateNative);
        ydsdkMap.put(StreamType.GDT, ylhTemplateNative);
//        ydsdkMap.put(StreamType.KS, ksTemplateNative);
        ydsdkMap.put(StreamType.BAIDU, bdTemplateNative);


        MixNative mixNative = new MixNative(ydNative, iyuNative, ydsdkMap);
        PositionLoadWay loadWay = new PositionLoadWay();
        loadWay.setStreamSource(new int[]{
                Integer.parseInt(dataBean.getFirstLevel()),
                Integer.parseInt(dataBean.getSecondLevel()),
                Integer.parseInt(dataBean.getThirdLevel())});
        mixNative.setLoadWay(loadWay);

        int startPosition = 3;
        int positionInterval = 5;
        NativeAdPositioning.ClientPositioning positioning = new NativeAdPositioning.ClientPositioning();
        positioning.addFixedPosition(startPosition);
        positioning.enableRepeatingPositions(positionInterval);
        NativeRecyclerAdapter mAdAdapter = new NativeRecyclerAdapter(requireActivity(), newHeadlineAdapter, positioning);
        mAdAdapter.setNativeEventListener(new NativeEventListener() {
            @Override
            public void onNativeImpression(View view, com.iyuba.sdk.nativeads.NativeResponse nativeResponse) {

            }

            @Override
            public void onNativeClick(View view, com.iyuba.sdk.nativeads.NativeResponse nativeResponse) {

            }
        });
        mAdAdapter.setAdSource(mixNative);


        mixNative.setYDSDKTemplateNativeClosedListener(new MixNative.YDSDKTemplateNativeClosedListener() {
            @Override
            public void onClosed(View view) {
                View itemView = (View) ((View) view.getParent()).getParent();
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(itemView);
                int position = viewHolder.getBindingAdapterPosition();
                mAdAdapter.removeAdsWithAdjustedPosition(position);
            }
        });

        MixViewBinder mixViewBinder = new MixViewBinder.Builder(R.layout.item_ad_mix)
                .templateContainerId(R.id.mix_fl_ad)
                .nativeContainerId(R.id.headline_ll_item)
                .nativeImageId(R.id.native_main_image)
                .nativeTitleId(R.id.native_title)
                .build();
        MixAdRenderer mixAdRenderer = new MixAdRenderer(mixViewBinder);
        mAdAdapter.registerAdRenderer(mixAdRenderer);
        recyclerView.setAdapter(mAdAdapter);
        mAdAdapter.loadAds();
    }

 /*    @Override
    public void onNativeLoad(NativeAds nativeAds) {

        Log.d("titlef", "onNativeLoad");
       List<NativeResponse> nativeResponseList = nativeAds.nativeResponses;

        List<SumBean.DataDTO> dataDTOList = newHeadlineAdapter.getData();
        int np = 0;
        for (int i = 0; i < dataDTOList.size(); ) {

            SumBean.DataDTO dataDTO = dataDTOList.get(i);
            if (dataDTO.getNativeResponse() == null) {

                SumBean.DataDTO dto = new SumBean.DataDTO();
                if (np >= nativeResponseList.size()) {
                    np = 0;
                }
                NativeResponse nativeResponse = nativeResponseList.get(np);
                dto.setNativeResponse(nativeResponse);
                np++;
                dataDTOList.add(i + 1, dto);
                i = i + 2;
            } else {

                i++;
            }
        }
        newHeadlineAdapter.notifyDataSetChanged();
    }
    */

/*    @Override
    public void onNativeFail(NativeErrorCode nativeErrorCode) {

        Log.d("titlef", "onNativeFail");
    }*/
}
