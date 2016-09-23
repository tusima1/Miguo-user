package com.miguo.utils.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fanwe.gif.SDImageTextView;
import com.fanwe.o2o.miguo.R;

public class DTestActivity extends AppCompatActivity {

    private SDImageTextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtest);

        mTv = ((SDImageTextView) findViewById(R.id.tv_imageviewText));
        mTv.setImage("http://img.chinatimes.com/newsphoto/2016-04-26/656/20160426002930.jpg");
    }
}
