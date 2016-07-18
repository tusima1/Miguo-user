package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.CateTypeModel;
import com.fanwe.o2o.miguo.R;

public class CateTypeAdapter extends SDBaseAdapter<CateTypeModel>
{

	public CateTypeAdapter(List<CateTypeModel> listModel, Activity activity)
	{
		super(listModel, activity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_gv_cate_type, null);
		}
		TextView tvCategory = ViewHolder.get(convertView, R.id.item_gv_cate_type_tv_category);
		CateTypeModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvCategory, model.getName());
		}

		return convertView;
	}

}
