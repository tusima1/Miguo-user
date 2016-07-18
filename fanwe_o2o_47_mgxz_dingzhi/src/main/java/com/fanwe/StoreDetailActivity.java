package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.HoltelDetailFragment;
import com.fanwe.fragment.StoreDetailBriefFragment;
import com.fanwe.fragment.StoreDetailCommentFragment;
import com.fanwe.fragment.StoreDetailGoodsFragment;
import com.fanwe.fragment.StoreDetailInfoFragment;
import com.fanwe.fragment.StoreDetailInfoFragment.OnStoreDetailListener;
import com.fanwe.fragment.StoreDetailOtherStoreFragment;
import com.fanwe.fragment.StoreDetailTuanFragment;
import com.fanwe.fragment.StoreDetailYouhuiFragment;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.customview.StickyScrollView;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.RequestModel;
import com.fanwe.model.StoreActModel;
import com.fanwe.model.Store_infoModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.content.Intent;
import android.os.Bundle;

/**
 * 门店详情
 * 
 * @author js02
 * 
 */
public class StoreDetailActivity extends BaseActivity
{

	/** 商家id (int) */
	public static final String EXTRA_MERCHANT_ID = "extra_merchant_id";
	public static final String EXTRA_SHOP_ID = "extra_shop_id";

	@ViewInject(R.id.ssv_scroll)
	private SDStickyScrollView mScrollView;
	
	private StoreDetailInfoFragment mFragInfo;
	private StoreDetailOtherStoreFragment mFragOtherSupplier;
	private StoreDetailBriefFragment mFragBrief;
	private StoreDetailTuanFragment mFragTuan;
	private StoreDetailGoodsFragment mFragGoods;
	private StoreDetailYouhuiFragment mFragYouhui;
	private StoreDetailCommentFragment mFragComment;

	private StoreActModel mActModel;
	
	private int mShopId=-1;

	private int mType;
	
	private int MerchantID=-1;

	private String begin;  
	
	private String end;

	protected int mDay;

	protected HoltelDetailFragment mFragHotel2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_store_detail);
		init();
	}
	
	private void init()
	{
		getIntentData();
		if (mShopId <= 0 && MerchantID<=0)
		{
			SDToast.showToast("id为空");
			finish();
			return;
		}
		initTitle();
		initScrollView();
	}

	private void initScrollView()
	{
		mScrollView.setMode(Mode.PULL_FROM_START);
		mScrollView.setOnRefreshListener(new OnRefreshListener2<StickyScrollView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{
				requestDetail();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<StickyScrollView> refreshView)
			{

			}
		});
		mScrollView.setRefreshing();
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop(SDResourcesUtil.getString(R.string.store_detail));
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setImageLeft(R.drawable.ic_tuan_detail_share);
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		clickShare();
	}

	/**
	 * 分享
	 */
	private void clickShare()
	{
		if (mActModel == null)
		{
			SDToast.showToast("未找到可分享内容");
			return;
		}

		Store_infoModel infoModel = mActModel.getStore_info();
		if (infoModel == null)
		{
			SDToast.showToast("未找到可分享内容");
			return;
		}

		String content = infoModel.getName() + infoModel.getShare_url();
		String imageUrl = infoModel.getPreview();
		String clickUrl = infoModel.getShare_url();

		UmengShareManager.share(this, "分享", content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
	}

	private void getIntentData()
	{
		mShopId = getIntent().getExtras().getInt(EXTRA_SHOP_ID,-1);
		mType = getIntent().getExtras().getInt("type");
		MerchantID=getIntent().getExtras().getInt(EXTRA_MERCHANT_ID,-1);
		if(mType == 15)
		{
			begin = getIntent().getExtras().getString("begin_time");
			end = getIntent().getExtras().getString("end_time");
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	/**
	 * 加载商家详细信息
	 */
	private void requestDetail()
	{
		RequestModel model = new RequestModel();
		model.putCtl("store");
		model.putUser();
		model.put("cate_id",mType);
		
		if (MerchantID<=0) {
			model.put("data_id",mShopId);
		}else if(mShopId<=0){
			model.put("data_id", MerchantID);
		}else {
			return;
		}
		//model.put("data_id",mShopId);
		SDRequestCallBack<StoreActModel> handler = new SDRequestCallBack<StoreActModel>()
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
					mActModel = actModel;
					addFragments(actModel);
				}
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				mScrollView.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	
	private void addFragments(final StoreActModel model)
	{
		if (model == null)
		{
			return;   
		}

		// 商家信息
		mFragInfo = new StoreDetailInfoFragment();
		mFragInfo.onSetStoreDetailListener(new OnStoreDetailListener() {
			
			@Override
			public void setOnHotelNumberDay(int day) 
			{
			    mDay = day;
			    mFragHotel2 = new HoltelDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putInt("number", mDay);
				bundle.putInt("type",mType);
				mFragHotel2.setArguments(bundle);
				mFragHotel2.setmStoreModel(model);
				getSDFragmentManager().replace(R.id.act_holtel_detail_fl_home, mFragHotel2);
			}
		});
		mFragInfo.setmStoreModel(model);
		Bundle bundle1 = new Bundle();
		bundle1.putInt("type",mType);
		if(mType == 15)
		{
			bundle1.putString("begin", begin);
			bundle1.putString("end",end);
		}
		mFragInfo.setArguments(bundle1);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_info, mFragInfo);

		//其他门店
		mFragOtherSupplier = new StoreDetailOtherStoreFragment();
		mFragOtherSupplier.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_other_merchant, mFragOtherSupplier);
		
		//商家介绍
		mFragBrief = new StoreDetailBriefFragment();
		mFragBrief.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_brief, mFragBrief);
		/*//酒店房型
		mFragHotel = new HoltelDetailFragment();
		mFragHotel.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_holtel_detail_fl_home, mFragHotel);*/
		
		//商家其他团购
		mFragTuan = new StoreDetailTuanFragment();
		mFragTuan.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_tuan, mFragTuan);
		
		// 商家其他商品
		mFragGoods = new StoreDetailGoodsFragment();
		mFragGoods.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_goods, mFragGoods);

		// 商家的优惠券
		mFragYouhui = new StoreDetailYouhuiFragment();
		mFragYouhui.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_youhui, mFragYouhui);
		
		// 商家评价
		mFragComment = new StoreDetailCommentFragment();
		mFragComment.setmStoreModel(model);
		getSDFragmentManager().replace(R.id.act_store_detail_fl_comment, mFragComment);
		
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case COMMENT_SUCCESS:
			setmIsNeedRefreshOnResume(true);
			break;
			
		default:
			break;
		}
	}

	@Override
	protected void onNeedRefreshOnResume()
	{
		requestDetail();
		super.onNeedRefreshOnResume();
	}

}