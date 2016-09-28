package com.fanwe.user.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fanwe.AccountMoneyActivity;
import com.fanwe.base.CallbackView2;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.miguo.live.model.getWallet.ModelMyWallet;
import com.miguo.live.views.RechargeDiamondActivity;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

public class WalletActivity extends Activity implements CallbackView2, View.OnClickListener {

    private ModelMyWallet myWallet;
    private TextView mTvTiXian;//可提现
    private TextView mTvDaiYan;//代言金额
    private TextView mTvShareMoney;//分享金额(元)
    private TextView mTvShareMG;//分享金额(米果币)
    private TextView mTvLiveMoney;//直播金额(元)
    private TextView mTvLiveMG;//直播金额(米果币)
    private View mRedPacket;//红包
    private View mMGDiamond;//米果钻
    private View mIvShareInfo;//分享收益帮助说明
    private View mIvLiveInfo;//直播收益帮助说明
    private View mBtTiXian;//提现按钮
    private View mLL_DaiYan;
    private View mLL_Share;
    private View mLL_Live;
    private UserHttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        initView();
        httpHelper = new UserHttpHelper(null, this);
    }

    private void initView() {
        initTitle();
        mTvTiXian = ((TextView) findViewById(R.id.tv_tixian));
        mTvDaiYan = ((TextView) findViewById(R.id.tv_daiyan));

        mTvShareMoney = ((TextView) findViewById(R.id.tv_share_money));
        mTvShareMG = ((TextView) findViewById(R.id.tv_share_mg));

        mTvLiveMoney = ((TextView) findViewById(R.id.tv_live_money));
        mTvLiveMG = ((TextView) findViewById(R.id.tv_live_mg));

        mBtTiXian = findViewById(R.id.tv_tixian_action);

        mLL_DaiYan = findViewById(R.id.ll_daiyan);
        mLL_Share = findViewById(R.id.ll_share);
        mLL_Live = findViewById(R.id.ll_live);


        mRedPacket = findViewById(R.id.ll_redPacket);
        mMGDiamond = findViewById(R.id.ll_mg_diamond);
        mIvShareInfo = findViewById(R.id.iv_share_info);
        mIvLiveInfo = findViewById(R.id.iv_live_info);
        mRedPacket.setOnClickListener(this);
        mMGDiamond.setOnClickListener(this);
        mIvShareInfo.setOnClickListener(this);
        mIvLiveInfo.setOnClickListener(this);
        mBtTiXian.setOnClickListener(this);
        mLL_DaiYan.setOnClickListener(this);
        mLL_Share.setOnClickListener(this);
        mLL_Live.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpHelper.getMyWallet();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("我的钱包");
    }

    private void bindData() {
        if (myWallet == null) {
            MGToast.showToast("数据异常!");
            return;
        }
        mTvTiXian.setText(myWallet.getNow_user_account_money());
        mTvDaiYan.setText(myWallet.getTotal_user_commission());
        mTvShareMoney.setText(myWallet.getShare_money());
        mTvShareMG.setText(myWallet.getCommon_diamond());
        mTvLiveMoney.setText(myWallet.getMiguobean_money());
        mTvLiveMG.setText(myWallet.getMiguobean());
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.MY_WALLET.equals(method)) {
            if (datas != null && datas.size() > 0) {
                myWallet = (ModelMyWallet) datas.get(0);
                bindData();
            }
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }

    @Override
    public void onClick(View v) {
        if (v==mRedPacket){
            //点击红包
            startActivity(RedPacketListActivity.class);
        }else if (v==mMGDiamond){
            //米果钻
            startActivity(RechargeDiamondActivity.class);
        }else if (v==mIvLiveInfo){
            //直播收益说明
            MGToast.showToast("info");
        }else if (v==mIvShareInfo){
            //分享收益说明
            MGToast.showToast("info");
        }else if (v==mBtTiXian){
            //提现按钮点击
            clickTiXian();
        }else if (v==mLL_DaiYan){
            //代言
        }else if (v==mLL_Live){
            //直播
            MGToast.showToast("系统升级,敬请期待");
        }else if (v==mLL_Share){
            //分享
            MGToast.showToast("系统升级,敬请期待");
        }
    }

    private void clickTiXian() {
//        MGToast.showToast("提现");
        startActivity(AccountMoneyActivity.class);
    }
    /**
     * goto 新Activity
     *
     * @param clazz 类
     */
    public void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
