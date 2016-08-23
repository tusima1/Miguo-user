package com.fanwe.fragment;

import com.fanwe.shoppingcart.model.ShoppingBody;

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
		refreshData();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}