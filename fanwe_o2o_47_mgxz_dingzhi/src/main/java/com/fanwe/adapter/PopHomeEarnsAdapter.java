package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.model.getCityList.ModelCityList;

import java.util.List;

public class PopHomeEarnsAdapter extends SDBaseAdapter<ModelCityList>
{

	public PopHomeEarnsAdapter(List<ModelCityList> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_gv_earns, null);
		}
		TextView tvEarn = ViewHolder.get(convertView, R.id.item_gv_earns_tv_earn);
		ModelCityList model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvEarn, model.getName());
		}

		return convertView;
	}

}
