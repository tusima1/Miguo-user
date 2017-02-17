package com.miguo.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.didikee.uilibs.utils.StatusBarUtil;
import com.fanwe.o2o.miguo.R;
import com.miguo.ui.view.customviews.ArcDrawable;

public class RedPacketResultActivity extends AppCompatActivity implements View.OnClickListener {

    private View mBack;
    private View redPacketView;
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvMoney;
    private ImageView ivFace;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repacket_result);
        StatusBarUtil.setColor(this, Color.parseColor("#D23838"),0);
        initView();
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvDesc = ((TextView) findViewById(R.id.tv_desc));
        tvMoney = ((TextView) findViewById(R.id.tv_money));
        redPacketView = findViewById(R.id.redPacket_view);

        ivFace = ((ImageView) findViewById(R.id.iv_face));

        mBack.setOnClickListener(this);
        redPacketView.setBackground(new ArcDrawable());
    }

    @Override
    public void onClick(View v) {
        if (mBack == v){
            onBackPressed();
            return;
        }


    }
}
