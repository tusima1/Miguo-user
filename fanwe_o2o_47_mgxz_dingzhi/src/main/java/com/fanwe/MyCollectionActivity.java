package com.fanwe;

import android.os.Bundle;
import android.view.View;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.MyCollectionEventFragment;
import com.fanwe.fragment.MyCollectionTuanGoodsFragment;
import com.fanwe.fragment.MyCollectionYouhuiFragment;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewAttr;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的收藏
 * 
 * @author js02
 * 
 */
public class MyCollectionActivity extends BaseActivity
{
	@ViewInject(R.id.tab_goods)
	private SDTabItemCorner mTab_goods;

	@ViewInject(R.id.tab_youhui)
	private SDTabItemCorner mTab_youhui;

	@ViewInject(R.id.tab_event)
	private SDTabItemCorner mTab_event;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_my_collection);
		init();
	}

	private void init()
	{
		initTitle();
		initTabs();
	}

	private void initTabs()
	{
		mTab_goods.getmAttr().setmBackgroundColorNormalResId(R.color.white);
		mTab_goods.getmAttr().setmBackgroundColorSelectedResId(R.color.main_color);
		mTab_goods.getmAttr().setmTextColorNormalResId(R.color.main_color);
		mTab_goods.getmAttr().setmTextColorSelectedResId(R.color.white);
		mTab_goods.getmAttr().setmStrokeColorResId(R.color.main_color);
		mTab_goods.getmAttr().setmStrokeWidth(SDViewUtil.dp2px(1));

		mTab_goods.setTabName(SDResourcesUtil.getString(R.string.tuan_gou_and_goods));
		mTab_goods.setTabTextSizeSp(14);
		mTab_goods.setmPosition(EnumTabPosition.FIRST);

		mTab_youhui.setmAttr((SDViewAttr) mTab_goods.getmAttr().clone());
		mTab_youhui.setTabName(SDResourcesUtil.getString(R.string.youhui_coupon));
		mTab_youhui.setTabTextSizeSp(14);
		mTab_youhui.setmPosition(EnumTabPosition.MIDDLE);

		mTab_event.setmAttr((SDViewAttr) mTab_goods.getmAttr().clone());
		mTab_event.setTabName(SDResourcesUtil.getString(R.string.event));
		mTab_event.setTabTextSizeSp(14);
		mTab_event.setmPosition(EnumTabPosition.LAST);

		mViewManager.setItems(new SDViewBase[] { mTab_goods, mTab_youhui, mTab_event });
		mViewManager.setmListener(new SDViewNavigatorManagerListener()
		{
			@Override
			public void onItemClick(View v, int index)
			{
				switch (index)
				{
				case 0:
					clickGoods();
					break;
				case 1:
					clickYouhui();
					break;
				case 2:
					clickEvent();
					break;
				default:
					break;
				}
			}
		});
		mViewManager.setSelectIndex(0, mTab_goods, true);

	}

	protected void clickGoods()
	{
		getSDFragmentManager().toggle(R.id.act_my_collection_fl_content, null, MyCollectionTuanGoodsFragment.class);
	}

	protected void clickYouhui()
	{
		getSDFragmentManager().toggle(R.id.act_my_collection_fl_content, null, MyCollectionYouhuiFragment.class);
	}

	protected void clickEvent()
	{
		getSDFragmentManager().toggle(R.id.act_my_collection_fl_content, null, MyCollectionEventFragment.class);
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("我的收藏");
	}
}