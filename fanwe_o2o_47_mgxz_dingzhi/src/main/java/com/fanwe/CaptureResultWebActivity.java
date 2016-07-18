package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;

public class CaptureResultWebActivity extends BaseActivity
{
	
	private String uriPath;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE_NONE);
		setContentView(R.layout.act_capture_web);
		init();
	}

	private void init() 
	{
		initgetData();
		initWeb();
	}

	private void initWeb()
	{
		AppWebViewFragment frag = new AppWebViewFragment();
		frag.setShowTitle(true);
		frag.setUrl(uriPath);
		frag.setmProgressMode(EnumProgressMode.NONE);
		getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all, frag);
	}

	private void initgetData()
	{
		uriPath= getIntent().getStringExtra("url");
	}

}
