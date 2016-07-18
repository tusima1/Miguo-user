package com.fanwe.fragment;

import com.fanwe.model.Cart_checkActModel;

public class OrderDetailBaseFragment extends BaseFragment
{
	protected Cart_checkActModel mCheckActModel;
	
	public Cart_checkActModel getmCheckActModel()
	{
		return mCheckActModel;
	}

	public void setmCheckActModel(Cart_checkActModel mCheckActModel)
	{
		this.mCheckActModel = mCheckActModel;
		refreshData();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}