package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.DistributionGoodsModel;
import com.fanwe.o2o.miguo.R;

public class DistributionStoreTuanAdapter extends SDBaseAdapter<DistributionGoodsModel>
{

	public DistributionStoreTuanAdapter(List<DistributionGoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent, final DistributionGoodsModel model)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.item_distribution_store_tuan, null);
		}

		ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
		TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
		TextView tv_current_price = ViewHolder.get(convertView, R.id.tv_current_price);
		TextView tv_original_price = ViewHolder.get(convertView, R.id.tv_original_price);

		tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		SDViewBinder.setImageView(iv_image, model.getIcon_157());
		SDViewBinder.setTextView(tv_name, model.getName());
		SDViewBinder.setTextView(tv_current_price, model.getCurrent_priceFormat());
		SDViewBinder.setTextView(tv_original_price, model.getOrigin_priceFormat());

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
