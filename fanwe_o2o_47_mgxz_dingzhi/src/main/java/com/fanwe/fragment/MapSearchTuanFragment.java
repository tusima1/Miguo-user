package com.fanwe.fragment;

import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.http.HttpHandler;

public class MapSearchTuanFragment extends MapSearchFragment
{

	@Override
	protected void init()
	{
		super.init();
		mTitle.setMiddleTextTop(SDResourcesUtil.getString(R.string.nearby_tuan_gou));
	}

	@Override
	protected HttpHandler<String> requestMapsearch()
	{
		stopRequest();
		return null;
	}
}
