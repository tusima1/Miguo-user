package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.HoltelListActivity;
import com.fanwe.HoltelSearchCityActivity;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;

public class GPSFragment extends BaseFragment
{

	@ViewInject(R.id.ll_gps_city)
	private LinearLayout mLl_city;
	
	@ViewInject(R.id.tv_city)
	private TextView mTv_city;
	
	private static int request_Code = 675;
	
	protected OnGPSLintener mListener;

	public void setOnGPSListener(OnGPSLintener listener)
	{
		this.mListener = listener;
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_gps);
	}
	@Override
	protected void init() {
		super.init();
		registeClick();
		initTextView();
	}
	private void initTextView() {
		
		mTv_city.setText(AppRuntimeWorker.getCity_name());
		if(mListener != null)
		{
			mListener.setOnGPSLintener(AppRuntimeWorker.getCity_name());
		}
	}
	private void registeClick() 
	{
	
		mLl_city.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_gps_city:
			clickCity();
			break;

		default:
			break;
		}
	}
	private void clickCity() {
	
		Intent intent = new Intent(getActivity(), HoltelSearchCityActivity.class);
		startActivityForResult(intent, request_Code);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == request_Code)
		{
			if(resultCode == HoltelSearchCityActivity.result_Code)
			{
				mTv_city.setText(data.getExtras().getString("city"));
				if(mListener != null)
				{
					mListener.setOnGPSLintener(data.getExtras().getString("city"));
				}
			}
		}
	}
	public interface OnGPSLintener
	{
		public void setOnGPSLintener(String city);
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
