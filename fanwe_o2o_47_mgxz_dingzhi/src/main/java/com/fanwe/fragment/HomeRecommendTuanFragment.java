package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.fanwe.TuanListActivity;
import com.fanwe.adapter.TuanListAdapter;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.customview.SDGridLinearLayout;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsModel;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDValidateUtil;
import com.fanwe.zxing.InactivityTimer;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;
import com.umeng.socialize.utils.Log;

/**
 * 首页推荐团购
 * 
 * @author js02
 * 
 */
public class HomeRecommendTuanFragment extends BaseFragment
{

	@ViewInject(R.id.frag_home_recommend_deals_ll_deals)
	protected SDListViewInScroll mLlDeals;
	
	@ViewInject(R.id.ll_tuanlist)
	private LinearLayout mLl_tuanlist;

	@ViewInject(R.id.tv_see_all_tuan)
	private TextView mTv_see_all_tuan;
	
	@ViewInject(R.id.iv_image_tuan)
	private ImageView iv_image;
	
	@ViewInject(R.id.f_image_tuan)
	private FrameLayout f_image_tuan;
	
	private List<GoodsModel> mListModel = new ArrayList<GoodsModel>();
	
	protected TuanListAdapter mAdapter;
	
	private int page;
	
	public void setmIndexModel(List<GoodsModel> inActModel,int page)
	{
		if (inActModel == null && !SDCollectionUtil.isEmpty(mListModel)) {
			this.mListModel.clear();
		}
		this.mListModel = inActModel;
		this.page = page;
	}
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_home_recommend_tuan);
	}
	
	@Override
	protected void init()
	{
		super.init();
		bindData();
		registeClick();
	}

	public TuanListAdapter getAdapter()
	{
		return new TuanListAdapter(mListModel, getActivity());
		
	}

	private void bindData()
	{
		if(mListModel == null || isEmpty(mListModel) )
		{
			SDViewUtil.hide(mLl_tuanlist);
			
			SDViewUtil.show(f_image_tuan);
			
		}else
		{
			if(page == 3)
			{
				SDViewUtil.show(mTv_see_all_tuan);
			}else
			{
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
				    mLlDeals.setLayoutParams(params);
				SDViewUtil.hide(mTv_see_all_tuan);
			}
			SDViewUtil.hide(iv_image);
		}
		mAdapter = getAdapter();    
		mLlDeals.setAdapter(mAdapter);
	}

	private void registeClick()
	{
		mTv_see_all_tuan.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickSeeAllTuan();
			}
		});
	}

	private void clickSeeAllTuan()
	{
		startActivity(new Intent(getActivity(), TuanListActivity.class));
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
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}