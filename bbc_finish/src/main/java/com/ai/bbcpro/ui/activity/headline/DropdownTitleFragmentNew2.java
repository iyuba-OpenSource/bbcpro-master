package com.ai.bbcpro.ui.activity.headline;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.R;
import com.ai.bbcpro.Constant;
import com.ai.bbcpro.manager.ConfigManager;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.presenter.home.DropdownTitlePresenter;
import com.ai.bbcpro.mvp.view.DropdownTitleContract;
import com.iyuba.headlinelibrary.HeadlineType;
import com.iyuba.headlinelibrary.IHeadline;
import com.iyuba.headlinelibrary.ui.title.DropdownTitleFragmentNew;

/**
 * 扩展DropdownTitleFragmentNew。
 */
public class DropdownTitleFragmentNew2 extends Fragment implements DropdownTitleContract.DropdownTitleView {

    private DropdownTitlePresenter dropdownTitlePresenter;

    public DropdownTitleFragmentNew2() {
        // Required empty public constructor
    }

    public static DropdownTitleFragmentNew2 newInstance() {
        DropdownTitleFragmentNew2 fragment = new DropdownTitleFragmentNew2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dropdownTitlePresenter = new DropdownTitlePresenter();
        dropdownTitlePresenter.attchView(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (dropdownTitlePresenter != null) {

            dropdownTitlePresenter.detachView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dropdown_title_new2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String uid = ConfigManager.Instance().loadString("userId", "0");
        String vipStatus = ConfigManager.Instance().loadInt("isvip2", 0) + "";
        dropdownTitlePresenter.doCheckIPBBC(uid, Constant.APPID, Build.BRAND, vipStatus);

        //设置fake_status_bar的高度
        View v = view.findViewById(R.id.fake_status_bar);
        v.getLayoutParams().height = Constant.STATUSBAR_HEIGHT;
    }

    @Override
    public void doCheckIPBBC(CheckBBCBean checkBBCBean) {


        if (checkBBCBean == null) {//隐藏某些栏目

            Pair<Integer, Integer> p = Pair.create(R.array.category_bbc_name_bjhd, R.array.category_bbc_code_bihd);
            IHeadline.setCategoryArrayId(HeadlineType.BBCWORDVIDEO, p);
        } else {


            if (checkBBCBean.getFlag() == 1) {//控制显示

                Pair<Integer, Integer> p = Pair.create(R.array.category_bbc_name_bjhd, R.array.category_bbc_code_bihd);
                IHeadline.setCategoryArrayId(HeadlineType.BBCWORDVIDEO, p);
            } else {

            }
        }

        String[] types = new String[]{HeadlineType.BBCWORDVIDEO, HeadlineType.MEIYU,
                HeadlineType.SMALLVIDEO,
                HeadlineType.TED, HeadlineType.TOPVIDEOS, HeadlineType.VOAVIDEO};
        Bundle bundle = DropdownTitleFragmentNew.buildArguments(10, types, false);
        DropdownTitleFragmentNew dropdownTitleFragmentNew = DropdownTitleFragmentNew.newInstance(bundle);


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fdtn_fl_fragment, dropdownTitleFragmentNew)
                .show(dropdownTitleFragmentNew)
                .commit();
    }

    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void toast(String msg) {

        Toast.makeText(MainApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }
}