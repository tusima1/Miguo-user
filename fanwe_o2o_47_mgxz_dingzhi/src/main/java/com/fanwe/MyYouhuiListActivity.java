package com.fanwe;

import android.os.Bundle;
import android.view.View;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.MyYouhuiListFragment;
import com.fanwe.fragment.MyYouhuiListFragment.YouhuiTag;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewAttr;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MyYouhuiListActivity extends BaseActivity
{

	@ViewInject(R.id.tab_unused)
	private SDTabItemCorner mTab_unused;

	@ViewInject(R.id.tab_will_overdue)
	private SDTabItemCorner mTab_will_overdue;

	@ViewInject(R.id.tab_overdue)
	private SDTabItemCorner mTab_overdue;

	@ViewInject(R.id.tab_all)
	private SDTabItemCorner mTab_all;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	private MyYouhuiListFragment mFragUnused;
	private MyYouhuiListFragment mFragWillOverdue;
	private MyYouhuiListFragment mFragOverdue;
	private MyYouhuiListFragment mFragAll;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_my_youhui_list);
		init();
	}

	private void init()
	{
		initTitle();
		createFragments();
		initTabs();
	}

	private void createFragments()
	{
		mFragUnused = MyYouhuiListFragment.newInstance(YouhuiTag.UN_USED);
		mFragWillOverdue = MyYouhuiListFragment.newInstance(YouhuiTag.WILL_OVERDUE);
		mFragOverdue = MyYouhuiListFragment.newInstance(YouhuiTag.OVERDUE);
		mFragAll = MyYouhuiListFragment.newInstance(YouhuiTag.ALL);
	}

	private void initTabs()
	{
		mTab_unused.getmAttr().setmBackgroundColorNormalResId(R.color.white);
		mTab_unused.getmAttr().setmBackgroundColorSelectedResId(R.color.main_color);
		mTab_unused.getmAttr().setmTextColorNormalResId(R.color.main_color);
		mTab_unused.getmAttr().setmTextColorSelectedResId(R.color.white);
		mTab_unused.getmAttr().setmStrokeColorResId(R.color.main_color);
		mTab_unused.getmAttr().setmStrokeWidth(SDViewUtil.dp2px(1));

		mTab_unused.setTabName("未使用");
		mTab_unused.setTabTextSizeSp(14);
		mTab_unused.setmPosition(EnumTabPosition.FIRST);

		mTab_will_overdue.setmAttr((SDViewAttr) mTab_unused.getmAttr().clone());
		mTab_will_overdue.setTabName("即将过期");
		mTab_will_overdue.setTabTextSizeSp(14);
		mTab_will_overdue.setmPosition(EnumTabPosition.MIDDLE);

		mTab_overdue.setmAttr((SDViewAttr) mTab_unused.getmAttr().clone());
		mTab_overdue.setTabName("已失效");
		mTab_overdue.setTabTextSizeSp(14);
		mTab_overdue.setmPosition(EnumTabPosition.MIDDLE);

		mTab_all.setmAttr((SDViewAttr) mTab_unused.getmAttr().clone());
		mTab_all.setTabName("全部");
		mTab_all.setTabTextSizeSp(14);
		mTab_all.setmPosition(EnumTabPosition.LAST);

		mViewManager.setItems(new SDViewBase[] { mTab_unused, mTab_will_overdue, mTab_overdue, mTab_all });
		mViewManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0: // 未使用
					clickUnused();
					break;
				case 1: // 即将过期
					clickWillOverdue();
					break;
				case 2: // 已失效
					clickOverdue();
					break;
				case 3: // 全部
					clickAll();
					break;
				default:
					break;
				}
			}
		});
		mViewManager.setSelectIndex(0, mTab_unused, true);

	}

	protected void clickUnused()
	{
		getSDFragmentManager().toggle(R.id.act_my_youhui_list_fl_content, null, mFragUnused);
	}

	protected void clickWillOverdue()
	{
		getSDFragmentManager().toggle(R.id.act_my_youhui_list_fl_content, null, mFragWillOverdue);
	}

	protected void clickOverdue()
	{
		getSDFragmentManager().toggle(R.id.act_my_youhui_list_fl_content, null, mFragOverdue);
	}

	protected void clickAll()
	{
		getSDFragmentManager().toggle(R.id.act_my_youhui_list_fl_content, null, mFragAll);
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("我的优惠券");
	}

}