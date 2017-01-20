package com.miguo.ui.view.floatdropdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.floatdropdown.view.CheckTextView;

public class TestActivity extends AppCompatActivity {

    private CheckTextView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        view = (CheckTextView) findViewById(R.id.view);
        view.setText("测试");


    }



}
