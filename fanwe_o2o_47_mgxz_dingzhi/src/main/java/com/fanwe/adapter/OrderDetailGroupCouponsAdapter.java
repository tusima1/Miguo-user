package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.OrderDetailCoupon_listModel;
import com.fanwe.o2o.miguo.R;

public class OrderDetailGroupCouponsAdapter extends SDBaseAdapter<OrderDetailCoupon_listModel>
{
	public OrderDetailGroupCouponsAdapter(List<OrderDetailCoupon_listModel> listModel, Activity activity)
	{

		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_detail_group_coupons, null);
		}
		View viewDiv = ViewHolder.get(convertView, R.id.item_order_detail_group_coupons_view_div);
		TextView tvPassword = ViewHolder.get(convertView, R.id.item_order_detail_group_coupons_tv_password);
		TextView tvStatus = ViewHolder.get(convertView, R.id.item_order_detail_group_coupons_tv_status);

		if (position == 0)
		{
			viewDiv.setVisibility(View.GONE);
		}

		OrderDetailCoupon_listModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvPassword, model.getPassword(), "未找到");
			SDViewBinder.setTextView(tvStatus, model.getStatus_format(), "未找到");
		}

		return convertView;
	}

}