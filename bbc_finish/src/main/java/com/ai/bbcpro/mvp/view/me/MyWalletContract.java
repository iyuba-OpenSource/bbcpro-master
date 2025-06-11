package com.ai.bbcpro.mvp.view.me;



import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.model.bean.me.RewardBean;
import com.ai.bbcpro.mvp.presenter.IBasePresenter;
import com.ai.bbcpro.mvp.view.LoadingView;

import java.util.List;

import io.reactivex.disposables.Disposable;

public interface MyWalletContract {

    interface MyWalletView extends LoadingView {


        void wallet(int pages, List<RewardBean.DataDTO> dataDTOS);
    }

    interface MyWalletPresenter extends IBasePresenter<MyWalletView> {

        void getUserActionRecord(int uid, int pages, int pageCount, String sign);
    }


    interface MyWalletModel extends BaseModel {

        Disposable getUserActionRecord(int uid, int pages, int pageCount, String sign, WalletCallback walletCallback);
    }

    interface WalletCallback {

        void success(RewardBean rewardBean);

        void error(Exception e);
    }
}
