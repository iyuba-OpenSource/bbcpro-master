package com.ai.bbcpro.mvp.presenter;


import com.ai.bbcpro.model.BaseModel;
import com.ai.bbcpro.mvp.view.BaseView;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public abstract class BasePresenter<V extends BaseView, M extends BaseModel> implements IBasePresenter<V> {

    protected V view;
    protected M model;
    protected CompositeDisposable compositeDisposable;

    public BasePresenter() {
        this.model = initModel();
    }

    protected abstract M initModel();

    public void attchView(V view) {

        WeakReference<V> viewWeakReference = new WeakReference<>(view);
        this.view = viewWeakReference.get();
    }


    public void detachView() {

        unSubscribe();
    }

    public void unSubscribe() {

        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
    }

    public void addSubscribe(Disposable disposable) {

        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }
}
