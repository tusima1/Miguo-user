package com.fanwe.fragment;

public class HomeBaseFragment extends BaseFragment
{

	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
