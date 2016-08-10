package com.miguo.live.views;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fanwe.o2o.miguo.R;

/**
 * Created by Administrator on 2016/8/2.
 */
public class TestRedActivity extends Activity {

    private View mRootView;
    private Button mBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_test_red);
        mRootView = findViewById(R.id.rootView);
        mBt = ((Button) findViewById(R.id.goRed));

        mBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LiveUserPopHelper helper=new LiveUserPopHelper(TestRedActivity.this,mRootView);
                helper.show();
            }
        });
    }

}
