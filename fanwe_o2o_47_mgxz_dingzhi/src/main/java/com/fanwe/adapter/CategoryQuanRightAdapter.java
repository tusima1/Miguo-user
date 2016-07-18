package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.customview.SD2LvCategoryViewHelper.SD2LvCategoryViewHelperAdapterInterface;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Quan_listModel;
import com.fanwe.o2o.miguo.R;

public class CategoryQuanRightAdapter extends SDBaseAdapter<Quan_listModel> implements SD2LvCategoryViewHelperAdapterInterface
{
	private int mDefaultIndex;

	public CategoryQuanRightAdapter(List<Quan_listModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	public void setmDefaultIndex(int rightIndex)
	{
		mDefaultIndex = rightIndex;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_category_right, null);
		}

		TextView tvTitle = ViewHolder.get(convertView, R.id.item_category_right_tv_title);

		Quan_listModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvTitle, model.getName());
			if (model.isSelect())
			{
				convertView.setBackgroundResource(R.drawable.choose_item_right);
			} else
			{
				convertView.setBackgroundColor(SDResourcesUtil.getColor(R.color.bg_gray_categoryview_item_select));
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

	@Override
	public Object getRightListModelFromPosition_left(int position)
	{
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateRightListModel_right(Object rightListModel)
	{
		List<Quan_listModel> listRight = (List<Quan_listModel>) rightListModel;
		updateData(listRight);
	}

	@Override
	public void setPositionSelectState_left(int positionLeft, int positionRight, boolean select)
	{

	}

}
