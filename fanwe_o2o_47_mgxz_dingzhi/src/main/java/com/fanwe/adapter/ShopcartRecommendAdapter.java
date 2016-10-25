package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;

import java.util.List;

public class ShopcartRecommendAdapter extends SDBaseAdapter<GoodsModel>
{

	public ShopcartRecommendAdapter(List<GoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final GoodsModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_distribution_hot_goods, null);
		}

		
		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_used = ViewHolder.get(convertView, R.id.tv_used);
		TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
		ImageView iv_is_reservation = ViewHolder.get(convertView, R.id.iv_is_reservation);
		
		SDViewBinder.setImageView(iv_image, model.getIcon());
		SDViewBinder.setTextView(tv_name, model.getLocation_name() );
		SDViewBinder.setTextView(tv_price, model.getCurrent_price_format());
		SDViewBinder.setTextView(tv_used, model.getBrief());
		
		SDViewUtil.hide(iv_is_reservation);
		if (model.getAuto_order() ==1) {
			SDViewUtil.show(iv_is_reservation);
		}
		convertView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(App.getApplication(), GoodsDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				mActivity.startActivity(intent);
			}
		});

		return convertView;
	}

}
