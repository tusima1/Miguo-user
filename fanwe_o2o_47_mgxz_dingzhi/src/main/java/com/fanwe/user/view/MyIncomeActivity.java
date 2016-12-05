package com.fanwe.user.view;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.WalletHttpHelper;
import com.miguo.BaseNewActivity;
import com.miguo.utils.MGUIUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 我的收益页。
 * Created by zhouhy on 2016/11/17.
 */

public class MyIncomeActivity extends BaseNewActivity implements CallbackView, View.OnClickListener {

    MGProgressDialog progressDialog;
    private WalletHttpHelper walletHttpHelper = null;
    TextView nutlet_refund;
    TextView validate_income;
    private String nutlet_refund_str = "";
    private String validate_income_str = "";
    private Button exchange_diamond;
    private Button exchange_money;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_myincome);
        nutlet_refund = (TextView) findViewById(R.id.nutlet_refund);
        validate_income = (TextView) findViewById(R.id.validate_income);
        exchange_diamond = (Button) findViewById(R.id.exchange_diamond);
        exchange_diamond.setOnClickListener(this);

        exchange_money = (Button) findViewById(R.id.exchange_money);
        exchange_money.setOnClickListener(this);
        exchange_money.setClickable(false);


        initTitleView("我的收益");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (walletHttpHelper == null) {
            walletHttpHelper = new WalletHttpHelper(this);
        }
        if (progressDialog == null) {
            progressDialog = new MGProgressDialog(this);
        }
        progressDialog.show();
        walletHttpHelper.getWalletIncomeTotal();
    }


    private void bindData(List<HashMap<String, String>> datas) {
        onFinish(UserConstants.WALLET_INCOME_GET);
        if (datas == null || datas.size() < 1) {
            return;
        }
        HashMap<String, String> map = datas.get(0);
        nutlet_refund_str = map.get(UserConstants.BEAN);
        validate_income_str = map.get(UserConstants.BEAN_CONVERT_RMB);
        nutlet_refund.setText(nutlet_refund_str);
        setButtonColorAndClickAble(exchange_diamond, nutlet_refund_str);
//        setButtonColorAndClickAble(exchange_money,validate_income_str);

        validate_income.setText(validate_income_str);
    }


    public void setButtonColorAndClickAble(Button btn, String value) {
        Resources resources = getResources();
        if (!TextUtils.isEmpty(value) && Float.valueOf(value) > 0) {
            btn.setTextColor(resources.getColor(R.color.my_main_color));
            btn.setClickable(true);
        } else {
            btn.setTextColor(resources.getColor(R.color.c_CCCCCC));
            btn.setClickable(false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == exchange_diamond) {
            startActivity(ExchangeDiamondActivity.class);
        } else if (v == exchange_money) {

        }


    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case UserConstants.WALLET_INCOME_GET:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindData(datas);
                    }
                });

                break;
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {

    }


    @Override
    public void onFinish(String method) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        walletHttpHelper = null;
        progressDialog = null;
    }


}
