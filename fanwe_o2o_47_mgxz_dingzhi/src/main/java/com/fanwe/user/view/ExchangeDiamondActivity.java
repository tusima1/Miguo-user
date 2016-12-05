package com.fanwe.user.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.fanwe.base.CallbackView;
import com.fanwe.customview.MGProgressDialog;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.adapters.ExchangeDiamondAdapter;
import com.fanwe.user.model.wallet.Convert_list;
import com.fanwe.user.model.wallet.ExchangeListModel;
import com.fanwe.user.presents.WalletHttpHelper;
import com.fanwe.utils.SDFormatUtil;
import com.miguo.BaseNewActivity;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;
import com.miguo.utils.test.MGDialog;

import java.util.HashMap;
import java.util.List;

/**
 * 兑换果钻。
 * Created by zhouhy on 2016/11/23.
 */

public class ExchangeDiamondActivity extends BaseNewActivity implements CallbackView {

    MGProgressDialog progressDialog;
    private WalletHttpHelper walletHttpHelper = null;
    private List<Convert_list> mDatas;
    private ExchangeDiamondAdapter mAdapter;
    private TextView self_bean;
    private RecyclerView mRecyclerView;
    private ExchangeDiamondAdapter.ExchangeListener exchangeListener;
    private MGDialog mgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_exchangediamond);
        //头部初始化。
        initTitleView("兑换果钻");
        setLeftDrawable(R.drawable.ic_left_arrow_dark);
        setRightTextViewText("兑换记录");
        setRightTextViewVisible(true);
        //其它控件初始化。
        self_bean = (TextView) findViewById(R.id.self_bean);
        mAdapter = new ExchangeDiamondAdapter(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.exchange_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        exchangeListener = new ExchangeDiamondAdapter.ExchangeListener() {
            @Override
            public void doExchange(Convert_list data) {
                //显示确认是否兑换的DIALOG。
                showDialog(data);
            }
        };
        mAdapter.setExchangeListener(exchangeListener);
    }

    private void showDialog(final Convert_list data) {

        //把上一个dialog消失。
        if (mgDialog != null) {
            mgDialog = null;
        }
        mgDialog = new MGDialog(this)
                .setContentText("是否兑换米果钻？")
                .setOnSureClickListener(new MGDialog.OnSureClickListener() {
                    @Override
                    public void onSure(MGDialog dialog) {
                        doExchangeDiamond(data);
                        dialog.dismiss();
                    }
                })
                .setOnCancelClickListener(new MGDialog.OnCancelClickListener() {
                    @Override
                    public void onCancel(MGDialog dialog) {
                        dialog.dismiss();
                    }
                });
        mgDialog.show();
    }

    /**
     * 接口请求兑换。
     *
     * @param data
     */
    private void doExchangeDiamond(Convert_list data) {
        if (!TextUtils.isEmpty(data.getId())) {
            if (progressDialog == null) {
                progressDialog = new MGProgressDialog(this);
            }
            progressDialog.show();
            walletHttpHelper.doExchangeDiamond(data.getId());
        } else {
            MGToast.showToast("请选择您想兑换的果钻");
        }
    }

    @Override
    public void onRightTextViewPressed() {
        //兑换记录页。
        Intent intent = new Intent(this, ExchangeHistoryActivity.class);
        startActivity(intent);

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
        walletHttpHelper.getDiamondExchangeList();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    private void bindData(List datas) {
        onFinish(UserConstants.WALLET_INCOME_POST);
        if (datas == null || datas.size() < 1) {
            return;
        }
        ExchangeListModel model = (ExchangeListModel) datas.get(0);
        String selfBeanValue = model.getBean();
        self_bean.setText(SDFormatUtil.formatNumberString(model.getBean(), 2));
        mDatas = model.getConvert_list();
        if (mDatas == null || mDatas.size() < 1) {
            mAdapter.setmDatas(null);
            mAdapter.notifyDataSetChanged();
            return;
        }
        for (int i = 0; i < mDatas.size(); i++) {
            Convert_list entity = mDatas.get(i);
            if (SDFormatUtil.compareNumber(selfBeanValue, entity.getBean())) {
                entity.setExchangeAble(true);
            } else {
                entity.setExchangeAble(false);
            }
        }
        mAdapter.setmDatas(mDatas);
        mAdapter.notifyDataSetChanged();
    }


    private void updateData(List datas) {
        if (datas != null && datas.size() > 0) {
            HashMap<String, String> map = (HashMap<String, String>) datas.get(0);
            String convert_status = map.get(UserConstants.CONVERT_STATUS);
            String bean = map.get(UserConstants.BEAN);
            if (!TextUtils.isEmpty(convert_status) && "1".equals(convert_status)) {
                self_bean.setText(SDFormatUtil.formatNumberString(bean, 2));
                MGToast.showToast("兑换成功");
            } else {
                MGToast.showToast("兑换失败");
            }

        }
    }

    @Override
    public void onSuccess(String method, final List datas) {
        onFinish(method);
        switch (method) {
            case UserConstants.WALLET_INCOME_POST:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        bindData(datas);
                    }
                });
                break;
            case UserConstants.WALLET_INCOME_PUT:
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateData(datas);
                    }
                });
            default:
                break;
        }

    }

    @Override
    public void onFailue(String responseBody) {

        onFinish(responseBody);
        switch (responseBody) {
            case UserConstants.WALLET_INCOME_PUT:
                MGToast.showToast("兑换失败");
                break;
            default:
                break;

        }
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
        mgDialog = null;
        walletHttpHelper = null;
        mAdapter = null;
    }
}
