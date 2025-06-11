package com.ai.bbcpro.mvp.presenter;

import android.widget.Toast;

import com.ai.bbcpro.model.MainModel;
import com.ai.bbcpro.model.bean.home.CheckBBCBean;
import com.ai.bbcpro.mvp.view.MainContract;
import com.ai.bbcpro.MainApplication;
import com.ai.bbcpro.ui.http.bean.BBCContentBean;
import com.sd.iyu.training_camp.model.bean.CourseTitleBean;

import java.net.UnknownHostException;

import io.reactivex.disposables.Disposable;

public class MainPresenter extends BasePresenter<MainContract.MainView, MainContract.MainModel>
        implements MainContract.MainPresenter {


    @Override
    protected MainContract.MainModel initModel() {
        return new MainModel();
    }

    @Override
    public void textAllApi(String format, int bbcid, CourseTitleBean.DataDTO dataDTO) {

        Disposable disposable = model.textAllApi(format, bbcid, new MainContract.Callback() {
            @Override
            public void success(BBCContentBean bbcContentBean) {

                if (!bbcContentBean.getTotal().equals("0")) {

                    view.textAllApi(bbcContentBean, dataDTO);

                } else {

                    Toast.makeText(MainApplication.getApplication(), "暂无数据", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Exception e) {

                if (e instanceof UnknownHostException) {

                    view.toast("请求超时");
                }
            }
        });
        addSubscribe(disposable);
    }

    @Override
    public void doCheckIPBBC(String uid, String appid, String platform, String vip) {

        Disposable disposable = model.doCheckIPBBC(uid, appid, platform, vip, new MainContract.CheckIPCallback() {
            @Override
            public void success(CheckBBCBean checkBBCBean) {

                view.doCheckIPBBC(checkBBCBean);
            }

            @Override
            public void error(Exception e) {

                view.doCheckIPBBC(null);
            }
        });
        addSubscribe(disposable);
    }
}
