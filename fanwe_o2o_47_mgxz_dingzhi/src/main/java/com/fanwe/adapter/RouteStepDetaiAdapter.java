package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.o2o.miguo.R;

public class RouteStepDetaiAdapter extends SDBaseAdapter<String>
{

	private int mSelectPos = 0;

	public RouteStepDetaiAdapter(List<String> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_route_step, null);
		}

		TextView tvIndex = ViewHolder.get(convertView, R.id.item_route_step_tv_index);
		TextView tvContent = ViewHolder.get(convertView, R.id.item_route_step_tv_content);

		tvIndex.setText(String.valueOf(position + 1));
		SDViewBinder.setTextView(tvContent, getItem(position));
		if (position == mSelectPos)
		{
			convertView.setBackgroundResource(R.drawable.layer_gray);
		} else
		{
			convertView.setBackgroundResource(R.drawable.layer_white);
		}

		return convertView;
	}

	public void setSelectPos(int pos)
	{
		this.mSelectPos = pos;
		notifyDataSetChanged();
	}

}
