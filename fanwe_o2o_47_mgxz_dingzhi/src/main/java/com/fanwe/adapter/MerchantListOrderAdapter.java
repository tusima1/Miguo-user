package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;

public class MerchantListOrderAdapter extends SDBaseAdapter<StoreModel>
{

	public MerchantListOrderAdapter(List<StoreModel> listModel, Activity activity)
	{
		super(listModel, activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lv_merchant_list_order, null);
		}
		ImageView ivLogo = ViewHolder.get(convertView, R.id.item_lv_merchant_list_order_iv_logo);
		TextView tvName = ViewHolder.get(convertView, R.id.item_lv_merchant_list_order_tv_name);
		TextView tvbrief = ViewHolder.get(convertView, R.id.item_lv_merchant_list_order_tv_brief);
		TextView tvAddress = ViewHolder.get(convertView, R.id.item_lv_merchant_list_order_tv_address);
		TextView tvDistance = ViewHolder.get(convertView, R.id.item_lv_merchant_list_order_tv_distance);

		StoreModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(ivLogo, model.getPreview());
			SDViewBinder.setTextView(tvName, model.getName());
			// SDViewBinder.setTextView(tvbrief, model.getBrief());
			SDViewBinder.setTextView(tvAddress, model.getAddress());
			SDViewBinder.setTextView(tvDistance, model.getDistanceFormat());
		}

		return convertView;
	}

}
