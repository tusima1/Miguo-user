package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.SDLvCategoryViewHelper.SDLvCategoryViewHelperAdapterInterface;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Quan_listModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;

public class CategoryCityListAdapter extends SDBaseAdapter<Quan_listModel> implements SDLvCategoryViewHelperAdapterInterface
{

	public CategoryCityListAdapter(List<Quan_listModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_category_single, null);
		}
		TextView tvTitle = ViewHolder.get(convertView, R.id.item_category_single_tv_title);

		Quan_listModel model = getItem(position);
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
		String city = AppRuntimeWorker.getCity_name();
		if (!TextUtils.isEmpty(city))
		{
			if (mListModel != null && mListModel.size() > 0)
			{
				Quan_listModel model = null;
				for (int i = 0; i < mListModel.size(); i++)
				{
					model = mListModel.get(i);
					if (model != null && city.equals(model.getName()))
					{
						return i;
					}
				}
			}
		}
		return -1;
	}
}
