package com.ai.bbcpro.newSection;


import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;


/**
 * BaseFragment
 */
public class BaseFragment extends Fragment {
    protected Activity mActivity;
    protected Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        mContext = activity;
    }

    public void showShort(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showShort(@StringRes int id) {
        T.showShort(mContext, id);
    }

    public void showLong(Object info) {
        T.showShort(mContext, info.toString());
    }

    public void showLong(@StringRes int id) {
        T.showShort(mContext, id);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            Log.d("currentFragmentName", "currentFragmentName-------->>  " + getClass().getSimpleName());
        }
    }

}

