package com.miguo.live.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveAuthWaitActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_auth_wait);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_close_wait_auth:
                finish();
                break;
        }
    }
}
