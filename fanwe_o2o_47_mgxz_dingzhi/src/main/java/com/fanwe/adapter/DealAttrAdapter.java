package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Deal_attrValueModel;
import com.fanwe.o2o.miguo.R;

public class DealAttrAdapter extends SDBaseAdapter<Deal_attrValueModel>
{

	private DealAttrAdapterListener mListener;

	public void setmListener(DealAttrAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public DealAttrAdapter(List<Deal_attrValueModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent, final Deal_attrValueModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_deal_attr, null);
		}

		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		SDViewBinder.setTextView(tv_name, model.getName());

		if (model.isSelected())
		{
			SDViewUtil.setBackgroundResource(tv_name, R.drawable.layer_goods_attr_selected);
			SDViewUtil.setTextViewColorResId(tv_name, R.color.main_color);
		} else
		{
			SDViewUtil.setBackgroundResource(tv_name, R.drawable.layer_goods_attr_normal);
			SDViewUtil.setTextViewColorResId(tv_name, R.color.gray);
		}
		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				setmSelectedPosition(position, true);
				if (mListener != null)
				{
					mListener.onClickItem(v, position, model, DealAttrAdapter.this);
				}
			}
		});
		getViewUpdate(position, convertView, parent);
		return convertView;
	}

	@Override
	protected void onSelectedChange(int position, boolean selected, boolean notify)
	{
		getItem(position).setSelected(selected);
		updateItem(position);
		super.onSelectedChange(position, selected, notify);
	}

	public interface DealAttrAdapterListener
	{
		void onClickItem(View v, int position, Deal_attrValueModel model, DealAttrAdapter adapter);
	}

}
