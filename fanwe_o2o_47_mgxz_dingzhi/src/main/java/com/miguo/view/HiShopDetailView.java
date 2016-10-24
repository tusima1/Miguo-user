package com.miguo.view;

import com.miguo.entity.HiShopDetailBean;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface HiShopDetailView extends BaseView{

    void getShopDetailSuccess(HiShopDetailBean.Result result);
    void getShopDetailError();

}
