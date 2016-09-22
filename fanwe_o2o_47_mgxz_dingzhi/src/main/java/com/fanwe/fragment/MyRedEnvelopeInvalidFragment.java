package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.fanwe.adapter.MyRedEnvelopeAdapter;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RedEnvelopeModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_ecv_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.sunday.eventbus.SDBaseEvent;

/**
 * 失效红包
 * 
 * @author Administrator
 * 
 */
public class MyRedEnvelopeInvalidFragment extends BaseFragment
{

	protected PullToRefreshListView mPtrlv_content;

	protected MyRedEnvelopeAdapter mAdapter;
	protected List<RedEnvelopeModel> mListModel = new ArrayList<RedEnvelopeModel>();

	protected Uc_ecv_indexActModel mActModel;
	protected PageModel mPage = new PageModel();
	/** 1:已失效，0:可使用 */
	protected int n_valid = 1;

	private LinearLayout mLl_empty;
	
	

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_my_red_envelope_invalid);
	}

	@Override
	protected void init()
	{
		super.init();
		findViews();
		bindDefaultData();
		initPullToRefreshListView();
	}

	protected void findViews()
	{
		mPtrlv_content = (PullToRefreshListView) findViewById(R.id.ptrlv_content);
		mLl_empty = (LinearLayout) findViewById(R.id.ll_empty);
	}

	private void bindDefaultData()
	{
		mAdapter = new MyRedEnvelopeAdapter(mListModel, getActivity());
		mPtrlv_content.getRefreshableView().setAdapter(mAdapter);
	}

	private void initPullToRefreshListView()
	{
		mPtrlv_content.setMode(Mode.BOTH);
		mPtrlv_content.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mPage.resetPage();
				requestData(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				if (mPage.increment())
				{
					requestData(true);
				} else
				{
					MGToast.showToast("没有更多数据了");
					mPtrlv_content.onRefreshComplete();
				}
			}
		});
		mPtrlv_content.setRefreshing();
	}

	protected void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_red_packet");
		model.put("invalid", n_valid);
		model.putPage(mPage.getPage());
		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Uc_ecv_indexActModel actModel = JsonUtil.json2Object(responseInfo.result, Uc_ecv_indexActModel.class);
				if (actModel.getStatus() > 0)
				{
					mActModel = actModel;
					mPage.update(actModel.getPage());
					bindData(isLoadMore);
				}
			}

			@Override
			public void onFinish()
			{
				mPtrlv_content.onRefreshComplete();
				SDViewUtil.toggleEmptyMsgByList(mListModel, mLl_empty);
				super.onFinish();
			}

		});
	}

	protected void bindData(boolean isLoadMore)
	{
		SDViewUtil.updateAdapterByList(mListModel, mActModel.getList(), mAdapter, isLoadMore);
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case EXCHANGE_RED_ENVELOPE_SUCCESS:
			requestData(false);
			break;
		case GET_RED_ENVELOPE_SUCCESS:
			requestData(false);
			break;

		default:
			break;
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
