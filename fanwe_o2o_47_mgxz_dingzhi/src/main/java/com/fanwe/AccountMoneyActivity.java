package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.base.CallbackView2;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getDistrInfo.ModelDistrInfo;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

/**
 * 账户余额
 *
 * @author Administrator
 *
 */
public class AccountMoneyActivity extends BasePullToRefreshScrollViewActivity implements CallbackView2 {

    @ViewInject(R.id.tv_money)
    private TextView mTv_money;

    @ViewInject(R.id.tv_withdraw)
    private TextView mTv_withdraw;

    private double money=0;
    private UserHttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_account_money);
        init();
    }

    private void init() {
        httpHelper = new UserHttpHelper(null,this);
        initTitle();
        registerClick();
    }

    @Override
    protected void onResume() {
        requestData();
        super.onResume();
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        requestData();
        super.onPullUpToRefresh(refreshView);
    }

    private void requestData() {
        httpHelper.getDistrInfo();
    }

    private void registerClick() {
        mTv_withdraw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (money > 0) {
                        Intent intent = new Intent(getApplicationContext(), DistributionWithdrawActivity.class);
                        intent.putExtra("money",money+"");
                        intent.putExtra("money_type",1);
                        startActivity(intent);
                    } else {
                        MGToast.showToast("没有余额可提现");
                    }
            }
        });
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("账户余额");
        mTitle.initRightItem(1);
        mTitle.getItemRight(0).setTextBot("提现明细");
    }

    @Override
    public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {
        Intent intent = new Intent(getApplicationContext(), DistributionWithdrawLogActivity.class);
        intent.putExtra("money_type", 1);
        intent.putExtra("money", money);
        startActivity(intent);
        super.onCLickRight_SDTitleSimple(v, index);
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (UserConstants.DISTR_INFO.equals(method)){
            if (datas==null){
                money =0;
                mTv_money.setText("0.00");
            }else {
                ModelDistrInfo modelDistrInfo= (ModelDistrInfo) datas.get(0);
                String fx_money = modelDistrInfo.getFx_money();
                try {
                    money=Double.valueOf(fx_money);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    money=0;
                }
                mTv_money.setText(MGStringFormatter.getFloat2(money));
            }
        }
    }

    @Override
    public void onFailue(String responseBody) {
        MGToast.showToast(responseBody);
        finish();
    }

    @Override
    public void onFinish(String method) {
        mPtrsv_all.onRefreshComplete();
    }
}
