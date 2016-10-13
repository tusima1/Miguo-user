package com.fanwe;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.adapter.UserWithdrawLogAdapter;
import com.fanwe.base.CallbackView2;
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
 * 分销提现日志
 *
 * @author Administrator
 *
 */
public class UserWithdrawLogActivity extends BaseActivity implements CallbackView2 {

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlv_content;

    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    private List<ModelWithdrawLog> mListModel = new ArrayList<>();
    private UserWithdrawLogAdapter mAdapter;
    private MoneyHttpHelper httpHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_distribution_withdraw_log);
        init();
    }

    private void init() {
        httpHelper = new MoneyHttpHelper(this);
        initTitle();
        bindDefaultData();
        initPullToRefreshListView();
    }

    private void bindDefaultData() {
        mAdapter = new UserWithdrawLogAdapter(mListModel, this);
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initTitle() {
        mTitle.setMiddleTextTop("提现明细");
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                httpHelper.getUserWithdrawLog("1");
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

