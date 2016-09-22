package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.HistoryShowBean;
import com.fanwe.o2o.miguo.R;



public class SearchHistoryAdapter extends SDBaseAdapter<HistoryShowBean>
{

	private OnSearchHistoryListener mListener;
	public SearchHistoryAdapter(List<HistoryShowBean> listModel,
			Activity activity) {
		super(listModel, activity);
		
	}
	public void setOnSearchHistoryData(OnSearchHistoryListener listener)
	{
		this.mListener = listener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_holtel_search_bs, null);
		}
		final TextView tv_name = ViewHolder.get(R.id.tv_name, convertView);
		View v_line = ViewHolder.get(R.id.v_line, convertView);
		SDViewBinder.setTextView(tv_name, mListModel.get(position).getJingdian());
		if(position< mListModel.size())
		{
			SDViewUtil.show(v_line);
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("id", 1);
				bundle.putInt("qid", 0);
				bundle.putString("search", mListModel.get(position).getJingdian());
				if(mListener != null)
				{
					mListener.setOnHistoryData(bundle);
				}
			}
		});
		return convertView;
	}
		// 刷新适配器,更新数据
		public void refresh(List<HistoryShowBean> data) {
			this.mListModel = data;
			notifyDataSetChanged();
		}
		
	public interface OnSearchHistoryListener
	{
		public void setOnHistoryData(Bundle bundle);
	}
		
}
