package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveAuthStartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_live_auth_start);
    }

    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_close_start_auth:
                finish();
                break;
            case R.id.btn_start_live_start:
                Intent intent = new Intent(this, LiveAuthActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
