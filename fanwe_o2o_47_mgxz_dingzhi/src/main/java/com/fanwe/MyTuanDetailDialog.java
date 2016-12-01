package com.fanwe;

import com.fanwe.customview.SDStickyScrollView;
import com.fanwe.event.EnumEventTag;
import com.fanwe.fragment.TuanDetailAttrsFragment;
import com.fanwe.fragment.TuanDetailBuyNoticelFragment;
import com.fanwe.fragment.TuanDetailCombinedPackagesFragment;
import com.fanwe.fragment.TuanDetailCommentFragment;
import com.fanwe.fragment.TuanDetailDetailFragment;
import com.fanwe.fragment.TuanDetailImagePriceFragment;
import com.fanwe.fragment.TuanDetailMoreDetailFragment;
import com.fanwe.fragment.TuanDetailOtherMerchantFragment;
import com.fanwe.fragment.TuanDetailRatingFragment;
import com.fanwe.library.customview.StickyScrollView;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MyTuanDetailDialog extends BaseActivity{

	/** 商品id (int) */
	public static final String EXTRA_GOODS_ID = "extra_goods_id";

	@ViewInject(R.id.ll_add_distribution)
	private LinearLayout mLl_add_distribution;

	@ViewInject(R.id.act_tuan_detail_ssv_scroll)
	private SDStickyScrollView mScrollView;
	

	@ViewInject(R.id.act_tuan_detail_fl_attr)
	private FrameLayout mFlAttr;
	
	@ViewInject(R.id.btn_delect)
	private Button mBtn_delete;
	
	@ViewInject(R.id.btn_add)
	private Button mBtn_add;
	
	public static int resultCode =111;
	
	private int mId;
	private Deal_indexActModel mGoodsModel;

	private TuanDetailImagePriceFragment mFragImagePrice;
	private TuanDetailDetailFragment mFragDetail;
	private TuanDetailRatingFragment mFragRating;
	
	private TuanDetailOtherMerchantFragment mFragOtherMerchant;
	private TuanDetailAttrsFragment mFragAttr;
	private TuanDetailCombinedPackagesFragment mFragCombinedPackages;
	private TuanDetailMoreDetailFragment mFragMoreDetail;
	private TuanDetailBuyNoticelFragment mFragBuyNotice;
	private TuanDetailCommentFragment mFragComment;

	public TuanDetailAttrsFragment getTuanDetailAttrsFragment()
	{
		return mFragAttr;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flag_tuan_detail);
		init();
	}

	private void init()
	{
		
		getIntentData();
		if (mId <= 0)
		{
			MGToast.showToast("id为空");
			finish();
			return;
		}
		 WindowManager m = getWindowManager();    
		   //为获取屏幕宽、高    
	       Display d = m.getDefaultDisplay();  
	       //获取对话框当前的参数值    
	       android.view.WindowManager.LayoutParams p = getWindow().getAttributes();
	       //高度设置为屏幕的1.0   
	       p.height = (int) (d.getHeight() * 0.7); 
	       //宽度设置为屏幕的0.8   
	       p.width = (int) (d.getWidth() * 0.9);    
	       //设置本身透明度  
	       p.alpha = 1.0f;     
	       //设置黑暗度  
	       p.dimAmount = 0.2f; 
	       getWindow().setAttributes(p);
		registerClick();
		initScrollView();
		
	}


	private void registerClick() {
		
		mBtn_delete.setOnClickListener(this);
		mBtn_add.setOnClickListener(this);
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

	public void scrollToAttr()
	{
		SDViewUtil.scrollToViewY(mScrollView.getRefreshableView(), (int) mFlAttr.getY(), 100);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	/**
	 * 请求商品详情接口
	 */
	private void requestDetail()
	{
	}

	protected void bindData()
	{
		if (mGoodsModel == null)
		{
			return;
		}
		addFragments(mGoodsModel);
	}

	private void getIntentData()
	{
		Intent intent = getIntent();
		mId = intent.getIntExtra(EXTRA_GOODS_ID, -1);
	}

	/**
	 * 添加fragment
	 */
	private void addFragments(Deal_indexActModel model)
	{
		if (model == null)
		{
			return;
		}

		mFragImagePrice = new TuanDetailImagePriceFragment();
		mFragImagePrice.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_image_price, mFragImagePrice);

		mFragDetail = new TuanDetailDetailFragment();
		mFragDetail.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_detail, mFragDetail);

		// ---------------评分----------------
		mFragRating = new TuanDetailRatingFragment();
		mFragRating.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_rating, mFragRating);

		// ---------------商品属性----------------
		mFragAttr = new TuanDetailAttrsFragment();
		mFragAttr.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_attr, mFragAttr);

		// ---------------组合推荐----------------
		mFragCombinedPackages = new TuanDetailCombinedPackagesFragment();
		mFragCombinedPackages.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_combined_packages, mFragCombinedPackages);

		// ---------------购买须知----------------
		mFragBuyNotice = new TuanDetailBuyNoticelFragment();
		mFragBuyNotice.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_buy_notice, mFragBuyNotice);

		// ---------------更多详情----------------
		mFragMoreDetail = new TuanDetailMoreDetailFragment();
		mFragMoreDetail.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_more_detail, mFragMoreDetail);

		// ---------------其他门店----------------
		mFragOtherMerchant = new TuanDetailOtherMerchantFragment();
		mFragOtherMerchant.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_other_merchant, mFragOtherMerchant);
		
		// ---------------评论----------------
		mFragComment = new TuanDetailCommentFragment();
		mFragComment.setmDealModel(model);
		getSDFragmentManager().replace(R.id.act_tuan_detail_fl_comment, mFragComment);
	}

	@Override
	public void onClick(View v)
	{
		if(v == mBtn_add)
		{
			boolean add = true;
			Intent data = new Intent();
			data.putExtra("mId", mId);
			data.putExtra("add",add);
			setResult(resultCode, data);
			finish();
		}else if(v == mBtn_delete)
		{
			
			this.finish();
			
		}
	}
	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case COMMENT_SUCCESS:
			requestDetail();
			break;
		case ADD_DISTRIBUTION_GOODS_SUCCESS:
			requestDetail();
		default:
			break;
		}
	}

}
