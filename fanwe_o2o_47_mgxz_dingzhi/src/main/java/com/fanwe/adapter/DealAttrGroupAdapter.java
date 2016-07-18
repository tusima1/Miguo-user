package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.adapter.DealAttrAdapter.DealAttrAdapterListener;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.FlowLayout;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Deal_attrModel;
import com.fanwe.o2o.miguo.R;

public class DealAttrGroupAdapter extends SDBaseAdapter<Deal_attrModel>
{

	private DealAttrAdapterListener mListener;

	public void setmListener(DealAttrAdapterListener mListener)
	{
		this.mListener = mListener;
	}

	public DealAttrGroupAdapter(List<Deal_attrModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, Deal_attrModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_deal_attr_group, null);
		}

		TextView tv_name = ViewHolder.get(convertView, R.id.tv_attr_name);
		FlowLayout flow_attr = ViewHolder.get(convertView, R.id.flow_attr);

		SDViewBinder.setTextView(tv_name, model.getName());

		DealAttrAdapter adapter = new DealAttrAdapter(model.getAttr_list(), mActivity);
		adapter.setmListener(mListener);
		flow_attr.setAdapter(adapter);

		return convertView;
	}

}
