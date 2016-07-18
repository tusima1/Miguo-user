package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;

public class HomeForenoticeYouhuiAdapter extends SDBaseAdapter<GoodsModel>
{

	public HomeForenoticeYouhuiAdapter(List<GoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final GoodsModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_home_forenotice_youhui, null);
		}

		View view_div = ViewHolder.get(convertView, R.id.view_div);
		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);

		if (position == 0)
		{
			SDViewUtil.hide(view_div);
		}

		SDViewBinder.setImageView(iv_image, model.getIcon());
		SDViewBinder.setTextView(tv_name, model.getName());

		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(mActivity, TuanDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				mActivity.startActivity(intent);
			}
		});

		return convertView;
	}

}
