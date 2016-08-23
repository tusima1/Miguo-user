package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.BuyItem;
import com.fanwe.shoppingcart.model.Deals;

import java.util.List;

/**
 * 按商家排序的订单详情列表。
 */
public class OrderDetailGroupGoodsAdapter extends SDBaseAdapter<Deals>
{

	private int mIsScore;

	public OrderDetailGroupGoodsAdapter(List<Deals> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, Deals model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_detail_group_goods, null);
		}

		TextView tv_supplier = ViewHolder.get(convertView, R.id.tv_supplier);
		LinearLayout ll_goods = ViewHolder.get(convertView, R.id.ll_goods);

		ll_goods.removeAllViews();

		SDViewBinder.setTextView(tv_supplier, model.getEntName());

		List<BuyItem> listModel = model.getBuyItem();
		OrderDetailGoodsAdapter adapter = new OrderDetailGoodsAdapter(listModel, mActivity);
		for (int i = 0; i < adapter.getCount(); i++)
		{
			View viewGoods = adapter.getView(i, null, null);
			ll_goods.addView(viewGoods);
		}

		return convertView;
	}
}
