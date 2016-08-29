package com.miguo.live.views.view.model.impl;


import com.fanwe.app.App;
import com.fanwe.user.model.UserCurrentInfo;
import com.miguo.live.views.category.Category;
import com.miguo.live.views.view.BaseView;

/**
 * Created by mac on 16/3/14.
 */
public abstract class BaseModelImpl {

    protected BaseView baseView;

    protected String tag = this.getClass().getSimpleName();

    public BaseModelImpl(BaseView baseView){
        this.baseView = baseView;
    }

    public App getApp(){
        return App.getInstance();
    }

    public Category getCategory(){
        return (Category)baseView;
    }

    public UserCurrentInfo getCurrentUser(){
        return getApp().getmUserCurrentInfo();
    }

    public abstract BaseView getListener();

}
