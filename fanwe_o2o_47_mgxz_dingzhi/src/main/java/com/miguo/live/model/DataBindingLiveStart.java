package com.miguo.live.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;


public class DataBindingLiveStart {

    public final int NONE = 0;
    public final int QQ = 1;
    public final int WEIXIN = 2;
    public final int FRIEND = 3;
    public final int SINA = 4;
    public final int QQZONE = 5;

    public final ObservableBoolean isLiveRight = new ObservableBoolean();

    public final ObservableInt mode = new ObservableInt();
    //选择的店铺
    public final ObservableField<String> shopName = new ObservableField<String>();

    public DataBindingLiveStart() {
        mode.set(FRIEND);
    }
}
