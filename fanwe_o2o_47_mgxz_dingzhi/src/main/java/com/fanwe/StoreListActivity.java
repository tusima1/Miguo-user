package com.fanwe;

import android.os.Bundle;

import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.SellerFragment;

/**
 * 商家列表
 * 
 * @author js02
 * 
 */
public class StoreListActivity extends BaseActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_tuan_list);
		init();

	}

	private void init()
	{
		getSDFragmentManager().replace(R.id.act_tuan_list_fl_container, SellerFragment.class);
	}

}