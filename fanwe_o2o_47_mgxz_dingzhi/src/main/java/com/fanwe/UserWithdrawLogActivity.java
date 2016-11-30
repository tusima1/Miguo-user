package com.fanwe;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.adapter.UserWithdrawLogAdapter;
import com.fanwe.base.CallbackView;
import com.fanwe.commission.model.getWithdrawLog.ModelWithdrawLog;
import com.fanwe.commission.model.getWithdrawLog.ResultWithdrawLog;
import com.fanwe.commission.presenter.MoneyHttpHelper;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现(佣金与余额)
 *
 * @author Administrator
 *
 */
public class UserWithdrawLogActivity extends BaseActivity implements CallbackView {

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlv_content;

    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    private List<ModelWithdrawLog> mListModel = new ArrayList<>();
    private UserWithdrawLogAdapter mAdapter;
    private MoneyHttpHelper httpHelper;

    private int money_type=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_distribution_withdraw_log);
        init();
    }

    private void init() {
        httpHelper = new MoneyHttpHelper(this);
        getIntentData();
        bindDefaultData();
        initPullToRefreshListView();
    }

    private void getIntentData() {
        money_type = getIntent().getIntExtra("money_type", 0);
        if(money_type == 0){
            MGToast.showToast("类型错误!");
            finish();
            return;
        }
        if (money_type==1){
            mTitle.setMiddleTextTop("提现明细");
        }else if (money_type==2){
            mTitle.setMiddleTextTop("分销提现日志");
        }
    }

    private void bindDefaultData() {
        mAdapter = new UserWithdrawLogAdapter(mListModel, this);
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                httpHelper.getUserWithdrawLog(money_type+"");
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                MGToast.showToast("未找到更多数据");
                mPtrlv_content.onRefreshComplete();
            }
        });
        mPtrlv_content.setRefreshing();
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
        if (datas!=null && datas.size()>0){
            ResultWithdrawLog resultWithdrawLog = (ResultWithdrawLog) datas.get(0);
            List<ModelWithdrawLog> body = resultWithdrawLog.getBody();
            if (body!=null && body.size()>0){
                SDViewUtil.updateAdapterByList(mListModel, body, mAdapter, false);
            }
            ll_empty.setVisibility(View.GONE);
        }else {
            ll_empty.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {
        mPtrlv_content.onRefreshComplete();
    }
}

