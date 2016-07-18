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
import com.fanwe.model.Uc_lotteryActItemModel;
import com.fanwe.o2o.miguo.R;

public class MyLotteryAdapter extends SDBaseAdapter<Uc_lotteryActItemModel>
{

	public MyLotteryAdapter(List<Uc_lotteryActItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_my_lottery, null);
		}

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_sn_number = ViewHolder.get(convertView, R.id.tv_sn_number);
		TextView tv_create_time = ViewHolder.get(convertView, R.id.tv_create_time);

		final Uc_lotteryActItemModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(iv_image, model.getIcon());
			SDViewBinder.setTextView(tv_name, model.getName());
			SDViewBinder.setTextView(tv_sn_number, model.getLottery_sn());
			SDViewBinder.setTextView(tv_create_time, model.getCreate_time());
		}

		return convertView;
	}

}
