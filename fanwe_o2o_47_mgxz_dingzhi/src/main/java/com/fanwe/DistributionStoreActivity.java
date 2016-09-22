package com.fanwe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.common.CommonInterface;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.fragment.DistributionStoreBaseDealFragment;
import com.fanwe.fragment.DistributionStoreGoodsFragment;
import com.fanwe.fragment.DistributionStoreTuanFragment;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.SDTabItemCorner;
import com.fanwe.library.customview.SDTabItemCorner.EnumTabPosition;
import com.fanwe.library.customview.SDViewBase;
import com.fanwe.library.customview.SDViewNavigatorManager;
import com.fanwe.library.customview.SDViewNavigatorManager.SDViewNavigatorManagerListener;
import com.fanwe.library.customview.StickyScrollView;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.MyDistributionUser_dataModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.Uc_fx_mallActModel;
import com.fanwe.o2o.miguo.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 分销小店
 * 
 * @author Administrator
 * 
 */
public class DistributionStoreActivity extends BaseActivity
{
	@ViewInject(R.id.ssv_all)
	private SDStickyScrollView mSsv_all;

	@ViewInject(R.id.siv_image)
	private ImageView mSiv_image;

	@ViewInject(R.id.iv_user_avatar)
	private CircularImageView miv_user_avatar;

	@ViewInject(R.id.tv_username)
	private TextView mTv_username;

	@ViewInject(R.id.tab_tuan)
	private SDTabItemCorner mTab_tuan;

	@ViewInject(R.id.tab_goods)
	private SDTabItemCorner mTab_goods;

	private SDViewNavigatorManager mViewManager = new SDViewNavigatorManager();

	private DistributionStoreBaseDealFragment mFragDeal;
	private DistributionStoreTuanFragment mFragTuan;
	private DistributionStoreGoodsFragment mFragGoods;

	private PageModel mPage;

	/** 0:团购，1:商品 */
	private int mType;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_distribution_store);
		init();
	}

	private void init()
	{
		initTitle();
		initTabs();
		initScrollView();
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("小店");
	}

	private void initTabs()
	{
		mTab_tuan.setTabName("团购精品");
		mTab_tuan.setTabTextSizeSp(14);
		mTab_tuan.setmPosition(EnumTabPosition.FIRST);

		mTab_goods.setTabName("商城热销");
		mTab_goods.setTabTextSizeSp(14);
		mTab_goods.setmPosition(EnumTabPosition.LAST);

		SDViewBase[] items = new SDViewBase[] { mTab_tuan, mTab_goods };
		mViewManager.setItems(items);
		mViewManager.setmListener(new SDViewNavigatorManagerListener()
		{

			@Override
			public void onItemClick(View v, int index)
			{
				boolean needRefresh = false;
				switch (index)
				{
				case 0:
					if (mFragTuan == null)
					{
						needRefresh = true;
					}
					mFragTuan = (DistributionStoreTuanFragment) getSDFragmentManager().toggle(R.id.act_distribution_store_fl_content, null,
							DistributionStoreTuanFragment.class);
					break;
				case 1:
					if (mFragGoods == null)
					{
						needRefresh = true;
					}
					mFragGoods = (DistributionStoreGoodsFragment) getSDFragmentManager().toggle(R.id.act_distribution_store_fl_content, null,
							DistributionStoreGoodsFragment.class);
					break;

				default:
					break;
				}
				if (needRefresh)
				{
					mSsv_all.setRefreshing();
				}
			}
		});
		mViewManager.setSelectIndex(0, null, true);
	}

	private void initScrollView()
	{
		mSsv_all.setMode(Mode.BOTH);
		mSsv_all.setOnRefreshListener(new OnRefreshListener2<StickyScrollView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{
				pullRefresh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{
				pullLoadMore();
			}
		});
	}

	protected void pullRefresh()
	{
		resetPage();
		requestData(false);
	}

	private DistributionStoreBaseDealFragment getSelectFragment()
	{
		DistributionStoreBaseDealFragment frag = null;
		switch (mViewManager.getSelectedIndex())
		{
		case 0:
			frag = mFragTuan;
			break;
		case 1:
			frag = mFragGoods;
			break;

		default:
			break;
		}
		return frag;
	}

	private void findParams()
	{
		mFragDeal = getSelectFragment();
		if (mFragDeal != null)
		{
			mPage = mFragDeal.getmPage();
			mType = mFragDeal.getmType();
		}
	}

	private void resetPage()
	{
		findParams();
		if (mPage != null)
		{
			mPage.resetPage();
		}
	}

	protected void pullLoadMore()
	{
		findParams();
		if (mPage != null)
		{
			if (mPage.increment())
			{
				requestData(true);
			} else
			{
				MGToast.showToast("没有更多数据了");
				mSsv_all.onRefreshComplete();
			}
		}
	}

	private void requestData(final boolean isLoadMore)
	{
		CommonInterface.requestDistributionStore(mType, mPage.getPage(), new SDRequestCallBack<Uc_fx_mallActModel>()
		{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					MyDistributionUser_dataModel userData = actModel.getUser_data();
					if (userData != null)
					{
						SDViewBinder.setImageView(mSiv_image, userData.getFx_mall_bg());
						SDViewBinder.setImageView(miv_user_avatar, userData.getUser_avatar());
						SDViewBinder.setTextView(mTv_username, userData.getUser_name());
						String userName = userData.getUser_name();
						mTitle.setMiddleTextTop(userName + "的小店");
					}

					if (mFragDeal != null)
					{
						if (isLoadMore)
						{
							mFragDeal.pullLoadMore(actModel);
						} else
						{
							mFragDeal.pullRefresh(actModel);
						}
					}
				}
			}

			@Override
			public void onStart()
			{
			}

			@Override
			public void onFinish()
			{
				mSsv_all.onRefreshComplete();
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
			}
		});
	}

}
