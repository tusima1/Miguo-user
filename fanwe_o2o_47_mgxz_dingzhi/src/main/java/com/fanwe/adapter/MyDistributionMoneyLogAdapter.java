package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DistributionMoneyLogModel;
import com.fanwe.o2o.miguo.R;

public class MyDistributionMoneyLogAdapter extends SDBaseAdapter<DistributionMoneyLogModel>
{

	public MyDistributionMoneyLogAdapter(List<DistributionMoneyLogModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, DistributionMoneyLogModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_my_distribution_money_log, null);
		}

		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		TextView tv_detail = ViewHolder.get(convertView, R.id.tv_detail);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);

		SDViewBinder.setTextView(tv_money, model.getMoney());
		SDViewBinder.setTextView(tv_detail, model.getLog());
		SDViewBinder.setTextView(tv_time, model.getCreate_time());

		return convertView;
	}

}
