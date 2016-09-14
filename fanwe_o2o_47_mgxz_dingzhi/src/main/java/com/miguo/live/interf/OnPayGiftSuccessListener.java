package com.miguo.live.interf;

import com.miguo.live.model.getGiftInfo.GiftListBean;

/**
 * Created by didik on 2016/9/14.
 */
public interface OnPayGiftSuccessListener {
    /**
     * 礼物支付成功
     * @param giftInfo 礼物info
     * @param num 数量 1为单发 >2 为连发
     */
    void onPaySuc(GiftListBean giftInfo,int num);
}
