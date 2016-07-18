package com.fanwe.adapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DistributionWithdrawLogModel;
import com.fanwe.o2o.miguo.R;

public class DistributionWithdrawLogAdapter extends SDBaseAdapter<DistributionWithdrawLogModel>
{

	public DistributionWithdrawLogAdapter(List<DistributionWithdrawLogModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, DistributionWithdrawLogModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_distribution_withdraw_log, null);
		}

		TextView tv_money = ViewHolder.get(convertView, R.id.tv_money);
		TextView tv_withdraw_type = ViewHolder.get(convertView, R.id.tv_withdraw_type);
		TextView tv_status = ViewHolder.get(convertView, R.id.tv_status);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		
		if(model != null)
		{
		BigDecimal bd = new BigDecimal((model.getMoney()+model.getMoney_people()));
		bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(tv_money, "￥"+String.valueOf(bd));
		
		if (model.getBank_info().length()>4) {
			SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name()+" 尾号"+model.getBank_info().substring(model.getBank_info().length()-4, model.getBank_info().length()));
		}else{
			SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name());
		}
		if(model.getStatus() == 1)
		{
			SDViewBinder.setTextView(tv_status,"提现成功");
		}else if(model.getStatus() == 0)
		{
			SDViewBinder.setTextView(tv_status,"审核中");
		}else if(model.getStatus() == 2)
		{
			SDViewBinder.setTextView(tv_status,"未通过");
		}
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SDViewBinder.setTextView(tv_time, form.format(new Date(model.getUpdate_time()*1000)));
		}
		return convertView;
	}

}
