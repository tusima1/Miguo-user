package com.fanwe.user.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.WalletAdapter;
import com.fanwe.user.model.wallet.ModelWalletNew;
import com.fanwe.user.model.wallet.WalletResult;
import com.fanwe.user.presents.WalletHttpHelper;
import com.miguo.BaseNewActivity;
import com.miguo.live.views.RechargeDiamondActivity;
import com.miguo.utils.MGUIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 新的我的钱包功能。
 * Created by zhouhy on 2016/11/14.
 */
public class WalletNewActivity extends BaseNewActivity implements CallbackView2 {

    private ListView walletListView;

    private WalletHttpHelper walletHttpHelper;

    private WalletAdapter walletAdapter;

    List<ModelWalletNew> walletList;
    MGProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_new);
        initView();
        initTitleView("我的钱包");
        walletHttpHelper = new WalletHttpHelper(this);
    }




    public void initWalletList() {
        if (walletList == null) {
            walletList = new ArrayList<>();
            ModelWalletNew modelWalletNew1 = new ModelWalletNew();
            modelWalletNew1.setIcon(R.drawable.balance);
            modelWalletNew1.setTitle("余额");
            modelWalletNew1.setUnit("元");
            walletList.add(modelWalletNew1);
            ModelWalletNew modelWalletNew2 = new ModelWalletNew();
            modelWalletNew2.setIcon(R.drawable.recharge);
            modelWalletNew2.setTitle("充值");
            modelWalletNew2.setUnit("果钻");
            walletList.add(modelWalletNew2);
            ModelWalletNew modelWalletNew3 = new ModelWalletNew();
            modelWalletNew3.setIcon(R.drawable.income);
            modelWalletNew3.setTitle("果仁");
            modelWalletNew3.setUnit("个");
            walletList.add(modelWalletNew3);
            ModelWalletNew modelWalletNew4 = new ModelWalletNew();
            modelWalletNew4.setIcon(R.drawable.gift);
            modelWalletNew4.setTitle("礼包");
            modelWalletNew4.setUnit("张优惠券");
            walletList.add(modelWalletNew4);
        }
    }

    private void initView() {

        walletListView = (ListView) findViewById(R.id.wallet_listview);
        initWalletList();

        walletAdapter = new WalletAdapter(WalletNewActivity.this, new WalletAdapter.WalletLineOnClickListener() {
            @Override
            public void onClick(View v, int position) {
                switch (position) {
                    case 0:
                        startActivity(BalanceActivity.class);
                        break;
                    case 1:
                        //米果钻
                        startActivity(RechargeDiamondActivity.class);
                        break;
                    case 2:
                        startActivity(MyIncomeActivity.class);
                        break;
                    case 3:
                        //点击红包
                        startActivity(RedPacketListActivity.class);
                        break;
                    default:
                        break;
                }

            }
        },walletList);
//        walletAdapter.setWalletList(walletList);
        walletListView.setAdapter(walletAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
         if(progressDialog==null){
            progressDialog = new MGProgressDialog(this);
        }
        progressDialog.show();
        walletHttpHelper.getWalletBalance();
        walletHttpHelper.getWalletNew();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onFinish("");
        finish();

    }

    @Override
    public void onSuccess(String responseBody) {

    }

    public void dealWithWalletData(List<WalletResult> datas) {
        if (datas == null || datas.size() < 1 || walletList == null || walletList.size() < 4) {
            return;
        } else {
            WalletResult walletResult = datas.get(0);
            if (walletList.get(0) != null) {
                walletList.get(0).setValue(walletResult.getBalance());
            }
            if (walletList.get(1) != null) {
                walletList.get(1).setValue(walletResult.getDiamond());
            }
            if (walletList.get(2) != null) {
                walletList.get(2).setValue(walletResult.getBean());
            }
            if (walletList.get(3) != null) {
                walletList.get(3).setValue(walletResult.getRedpacket());
            }
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    walletAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public void onSuccess(String method, List datas) {
        if (method.equals(UserConstants.WALLET)) {
            dealWithWalletData(datas);
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        progressDialog=null;

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog = null;
        walletHttpHelper = null;
    }
}
