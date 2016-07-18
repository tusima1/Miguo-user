package com.fanwe.fragment;

import com.fanwe.o2o.miguo.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MarketSortFragment extends BaseFragment{
	
	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.frag_market_sort);
	}
	
	@Override
	protected void init() {
		super.init();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
