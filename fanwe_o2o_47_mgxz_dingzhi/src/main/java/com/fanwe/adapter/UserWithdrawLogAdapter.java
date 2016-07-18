package com.fanwe.adapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.UserWithdrawLogActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.listener.TextMoney;
import com.fanwe.model.DistributionWithdrawLogModel;
import com.fanwe.model.Uc_money_withdrawActModel;
import com.fanwe.o2o.miguo.R;

public class UserWithdrawLogAdapter extends SDBaseAdapter<Uc_money_withdrawActModel>
{

	public UserWithdrawLogAdapter(List<Uc_money_withdrawActModel> mListModel, UserWithdrawLogActivity activity) {
		super(mListModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, Uc_money_withdrawActModel model)
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
		
		SDViewBinder.setTextView(tv_money, "￥"+TextMoney.textFarmat(model.getMoney()));
		
		/*if (model.getBank_info().length()>4) {
			SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name()+" 尾号"+model.getBank_info().substring(model.getBank_info().length()-4, model.getBank_info().length()));
		}else{
			SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name());
		}*/
		SDViewBinder.setTextView(tv_withdraw_type, model.getBank_name());
		if(model.getIs_paid() == 0)
		{
			SDViewBinder.setTextView(tv_status,"审核中");
		}else if(model.getIs_paid() == 1)
		{
			SDViewBinder.setTextView(tv_status,"审核成功");
		}
		
		SDViewBinder.setTextView(tv_time, model.getCreate_time());
		}
		return convertView;
	}

}

