package com.fanwe.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.miguo.live.views.customviews.MGToast;

import java.util.List;

public class HotelListTuanAdapter extends SDSimpleBaseAdapter<GoodsModel>{

	private int mNumber = 0;
	public HotelListTuanAdapter(List<GoodsModel> listModel, Activity activity,int number) {
		super(listModel, activity);
		this.mNumber = number;
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{

		return R.layout.item_hotel_list;
	}

	@Override
	public void bindData(int position, View convertView, ViewGroup parent,
						 final GoodsModel model)
	{
		TextView tv_title = ViewHolder.get(R.id.tv_title, convertView);
		TextView tv_time = ViewHolder.get(R.id.tv_time, convertView);
		TextView tv_money = ViewHolder.get(R.id.tv_money, convertView);
		Button mTn_confirm = ViewHolder.get(R.id.act_confirm_order_btn_search, convertView);
		View v_line = ViewHolder.get(R.id.v_line, convertView);

		if(position == mListModel.size()-1)
		{
			SDViewUtil.hide(v_line);

		}else
		{
			SDViewUtil.show(v_line);
		}
		if(model.getAuto_order() == 1)
		{
			SDViewUtil.hide(tv_time);
		}else
		{
			SDViewUtil.show(tv_time);
		}
		SDViewBinder.setTextView(tv_title, model.getSub_name());
		SDViewBinder.setTextView(tv_money, model.getCurrent_price_format());
		mTn_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				MGToast.showToast("PHP");
			}

		});
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(App.getApplication(), GoodsDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				intent.putExtra(TuanDetailActivity.EXTRA_HOTEL_NUM, mNumber);
				mActivity.startActivity(intent);
			}
		});
	}

}