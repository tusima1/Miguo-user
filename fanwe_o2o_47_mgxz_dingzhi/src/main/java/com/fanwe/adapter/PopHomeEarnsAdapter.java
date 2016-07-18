package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CitylistModel;
import com.fanwe.o2o.miguo.R;

public class PopHomeEarnsAdapter extends SDBaseAdapter<CitylistModel>
{

	public PopHomeEarnsAdapter(List<CitylistModel> listModel, Activity activity)
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
		CitylistModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvEarn, model.getName());
		}

		return convertView;
	}

}
