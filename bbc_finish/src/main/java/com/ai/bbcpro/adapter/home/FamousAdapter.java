package com.ai.bbcpro.adapter.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ai.bbcpro.R;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.bumptech.glide.Glide;

import java.util.List;

public class FamousAdapter extends RecyclerView.Adapter<FamousAdapter.FamousViewHodler> {


    private int pos = -1;

    private List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList;

    private Callback callback;


    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }


    public List<FamousPersonBean.AnnouncerDataDTO> getAnnouncerDataDTOList() {
        return announcerDataDTOList;
    }

    public void setAnnouncerDataDTOList(List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList) {
        this.announcerDataDTOList = announcerDataDTOList;
    }

    public FamousAdapter(List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList) {
        this.announcerDataDTOList = announcerDataDTOList;
    }

    @NonNull
    @Override
    public FamousViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_famous_person, parent, false);
        return new FamousViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FamousViewHodler holder, int position) {

        FamousPersonBean.AnnouncerDataDTO announcerDataDTO = announcerDataDTOList.get(position);
        holder.setData(announcerDataDTO);
    }

    @Override
    public int getItemCount() {
        return announcerDataDTOList.size();
    }

    public class FamousViewHodler extends RecyclerView.ViewHolder {

        ImageView fp_iv_icon;
        TextView fp_tv_text;
        ImageView fp_iv_choose;
        FamousPersonBean.AnnouncerDataDTO announcerDataDTO;

        public FamousViewHodler(@NonNull View itemView) {
            super(itemView);

            fp_iv_icon = itemView.findViewById(R.id.fp_iv_icon);
            fp_tv_text = itemView.findViewById(R.id.fp_tv_text);
            fp_iv_choose = itemView.findViewById(R.id.fp_iv_choose);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (callback != null) {

                        callback.click(announcerDataDTO, getBindingAdapterPosition());
                    }
                }
            });
        }

        public void setData(FamousPersonBean.AnnouncerDataDTO announcerDataDTO) {

            this.announcerDataDTO = announcerDataDTO;

            if (announcerDataDTO.getSpeakerReal().equals("官方")) {

                Glide.with(fp_iv_icon.getContext()).load(R.drawable.bbe).into(fp_iv_icon);
            } else {

                String imageUrl = "http://iuserspeech.iyuba.cn:9001/voa/wav4/images/" + announcerDataDTO.getSpeaker() + "/" + announcerDataDTO.getSpeaker() + ".png";
                Glide.with(fp_iv_icon.getContext()).load(imageUrl).into(fp_iv_icon);
            }
            fp_tv_text.setText(announcerDataDTO.getSpeakerReal());

            if (getBindingAdapterPosition() == pos) {

                fp_iv_choose.setImageResource(R.drawable.vector_famous_choose);
            } else {

                fp_iv_choose.setImageResource(0);
            }
        }
    }

    public interface Callback {

        void click(FamousPersonBean.AnnouncerDataDTO announcerDataDTO, int pos);

    }
}
