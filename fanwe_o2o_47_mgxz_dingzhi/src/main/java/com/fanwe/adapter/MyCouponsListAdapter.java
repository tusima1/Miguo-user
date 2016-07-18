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
import com.fanwe.model.Uc_couponActItemModel;
import com.fanwe.o2o.miguo.R;

public class MyCouponsListAdapter extends SDBaseAdapter<Uc_couponActItemModel>
{

	public MyCouponsListAdapter(List<Uc_couponActItemModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lv_my_coupons, null);
		}

		ImageView iv_qrcode = ViewHolder.get(convertView, R.id.iv_qrcode);
		TextView tv_password = ViewHolder.get(convertView, R.id.tv_password);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_expire_time = ViewHolder.get(convertView, R.id.tv_expire_time);

		Uc_couponActItemModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(iv_qrcode, model.getQrcode());
			SDViewBinder.setTextView(tv_password, model.getPassword());
			SDViewBinder.setTextView(tv_name, model.getName());
			SDViewBinder.setTextView(tv_expire_time, model.getEnd_time());
		}

		return convertView;
	}

}
