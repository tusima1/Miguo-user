package com.miguo.live.views;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.o2o.miguo.R;
import com.miguo.live.views.view.frag.GuideLiveFragment;

public class TestLiveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_live);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.beginTransaction().add(R.id.activity_test_live,new GuideLiveFragment()).commit();
    }
}
