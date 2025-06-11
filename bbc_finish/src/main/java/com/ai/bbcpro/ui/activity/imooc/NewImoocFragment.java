package com.ai.bbcpro.ui.activity.imooc;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.ui.fragment.IMoocFragment;
import com.ai.bbcpro.ui.utils.ListUtils;
import com.iyuba.imooclib.ui.mobclass.MobClassFragment;

import java.util.ArrayList;

/**
 * 微课
 */
public class NewImoocFragment extends Fragment {

    public NewImoocFragment() {
        // Required empty public constructor
    }

    public static NewImoocFragment newInstance() {
        NewImoocFragment fragment = new NewImoocFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_imooc, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置fake_status_bar的高度
        View v = view.findViewById(R.id.fake_status_bar);
        v.getLayoutParams().height = Constant.STATUSBAR_HEIGHT;

        ArrayList<Integer> filter = ListUtils.getEnglishFilter();
        IMoocFragment iMoocFragment = IMoocFragment.newInstance(MobClassFragment.buildArguments(Integer.parseInt(Constant.courseTypeId), false,
                filter));

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ni_fl_fragment, iMoocFragment)
                .show(iMoocFragment)
                .commit();
    }
}