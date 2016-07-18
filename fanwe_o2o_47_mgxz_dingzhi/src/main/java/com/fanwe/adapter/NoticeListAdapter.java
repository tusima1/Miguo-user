package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Notice_indexActListModel;
import com.fanwe.o2o.miguo.R;

public class NoticeListAdapter extends SDBaseAdapter<Notice_indexActListModel>
{

	public NoticeListAdapter(List<Notice_indexActListModel> listModel, Activity activity)
	{
		super(listModel, activity);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_notice_list, null);
		}
		TextView tvNews = ViewHolder.get(convertView, R.id.item_news_list_tv_title);

		final Notice_indexActListModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setTextView(tvNews, model.getName());
		}
		return convertView;
	}
}
