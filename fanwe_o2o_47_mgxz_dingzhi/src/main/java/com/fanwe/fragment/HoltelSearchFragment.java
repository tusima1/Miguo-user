package com.fanwe.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.SearchListActivity;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class HoltelSearchFragment extends BaseFragment
{
	@ViewInject(R.id.ll_holtel_search)
	private LinearLayout mLl_search;
	
	@ViewInject(R.id.tv_search)
	private TextView mTv_search;

	private String city;
	
	public OnHoltelSearchListener mListener;

	private static int request_Code =656;
	
	public void setOnHoltelSearchListener(OnHoltelSearchListener listener)
	{
		this.mListener =listener;
	}
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_search);
	}
	@Override
	protected void init()
	{
		super.init();
		registeClick();
	}

	private void registeClick()
	{
		mLl_search.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) 
	{
		switch (v.getId())
		{
		case R.id.ll_holtel_search:
			clickSearch();
			break;
			
		default:
			break;
		}
	}
	private void clickSearch()
	{
		Intent intent = new Intent(getActivity(),SearchListActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("city",getArguments().getString("city"));
		bundle.putInt("id", 22);
		intent.putExtras(bundle);
		startActivityForResult(intent, request_Code);
		
	}
	
	public interface OnHoltelSearchListener
	{
		public void onBusinessSearchListener(String search,int qid,int id);
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode,Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == request_Code)
		{
			if(resultCode == SearchListActivity.result_CODE)
			{	
				String name = data.getExtras().getString("search");
				int qid = data.getExtras().getInt("qid");
				int id = data.getExtras().getInt("id");
				mTv_search.setText(name);
				if(mListener !=null)
				{
					mListener.onBusinessSearchListener(name, qid,id);
				}
			}
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
