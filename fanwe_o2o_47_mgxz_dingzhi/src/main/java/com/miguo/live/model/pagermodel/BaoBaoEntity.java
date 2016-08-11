package com.miguo.live.model.pagermodel;

import com.fanwe.seller.model.GoodsDetailInfo;

/**
 * Created by didik on 2016/8/8.
 * 镇店之宝
 */
public class BaoBaoEntity extends GoodsDetailInfo {
    private boolean isClicked=false;

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
