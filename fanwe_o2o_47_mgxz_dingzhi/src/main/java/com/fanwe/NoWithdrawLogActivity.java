package com.fanwe;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.adapter.NoWithdrawLogAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.http.InterfaceServer;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_money_Zijin_LogActModel;
import com.fanwe.model.ZijinLogModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;


public class NoWithdrawLogActivity extends BaseActivity {
	@ViewInject(R.id.ptrlv_no_content)
	private PullToRefreshScrollView mPtrlv_noContent;

	@ViewInject(R.id.tv_no_circle)
	private TextView mTv_noCircle;

	@ViewInject(R.id.ll_no_tixian)
	private Button mBt_noTixian;

	@ViewInject(R.id.lv_no_withdraw)
	private SDListViewInScroll mLv_Scroll;

	private PageModel mPage = new PageModel();

	private List<ZijinLogModel> mListModel = new ArrayList<ZijinLogModel>();
	protected Uc_money_Zijin_LogActModel mActModel;

	private NoWithdrawLogAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_no_withdraw);
		init();
	}

	private void init() {
		initTitle();
		bindDefaultData();
		initPullToRefreshScrollView();
	}

	private void initIntentData() {

		if (mActModel == null) {
			return;
		}

		mBt_noTixian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShareRedPacketActivity.class);
				intent.putExtra(
						"money",
						Float.parseFloat(mActModel.getSalary())
								- Float.parseFloat(mActModel.getBlocksalary())
								+ "");
				startActivity(intent);
			}
		});
	}

	private void bindDefaultData() {
		mAdapter = new NoWithdrawLogAdapter(mListModel, this);
		mLv_Scroll.setAdapter(mAdapter);
	}

	@SuppressWarnings("unchecked")
	private void initPullToRefreshScrollView() {
		mPtrlv_noContent.setMode(Mode.BOTH);
		mPtrlv_noContent.setOnRefreshListener(new OnRefreshListener2() {

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
					mPtrlv_noContent.onRefreshComplete();
				}
			}
		});
		mPtrlv_noContent.setRefreshing();
	}

	@Override
	protected void onResume() {
		requestData(false);
		super.onResume();
	}

	private void requestData(final boolean isLoadMore) {
		RequestModel model = new RequestModel();
		model.putCtl("fxwithdraw");
		model.putAct("moneylog");
		model.putPage(mPage);
		model.put("type", 1);
		model.put("block", 1);
		InterfaceServer.getInstance().requestInterface(model,
				new RequestCallBack<String>() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {

						Uc_money_Zijin_LogActModel actModel = JsonUtil
								.json2Object(responseInfo.result,
										Uc_money_Zijin_LogActModel.class);
						if (mPage.getPage()==1) {
							mTv_noCircle.setText("不可提现金额 "+actModel.getBlocksalary()+" (元)");
						}

						if (actModel.getStatus() == 1) {
							
							if (mPage.getPage()==1) {
								mListModel.clear();
							}
							if (!isLoadMore) {
								mListModel.addAll(actModel.getResult());
							}
							
							mActModel = actModel;
							mPage.update(actModel.getPage());
							initIntentData();
							SDViewUtil
									.updateAdapterByList(mListModel,
											mActModel.getResult(), mAdapter,
											isLoadMore);
						}

					}

					@Override
					public void onFinish() {
						mPtrlv_noContent.onRefreshComplete();
						super.onFinish();
					}
				});
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("资金日志");
	}
}
