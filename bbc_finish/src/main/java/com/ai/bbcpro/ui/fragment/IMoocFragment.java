package com.ai.bbcpro.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.ui.utils.ListUtils;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;

import java.util.ArrayList;

public class IMoocFragment extends Fragment {

    private boolean visible = false;
    private boolean isLoaded;
    MobClassFragment fragment;

    public static IMoocFragment newInstance(Bundle args) {
        IMoocFragment fragment = new IMoocFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.empty, container, false);
        return view;
    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        visible = isVisibleToUser;
//        if (visible) {
//            if(!isLoaded){
//                getChildFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();
//                isLoaded = true ;
//            }else {
//                getChildFragmentManager().beginTransaction().show(fragment);
//            }
//        }
//        super.setUserVisibleHint(isVisibleToUser);
//    }

    private MobClassFragment buildMobFragment() {
        ArrayList<Integer> filter = ListUtils.getEnglishFilter();
        Bundle args = MobClassFragment.buildArguments(Integer.parseInt(Constant.courseTypeId), false,
                filter);
        
        return MobClassFragment.newInstance(args);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragment = buildMobFragment();
        getChildFragmentManager().beginTransaction().add(R.id.frame_container, fragment).commit();


    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
