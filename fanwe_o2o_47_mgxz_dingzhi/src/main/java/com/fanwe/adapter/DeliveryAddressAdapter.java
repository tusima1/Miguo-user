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
import com.fanwe.model.Consignee_infoModel;
import com.fanwe.o2o.miguo.R;

public class DeliveryAddressAdapter extends SDBaseAdapter<Consignee_infoModel>
{

	private boolean mIsManage;

	public DeliveryAddressAdapter(List<Consignee_infoModel> listModel, boolean isManage, Activity activity)
	{
		super(listModel, activity);
		this.mIsManage = isManage;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_lv_delivery_address, null);
		}

		TextView tv_default = ViewHolder.get(convertView, R.id.tv_default);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_mobile = ViewHolder.get(convertView, R.id.tv_mobile);
		TextView tv_address = ViewHolder.get(convertView, R.id.tv_address);
		ImageView iv_arrow_right = ViewHolder.get(convertView, R.id.iv_arrow_right);

		if (mIsManage)
		{
			iv_arrow_right.setVisibility(View.VISIBLE);
		} else
		{
			iv_arrow_right.setVisibility(View.GONE);
		}

		final Consignee_infoModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tv_name, model.getConsignee()); // 名字
			SDViewBinder.setTextView(tv_mobile, model.getMobile()); // 电话
			SDViewBinder.setTextView(tv_address, model.getAddressLong()); // 地址
			SDViewBinder.setViewsVisibility(tv_default, model.getIs_default());
		}

		return convertView;
	}

}
