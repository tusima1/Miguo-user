package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.QuansModel;
import com.fanwe.o2o.miguo.R;

public class QuanListAdapter extends SDBaseAdapter<QuansModel>
{

	public QuanListAdapter(List<QuansModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.act_cate_list_item, null);
		}

		TextView tvName = ViewHolder.get(convertView, R.id.act_cate_list_item_tv_name);
		LinearLayout llbg = ViewHolder.get(convertView, R.id.act_cate_list_item);

		final QuansModel model = getItem(position);
		if (model != null)
		{

			if (position == 0)
			{
				llbg.setBackgroundResource(R.drawable.layer_white_stroke_top_left_right);
			} else
			{
				llbg.setBackgroundResource(R.drawable.layer_white_stroke_all);
			}

			SDViewBinder.setTextView(tvName, model.getName());

			convertView.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View arg0)
				{

					Intent intent = new Intent();
					// intent.putExtra(YouHuiListActivity.EXTRA_QUAN_ID,
					// model.getId());
					// intent.putExtra(YouHuiListActivity.EXTRA_QUAN_NAME,
					// model.getName());
					// intent.setClass(activity, FavorableListActivity.class);
					mActivity.setResult(1, intent);
					mActivity.finish();
				}
			});
		}
		return convertView;
	}
}
