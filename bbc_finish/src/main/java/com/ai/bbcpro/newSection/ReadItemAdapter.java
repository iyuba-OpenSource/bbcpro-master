package com.ai.bbcpro.newSection;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.manager.RuntimeManager;
import com.ai.bbcpro.ui.utils.ToastUtil;
import com.ai.bbcpro.widget.HttpRequestFactory;
import com.ai.bbcpro.widget.SendGoodResponse;
import com.ai.bbcpro.widget.SpeakRankWork;
import com.ai.bbcpro.word.CetDBHelper;
import com.ai.common.CommonConstant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.lid.lib.LabelTextView;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author yq QQ:1032006226
 * @name cetListening-1
 * @class name：com.iyuba.cet4.activity.adapter
 * @class describe
 * @time 2019/1/23 14:17
 * @change
 * @chang time
 * @class describe
 */
public class ReadItemAdapter extends RecyclerView.Adapter<ReadItemAdapter.ViewHolder> {


    private Context context;
    private List<SpeakRankWork> speaks;

    private MediaPlayer mPlayer;
    private String userName;
    private String userImage;

    public ReadItemAdapter(String userImageUrl, String userName) {
        userImage = userImageUrl;
        mPlayer = new MediaPlayer();
        this.userName = userName;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup group, int i) {
        context = group.getContext();
        View view = LayoutInflater.from(group.getContext()).inflate(R.layout.item_read_rank_work, group, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        Glide.with(context).load(userImage).into(holder.image_user_head);
        holder.text_username.setText(userName);
        holder.text_score.setText(speaks.get(i).score + "");
        holder.text_time.setText(speaks.get(i).createdate);
        holder.text_downvote_count.setText(speaks.get(i).getDownvoteCount() + "");
        holder.text_upvote_count.setText(speaks.get(i).getUpvoteCount() + "");
        holder.item = speaks.get(i);
        holder.text_read_sentence.setText(getSentence(speaks.get(i)));
        holder.text_label_stub.setLabelTextSize(26);
        holder.text_label_stub.setLabelBackgroundColor(R.color.colorPrimary);
        holder.text_label_stub.setLabelText(holder.item.idindex + "");

        if (holder.item.shuoshuoType == 4) {
            holder.text_label_stub.setLabelBackgroundColor(Color.parseColor("#FBA474"));
            holder.text_label_stub.setLabelText("播音");
        } else {
            holder.text_label_stub.setLabelBackgroundColor(R.color.colorPrimary);
            holder.text_label_stub.setLabelText("跟读");
        }

    }

    @Override
    public int getItemCount() {
        return speaks.size();
    }

    public void setData(List<SpeakRankWork> list) {
        speaks = list;
    }

    private String getSentence(SpeakRankWork speakRankWork) {
        CetDBHelper helper = new CetDBHelper(RuntimeManager.getContext());

        if ((speakRankWork.idindex + "").contains("000")) {
            String a = speakRankWork.idindex.split("000")[0];
            String b = speakRankWork.idindex.split("000")[1];
            if (speakRankWork.paraid == 1) {
                return helper.getSentence(speakRankWork.voaId, a, b, "A");
            } else if (speakRankWork.paraid == 2) {
                return helper.getSentence(speakRankWork.voaId, a, b, "B");
            } else if (speakRankWork.paraid == 3) {
                return helper.getSentence(speakRankWork.voaId, a, b, "C");
            }
//            return  helper.getSentence(speakRankWork.voaId , a ,b, ACache.get(context).getAsString("testType"));
            return helper.getSentence(speakRankWork.voaId, a, b, "C");
        }
        return "";
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView image_user_head;
        TextView text_username;
        TextView text_time;
        LinearLayout linear_name_container;
        LabelTextView text_label_stub;
        ImageView image_body;
        TextView text_read_sentence;
        LinearLayout linear_audio_comment_container;
        TextView text_score;
        ImageView image_share;
        ImageView image_upvote;
        TextView text_upvote_count;
        ImageView image_downvote;
        TextView text_downvote_count;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            text_score = itemView.findViewById(R.id.text_score);
            image_share = itemView.findViewById(R.id.image_share);
            image_upvote = itemView.findViewById(R.id.image_upvote);
            text_upvote_count = itemView.findViewById(R.id.text_upvote_count);
            image_downvote = itemView.findViewById(R.id.image_downvote);
            text_downvote_count = itemView.findViewById(R.id.text_downvote_count);

            image_user_head = itemView.findViewById(R.id.image_user_head);
            text_username = itemView.findViewById(R.id.text_username);
            text_time = itemView.findViewById(R.id.text_time);
            linear_name_container = itemView.findViewById(R.id.linear_name_container);
            image_body = itemView.findViewById(R.id.image_body);
            text_read_sentence = itemView.findViewById(R.id.text_read_sentence);
            linear_audio_comment_container = itemView.findViewById(R.id.linear_audio_comment_container);

            image_body.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onMImageBodyClicked();
                }
            });
            text_read_sentence.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onMTextReadSentenceClicked();
                }
            });
            linear_audio_comment_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onMLinearAudioCommentContainerClicked();
                }
            });
            image_upvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onMImageUpvoteClicked();
                }
            });
            image_downvote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onMImageDownvoteClicked();
                }
            });
        }

        SpeakRankWork item;


        public void onMImageBodyClicked() {
            playUrl(item.getShuoShuoUrl());
        }

        public void onMTextReadSentenceClicked() {
        }

        public void onMLinearAudioCommentContainerClicked() {
        }

        public void onMTextScoreClicked() {
        }

        public void onMImageShareClicked() {
        }

        public void onMImageUpvoteClicked() {
            Call<SendGoodResponse> call = HttpRequestFactory.getPublishApi().sendGood(item.id, "61001");
            call.enqueue(new Callback<SendGoodResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<SendGoodResponse> call, Response<SendGoodResponse> response) {
                    if (response.body().getResultCode().equals("001")) {
                        ToastUtil.showToast(context, "点赞成功");
                        text_upvote_count.setText(String.valueOf(item.getUpvoteCount() + 1));

                    }
                }

                @Override
                public void onFailure(Call<SendGoodResponse> call, Throwable t) {

                }
            });
        }

        public void onMImageDownvoteClicked() {
            Call<SendGoodResponse> call = HttpRequestFactory.getPublishApi().sendGood(item.id, "61002");
            call.enqueue(new Callback<SendGoodResponse>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(Call<SendGoodResponse> call, Response<SendGoodResponse> response) {
                    if (response.body().getResultCode().equals("001")) {
                        text_downvote_count.setText(String.valueOf(item.getDownvoteCount() + 1));

                    }
                }

                @Override
                public void onFailure(Call<SendGoodResponse> call, Throwable t) {

                }
            });
        }


    }

    private void playUrl(String url) {
        mPlayer.reset();
        try {
            mPlayer.setDataSource(url.replace("iyuba.com", CommonConstant.domain));
            mPlayer.prepareAsync();
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void releasePlayer() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}

