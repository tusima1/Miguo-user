package com.miguo.live.views;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LiveAuthStartActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_live_auth_start);
    }
}
