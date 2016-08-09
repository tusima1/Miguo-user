package com.miguo.live.presenters;

import com.tencent.qcloud.suixinbo.presenters.viewinface.MvpView;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface ShopAndProductView extends MvpView {

    /**
     * 取商店 详情。
     * @param shopId 门店ID
     */
    void getShopDetail(String shopId);
}
