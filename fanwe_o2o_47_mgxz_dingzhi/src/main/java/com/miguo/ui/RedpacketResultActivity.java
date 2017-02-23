package com.miguo.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.didikee.uilibs.utils.StatusBarUtil;
import com.fanwe.o2o.miguo.R;
<<<<<<< HEAD
import com.miguo.definition.ClassPath;
=======
import com.fanwe.user.view.WalletNewActivity;
>>>>>>> e475f019f4465ecbb1b44832cfa026b876016ac7
import com.miguo.definition.IntentKey;
import com.miguo.factory.ClassNameFactory;
import com.miguo.ui.view.customviews.ArcDrawable;
import com.miguo.utils.BaseUtils;

public class RedPacketResultActivity extends AppCompatActivity implements View.OnClickListener {

    private View mBack;
    private View redPacketView;
    private TextView tvTitle;
    private TextView tvName;
    private TextView tvDesc;
    private TextView tvMoney;
    private TextView tv_link;
    private ImageView ivFace;


    private String money;
    private String face_icon;
    private String showContent;
    private TextView myWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        setContentView(R.layout.activity_repacket_result);
        StatusBarUtil.setColor(this, Color.parseColor("#D23838"),0);
        initView();
        bindData();
    }

    private void bindData() {
        tvDesc.setText(showContent);
        Glide.with(this).load(face_icon).into(ivFace);
        tvMoney.setText(money);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent==null){
            finish();
            return;
        }
        money = intent.getStringExtra(IntentKey.MONEY);
        face_icon = intent.getStringExtra(IntentKey.ICON);
        showContent = intent.getStringExtra(IntentKey.DESC);

        if (TextUtils.isEmpty(money) || TextUtils.isEmpty(face_icon)){
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void initView() {
        mBack = findViewById(R.id.iv_back);
        tvTitle = ((TextView) findViewById(R.id.tv_title));
        tvName = ((TextView) findViewById(R.id.tv_name));
        tvDesc = ((TextView) findViewById(R.id.tv_desc));
        tv_link = ((TextView) findViewById(R.id.tv_link));
        tvMoney = ((TextView) findViewById(R.id.tv_money));
        myWallet = ((TextView) findViewById(R.id.my_wallet));
        redPacketView = findViewById(R.id.redPacket_view);

        ivFace = ((ImageView) findViewById(R.id.iv_face));

        mBack.setOnClickListener(this);
<<<<<<< HEAD
        myWallet.setOnClickListener(this);
=======
        tv_link.setOnClickListener(this);
>>>>>>> e475f019f4465ecbb1b44832cfa026b876016ac7
        redPacketView.setBackground(new ArcDrawable());
    }

    @Override
    public void onClick(View v) {
        if (mBack == v){
            onBackPressed();
            return;
        }
<<<<<<< HEAD
        if(myWallet == v){
            Intent intent = new Intent(this, ClassNameFactory.getClass(ClassPath.MY_WALLET));
            BaseUtils.jumpToNewActivityWithFinish(this, intent);
        }

=======
        if (tv_link == v){
            startActivity(new Intent(this, WalletNewActivity.class));
            finish();
        }
>>>>>>> e475f019f4465ecbb1b44832cfa026b876016ac7

    }
}
