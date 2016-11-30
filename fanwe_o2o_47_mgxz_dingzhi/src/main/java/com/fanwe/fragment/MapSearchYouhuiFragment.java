package com.fanwe.fragment;

import com.lidroid.xutils.http.HttpHandler;

public class MapSearchYouhuiFragment extends MapSearchFragment
{

	@Override
	protected void init()
	{
		super.init();
		mTitle.setMiddleTextTop("附近优惠");
	}

	@Override
	protected HttpHandler<String> requestMapsearch()
	{
		stopRequest();
		return null;
	}
}
