package com.miguo.live.views;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;

import com.fanwe.app.App;
import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.customviews.HostMeiToolView;

public class LiveTestActivity extends Activity implements View.OnClickListener {

    private HostMeiToolView mToolView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_test2);
        getDeviceId();
    }

    private void init() {
//        mToolView = ((HostMeiToolView) findViewById(R.id.toolview));

    }

    @Override
    public void onClick(View v) {
    }



    /**
     * 获取设备IMEI
     * 需要权限.6.0申请无效
     */
    public void getDeviceId(){
        TelephonyManager telephonyManager = (TelephonyManager) this
                .getSystemService(Context.TELEPHONY_SERVICE);
        App.getInstance().setImei(telephonyManager.getDeviceId());
    }

}
