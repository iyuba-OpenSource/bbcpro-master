package com.ai.bbcpro.ui.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.AccountManager;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.ui.helper.SyncDataHelper;
import com.ai.bbcpro.ui.http.bean.SumBean;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.bumptech.glide.Glide;
import com.iyuba.widget.rpb.RoundProgressBar;

import java.io.File;
import java.util.List;

public class NewHeadlineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SumBean.DataDTO> dataDTOList;
    private Context context;
    private ILoadMoreData iLoadMoreData;
    private DownloadNewsCallback downloadCallback;
    private boolean isRecommend;
    private SyncDataHelper syncDataHelper;
    private AccountManager accountManager;

    private Callback callback;

    public NewHeadlineAdapter(List<SumBean.DataDTO> dataDTOList, Context context, ILoadMoreData iLoadMoreData, boolean isRecommend) {
        this.dataDTOList = dataDTOList;
        this.context = context;
        this.iLoadMoreData = iLoadMoreData;
        this.isRecommend = isRecommend;
        accountManager = AccountManager.Instance(context);
    }


    public NewHeadlineAdapter(List<SumBean.DataDTO> dataDTOList, Context context, ILoadMoreData iLoadMoreData, DownloadNewsCallback downloadCallback) {
        this.dataDTOList = dataDTOList;
        this.context = context;
        this.iLoadMoreData = iLoadMoreData;
        this.downloadCallback = downloadCallback;
        syncDataHelper = SyncDataHelper.getInstance(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_designed_for_news, parent, false);
        return new TitleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        TitleHolder titleHolder = (TitleHolder) holder;
        SumBean.DataDTO dataDTO = dataDTOList.get(position);

        titleHolder.dealSumBean(dataDTO);
    }


    @Override
    public int getItemCount() {

        return dataDTOList.size();

    }

    /**
     * 获取某条数据
     *
     * @param position
     * @return
     */
    public SumBean.DataDTO getItem(int position) {

        if (position < dataDTOList.size()) {

            return dataDTOList.get(position);
        }
        return null;
    }


    public List<SumBean.DataDTO> getDataDTOList() {
        return dataDTOList;
    }

    public void setDataDTOList(List<SumBean.DataDTO> dataDTOList) {
        this.dataDTOList = dataDTOList;
    }

    public class TitleHolder extends RecyclerView.ViewHolder {


        TextView title_tv, count, time;
        public ImageView titleImage, download;
        View itemLayout;
        ProgressBar progressBar;
        //听力
        TextView dfn_tv_listen;
        RoundProgressBar dfn_rpb_listen;
        //练习题
        TextView dfn_tv_exercises;
        RoundProgressBar dfn_rpb_exercises;
        LinearLayout dfn_ll_exercises;
        //评测
        RoundProgressBar dfn_rpb_test;
        TextView dfn_tv_test;
        LinearLayout dfn_ll_test;

        RelativeLayout dfn_rl_statistics;
        RelativeLayout dfn_rl_bottom;

        TextView dfn_tv_ad;

        SumBean.DataDTO dataDTO;


        public TitleHolder(@NonNull View itemView) {
            super(itemView);

            titleImage = itemView.findViewById(R.id.iv_headlines);
            title_tv = itemView.findViewById(R.id.title_tv);
            count = itemView.findViewById(R.id.tv_view_count);
            time = itemView.findViewById(R.id.text_category);
            itemLayout = itemView.findViewById(R.id.linear_container);
            download = itemView.findViewById(R.id.download_item);
            progressBar = itemView.findViewById(R.id.download_progress);

            dfn_tv_listen = itemView.findViewById(R.id.dfn_tv_listen);
            dfn_rpb_listen = itemView.findViewById(R.id.dfn_rpb_listen);

            dfn_tv_exercises = itemView.findViewById(R.id.dfn_tv_exercises);
            dfn_rpb_exercises = itemView.findViewById(R.id.dfn_rpb_exercises);
            dfn_ll_exercises = itemView.findViewById(R.id.dfn_ll_exercises);

            dfn_rpb_test = itemView.findViewById(R.id.dfn_rpb_test);
            dfn_tv_test = itemView.findViewById(R.id.dfn_tv_test);
            dfn_ll_test = itemView.findViewById(R.id.dfn_ll_test);

            dfn_rl_statistics = itemView.findViewById(R.id.dfn_rl_statistics);
            dfn_rl_bottom = itemView.findViewById(R.id.dfn_rl_bottom);

            dfn_tv_ad = itemView.findViewById(R.id.dfn_tv_ad);

            download.setOnClickListener(view -> {

                int freeDownloadTime = ConfigManager.Instance().loadInt("freeDownloadTime", 0);
                if (freeDownloadTime < 5 || AccountManager.isVip()) {

                    ConfigManager.Instance().putInt("freeDownloadTime", ++freeDownloadTime);
                    dataDTO.download();
                } else {
                    ToastUtil.showToast(context, "非vip用户仅限下载5次");
                }
            });
        }


        public SumBean.DataDTO getDataDTO() {
            return dataDTO;
        }

        public void setDataDTO(SumBean.DataDTO dataDTO) {
            this.dataDTO = dataDTO;
        }

        private void dealSumBean(SumBean.DataDTO dataDTO) {

            this.dataDTO = dataDTO;

            dfn_rl_statistics.setVisibility(View.VISIBLE);
            dfn_rl_bottom.setVisibility(View.VISIBLE);
            dfn_tv_ad.setVisibility(View.GONE);
            title_tv.setText(dataDTO.title_cn);
            count.setText(dataDTO.readCount);
            time.setText(dataDTO.creatTime.substring(0, 10));
            Glide.with(MainApplication.getApplication()).load(dataDTO.pic).into(titleImage);

            //进度
            updateProgress(dataDTO);

            File file = new File(MainApplication.getApplication().getExternalFilesDir(Environment.DIRECTORY_MUSIC), dataDTO.getBbcId() + ".mp3");
            if (file.exists()) {

                download.setImageResource(R.drawable.download_finish);
            } else {

                download.setImageResource(R.drawable.download_before);
            }
            //下载进度
            if (dataDTO.getDownloadProgress() == 0) {

                progressBar.setVisibility(View.GONE);
            } else if (dataDTO.getDownloadProgress() == 100) {

                progressBar.setVisibility(View.GONE);
            } else {

                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(dataDTO.getDownloadProgress());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                iLoadMoreData.myOnClick(data.get(position).sound, data.get(position).bbcId, data.get(position).title_cn, data.get(position).pic);
//                iLoadMoreData.clickForPosition(position);

                    if (callback != null) {

                        callback.clickEssay(getBindingAdapterPosition());
                    }
                }
            });
            if (isRecommend) {
                download.setVisibility(View.GONE);
            }
        }


        /**
         * 更新数据
         *
         * @param dataDTO
         */
        public void updateProgress(SumBean.DataDTO dataDTO) {

            //听力
            dfn_rpb_listen.setMax(100);
            if (dataDTO.getEndFlag() == 1) {

                dfn_tv_listen.setText("100%");
                dfn_rpb_listen.setProgress(100);
                dfn_rpb_listen.setBackgroundResource(R.mipmap.icon_listen_blue);
            } else {

                int lp = (int) (100.0 * dataDTO.getTestNumber() / dataDTO.getTexts());
                dfn_tv_listen.setText(lp + "%");
                dfn_rpb_listen.setProgress(lp);
                if (lp == 0) {

                    dfn_rpb_listen.setBackgroundResource(R.mipmap.icon_listen);
                } else {

                    dfn_rpb_listen.setBackgroundResource(R.mipmap.icon_listen_blue);
                }
            }
            //练习题
            dfn_rpb_exercises.setMax(1);
            dfn_rpb_exercises.setProgress(dataDTO.getExercises());
            dfn_tv_exercises.setText(dataDTO.getExercises() + "/1");
            if (dataDTO.getExercises() == 0) {

                dfn_rpb_exercises.setBackgroundResource(R.mipmap.icon_exercises);
            } else {

                dfn_rpb_exercises.setBackgroundResource(R.mipmap.icon_exercises_blue);
            }
            dfn_ll_exercises.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (callback != null) {

                        callback.exercises(getBindingAdapterPosition());
                    }
                }
            });
            //评测
            dfn_rpb_test.setProgress(dataDTO.getEvalCount());
            dfn_rpb_test.setMax(dataDTO.getTexts());
            dfn_tv_test.setText(dataDTO.getEvalCount() + "/" + dataDTO.getTexts());
            if (dataDTO.getEvalCount() == 0) {

                dfn_rpb_test.setBackgroundResource(R.mipmap.icon_test);
            } else {

                dfn_rpb_test.setBackgroundResource(R.mipmap.icon_test_blue);
            }
            dfn_ll_test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (callback != null) {

                        callback.test(getBindingAdapterPosition());
                    }
                }
            });
        }

    }


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {

        void exercises(int position);

        void test(int position);

        //点击广告
        void clickAD(int position);

        /**
         * 点击文章
         *
         * @param position
         */
        void clickEssay(int position);
    }
}
