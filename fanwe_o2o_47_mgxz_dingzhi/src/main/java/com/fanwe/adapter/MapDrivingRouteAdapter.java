package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.MapDrivingRouteModel;
import com.fanwe.o2o.miguo.R;

public class MapDrivingRouteAdapter extends SDBaseAdapter<MapDrivingRouteModel>
{

	public MapDrivingRouteAdapter(List<MapDrivingRouteModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_routeinfo, null);
		}
		TextView tvIndex = ViewHolder.get(convertView, R.id.item_routeinfo_tv_index);
		TextView tvName = ViewHolder.get(convertView, R.id.item_routeinfo_tv_name);
		TextView tvTime = ViewHolder.get(convertView, R.id.item_routeinfo_tv_time);
		TextView tvDistance = ViewHolder.get(convertView, R.id.item_routeinfo_tv_distance);

		MapDrivingRouteModel model = getItem(position);
		if (model != null)
		{
			tvIndex.setText(String.valueOf(position + 1));
			SDViewBinder.setTextView(tvName, model.getName());
			SDViewBinder.setTextView(tvTime, model.getTime());
			SDViewBinder.setTextView(tvDistance, model.getDistance());
		}

		return convertView;
	}

}
