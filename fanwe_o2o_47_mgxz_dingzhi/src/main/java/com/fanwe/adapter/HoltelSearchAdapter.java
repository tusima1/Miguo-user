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
import com.fanwe.model.Holtel_cityAct;
import com.fanwe.o2o.miguo.R;

public class HoltelSearchAdapter extends SDBaseAdapter<Holtel_cityAct>
{

	protected int mId;
	
	protected OnResultSearchListener mListener;
	public HoltelSearchAdapter(List<Holtel_cityAct> listModel, Activity activity, int mId) {
		super(listModel, activity);
		this.mId = mId;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_holtel_search_bs, null);
		}
		final TextView tv_name = ViewHolder.get(R.id.tv_name, convertView);
		View v_line = ViewHolder.get(R.id.v_line, convertView);
		SDViewBinder.setTextView(tv_name, mListModel.get(position).getName());
		if(position< mListModel.size())
		{
			SDViewUtil.show(v_line);
		}
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				Bundle bundle = new Bundle();
				bundle.putInt("id", 0);
				bundle.putInt("qid", mListModel.get(position).getId());
				bundle.putString("search",mListModel.get(position).getName());
				if(mListener != null)
				{
					mListener.resultSearch(bundle);
				}
			}
		});
		return convertView;
	}
	public void setOnResultSearchListener(OnResultSearchListener listener)
	{
		mListener = listener;
	}
	public interface OnResultSearchListener
	{
		void resultSearch(Bundle bundle);
	}
}
