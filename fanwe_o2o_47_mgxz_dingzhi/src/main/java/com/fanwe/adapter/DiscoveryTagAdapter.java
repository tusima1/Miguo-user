package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DiscoveryTagModel;
import com.fanwe.o2o.miguo.R;

public class DiscoveryTagAdapter extends SDBaseAdapter<DiscoveryTagModel>
{

	public DiscoveryTagAdapter(List<DiscoveryTagModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final DiscoveryTagModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_discovery_tag, null);
		}

		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		View view_bot = ViewHolder.get(convertView, R.id.view_bot);

		SDViewBinder.setTextView(tv_name, model.getName());

		if (model.isSelected())
		{
			SDViewUtil.setTextViewColorResId(tv_name, R.color.main_color);
			SDViewUtil.setBackgroundColorResId(view_bot, R.color.main_color);
		} else
		{
			SDViewUtil.setTextViewColorResId(tv_name, R.color.gray);
			SDViewUtil.setBackgroundColorResId(view_bot, R.color.transparent);
		}
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	@Override
	protected void onSelectedChange(int position, boolean selected, boolean notify)
	{
		getItem(position).setSelected(selected);
		updateItem(position);
		super.onSelectedChange(position, selected, notify);
	}

}
