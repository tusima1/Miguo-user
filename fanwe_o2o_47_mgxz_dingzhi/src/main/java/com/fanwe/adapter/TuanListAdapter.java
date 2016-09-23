package com.fanwe.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.TuanDetailActivity;
import com.fanwe.app.App;
import com.fanwe.library.adapter.SDSimpleBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.MGStringFormatter;

public class TuanListAdapter extends SDSimpleBaseAdapter<GoodsModel>
{
	private boolean mShowDistance;
	public TuanListAdapter(List<GoodsModel> listModel, Activity activity)
	{
		this(listModel, activity, true);
	}

	public TuanListAdapter(List<GoodsModel> listModel, Activity activity, boolean showDistance)
	{
		super(listModel, activity);
		mShowDistance = showDistance;
	}
	
	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_tuan_list;
	}
	
	@Override
	public void bindData(int position, View convertView, ViewGroup parent, final GoodsModel model)
	{
		ImageView iv_red = get(R.id.iv_auto_red,convertView);
		ImageView iv_image = get(R.id.iv_image, convertView);
		ImageView iv_auto_order = get(R.id.iv_auto_order, convertView);
		ImageView iv_is_new = get(R.id.iv_is_new, convertView);
		TextView tv_name = get(R.id.tv_name, convertView);
		TextView tv_soudan = get(R.id.tv_soudan,convertView);
		TextView tv_brief = get(R.id.tv_brief, convertView);
		TextView tv_current_price = get(R.id.tv_current_price, convertView);
		TextView tv_original_price = get(R.id.tv_original_price, convertView);
		TextView tv_buy_count = get(R.id.tv_buy_count, convertView);
		TextView tv_buy_count_label = get(R.id.tv_buy_count_label, convertView);
		TextView tv_distance = get(R.id.tv_distance, convertView);
		
		SDViewUtil.hide(iv_auto_order);
		SDViewUtil.hide(iv_is_new);
		
		DisplayMetrics metric = new DisplayMetrics();
	    mActivity.getWindowManager().getDefaultDisplay().getMetrics(metric);
	    int width = metric.widthPixels;
	    int height = (int) (width * 0.242 - 5);
	    SDViewUtil.setViewHeight(iv_image,height);
		if(model.getRed_packet() == 1)
		{
			SDViewUtil.show(iv_red);
		}else
		{
			SDViewUtil.hide(iv_red);
		}
		if(model.getIs_first() == 0)
		{
			SDViewUtil.hide(tv_soudan);
			
		}else
		{
			SDViewUtil.show(tv_soudan);
			String format[] =model.getCurrent_price_format().split("\\￥");
			float price = Float.parseFloat(format[1])-model.getIs_first_price();
			String str []=(price+"").split("\\.");
			if( str[1].equals("0"))
			{
				SDViewBinder.setTextView(tv_soudan, "再减"+str[0]+"元");
			}else{
				BigDecimal bd1 = new BigDecimal(price);
				bd1 = bd1.setScale(1,BigDecimal.ROUND_HALF_UP);
				SDViewBinder.setTextView(tv_soudan, "再减"+bd1+"元");
			}
		}
		
		SDViewBinder.setImageView(model.getIcon(), iv_image);

		SDViewBinder.setTextView(tv_name, MGStringFormatter.getLimitedString(model.getSub_name(),13));
		SDViewBinder.setTextView(tv_brief, MGStringFormatter.getLimitedString(model.getBrief(),46));
		
		tv_original_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 设置中划线效果
		SDViewBinder.setTextView(tv_buy_count, String.valueOf(model.getBuy_count()));

		if (model.getAuto_order() == 1)
		{
			SDViewUtil.show(iv_auto_order);
		}
		if (model.getIs_taday() == 1)
		{
			SDViewUtil.show(iv_is_new);
		}
		SDViewBinder.setTextView(tv_current_price, model.getCurrent_price_format());
		SDViewBinder.setTextView(tv_original_price, model.getOrigin_price_format());
		if (mShowDistance)
		{
			SDViewBinder.setTextView(tv_distance, model.getDistanceFormat());
		} else
		{
			SDViewBinder.setTextView(tv_distance, null);
		}
		
		tv_buy_count_label.setText("已售");
		
		convertView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(App.getApplication(), TuanDetailActivity.class);
				intent.putExtra(TuanDetailActivity.EXTRA_GOODS_ID, model.getId());
				intent.putExtra(TuanDetailActivity.EXTRA_HOTEL_NUM, 1);
				mActivity.startActivity(intent);
			}
		});
	}
		
}
