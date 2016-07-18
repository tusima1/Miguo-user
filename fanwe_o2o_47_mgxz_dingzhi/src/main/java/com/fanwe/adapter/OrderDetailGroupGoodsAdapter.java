package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.model.CartGroupGoodsModel;
import com.fanwe.o2o.miguo.R;

public class OrderDetailGroupGoodsAdapter extends SDBaseAdapter<CartGroupGoodsModel>
{

	private int mIsScore;

	public OrderDetailGroupGoodsAdapter(List<CartGroupGoodsModel> listModel, Activity activity, int isScore)
	{
		super(listModel, activity);
		this.mIsScore = isScore;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, CartGroupGoodsModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_detail_group_goods, null);
		}

		TextView tv_supplier = ViewHolder.get(convertView, R.id.tv_supplier);
		TextView tv_fee_info = ViewHolder.get(convertView, R.id.tv_fee_info);
		LinearLayout ll_goods = ViewHolder.get(convertView, R.id.ll_goods);

		ll_goods.removeAllViews();

		SDViewBinder.setTextView(tv_supplier, model.getSupplier());
		SDViewBinder.setTextView(tv_fee_info, model.getDeliveryFeeInfo());

		List<CartGoodsModel> listModel = model.getGoods_list();
		OrderDetailGoodsAdapter adapter = new OrderDetailGoodsAdapter(listModel, mActivity, mIsScore);
		for (int i = 0; i < adapter.getCount(); i++)
		{
			View viewGoods = adapter.getView(i, null, null);
			ll_goods.addView(viewGoods);
		}

		return convertView;
	}
}
