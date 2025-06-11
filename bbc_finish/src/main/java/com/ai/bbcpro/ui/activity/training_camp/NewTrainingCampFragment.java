package com.ai.bbcpro.ui.activity.training_camp;

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
import com.sd.iyu.training_camp.ui.TrainingCampFragment;

/**
 *
 */
public class NewTrainingCampFragment extends Fragment {

    public NewTrainingCampFragment() {
        // Required empty public constructor
    }

    public static NewTrainingCampFragment newInstance() {
        NewTrainingCampFragment fragment = new NewTrainingCampFragment();
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
        return inflater.inflate(R.layout.fragment_training_camp_new, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //设置fake_status_bar的高度
        View v = view.findViewById(R.id.fake_status_bar);
        v.getLayoutParams().height = Constant.STATUSBAR_HEIGHT;

        TrainingCampFragment trainingCampFragment = TrainingCampFragment.newInstance(false);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tc_fl_fragment, trainingCampFragment)
                .show(trainingCampFragment)
                .commit();
    }
}