package com.fanwe.fragment;

import android.app.Activity;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.model.Deal_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDFormatUtil;

public class TuanDetailBaseFragment extends BaseFragment
{

	protected Deal_indexActModel mDealModel;
	private TextView mTvCurrentPrice;
	private TextView mTvOriginalPrice;
	private String currentPrice;

	public void setmDealModel(Deal_indexActModel mDealModel)
	{
		this.mDealModel = mDealModel;
	}

	public void updateGoodsPrice()
	{
		if (mDealModel != null)
		{
			if(mDealModel.getIs_first() - mDealModel.getCheck_first() > 0)
			{
				currentPrice = "￥"+mDealModel.getIs_first_price();
			}else
			{
				currentPrice = String.valueOf(mDealModel.getCurrentPriceTotal());
			}
			
			double originalPrice = mDealModel.getOriginalPriceTotal();

			getTextViewCurrentPrice().setText(SDFormatUtil.formatMoneyChina(currentPrice));
			
		}
	}

	public TextView getTextViewCurrentPrice()
	{
		if (mTvCurrentPrice == null)
		{
			mTvCurrentPrice = (TextView) getActivity().findViewById(R.id.frag_tuan_detail_first_tv_current_price);
		}
		return mTvCurrentPrice;
	}

	public TextView getTextViewOriginalPrice()
	{
		if (mTvOriginalPrice == null)
		{
			mTvOriginalPrice = (TextView) getActivity().findViewById(R.id.frag_tuan_detail_first_tv_original_price);
		}
		return mTvOriginalPrice;
	}

	public TuanDetailAttrsFragment getTuanDetailAttrsFragment()
	{
		TuanDetailAttrsFragment fragment = null;
		TuanDetailActivity activity = getTuanDetailActivity();
		if (activity != null)
		{
			fragment = activity.getTuanDetailAttrsFragment();
		}
		return fragment;
	}

	public TuanDetailActivity getTuanDetailActivity()
	{
		TuanDetailActivity tuanDetailActivity = null;

		Activity activity = getActivity();
		if (activity != null && activity instanceof TuanDetailActivity)
		{
			tuanDetailActivity = (TuanDetailActivity) activity;
		}
		return tuanDetailActivity;
	}

	public void scrollToAttr()
	{
		TuanDetailActivity activity = getTuanDetailActivity();
		if (activity != null)
		{
			activity.scrollToAttr();
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}