package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.MyOrderListActivity;
import com.fanwe.adapter.MyOrderListAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_orderModel;
import com.fanwe.model.Uc_order_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * 我的订单fragment
 * 
 * @author js02
 * 
 */
public class MyOrderListFragment extends BaseFragment
{

	@ViewInject(R.id.ptrlv_content)
	private PullToRefreshListView mPtrlv_content;

	@ViewInject(R.id.ll_empty)
	private View mLl_empty;

	private MyOrderListAdapter mAdapter;
	private List<Uc_orderModel> mListModel = new ArrayList<Uc_orderModel>();

	private int mPage =1;

	private String mPayStatus;
	
	private boolean mStatus = false;

	protected Uc_order_indexActModel mActModel;

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.frag_my_order_list);
	}

	@Override
	protected void init()
	{
		super.init();
		getIntentData();
		initTitle();
		initPullToRefreshListView();
		bindDefaultData();
	}

	private void initTitle()
	{
		String title = "";
		if(mPayStatus.equals("pay_wait"))
		{
			title = "待付款订单";
		}else if(mPayStatus.equals("refund"))
		{
			title = "退款订单";
		}else if(mPayStatus.equals("use_wait"))
		{
			title = "待消费订单";
		}else if(mPayStatus.equals("comment_wait"))
		{
			title = "待评价订单";
		}else if(mPayStatus.equals("all")){
			title = "全部订单";
		}
		
		mTitle.setMiddleTextTop(title);
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextTop("编辑");
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		super.onCLickRight_SDTitleSimple(v, index);
		if(mStatus)
		{
			bindDefaultData();
			mTitle.getItemRight(0).setTextTop("编辑");
			mStatus = false;
		}else
		{
			mAdapter = new MyOrderListAdapter(mListModel, getActivity(),true);
			mPtrlv_content.setAdapter(mAdapter);
			mTitle.getItemRight(0).setTextTop("完成");
			mStatus = true;
		}
	}
	private void getIntentData()
	{
		mPayStatus = getActivity().getIntent().getStringExtra(MyOrderListActivity.EXTRA_ORDER_STATUS);
	}

	private void bindDefaultData()
	{
		mAdapter = new MyOrderListAdapter(mListModel, getActivity(),false);
		mPtrlv_content.setAdapter(mAdapter);
	}
	
	private void initPullToRefreshListView()
	{
		mPtrlv_content.setMode(Mode.BOTH);
		mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mPage = 1;
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				if(mActModel.getItem() != null)
				{
					if (mActModel.getItem().size() < mActModel.getPage_size())
					{
						SDToast.showToast("没有更多数据了");
						mPtrlv_content.onRefreshComplete();
					} else
					{
						mPage = mActModel.getPage_now()+1;
						requestData(true);
					}
				}
			}
		});
		
	}

	protected void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_order");
		model.putAct("orders");
		model.put("type", mPayStatus);
		model.putUser();
		model.putPage(mPage);
		SDRequestCallBack<Uc_order_indexActModel> handler = new SDRequestCallBack<Uc_order_indexActModel>()
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					mActModel = actModel;
					SDViewUtil.updateAdapterByList(mListModel, actModel.getItem(), mAdapter, isLoadMore);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				mPtrlv_content.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mListModel, mLl_empty);
			}
		};

		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case COMMENT_SUCCESS:
			refreshData();
			break;
		case PAY_ORDER_SUCCESS:
			refreshData();
			break;
		case REFRESH_ORDER_LIST:
			refreshData();
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mPtrlv_content.setRefreshing();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}

}