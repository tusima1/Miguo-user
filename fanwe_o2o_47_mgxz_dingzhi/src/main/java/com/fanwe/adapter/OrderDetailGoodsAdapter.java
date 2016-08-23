package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.BuyItem;

import java.util.List;

/**
 * 商品列表。
 *
 */
public class OrderDetailGoodsAdapter extends SDBaseAdapter<BuyItem>
{

	public OrderDetailGoodsAdapter(List<BuyItem> listModel, Activity activity)
	{
		super(listModel, activity);

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
		
		final BuyItem model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(iv_image, model.getImg());
			SDViewBinder.setTextView(tv_name, model.getGoodsName());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getNumber()));
			setPrice(tv_single_price, tv_total_price, model);

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					if (!TextUtils.isEmpty(model.getGoodsId()))
					{
						Intent intent = new Intent(mActivity, TuanDetailActivity.class);
						intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getGoodsId());
						mActivity.startActivity(intent);
					}
				}
			});

		}

		return convertView;
	}

	private void setPrice(TextView tvSinglePrice, TextView tvTotalPrice, BuyItem model)
	{
		if (model != null && tvSinglePrice != null && tvTotalPrice != null)
		{

				SDViewBinder.setTextView(tvSinglePrice, model.getPrice());
				SDViewBinder.setTextView(tvTotalPrice, model.getTotal());

		}
	}

}
