package com.miguo.live.views;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveStartBinding;
import com.miguo.live.model.DataBindingLiveStart;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveStartActivity extends Activity {
    DataBindingLiveStart dataBindingLiveStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveStartBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_start);
        dataBindingLiveStart = new DataBindingLiveStart();
        dataBindingLiveStart.shopName.set("选择你的消费场所");
        binding.setLive(dataBindingLiveStart);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                dataBindingLiveStart.shopName.set("米果小站");
                break;
            case R.id.iv_qq_live_start:
                if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.NONE);
                } else {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.QQ);
                }
                break;
            case R.id.iv_weixin_live_start:
                if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.NONE);
                } else {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.WEIXIN);
                }
                break;
            case R.id.iv_friend_live_start:
                if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.NONE);
                } else {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.FRIEND);
                }
                break;
            case R.id.iv_sina_live_start:
                if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.NONE);
                } else {
                    dataBindingLiveStart.mode.set(dataBindingLiveStart.SINA);
                }
                break;
            case R.id.btn_start_live_start:
                startLive();
                break;
        }
    }

    private void startLive() {
        if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.QQ) {
            Toast.makeText(this, "QQ", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.WEIXIN) {
            Toast.makeText(this, "WEIXIN", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.FRIEND) {
            Toast.makeText(this, "FRIEND", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveStart.mode.get() == dataBindingLiveStart.SINA) {
            Toast.makeText(this, "SINA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
        }
    }
}
