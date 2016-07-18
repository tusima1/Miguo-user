package com.fanwe.adapter;

import java.util.List;

import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.o2o.miguo.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class SDSimpleImageAdapter<Drawable> extends SDSimpleBaseAdapter<Drawable>{

	
	

	public SDSimpleImageAdapter(List<Drawable> listModel, Activity activity) {
		super(listModel, activity);
	}


	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent) {
		
		return R.layout.item_share_red;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
			Drawable model) {
		ImageView iv_share = get(R.id.iv_share,convertView);
		
		
		iv_share.setBackground((android.graphics.drawable.Drawable) model);
		
	}
}
