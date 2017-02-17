package com.miguo.ui.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.customviews.ArcDrawable;
import com.miguo.ui.view.customviews.RedPacketPopup;

public class TestActivity extends AppCompatActivity {

    private View arcView;
    private View open;
    private View content;
    private RedPacketPopup redPacketPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        content = findViewById(android.R.id.content);
        arcView = findViewById(R.id.view);
        open = findViewById(R.id.bt_open);
        ArcDrawable bg = new ArcDrawable();
        arcView.setBackground(bg);

        redPacketPopup = new RedPacketPopup(this,content);
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redPacketPopup.showAtLocation(content, Gravity.CENTER,0,0);
            }
        });
    }
}
