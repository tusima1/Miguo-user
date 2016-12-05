package com.fanwe.fragment;

import com.lidroid.xutils.http.HttpHandler;

public class MapSearchEventFragment extends MapSearchFragment
{

	@Override
	protected void init()
	{
		super.init();
		mTitle.setMiddleTextTop("附近活动");
	}

	@Override
	protected HttpHandler<String> requestMapsearch()
	{
		stopRequest();
		return null;
	}
}
