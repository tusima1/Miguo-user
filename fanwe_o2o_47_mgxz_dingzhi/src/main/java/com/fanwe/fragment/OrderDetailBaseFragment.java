package com.fanwe.fragment;

import com.fanwe.shoppingcart.model.ShoppingBody;
import com.miguo.utils.MGUIUtil;

public class OrderDetailBaseFragment extends BaseFragment
{
	protected ShoppingBody mCheckActModel;
	
	public ShoppingBody getmCheckActModel()
	{
		return mCheckActModel;
	}

	public void setmCheckActModel(ShoppingBody mCheckActModel)
	{
		this.mCheckActModel = mCheckActModel;
		MGUIUtil.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				refreshData();
			}
		});

	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}