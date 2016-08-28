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
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_HomeModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.MGStringFormatter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
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
//	private Uc_money_Zijin_LogActModel mActModel;

	private int rank = -1;
	private LogHttpHelper httpHelper;
	private ResultCommissionLog resultCommissionLog;
	private boolean isLoadMore;

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
		RequestModel model = new RequestModel();
		model.putCtl("uc_home");
		model.putAct("homepage");
		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_HomeModel>() {
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					rank = actModel.getDist().getRank();
				}
			}

			@Override
			public void onFinish() {
			}
		});
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
		// 不可提现金额
		/*
		 * mTv_moneyYes.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent intent = new
		 * Intent(getApplicationContext(),NoWithdrawLogActivity.class);
		 * startActivity(intent); } });
		 */
		ll_tixian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rank == -1) {
					getMemberRank();
					SDToast.showToast("网络异常,请重新点击!");
				} else {
					if (rank == 1) {// 青铜
						startActivity(new Intent(WithdrawLogActivity.this, MemberRankActivity.class));
					} else {
						Intent intent = new Intent(getApplicationContext(), DistributionWithdrawActivity.class);
						intent.putExtra("money", Float.parseFloat(resultCommissionLog.getSalary())
								- Float.parseFloat(resultCommissionLog.getBlocksalary()) + "");
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
					SDToast.showToast("没有更多数据了");
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
		httpHelper.getCommissionLog(mPage.getPage()+"","10");

//		RequestModel model = new RequestModel();
//		model.putCtl("fxwithdraw");
//		model.putAct("moneylog");
//		model.putPage(mPage);
//		model.put("type", 1);
//		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>() {
//			@Override
//			public void onStart() {
//				super.onStart();
//			}
//
//			@Override
//			public void onSuccess(ResponseInfo<String> responseInfo) {
//				Uc_money_Zijin_LogActModel actModel = JsonUtil.json2Object(responseInfo.result,
//						Uc_money_Zijin_LogActModel.class);
//
//				if (actModel.getStatus() == 1) {
//					mActModel = actModel;
//					if (mPage.getPage() == 1) {
//						if (actModel == null || actModel.getResult() == null || actModel.getResult().size() == 0) {
//							ll_empty.setVisibility(View.VISIBLE);
//						} else {
//							ll_empty.setVisibility(View.GONE);
//						}
//					} else {
//						ll_empty.setVisibility(View.GONE);
//					}
//
//					mPage.update(actModel.getPage());
//					initIntentData();
//					SDViewUtil.updateAdapterByList(mListModel, mActModel.getResult(), mAdapter, isLoadMore);
//				}
//
//			}
//
//			@Override
//			public void onFinish() {
//				mPtrlv_content.onRefreshComplete();
//				super.onFinish();
//			}
//		});
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("资金日志");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		httpHelper.onDestroy();
	}

	@Override
	public void onSuccess(String responseBody) {

	}

	@Override
	public void onSuccess(String method, List datas) {
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
			initIntentData();
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
