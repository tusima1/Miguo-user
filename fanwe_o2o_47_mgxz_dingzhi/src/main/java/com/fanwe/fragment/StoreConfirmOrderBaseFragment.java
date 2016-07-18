package com.fanwe.fragment;


import com.fanwe.model.Store_ActModel;

public class StoreConfirmOrderBaseFragment extends BaseFragment{

	protected Store_ActModel store_ActModel;
	
	public Store_ActModel getmStore_ActModel()
	{
		return store_ActModel;
	}
	public void setmStore_Model(Store_ActModel mStoreModel)
	{
		this.store_ActModel = mStoreModel;
		refreshData();
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
