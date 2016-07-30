package com.miguo.live.views;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveEndBinding;
import com.miguo.live.model.DataBindingLiveEnd;

/**
 * Created by Administrator on 2016/7/28.
 */
public class LiveEndActivity extends Activity {
    DataBindingLiveEnd dataBindingLiveEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActLiveEndBinding binding = DataBindingUtil.setContentView(this, R.layout.act_live_end);
        dataBindingLiveEnd = new DataBindingLiveEnd();
        dataBindingLiveEnd.shopName.set("当代时尚酒店");
        dataBindingLiveEnd.hostName.set("纯净小资");
        dataBindingLiveEnd.numViewers.set("32548359");
        dataBindingLiveEnd.timeLive.set("11:25:53");
        dataBindingLiveEnd.countMoney.set("32548359");
        dataBindingLiveEnd.countGood.set("23");
        dataBindingLiveEnd.countMi.set("356");
        binding.setLive(dataBindingLiveEnd);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_shop_name:
                break;
            case R.id.iv_qq_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.QQ);
                break;
            case R.id.iv_weixin_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.WEIXIN);
                break;
            case R.id.iv_friend_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.FRIEND);
                break;
            case R.id.iv_sina_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.SINA);
                break;
            case R.id.btn_submit_live_end:
                startLive();
                break;
        }
    }

    private void startLive() {
        if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.QQ) {
            Toast.makeText(this, "QQ", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.WEIXIN) {
            Toast.makeText(this, "WEIXIN", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.FRIEND) {
            Toast.makeText(this, "FRIEND", Toast.LENGTH_SHORT).show();
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.SINA) {
            Toast.makeText(this, "SINA", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
        }
    }
}
