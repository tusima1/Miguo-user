package com.fanwe.fragment;

import java.math.BigDecimal;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.customview.FlowLayout;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Deal_tagsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDNumberUtil;
import com.fanwe.utils.SDTimerDown;
import com.fanwe.utils.SDTimerDown.SDTimerDownListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.net.utils.SocializeNetUtils;

public class TuanDetailDetailFragment extends TuanDetailBaseFragment
{
	@ViewInject(R.id.frag_tuan_detail_second_tv_goods_name)
	private TextView mTvGoodsName;

	@ViewInject(R.id.frag_tuan_detail_second_tv_goods_detail)
	private TextView mTvGoodsDetail;

	@ViewInject(R.id.frag_tuan_detail_second_tv_buy_count)
	private TextView mTvBuyCount;

	@ViewInject(R.id.frag_tuan_detail_second_tv_time_down)
	private TextView mTvTimeDown;

	@ViewInject(R.id.frag_tuan_detail_second_ll_goods_support)
	private FlowLayout mLlGoodsSupport;
	
	@ViewInject(R.id.ll_youhui)
	private LinearLayout mLl_youhui;
	
	@ViewInject(R.id.ll_red)
	private LinearLayout mLl_red;
	
	@ViewInject(R.id.tv_red)
	private TextView mTv_red;
	
	private SDTimerDown mCounter = new SDTimerDown();

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_tuan_detail_detail);
	}

	@Override
	protected void init()
	{
		bindData();
	}

	private void bindData()
	{
		if (!toggleFragmentView(mDealModel))
		{
			return;
		}
		// 商品名称
		SDViewBinder.setTextView(mTvGoodsName, mDealModel.getSub_name());
		if(mDealModel.getIs_first() - mDealModel.getCheck_first() >0)
		{
			SDViewUtil.show(mLl_youhui);
		}else
		{
			SDViewUtil.hide(mLl_youhui);
		}
		if(mDealModel.getRed_packet() == 1)
		{
			SDViewUtil.show(mLl_red);
			String arr[]=String.valueOf(mDealModel.getRed_packet_money()).split("\\.");
			BigDecimal bd0 = new BigDecimal(mDealModel.getRed_packet_money());
			if(Integer.parseInt(arr[1]) > 0)
			{
				bd0 = bd0.setScale(1, BigDecimal.ROUND_HALF_UP);
			}else
			{
				bd0 = bd0.setScale(0, BigDecimal.ROUND_HALF_UP);
			}
			SDViewBinder.setTextView(mTv_red, "使用红包再减"+bd0+"元");
		}else
		{
			SDViewUtil.hide(mLl_red);
		}
		//商品描述
		String brief = mDealModel.getBrief();
		if (isEmpty(brief))
		{
			brief = mDealModel.getName();
		}
		SDViewBinder.setTextViewHtml(mTvGoodsDetail, brief);
		//商品是否支持随时退货，支持过期退货等信息
		bindGoodsTag(mDealModel.getDeal_tags());
		
		// 已售
		mTvBuyCount.setText(SDResourcesUtil.getString(R.string.has_sold) + mDealModel.getBuy_count());
		// 倒计时
		int timeStatus = mDealModel.getTime_status();
		if (timeStatus == 0)
		{
			mTvTimeDown.setText("未开始");
		} else if (timeStatus == 2)
		{
			mTvTimeDown.setText("已过期");
		} else if (timeStatus == 1) // 可以兑换或者购买
		{
			startCountDown(mDealModel.getLast_time());
		}
	}

	private void bindGoodsTag(List<Deal_tagsModel> listTags)
	{
		if (!SDCollectionUtil.isEmpty(listTags))
		{
			LayoutInflater inflater = getActivity().getLayoutInflater();
			for (Deal_tagsModel model : listTags)
			{
				View viewTag = inflater.inflate(R.layout.item_goods_tag, null);
				TextView tvName = (TextView) viewTag.findViewById(R.id.tv_name);
				ImageView ivIcon = (ImageView) viewTag.findViewById(R.id.iv_icon);
				
				SDViewBinder.setTextView(tvName, model.getV());
				ivIcon.setImageResource(model.getIcon());

				mLlGoodsSupport.addView(viewTag);
			}
		}
	}

	private void startCountDown(long timeSecond)
	{
		if (timeSecond > 0)
		{
			mCounter.stopCount();
			mCounter.startCount(mTvTimeDown, timeSecond, new SDTimerDownListener()
			{
				@Override
				public void onTickFinish()
				{
				}
				@Override
				public void onTick()
				{
				}
				@Override
				public void onStart()
				{

				}
			});
		}
	}

	@Override
	public void onClick(View v)
	{
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mCounter != null)
		{
			mCounter.stopCount();
		}
	}

}