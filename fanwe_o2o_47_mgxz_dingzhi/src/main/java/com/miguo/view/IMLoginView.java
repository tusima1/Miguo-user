package com.miguo.view;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/7.
 * IM登录回调接口
 */
public interface IMLoginView extends BaseView{
    void imLoginError(String message);
    void imLoginSuccess();
}
