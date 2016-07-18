package com.fanwe.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.OrderCoupon_listModel;
import com.fanwe.o2o.miguo.R;

public class RefundCouponAdapter extends SDBaseAdapter<OrderCoupon_listModel>
{

	public RefundCouponAdapter(List<OrderCoupon_listModel> listModel, Activity activity)
	{
		super(listModel, activity);
		setmMode(EnumAdapterMode.MULTI);
	}

	public List<String> getSelectedIds()
	{
		List<String> listIds = new ArrayList<String>();
		if (!SDCollectionUtil.isEmpty(mListModel))
		{
			for (OrderCoupon_listModel model : mListModel)
			{
				if (model.getIs_refund() == 1 && model.isSelected())
				{
					listIds.add(String.valueOf(model.getId()));
				}
			}
		}
		return listIds;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final OrderCoupon_listModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_refund_coupon, null);
		}

		View view_div = ViewHolder.get(convertView, R.id.view_div);
		TextView tv_password = ViewHolder.get(convertView, R.id.tv_password);
		TextView tv_consume_number = ViewHolder.get(convertView, R.id.tv_consume_number);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		TextView tv_status = ViewHolder.get(convertView, R.id.tv_status);
		final ImageView iv_selected = ViewHolder.get(convertView, R.id.iv_selected);

		if (position == 0)
		{
			SDViewUtil.hide(view_div);
		}

		if (model != null)
		{
			SDViewBinder.setTextView(tv_password, model.getPassword());
			SDViewBinder.setTextView(tv_consume_number, model.getConsumeString());
			SDViewBinder.setTextView(tv_time, model.getTime_str());
			SDViewBinder.setTextView(tv_status, model.getStatus_str());

			if (SDViewBinder.setViewsVisibility(iv_selected, model.getIs_refund()))
			{
				updateImageView(iv_selected, model.isSelected());
			}

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					model.toggleSelected();
					updateImageView(iv_selected, model.isSelected());
					setmSelectedPosition(position, model.isSelected());
				}
			});

		}
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	private void updateImageView(ImageView iv_selected, boolean isSelected)
	{
		if (isSelected)
		{
			iv_selected.setImageResource(R.drawable.ic_payment_selected);
		} else
		{
			iv_selected.setImageResource(R.drawable.ic_payment_normal);
		}
	}

	@Override
	protected void onSelectedChange(int position, boolean selected, boolean notify)
	{
		getItem(position).setSelected(selected);
		updateItem(position);
	}

}
