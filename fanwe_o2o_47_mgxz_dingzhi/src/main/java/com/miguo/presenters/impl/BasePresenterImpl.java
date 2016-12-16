package com.miguo.presenters.impl;

import android.widget.Toast;

import com.fanwe.app.App;
import com.miguo.app.HiBaseActivity;
import com.miguo.category.Category;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.view.BaseView;

/**
 * Created by zlh on 2016/12/5.
 */

public abstract class BasePresenterImpl {

    BaseView baseView;

    protected String tag = this.getClass().getSimpleName();

    public BasePresenterImpl(BaseView baseView) {
        this.baseView = baseView;
        initModels();
    }

    public HiBaseActivity getActivity(){
        if(baseView instanceof Category){
            return ((Category)baseView).getActivity();
        }
        return null;
    }

    public void showToast(String toast){
        MGToast.showToast(toast);
    }

    public App getApp(){
        return App.getInstance();
    }

    protected abstract void initModels();

    public abstract BaseView getListener();

}
