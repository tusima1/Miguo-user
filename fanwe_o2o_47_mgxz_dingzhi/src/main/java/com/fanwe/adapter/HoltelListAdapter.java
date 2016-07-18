package com.fanwe.adapter;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.StoreDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.customview.SDWeightLinearLayout;
import com.fanwe.library.customview.SDWeightLinearLayout.CalculateWidthListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.HoltelModel_list;
import com.fanwe.model.StoreModel;
import com.fanwe.o2o.miguo.R;
import com.umeng.socialize.utils.Log;

public class HoltelListAdapter extends SDSimpleBaseAdapter<HoltelModel_list>{

	private String begin;
	private String end;
	public HoltelListAdapter(List<HoltelModel_list> listModel, Activity activity,String begin,String end) {
		super(listModel, activity);
		this.begin =begin;
		this.end = end;
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent) {
		
		return R.layout.item_holtel_lv;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
			final HoltelModel_list model) 
	{
		ImageView iv_image = ViewHolder.get(R.id.iv_image, convertView);
		
		LinearLayout ll_name_bar = get(R.id.ll_name_bar, convertView);
		ImageView iv_tag_tuan = get(R.id.iv_tag_tuan, convertView);
		ImageView iv_tag_quan = get(R.id.iv_tag_quan, convertView);
		final TextView tv_name = get(R.id.tv_name, convertView);
		TextView tv_distance = get(R.id.tv_distance, convertView);
		RatingBar rb_rating = get(R.id.rb_rating, convertView);
		TextView tv_score = get(R.id.tv_score,convertView);
		TextView tv_money_low = get(R.id.tv_money_low,convertView);
		TextView tv_address = get(R.id.tv_address, convertView);
		TextView tv_number = get(R.id.tv_number,convertView);
		ImageView iv_tag_hui = get(R.id.iv_tag_hui,convertView);
		
		 DisplayMetrics metric = new DisplayMetrics();
	     mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;
	    int height = (int) (width * 0.242 - 5);
	    SDViewUtil.setViewHeight(iv_image,height);
		SDViewUtil.show(iv_tag_tuan);
	
		SDViewBinder.setImageView(model.getPreview(), iv_image);
		SDViewBinder.setTextView(tv_name, model.getName());
		SDViewBinder.setRatingBar(rb_rating, model.getAvg_point());
		SDViewBinder.setTextView(tv_score, String.valueOf(model.getAvg_point()));
		SDViewBinder.setTextView(tv_address, model.getAddress());
		SDViewBinder.setTextView(tv_money_low, "￥"+model.getMin_price());
		SDViewBinder.setTextView(tv_number, model.getBuy_count()+"人消费");
		SDViewBinder.setTextView(tv_distance, model.getDistance());
		SDWeightLinearLayout.calculateWidth(ll_name_bar, new CalculateWidthListener()
		{
			@Override
			public void onResult(int width0, int width1, int widthParent, int widthLeft, ViewGroup parent)
			{
				if (widthLeft > 0)
				{
					tv_name.setMaxWidth(widthLeft);
				}
			}

		});

		convertView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				
				Intent itemintent = new Intent();
				itemintent.setClass(App.getApplication(), StoreDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putInt(StoreDetailActivity.EXTRA_MERCHANT_ID, model.getLocation_id());
				bundle.putInt("type", 15);
				bundle.putString("begin_time",begin);
				bundle.putString("end_time",end);
				itemintent.putExtras(bundle);
				mActivity.startActivity(itemintent);
			}
		});
	}
}
