package com.miguo.presenters;

/**
 * Created by zlh on 2016/12/5.
 */

public interface TencentIMBindPresenter extends BasePresenter{
    /**
     * 登录成功后绑定IM获取sign
     */
    void tencentIMBinding();

    /**
     * 登录成功后绑定IM并且将本地购物车push到线上服务器
     */
    void tencentIMBindingWithPushLocalCart();
}
