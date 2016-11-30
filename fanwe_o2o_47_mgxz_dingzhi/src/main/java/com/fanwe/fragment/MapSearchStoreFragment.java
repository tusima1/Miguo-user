package com.fanwe.fragment;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.http.HttpHandler;

public class MapSearchStoreFragment extends MapSearchFragment
{

	@Override
	protected void init()
	{
		super.init();
		mTitle.setMiddleTextTop(SDResourcesUtil.getString(R.string.nearby_store));
	}

	@Override
	protected HttpHandler<String> requestMapsearch()
	{
		stopRequest();
		return null;
	}
}
