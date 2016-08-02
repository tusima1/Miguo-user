package com.miguo.live.views;

import android.app.Activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.fanwe.o2o.miguo.R;
import com.fanwe.o2o.miguo.databinding.ActLiveEndBinding;
import com.miguo.live.model.DataBindingLiveEnd;
import com.miguo.live.model.LiveConstants;
import com.miguo.utils.TimeUtils;
import com.tencent.qcloud.suixinbo.model.LiveInfoJson;

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

        binding.setLive(dataBindingLiveEnd);
    }

    public void getValue(){
        Intent intent = getIntent();
        LiveInfoJson liveInfoJson = (LiveInfoJson)intent.getSerializableExtra(LiveConstants.LIVEINFOJSON);
        if(liveInfoJson!=null){
            if(!TextUtils.isEmpty(liveInfoJson.getUsetime())){
                Integer seconds = Integer.valueOf(liveInfoJson.getUsetime());
                String timeStr = TimeUtils.secondToHHMMSS(seconds);
                dataBindingLiveEnd.timeLive.set(timeStr);
                String count = liveInfoJson.getWatch_count();
                if(!TextUtils.isEmpty(count)){

                    dataBindingLiveEnd.numViewers.set(count);

                    dataBindingLiveEnd.countMoney.set("32548359");
                    dataBindingLiveEnd.countGood.set("23");
                    dataBindingLiveEnd.countMi.set("356");
                }
            }

        }
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
            case R.id.iv_qqzone_live_end:
                dataBindingLiveEnd.mode.set(dataBindingLiveEnd.QQZONE);
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
        } else if (dataBindingLiveEnd.mode.get() == dataBindingLiveEnd.QQZONE) {
            Toast.makeText(this, "QQZONE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NONE", Toast.LENGTH_SHORT).show();
        }
    }
}
