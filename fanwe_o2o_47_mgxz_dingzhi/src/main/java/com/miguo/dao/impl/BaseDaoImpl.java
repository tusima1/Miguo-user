package com.miguo.dao.impl;

import com.fanwe.app.App;
import com.miguo.view.BaseView;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public abstract class BaseDaoImpl {

    protected BaseView baseView;

    /**
     * 请求失败消息显示
     */
    protected String BASE_ERROR_MESSAGE = "加载失败，请刷新或重试";

    protected String tag = this.getClass().getSimpleName();

    public BaseDaoImpl(BaseView baseView){
        this.baseView = baseView;
    }

    public App getApp(){
        return App.getInstance();
    }

    public boolean isEmpty(Object object){
        return null == object;
    }

    public abstract BaseView getListener();

}
