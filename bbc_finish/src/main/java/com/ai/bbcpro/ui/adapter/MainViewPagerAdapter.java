package com.ai.bbcpro.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import java.util.ArrayList;
import java.util.List;

public class MainViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentList;
    private List<String> items;


    public MainViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, List<String> items) {
        super(fm);
        this.fragmentList = fragments;
        this.items = items;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return items.get(position);
    }
}
