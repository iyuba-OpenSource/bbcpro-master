package com.ai.bbcpro.ui.activity.me;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.ai.bbcpro.R;
import com.ai.bbcpro.databinding.ActivityMyCollectBinding;
import com.ai.bbcpro.ui.fragment.me.SentenceCollectFragment;
import com.iyuba.module.favor.ui.BasicFavorFragment;

/**
 * 我的收藏   新闻和句子
 */
public class MyCollectActivity extends AppCompatActivity {

    private ActivityMyCollectBinding binding;

    private BasicFavorFragment basicFavorFragment;
    private SentenceCollectFragment sentenceCollectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyCollectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFragment();

        initOperation();
    }

    private void initOperation() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.mycollect_fl_fragment, basicFavorFragment)
                .add(R.id.mycollect_fl_fragment, sentenceCollectFragment)
                .show(basicFavorFragment)
                .hide(sentenceCollectFragment)
                .commit();

        binding.mycollectTvText.setTextColor(getResources().getColor(R.color.colorPrimary));
        binding.mycollectTvSentence.setTextColor(getResources().getColor(android.R.color.black));

        //文章
        binding.mycollectTvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.mycollectTvText.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.mycollectTvSentence.setTextColor(getResources().getColor(android.R.color.black));

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .show(basicFavorFragment)
                        .hide(sentenceCollectFragment)
                        .commit();
            }
        });
        //句子
        binding.mycollectTvSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.mycollectTvSentence.setTextColor(getResources().getColor(R.color.colorPrimary));
                binding.mycollectTvText.setTextColor(getResources().getColor(android.R.color.black));

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction
                        .show(sentenceCollectFragment)
                        .hide(basicFavorFragment)
                        .commit();
            }
        });
        //toolbar
        binding.toolbar.toolbarTvTitle.setText("我的收藏");
        binding.toolbar.toolbarIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initFragment() {

        basicFavorFragment = BasicFavorFragment.newInstance();

        sentenceCollectFragment = SentenceCollectFragment.newInstance();
    }
}