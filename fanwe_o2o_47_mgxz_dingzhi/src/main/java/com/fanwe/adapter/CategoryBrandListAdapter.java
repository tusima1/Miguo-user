package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.SDLvCategoryViewHelper.SDLvCategoryViewHelperAdapterInterface;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Brand_listModel;
import com.fanwe.o2o.miguo.R;

public class CategoryBrandListAdapter extends SDBaseAdapter<Brand_listModel> implements SDLvCategoryViewHelperAdapterInterface
{

	private int mDefaultIndex;

	public CategoryBrandListAdapter(List<Brand_listModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	public void setmDefaultIndex(int rightIndex)
	{
		this.mDefaultIndex = rightIndex;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_category_single, null);
		}
		TextView tvTitle = ViewHolder.get(convertView, R.id.item_category_single_tv_title);

		Brand_listModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvTitle, model.getName());
			if (model.isSelect())
			{
				convertView.setBackgroundColor(SDResourcesUtil.getColor(R.color.bg_gray_categoryview_item_select));
			} else
			{
				convertView.setBackgroundColor(SDResourcesUtil.getColor(R.color.white));
			}
		}
		return convertView;
	}

	@Override
	public void setPositionSelectState(int position, boolean select, boolean notify)
	{
		getItem(position).setSelect(select);
		if (notify)
		{
			notifyDataSetChanged();
		}
	}

	@Override
	public String getTitleNameFromPosition(int position)
	{
		return getItem(position).getName();
	}

	@Override
	public BaseAdapter getAdapter()
	{
		return this;
	}

	@Override
	public Object getSelectModelFromPosition(int position)
	{
		return getItem(position);
	}

	@Override
	public int getTitleIndex()
	{
		return mDefaultIndex;
	}

}
