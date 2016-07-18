package com.fanwe;

import android.os.Bundle;



import com.fanwe.fragment.MoreFragment;
import com.fanwe.o2o.miguo.R;

public class MoreSecondActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_more_container);
		init();
	}

	private void init()
	{
		getSDFragmentManager().replace(R.id.view_container_more, MoreFragment.class);
	}

	
}
