package com.ai.bbcpro.ui.fragment.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.adapter.home.FamousAdapter;
import com.ai.bbcpro.databinding.DialogFragmentFamousBinding;
import com.ai.bbcpro.model.bean.FamousPersonBean;
import com.ai.bbcpro.ui.vip.VipCenterActivity;

import java.util.List;

public class FamousDialogFragment extends DialogFragment {

    private DialogFragmentFamousBinding binding;

    private int famousPos = -1;

    private List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList;

    private Callback callback;

    public Callback getCallback() {
        return callback;
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public int getFamousPos() {
        return famousPos;
    }

    public void setFamousPos(int famousPos) {
        this.famousPos = famousPos;
    }

    public List<FamousPersonBean.AnnouncerDataDTO> getAnnouncerDataDTOList() {
        return announcerDataDTOList;
    }

    public void setAnnouncerDataDTOList(List<FamousPersonBean.AnnouncerDataDTO> announcerDataDTOList) {
        this.announcerDataDTOList = announcerDataDTOList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = DialogFragmentFamousBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FamousAdapter famousAdapter = new FamousAdapter(announcerDataDTOList);
        famousAdapter.setPos(famousPos);
        binding.flRvList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        binding.flRvList.setAdapter(famousAdapter);
        famousAdapter.setCallback(new FamousAdapter.Callback() {
            @Override
            public void click(FamousPersonBean.AnnouncerDataDTO announcerDataDTO, int pos) {


                if (famousAdapter.getPos() != pos) {
                    famousPos = pos;
                    famousAdapter.setPos(pos);
                    famousAdapter.notifyDataSetChanged();
                }
            }
        });
        //生成配音
        binding.flTvChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (callback != null) {

                    int pos = famousAdapter.getPos();
                    if (pos == -1) {

                        Toast.makeText(MainApplication.getApplication(), "请选择", Toast.LENGTH_SHORT).show();
                    } else {

                        FamousPersonBean.AnnouncerDataDTO announcerDataDTO = famousAdapter.getAnnouncerDataDTOList().get(pos);
                        callback.generate(pos, announcerDataDTO);
                    }
                }
            }
        });
        //生成个人音频
        binding.flTvPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(requireActivity())
                        .setTitle("提示")
                        .setMessage("请开通黄金会员后，联系客服QQ:445167605或2690080491获取个人配音。")
                        .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("去开通黄金会员", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                VipCenterActivity.start(MainApplication.getApplication(), true);
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    public interface Callback {

        void generate(int pos, FamousPersonBean.AnnouncerDataDTO announcerDataDTO);
    }
}
