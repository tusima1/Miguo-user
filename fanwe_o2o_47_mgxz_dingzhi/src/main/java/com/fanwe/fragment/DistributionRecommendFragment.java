package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 分销推荐
 * 
 * @author Administrator
 * 
 */
public class DistributionRecommendFragment extends BaseFragment
{

	@ViewInject(R.id.tab_my_distribution_recommend)
	private SDTabItemCorner mTab_my_distribution_recommend;

	@ViewInject(R.id.tab_distribution_money_log)
	private SDTabItemCorner mTab_distribution_money_log;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.frag_distribution_recommend);
	}

	@Override
	protected void init()
	{
		super.init();
		initTitle();
		initTabs();
	}

	private void initTabs()
	{
		mTab_my_distribution_recommend.setTabName("分销推荐");
		mTab_my_distribution_recommend.setTabTextSizeSp(14);
		mTab_my_distribution_recommend.setmPosition(EnumTabPosition.FIRST);

		mTab_distribution_money_log.setTabName("分销资金日志");
		mTab_distribution_money_log.setTabTextSizeSp(14);
		mTab_distribution_money_log.setmPosition(EnumTabPosition.LAST);

		SDViewBase[] items = new SDViewBase[] { mTab_my_distribution_recommend, mTab_distribution_money_log };
		mViewManager.setItems(items);
		mViewManager.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0:
					getSDFragmentManager().toggle(R.id.frag_distribution_recommend_fl_content, null, MyDistributionRecommendFragment.class);
					break;
				case 1:
					getSDFragmentManager().toggle(R.id.frag_distribution_recommend_fl_content, null, MyDistributionMoneyLogFragment.class);
					break;

				default:
					break;
				}
			}
		});
		mViewManager.setSelectIndex(0, null, true);
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("分销推荐");
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
