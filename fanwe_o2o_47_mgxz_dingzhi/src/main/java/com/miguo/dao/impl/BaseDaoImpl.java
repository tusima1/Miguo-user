package com.miguo.dao.impl;

import com.fanwe.app.App;
import com.miguo.view.BaseView;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public abstract class BaseDaoImpl {

    protected BaseView baseView;

    protected String tag = this.getClass().getSimpleName();

    public BaseDaoImpl(BaseView baseView){
        this.baseView = baseView;
    }

    public App getApp(){
        return App.getInstance();
    }


    public abstract BaseView getListener();

}
