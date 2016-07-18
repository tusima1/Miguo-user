package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.SearchTypeModel;
import com.fanwe.o2o.miguo.R;

public class SearchTypeAdapter extends SDBaseAdapter<SearchTypeModel>
{

	public SearchTypeAdapter(List<SearchTypeModel> listModel, Activity activity)
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
		SearchTypeModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvCategory, model.getName());
			if (model.isSelect())
			{
				tvCategory.setBackgroundResource(R.drawable.layer_main_color_normal);
				tvCategory.setTextColor(SDResourcesUtil.getColor(R.color.white));
			} else
			{
				tvCategory.setBackgroundResource(R.drawable.layer_white_stroke_all);
				tvCategory.setTextColor(SDResourcesUtil.getColor(R.color.gray));
			}
		}

		return convertView;
	}

}
