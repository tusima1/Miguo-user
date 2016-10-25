package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.adapter.WithdrawLogAdapter;
import com.fanwe.base.CallbackView2;
import com.fanwe.base.PageBean;
import com.fanwe.commission.model.CommissionConstance;
import com.fanwe.commission.model.getCommissionLog.ModelCommissionLog;
import com.fanwe.commission.model.getCommissionLog.ResultCommissionLog;
import com.fanwe.commission.presenter.LogHttpHelper;
import com.fanwe.constant.Constant.TitleType;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.model.getDistrInfo.ModelDistrInfo;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 资金日志
 * 
 * @author cxk
 * 
 */
public class WithdrawLogActivity extends BaseActivity implements CallbackView2 {

	@ViewInject(R.id.ptrlv_content)
	private PullToRefreshScrollView mPtrlv_content;

	@ViewInject(R.id.tv_money)
	private TextView tv_money;

	@ViewInject(R.id.ll_tixian)
	private Button ll_tixian;

	@ViewInject(R.id.lv_withdraw)
	private ListView lv_withdraw;

	@ViewInject(R.id.tv_money_yes)
	private TextView mTv_moneyYes;

	@ViewInject(R.id.ll_empty)
	private LinearLayout ll_empty;

	private List<ModelCommissionLog> mListModel = new ArrayList<ModelCommissionLog>();
	private WithdrawLogAdapter mAdapter;

	private PageModel mPage = new PageModel();
	private int rank = -1;
	private LogHttpHelper httpHelper;
	private ResultCommissionLog resultCommissionLog;
	private boolean isLoadMore;
	private UserHttpHelper tempHttp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_withdraw_log);
		init();
	}

	private void init() {
		initTitle();
		httpHelper = new LogHttpHelper(this);
		bindDefaultData();
		initPullToRefreshScrollView();
		getMemberRank();
	}

	private void getMemberRank() {
		tempHttp = new UserHttpHelper(this, new CallbackView2() {
			@Override
			public void onSuccess(String responseBody) {

			}

			@Override
			public void onSuccess(String method, List datas) {
				if (UserConstants.DISTR_INFO.endsWith(method)){
					if (datas!=null){
						ModelDistrInfo modelDistrInfo= (ModelDistrInfo) datas.get(0);
						String fx_level = modelDistrInfo.getFx_level();
						rank=MGStringFormatter.getInt(fx_level);
					}
				}
			}

			@Override
			public void onFailue(String responseBody) {

			}

			@Override
			public void onFinish(String method) {

			}
		});
		tempHttp.getDistrInfo();
	}

	private void initIntentData() {

		if (resultCommissionLog == null) {
			return;
		}
		BigDecimal bd2 = new BigDecimal(resultCommissionLog.getSalary());
		bd2 = bd2.setScale(2, BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(tv_money, "￥" + bd2, "￥0.00");

		BigDecimal bd1 = new BigDecimal(resultCommissionLog.getBlocksalary());
		bd1 = bd1.setScale(2, BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(mTv_moneyYes, "（不可提现金额：￥" + bd1 + "）", "（不可提现金额：￥0.00");
		ll_tixian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rank == -1) {
					tempHttp.getDistrInfo();
					MGToast.showToast("网络异常,请重新点击!");
				} else {
					if (rank == 1) {// 普通会员
						startActivity(new Intent(WithdrawLogActivity.this, MemberRankActivity.class));
					} else {
						Intent intent = new Intent(getApplicationContext(), DistributionWithdrawActivity.class);
						intent.putExtra("money", Float.parseFloat(resultCommissionLog.getSalary())
								- Float.parseFloat(resultCommissionLog.getBlocksalary()) + "");
						intent.putExtra("money_type",2);
						startActivity(intent);
					}
				}

			}
		});
	}

	private void bindDefaultData() {
		mAdapter = new WithdrawLogAdapter(mListModel, this);
		lv_withdraw.setAdapter(mAdapter);
	}

	@SuppressWarnings("unchecked")
	private void initPullToRefreshScrollView() {
		mPtrlv_content.setMode(Mode.BOTH);
		mPtrlv_content.setOnRefreshListener(new OnRefreshListener2() {

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
					MGToast.showToast("没有更多数据了");
					mPtrlv_content.onRefreshComplete();
				}
			}
		});
		mPtrlv_content.setRefreshing();
	}

	@Override
	protected void onResume() {
		requestData(false);
		super.onResume();
	}

	private void requestData(boolean isLoadMore) {
		this.isLoadMore=isLoadMore;
		httpHelper.getUserCommissionLog(mPage.getPage()+"","10");
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("资金日志");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpHelper.onDestroy();
		tempHttp.onDestroy();
	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onSuccess(String method, List datas) {
		if (CommissionConstance.USER_COMMISSION_LOG.endsWith(method)){
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
			initIntentData();
			SDViewUtil.updateAdapterByList(mListModel, resultCommissionLog.getList(), mAdapter, isLoadMore);
		}
	}

	@Override
	public void onFailue(String responseBody) {

	}

	@Override
	public void onFinish(String method) {
		if (CommissionConstance.USER_COMMISSION_LOG.equals(method)) {
			mPtrlv_content.onRefreshComplete();
		}
	}
}
