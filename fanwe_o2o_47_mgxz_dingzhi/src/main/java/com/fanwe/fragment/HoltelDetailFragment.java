package com.fanwe.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.adapter.HotelListTuanAdapter;
import com.fanwe.adapter.TuanListAdapter;
import com.fanwe.library.customview.SDMoreLinearLayout;
import com.fanwe.library.customview.SDMoreLinearLayout.OnOpenCloseListener;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.GoodsModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HoltelDetailFragment extends StoreDetailBaseFragment
{
	@ViewInject(R.id.frag_hotel_detail_tuan_ll_more)
	public SDMoreLinearLayout mLl_more;
	
	@ViewInject(R.id.tv_number)
	private TextView mTv_number;
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_hotel_detail);
	}
	
	@Override
	protected void init()
	{
		super.init();
		bindData();
	}

	private void bindData()
	{
		if (!toggleFragmentView(mStoreModel))
		{
			return;
		}
		
		final List<GoodsModel> listModel = mStoreModel.getMain_list();
		if (!toggleFragmentView(listModel) && getArguments().getInt("type") != 15)
		{
			return;
		}
		SDViewBinder.setTextView(mTv_number,"（"+mStoreModel.getMain_list().size()+"）");
		mLl_more.setmMaxShowCount(4);
		mLl_more.setmViewMoreLayoutId(R.layout.view_more_switch);
		mLl_more.setmListenerOnOpenClose(new OnOpenCloseListener()
		{
			@Override
			public void onOpen(List<View> listView, View viewMore)
			{
				TextView tvMore = (TextView) viewMore.findViewById(R.id.tv_more);
				ImageView ivImage = (ImageView) viewMore.findViewById(R.id.iv_image);
				ivImage.setImageResource(R.drawable.bg_holtel_up);
				SDViewUtil.show(ivImage);
				tvMore.setText(R.string.click_close);
			}
			
			@Override
			public void onClose(List<View> listView, View viewMore)
			{
				TextView tvMore = (TextView) viewMore.findViewById(R.id.tv_more);
				ImageView ivImage = (ImageView) viewMore.findViewById(R.id.iv_image);
				ivImage.setImageResource(R.drawable.bg_hotel_more);
				SDViewUtil.show(ivImage);
				tvMore.setText("查看全部的"+ mStoreModel.getMain_list().size()+"个房型");
			}
		});
		
		HotelListTuanAdapter adapter = new HotelListTuanAdapter(listModel, getActivity(),getArguments().getInt("number"));
		mLl_more.setAdapter(adapter);
	}
	
}
