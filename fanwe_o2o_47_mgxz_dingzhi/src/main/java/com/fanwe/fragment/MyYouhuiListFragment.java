package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.YouHuiDetailActivity;
import com.fanwe.adapter.MyYouhuiListAdapter;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_youhuiActItemModel;
import com.fanwe.model.Uc_youhui_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyYouhuiListFragment extends BaseFragment
{

	public static final class YouhuiTag
	{
		/** 所有 */
		public static final int ALL = 0;
		/** 快过期 */
		public static final int WILL_OVERDUE = 1;
		/** 未使用 */
		public static final int UN_USED = 2;
		/** 已失效 */
		public static final int OVERDUE = 3;
	}

	@ViewInject(R.id.ptrlv_content)
	private PullToRefreshListView mPtrlv_content;

	@ViewInject(R.id.ll_empty)
	private LinearLayout mIv_empty;
	
	

	private MyYouhuiListAdapter mAdapter;
	private List<Uc_youhuiActItemModel> mListModel = new ArrayList<Uc_youhuiActItemModel>();

	private PageModel mPage = new PageModel();

	private int mStatus;

	public void setmStatus(int mStatus)
	{
		this.mStatus = mStatus;
	}

	public static MyYouhuiListFragment newInstance(int status)
	{
		MyYouhuiListFragment fragment = new MyYouhuiListFragment();
		fragment.setmStatus(status);
		return fragment;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_my_youhui_list);
	}

	@Override
	protected void init()
	{
		super.init();
		findViews();
		bindDefaultData();
		initPullToRefreshListView();
	}

	private void findViews()
	{
		View view = getView();

		mPtrlv_content = (PullToRefreshListView) view.findViewById(R.id.ptrlv_content);
		mIv_empty = (LinearLayout) view.findViewById(R.id.ll_empty);
		
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
				requestYouhuiList(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				if (!mPage.increment())
				{
					SDToast.showToast("没有更多数据了");
					mPtrlv_content.onRefreshComplete();
				} else
				{
					requestYouhuiList(true);
				}
			}
		});
		mPtrlv_content.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
			{
				Uc_youhuiActItemModel model = mAdapter.getItem((int) id);
				if (model != null)
				{
					Intent intent = new Intent(getActivity(), YouHuiDetailActivity.class);
					intent.putExtra(YouHuiDetailActivity.EXTRA_YOUHUI_ID, model.getId());
					startActivity(intent);
				}
			}
		});

		mPtrlv_content.setRefreshing();
	}

	private void requestYouhuiList(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_youhui");
		model.putUser();
		model.put("tag", mStatus);
		model.putPage(mPage.getPage());
		SDRequestCallBack<Uc_youhui_indexActModel> handler = new SDRequestCallBack<Uc_youhui_indexActModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					mPage.update(actModel.getPage());
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
				SDViewUtil.toggleEmptyMsgByList(mListModel, mIv_empty);
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	private void bindDefaultData()
	{
		mAdapter = new MyYouhuiListAdapter(mListModel, getActivity());
		mPtrlv_content.setAdapter(mAdapter);
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}