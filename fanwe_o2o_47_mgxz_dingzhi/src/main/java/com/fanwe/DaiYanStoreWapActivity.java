package com.fanwe;

import android.content.Intent;
import android.os.Bundle;

import com.fanwe.fragment.AppWebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.o2o.miguo.R;


/**
 * 代言商品
 * 
 * @author Administrator
 * 
 */
public class DaiYanStoreWapActivity extends BaseActivity
{
	
	private String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_distribution_store_wap);
		init();
	}

	private void init()
	{
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		String url ="";
		if (extras!=null) {
			url=extras.getString("url", "");
			user_id =extras.getString("user_id");
		}
		AppWebViewFragment frag = new AppWebViewFragment();
//		String session = AppConfig.getSessionId();
		frag.setShowTitle(true);
		frag.setUrl(url);
		//隐藏“推广”
		//frag.setUserId(1);
		frag.setUserId(user_id);
		frag.setmProgressMode(EnumProgressMode.NONE);
		getSDFragmentManager().replace(R.id.act_distribution_store_wap_fl_all,frag);
	}

}
