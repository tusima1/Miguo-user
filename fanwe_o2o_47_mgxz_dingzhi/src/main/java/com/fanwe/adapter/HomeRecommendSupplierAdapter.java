package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.StoreDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;

public class HomeRecommendSupplierAdapter extends SDBaseAdapter<StoreModel>
{

	public HomeRecommendSupplierAdapter(List<StoreModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_home_recommend_supplier, null);
		}
		View view_div = ViewHolder.get(convertView, R.id.view_div);
		ImageView ivImage = ViewHolder.get(convertView, R.id.item_home_recommend_supplier_iv_image);
		TextView tvName = ViewHolder.get(convertView, R.id.item_home_recommend_supplier_tv_name);

		if (position == 0)
		{
			SDViewUtil.hide(view_div);
		}

		final StoreModel model = getItem(position);
		if (model != null)
		{
			SDViewBinder.setImageView(ivImage, model.getPreview());
			SDViewBinder.setTextView(tvName, model.getName());
			convertView.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Intent intent = new Intent(App.getApplication(), StoreDetailActivity.class);
					intent.putExtra(StoreDetailActivity.EXTRA_MERCHANT_ID, model.getId());
					mActivity.startActivity(intent);
				}
			});
		}
		return convertView;
	}

}
