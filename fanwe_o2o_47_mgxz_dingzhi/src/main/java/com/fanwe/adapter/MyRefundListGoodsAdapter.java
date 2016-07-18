package com.fanwe.adapter;


import java.util.List;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Uc_orderModelParcelable;
import com.fanwe.o2o.miguo.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyRefundListGoodsAdapter extends SDBaseAdapter<Uc_orderModelParcelable>{

	private int payId = -1;
	private OnPaymentId mListener;
	
	public void setPayment(OnPaymentId listener)
	{
		this.mListener = listener;
	}
	public MyRefundListGoodsAdapter(List<Uc_orderModelParcelable> listModel,
			Activity activity) 
	{
		super(listModel, activity);
	}
	public View getView(final int position, View convertView, ViewGroup parent, Uc_orderModelParcelable model)
	{

		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_order_goods, null);
		}
		
		TextView tv_order_sn = ViewHolder.get(convertView, R.id.tv_order_sn);
		TextView tv_total_price = ViewHolder.get(convertView, R.id.tv_total_price);
		TextView tv_pay_amount = ViewHolder.get(convertView, R.id.tv_pay_amount);
		TextView tv_number = ViewHolder.get(convertView, R.id.tv_number);
		final CheckBox ch_order = ViewHolder.get(R.id.ch_order, convertView);
		LinearLayout ll_order = ViewHolder.get(R.id.ll_order, convertView);
		LinearLayout ll_order_use = ViewHolder.get(R.id.ll_order_use, convertView);
		final CheckBox ch_order_use = ViewHolder.get(R.id.ch_order_use, convertView);
		TextView tv_status = ViewHolder.get(convertView, R.id.tv_status);
		TextView tv_create_time = ViewHolder.get(convertView, R.id.tv_create_time);
		if (model != null)
		{
			SDViewBinder.setTextView(tv_order_sn, model.getOrder_sn());
			SDViewBinder.setTextView(tv_total_price, model.getTotal_priceFormat());
			SDViewBinder.setTextView(tv_pay_amount, model.getPay_amountFormat());
			SDViewBinder.setTextView(tv_number, String.valueOf(model.getC()));
			SDViewBinder.setTextView(tv_status, model.getStatus());
			SDViewBinder.setTextView(tv_create_time, model.getCreate_time());
			if(model.getPayment_id() == 0 || model.getPayment_id() == 15)
			{
				ch_order_use.setChecked(true);
				ch_order.setChecked(false);
			}else
			{
				ch_order.setChecked(true);
				ch_order_use.setChecked(false);
			}
			
			//原路退款
			ch_order.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
				{
					if(isChecked)
					{
						ch_order_use.setChecked(false);
						payId = 1;
					}else
					{
						ch_order_use.setChecked(true);
						payId= 0;
					}
					if(mListener != null)
					{
						mListener.setPaymentId(payId);
					}
				}
			});
			
			//账户余额
			ch_order_use.setOnCheckedChangeListener(new OnCheckedChangeListener() 
			{
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
				{
					if(isChecked)
					{
						ch_order.setChecked(false);
						payId = 0;
					}else
					{
						ch_order.setChecked(true);
						payId =1;
					}
					if(mListener != null)
					{
						mListener.setPaymentId(payId);
					}
				}
			});
			ll_order.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if(ch_order.isChecked())
					{
						ch_order.setChecked(false);
					}else
					{
						ch_order.setChecked(true);
					}
				}
			});
			ll_order_use.setOnClickListener(new OnClickListener()
			{
				
				@Override
				public void onClick(View v)
				{
					if(ch_order_use.isChecked())
					{
						ch_order_use.setChecked(false);
					}else
					{
						ch_order_use.setChecked(true);
					}
				}
			});
		}
		return convertView;
	}
	public interface OnPaymentId
	{
		public void setPaymentId(int pay);
	}
}
