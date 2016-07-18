package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.GetbrandlistActItemModel;
import com.fanwe.o2o.miguo.R;

public class SubscribeAdapter extends SDBaseAdapter<GetbrandlistActItemModel>
{

	private SubscribeAdapterListener mListener = null;

	public SubscribeAdapter(List<GetbrandlistActItemModel> listModel, Activity activity, SubscribeAdapterListener mListener)
	{
		super(listModel, activity);
		this.mListener = mListener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lv_subscribe, null);
		}

		ImageView ivImage = ViewHolder.get(convertView, R.id.item_lv_subscribe_iv_image);
		TextView tvName = ViewHolder.get(convertView, R.id.item_lv_subscribe_tv_name);
		ToggleButton tbSubscribe = ViewHolder.get(convertView, R.id.item_lv_subscribe_tb_subscribe);

		final GetbrandlistActItemModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvName, model.getName());
			SDViewBinder.setImageView(ivImage, model.getLogo());
			tbSubscribe.setChecked(model.isIs_checked_format_boolean());
			tbSubscribe.setOnCheckedChangeListener(new OnCheckedChangeListener()
			{
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if (mListener != null)
					{
						mListener.onCheckedChanged(buttonView, isChecked, model);
					}
				}
			});
		}

		return convertView;
	}

	public interface SubscribeAdapterListener
	{
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked, GetbrandlistActItemModel model);
	}

}
