package com.fanwe.user.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.AccountMoneyActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.mine.views.RepresentIncomeActivity;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.wallet.WalletBalance;
import com.fanwe.user.presents.WalletHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.miguo.dao.GetUserLevelDao;
import com.miguo.dao.impl.GetUserLevelDaoImpl;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.MGUIUtil;
import com.miguo.view.GetUserLevelView;

import java.util.List;

/**
 * 余额页。
 * Created by zhouhy on 2016/11/16.
 */

public class BalanceActivity extends Activity implements CallbackView, View.OnClickListener {

    private ImageView money_arrow;
    /**
     * 返回事件。
     */
    private ImageView back_arrow;
    /**
     * 总余额。
     */
    private TextView balance_value;

    /**
     * 提现。
     */
    private RelativeLayout withdraw_ll;
    /**
     * 资金详情。
     */
    private RelativeLayout moneydetail_ll;
    /**
     * 是否显示资金明细。
     */
    private LinearLayout show_detail_ll;
    /**
     * 佣金流水
     */
    private RelativeLayout commission_ll;
    /**
     * 邀请奖金。
     */
    private RelativeLayout invite_ll;
    /**
     * 退款及其他。
     */
    private RelativeLayout refund_ll;
    private TextView commission_value;
    private TextView invite_value;
    private WalletHttpHelper walletHttpHelper;


    MGProgressDialog progressDialog;

    /**
     * 获取用户等级
     */
    GetUserLevelDao getUserLevelDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_balance);
        initView();
        walletHttpHelper = new WalletHttpHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (progressDialog == null) {
            progressDialog = new MGProgressDialog(this);
        }
        progressDialog.show();
        walletHttpHelper.getWalletBalance();
    }

    private void handleGetUserLevelSuccess(String level){
        App.getInstance().getCurrentUser().setFx_level(level);
    }

    public void initView() {
        back_arrow = (ImageView) findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(this);
        money_arrow = (ImageView) findViewById(R.id.money_arrow);

        balance_value = (TextView) findViewById(R.id.balance_value);
        withdraw_ll = (RelativeLayout) findViewById(R.id.withdraw_ll);
        withdraw_ll.setOnClickListener(this);
        moneydetail_ll = (RelativeLayout) findViewById(R.id.moneydetail_ll);
        moneydetail_ll.setOnClickListener(this);
        show_detail_ll = (LinearLayout) findViewById(R.id.show_detail_ll);
        commission_ll = (RelativeLayout) findViewById(R.id.commission_ll);
        commission_ll.setOnClickListener(this);
        invite_ll = (RelativeLayout) findViewById(R.id.invite_ll);
        invite_ll.setOnClickListener(this);
        refund_ll = (RelativeLayout) findViewById(R.id.refund_ll);
        refund_ll.setOnClickListener(this);
        commission_value = (TextView) findViewById(R.id.commission_value);
        invite_value = (TextView) findViewById(R.id.invite_value);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }


    @Override
    public void onClick(View v) {
        if (v == withdraw_ll) {
            clickWithdraw();
        } else if (v == moneydetail_ll) {
            show_detail_ll();
        } else if (v == commission_ll) {
            //去佣金流水
            startActivity(RepresentIncomeActivity.class);
        } else if (v == invite_ll) {
            //邀请收益
            startActivity(InviteActivity.class);
        } else if (v == refund_ll) {
            //退款及其它。
            startActivity(RefundHistoryActivity.class);
        } else if (v == back_arrow) {
            onBackPressed();
        } else {
            return;
        }
    }

    private void clickWithdraw(){
        getUserLevelDao = new GetUserLevelDaoImpl(new GetUserLevelView() {
            @Override
            public void getUserLevelSuccess(String level) {
                handleGetUserLevelSuccess(level);
                /**
                 * 初级代言人
                 */
                if(getUserLevel() == 1){
                    BaseUtils.jumpToNewActivity(BalanceActivity.this, new Intent(BalanceActivity.this, ClassNameFactory.getClass(ClassPath.WITHDRAWAL_CONDITIONS_ACTIVITY)));
                    return;
                }
                startActivity(AccountMoneyActivity.class);
            }
            @Override
            public void getUserLevelError(String message) {
                MGToast.showToast(message);
            }
        });
        getUserLevelDao.getUserLevel();
    }

    /**
     * 显示详情。
     */
    public void show_detail_ll() {
        money_arrow.setVisibility(View.GONE);
        show_detail_ll.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    public void bindDataToView(List datas) {
        if (datas == null || datas.size() < 1) {
            return;
        }
        WalletBalance balance = (WalletBalance) datas.get(0);
        balance_value.setText(MGStringFormatter.getFloat2(balance.getBalance()));
        commission_value.setText(MGStringFormatter.getFloat2(balance.getCommission_total()) + " 元");
        invite_value.setText(MGStringFormatter.getFloat2(balance.getShare_total()) + " 元");
    }

    @Override
    public void onSuccess(String method, final List datas) {
        switch (method) {
            case UserConstants.GET_WALLET_BALANCE:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindDataToView(datas);
                    }
                });
                break;
            default:
                break;

        }

    }

    @Override
    public void onFailue(String responseBody) {
        onFinish(responseBody);
    }

    @Override
    public void onFinish(String method) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    private int getUserLevel(){
        try{
            return Integer.parseInt(App.getInstance().getCurrentUser().getFx_level());
        }catch (Exception e){
            return 1;
        }
    }

    public void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onFinish("");
        progressDialog = null;
        walletHttpHelper = null;
    }
}
