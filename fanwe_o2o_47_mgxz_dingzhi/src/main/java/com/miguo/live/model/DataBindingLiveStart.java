package com.miguo.live.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;


public class DataBindingLiveStart {

    public final ObservableBoolean isQQ = new ObservableBoolean();
    public final ObservableBoolean isSina = new ObservableBoolean();
    public final ObservableBoolean isWeiXin = new ObservableBoolean();
    public final ObservableBoolean isFriend = new ObservableBoolean();
    //选择的店铺
    public final ObservableField<String> shopName = new ObservableField<String>();

}
