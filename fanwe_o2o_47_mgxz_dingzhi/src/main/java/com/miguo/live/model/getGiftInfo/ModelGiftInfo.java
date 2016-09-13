package com.miguo.live.model.getGiftInfo;

import java.util.List;

/**
 * Created by didik on 2016/9/12.
 */
public class ModelGiftInfo {
    private String userdiamond;
    private List<GiftListBean> giftList;

    public String getUserdiamond() {
        return userdiamond;
    }

    public void setUserdiamond(String userdiamond) {
        this.userdiamond = userdiamond;
    }
    public List<GiftListBean> getGiftList() {
        return giftList;
    }

    public void setGiftList(List<GiftListBean> giftList) {
        this.giftList = giftList;
    }

}
