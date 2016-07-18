package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Payment_doneActCouponlistModel;
import com.fanwe.o2o.miguo.R;

public class PayorderCodesAdapter extends SDBaseAdapter<Payment_doneActCouponlistModel>
{

	public PayorderCodesAdapter(List<Payment_doneActCouponlistModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_pay_order_code, null);
		}
		ImageView ivCode = ViewHolder.get(convertView, R.id.item_pay_order_code_iv_code);
		TextView tvCode = ViewHolder.get(convertView, R.id.item_pay_order_code_tv_code);

		Payment_doneActCouponlistModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(ivCode, model.getQrcode());
			if (!TextUtils.isEmpty(model.getPassword()))
			{
				tvCode.setText("验证码:" + model.getPassword());
			}
		}

		return convertView;
	}

}
