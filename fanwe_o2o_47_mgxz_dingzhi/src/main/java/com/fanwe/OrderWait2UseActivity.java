package com.fanwe;

import java.util.List;

import com.fanwe.adapter.OrderWait2UseOutAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.OrderOutItem;
import com.fanwe.model.RequestModel;
import com.fanwe.model.User_Order;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class OrderWait2UseActivity extends BaseActivity {

	@ViewInject(R.id.ptrlv_content)
	private PullToRefreshListView mPtrlv_content;

	@ViewInject(R.id.ll_empty)
	private View mLl_empty;

	private int mPage = 1;

	private boolean mStatus = false;

	protected User_Order mActModel;
	private List<OrderOutItem> mOutItems;

	private OrderWait2UseOutAdapter mOutAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_order_wait2use);
		init();
	}

	private void init() {
		initTitle();
		getIntentData();
		initPullToRefreshListView();
		requestData(false);
	}

	private void initPullToRefreshListView() {
		mPtrlv_content.setMode(Mode.BOTH);
		mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				mPage = 1;
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				if (mActModel.getItem() != null) {
					if (mActModel.getItem().size() < mActModel.getPage_size()) {
						MGToast.showToast("没有更多数据了");
						mPtrlv_content.onRefreshComplete();
					} else {
						mPage = mActModel.getPage_now() + 1;
						requestData(true);
					}
				}
			}
		});
	}

	protected void requestData(final boolean isLoadMore) {
		RequestModel model = new RequestModel();
		model.putCtl("uc_order");
		model.putAct("orders");
		model.put("type", "use_wait");
		model.putUser();
		model.putPage(mPage);
		SDRequestCallBack<User_Order> handler = new SDRequestCallBack<User_Order>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					mActModel = actModel;
					if (actModel.getPage_now() == 1) {
						mOutItems = mActModel.getItem();
						bindData();
					} else {
						if (mOutItems == null) {
							mOutItems = mActModel.getItem();
						} else {
							mOutItems.addAll(mActModel.getItem());
						}
						mOutAdapter.notifyDataSetChanged();
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mPtrlv_content.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mOutItems, mLl_empty);
			}
		};

		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	protected void bindData() {
		//TODO
		mOutAdapter = new OrderWait2UseOutAdapter(mOutItems, OrderWait2UseActivity.this, false);
		mPtrlv_content.setAdapter(mOutAdapter);
	}

	private void getIntentData() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onResume() {
		super.onResume();
		mPtrlv_content.setRefreshing();
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("待消费订单");
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextTop("编辑");
	}
	
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		super.onCLickRight_SDTitleSimple(v, index);
		if(mStatus)
		{
			bindData();
			mTitle.getItemRight(0).setTextTop("编辑");
			mStatus = false;
		}else
		{
			mOutAdapter = new OrderWait2UseOutAdapter(mOutItems, OrderWait2UseActivity.this, false);
			mPtrlv_content.setAdapter(mOutAdapter);
			mTitle.getItemRight(0).setTextTop("完成");
			mStatus = true;
		}
	}

}
