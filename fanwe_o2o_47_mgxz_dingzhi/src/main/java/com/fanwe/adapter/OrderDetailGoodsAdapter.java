package com.fanwe.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.o2o.miguo.R;

public class OrderDetailGoodsAdapter extends SDBaseAdapter<CartGoodsModel>
{

	private int mIsScore;
	private float total_price;

	public OrderDetailGoodsAdapter(List<CartGoodsModel> listModel, Activity activity, int isScore)
	{
		super(listModel, activity);
		this.mIsScore = isScore;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_cat_order_goods, null);
		}
		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
		final TextView tv_single_price = ViewHolder.get(convertView, R.id.tv_single_price);
		final TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);
		
		final CartGoodsModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(iv_image, model.getIcon());
			SDViewBinder.setTextView(tv_name, model.getSub_name());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getNumber()));
			setPrice(tv_single_price, tv_total_price, model);

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (model.getDeal_id() > 0)
					{
						Intent intent = new Intent(mActivity, TuanDetailActivity.class);
						intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getDeal_id());
						mActivity.startActivity(intent);
					}
				}
			});

		}

		return convertView;
	}

	private void setPrice(TextView tvSinglePrice, TextView tvTotalPrice, CartGoodsModel model)
	{
		if (model != null && tvSinglePrice != null && tvTotalPrice != null)
		{
			if (mIsScore == 1)
			{
				SDViewBinder.setTextView(tvSinglePrice, model.getReturn_scoreFormat());
				SDViewBinder.setTextView(tvTotalPrice, model.getReturn_total_scoreFormat());
			} else
			{
				SDViewBinder.setTextView(tvSinglePrice, model.getUnit_priceFormat());
				SDViewBinder.setTextView(tvTotalPrice, model.getTotal_price());
			}
		}
	}

}
