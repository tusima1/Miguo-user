package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.FeeinfoModel;
import com.fanwe.o2o.miguo.R;

public class OrderFeeItemAdapter extends SDBaseAdapter<FeeinfoModel>
{

	public OrderFeeItemAdapter(List<FeeinfoModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_fee, null);
		}

		TextView tvName = ViewHolder.get(convertView, R.id.item_order_fee_tv_name);
		TextView tvValue = ViewHolder.get(convertView, R.id.item_order_fee_tv_value);

		FeeinfoModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvName, model.getName());
			SDViewBinder.setTextView(tvValue, model.getValue());
		}

		return convertView;
	}
	
}
