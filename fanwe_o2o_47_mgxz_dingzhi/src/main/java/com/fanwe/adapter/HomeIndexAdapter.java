package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.HoltelListActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.IndexActIndexsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;

public class HomeIndexAdapter extends SDBaseAdapter<IndexActIndexsModel>
{

	public HomeIndexAdapter(List<IndexActIndexsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final IndexActIndexsModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_home_index, null);
		}
		ImageView ivImage = ViewHolder.get(convertView, R.id.item_home_index_iv_image);
		TextView tvName = ViewHolder.get(convertView, R.id.item_home_index_tv_name);

		SDViewUtil.setViewWidth(ivImage, SDViewUtil.getScreenWidth() / 8);
		SDViewUtil.setViewHeight(ivImage, SDViewUtil.getScreenWidth() / 8);

		SDViewBinder.setTextView(tvName, model.getName());
		SDViewBinder.setImageView(ivImage, model.getImg());

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				int type = model.getType();
				if(model.getData().getCate_id() == 15)
				{
					Intent intent = new Intent(mActivity,HoltelListActivity.class);
					mActivity.startActivity(intent);
				}else
				{
					Intent intent = AppRuntimeWorker.createIntentByType(type, model.getData(), true);
					SDActivityUtil.startActivity(mActivity, intent);
				}
			}
		});
		return convertView;
	}

}