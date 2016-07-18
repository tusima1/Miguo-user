package com.fanwe.adapter;

import java.util.List;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;

public class GoodsListAdapter extends TuanListAdapter
{

	public GoodsListAdapter(List<GoodsModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent, GoodsModel model)
	{
		super.bindData(position, convertView, parent, model);
		ImageView iv_image = get(R.id.iv_image,convertView);
		ImageView iv_auto_order = get(R.id.iv_auto_order, convertView);
		ImageView iv_is_new = get(R.id.iv_is_new, convertView);
		View v_top_line = get(R.id.v_top_line,convertView);
		TextView tv_distance = get(R.id.tv_distance,convertView);	
		DisplayMetrics metric = new DisplayMetrics();
	    mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;
	    int height = (int) (width * 0.242 - 5);
	    SDViewUtil.setViewHeight(iv_image,height);
	    if(model!= null)
	    {
	    	SDViewBinder.setImageView(iv_image, model.getIcon());
	    }
		SDViewUtil.hide(iv_auto_order);
		SDViewUtil.hide(iv_is_new);
		SDViewUtil.show(v_top_line);
		SDViewBinder.setTextView(tv_distance, null);
	}

}
