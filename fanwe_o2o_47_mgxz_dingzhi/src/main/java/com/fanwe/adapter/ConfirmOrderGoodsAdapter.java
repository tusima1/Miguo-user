package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CartGoodsModel;
import com.fanwe.o2o.miguo.R;

public class ConfirmOrderGoodsAdapter extends SDBaseAdapter<CartGoodsModel>
{

	public ConfirmOrderGoodsAdapter(List<CartGoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_goods_list, null);
		}

		View viewDiv = ViewHolder.get(convertView, R.id.item_order_goods_list_view_div); // 商品图片
		ImageView ivImage = ViewHolder.get(convertView, R.id.item_order_goods_list_iv_goods_image); // 商品图片
		TextView tvTitle = ViewHolder.get(convertView, R.id.item_order_goods_list_tv_goods_title); // 商品标题
		TextView tvNumber = ViewHolder.get(convertView, R.id.item_order_goods_list_tv_number); // 商品数量
		TextView tvAttr = ViewHolder.get(convertView, R.id.item_order_goods_list_tv_attr); // 商品属性
		TextView tvPriceSingle = ViewHolder.get(convertView, R.id.item_order_goods_list_tv_price_single); // 商品单价
		TextView tvPriceTotal = ViewHolder.get(convertView, R.id.item_order_goods_list_tv_price_total); // 商品总价

		if (position == 0)
		{
			viewDiv.setVisibility(View.GONE);
		}

		return convertView;
	}

}
