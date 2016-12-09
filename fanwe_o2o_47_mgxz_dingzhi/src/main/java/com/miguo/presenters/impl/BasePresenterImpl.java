package com.miguo.presenters.impl;

import com.fanwe.app.App;
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

    public App getApp(){
        return App.getInstance();
    }

    protected abstract void initModels();

    public abstract BaseView getListener();

}
