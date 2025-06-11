package com.ai.bbcpro.ui.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.List;

public class TitlePagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private FragmentManager fm;
    List<String> mTypes;

    public TitlePagerAdapter(@NonNull FragmentManager fm, List<Fragment> list, List<String> titles) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
        this.list = list;
        this.mTypes = titles;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return mTypes.get(position);
    }

    public int getItemPosition(Object object) {
        return -2;
    }
}
