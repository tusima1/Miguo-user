package com.fanwe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class BasePullToRefreshScrollViewActivity extends BaseActivity implements OnRefreshListener2<ScrollView>
{

	protected PullToRefreshScrollView mPtrsv_all;

	@Override
	public void setContentView(View view)
	{
		View viewScrollParent = LayoutInflater.from(this).inflate(R.layout.view_pull_to_refresh_scrollview, null);
		mPtrsv_all = (PullToRefreshScrollView) viewScrollParent.findViewById(R.id.ptrsv_all);
		mPtrsv_all.setOnRefreshListener(this);

		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		mPtrsv_all.getRefreshableView().addView(view, params);
		super.setContentView(viewScrollParent);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
	{

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
	{

	}

	public void setRefreshing()
	{
		mPtrsv_all.setRefreshing();
	}

	public void setMode(Mode mode)
	{
		mPtrsv_all.setMode(mode);
	}

	public void setModePullFromStart()
	{
		setMode(Mode.PULL_FROM_START);
	}

	public void setModePullFromEnd()
	{
		setMode(Mode.PULL_FROM_END);
	}

	public void setModeBoth()
	{
		setMode(Mode.BOTH);
	}

	public void setModeDisabled()
	{
		setMode(Mode.DISABLED);
	}

	public void onRefreshComplete()
	{
		mPtrsv_all.onRefreshComplete();
	}
}
