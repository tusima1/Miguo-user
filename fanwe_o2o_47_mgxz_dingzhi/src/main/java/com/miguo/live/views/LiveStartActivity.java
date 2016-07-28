package com.miguo.live.views;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveBinding;
import com.miguo.live.model.DataBindingLiveStart;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity {
    DataBindingLiveStart dataBindingLiveStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live);
        dataBindingLiveStart = new DataBindingLiveStart();
        dataBindingLiveStart.shopName.set("请选择代言店铺");
        binding.setLive(dataBindingLiveStart);
        dataBindingLiveStart.shopName.set("我就是我 哈哈哈");
    }
}
