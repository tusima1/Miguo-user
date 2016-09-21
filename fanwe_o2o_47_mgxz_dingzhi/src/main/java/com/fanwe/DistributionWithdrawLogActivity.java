package com.fanwe;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.adapter.WithdrawLogAdapter;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.PageBean;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.commission.model.getCommissionLog.ResultCommissionLog;
import com.fanwe.commission.presenter.LogHttpHelper;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.live.views.customviews.MGToast;

import java.util.ArrayList;
import java.util.List;

/**
 * 提现日志(余额提现和佣金提现一起的)
 *
 * @author Administrator
 *
 */
public class DistributionWithdrawLogActivity extends BaseActivity implements CallbackView2 {

    @ViewInject(R.id.ptrlv_content)
    private PullToRefreshListView mPtrlv_content;

    @ViewInject(R.id.ll_empty)
    private LinearLayout ll_empty;

    private List<ModelCommissionLog> mListModel = new
            ArrayList<ModelCommissionLog>();
    private WithdrawLogAdapter mAdapter;
    private ResultCommissionLog resultCommissionLog;
    private PageModel mPage = new PageModel();
    /*
    1:余额:对应的显示余额提现记录
    2:佣金:对应的显示佣金提现记录
    */
    private int money_type = 0;
    private LogHttpHelper httpHelper;
    private boolean isLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setmTitleType(TitleType.TITLE);
        setContentView(R.layout.act_distribution_withdraw_log);
        init();
    }

    private void init() {
        httpHelper = new LogHttpHelper(this);
        getIntentData();
        bindDefaultData();
        initPullToRefreshListView();
    }

    private void getIntentData() {
        money_type = getIntent().getIntExtra("money_type", 0);
        if (money_type==0){
            MGToast.showToast("类型错误!");
            finish();
        }
        initTitle();
    }

    private void bindDefaultData() {
        mAdapter = new WithdrawLogAdapter(mListModel, this);
        mPtrlv_content.setAdapter(mAdapter);
    }

    private void initTitle() {
        if (money_type == 2) {
            mTitle.setMiddleTextTop("分销提现日志");
        } else if (money_type == 1) {
            mTitle.setMiddleTextTop("提现明细");
        }
    }

    private void initPullToRefreshListView() {
        mPtrlv_content.setMode(Mode.BOTH);
        mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase refreshView) {
                mPage.resetPage();
                requestData(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase refreshView) {
                if (mPage.increment()) {
                    requestData(true);
                } else {
                    SDToast.showToast("没有更多数据了");
                    mPtrlv_content.onRefreshComplete();
                }
            }
        });
        mPtrlv_content.setRefreshing();
    }

    private void requestData(boolean isLoadMore) {
        this.isLoadMore=isLoadMore;
        httpHelper.getCommissionLog(mPage.getPage()+"","10",money_type+"");
    }

    @Override
    public void onSuccess(String responseBody) {

    }

    @Override
    public void onSuccess(String method, List datas) {
//        if (CommissionConstance.USER_WITHDRAW_LOG.equals(method)) {
//            ResultWithdrawLog resultWithdrawLog = (ResultWithdrawLog) datas.get(0);
//            if (resultWithdrawLog != null) {
//                int firstPage = MGStringFormatter.getInt(resultWithdrawLog.getPage());
//                if (firstPage == 1) {
//                    int page_total = MGStringFormatter.getInt(resultWithdrawLog.getPage_total());
//                    mPage.setPage_total(page_total);
//                }
//                List<ModelWithdrawLog> body = resultWithdrawLog.getBody();
//                if (mPage.getPage() == 1) {
//                    if (body == null || body.size() == 0) {
//                        ll_empty.setVisibility(View.VISIBLE);
//                    } else {
//                        ll_empty.setVisibility(View.GONE);
//                    }
//                } else {
//                    ll_empty.setVisibility(View.GONE);
//                }
//                SDViewUtil.updateAdapterByList(mListModel, body, mAdapter, isLoadMore);
//            }
//        }
        if (CommissionConstance.COMMISSION_LOG.endsWith(method)){
            resultCommissionLog = (ResultCommissionLog) datas.get(0);
            PageBean page = resultCommissionLog.getPage();
            int currentPage = MGStringFormatter.getInt(page.getPage());
            if (currentPage==1){
                mPage.setPage_total(MGStringFormatter.getInt(page.getPage_total()));
                int total = MGStringFormatter.getInt(page.getData_total());
                if (total<1){
                    ll_empty.setVisibility(View.VISIBLE);
                }else {
                    ll_empty.setVisibility(View.GONE);
                }
            }else {
                ll_empty.setVisibility(View.GONE);
            }
//            initIntentData();
            SDViewUtil.updateAdapterByList(mListModel, resultCommissionLog.getList(), mAdapter, isLoadMore);
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
