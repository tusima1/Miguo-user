package com.miguo.live.model;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;


public class DataBindingLiveEnd {

    public final int NONE = 0;
    public final int QQ = 1;
    public final int WEIXIN = 2;
    public final int FRIEND = 3;
    public final int SINA = 4;


    public final ObservableInt mode = new ObservableInt();
    //主播名
    public final ObservableField<String> hostName = new ObservableField<String>();
    //选择的店铺
    public final ObservableField<String> shopName = new ObservableField<String>();
    //观众数量
    public final ObservableField<String> numViewers = new ObservableField<String>();
    //直播时长
    public final ObservableField<String> timeLive = new ObservableField<String>();

    //红包
    public final ObservableField<String> countMoney = new ObservableField<String>();
    //商品
    public final ObservableField<String> countGood = new ObservableField<String>();
    //米果币
    public final ObservableField<String> countMi = new ObservableField<String>();

    public DataBindingLiveEnd() {
        mode.set(FRIEND);
    }
}
