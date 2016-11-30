
package com.fanwe.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.fanwe.BaseActivity;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.fragment.SDBaseFragment;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.title.SDTitleSimple;
import com.fanwe.library.title.SDTitleSimple.SDTitleSimpleListener;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.ViewUtils;

public abstract class BaseFragment extends SDBaseFragment implements SDTitleSimpleListener
{

	protected SDTitleSimple mTitle;
	private TitleType mTitleType = TitleType.TITLE_NONE;
//	private String tag="BaseFragement";

	public TitleType getmTitleType()
	{
		return mTitleType;
	}

	public void setmTitleType(TitleType mTitleType)
	{
		this.mTitleType = mTitleType;
	}

	@Override
	protected View onCreateTitleView()
	{
		View viewTitle = null;
		switch (getmTitleType())
		{
		case TITLE:
			viewTitle = LayoutInflater.from(getActivity()).inflate(R.layout.title_simple_sd, null);
			mTitle = (SDTitleSimple) viewTitle.findViewById(R.id.title);
			if (mTitle != null)
			{
				mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
				mTitle.setmListener(this);
			}
			break;

		default:
			break;
		}
		return viewTitle;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		ViewUtils.inject(this, view);
		init();
		super.onViewCreated(view, savedInstanceState);
	}

	protected void init()
	{
	}
	
	protected abstract String setUmengAnalyticsTag();

	public BaseActivity getBaseActivity()
	{
		Activity activity = getActivity();
		if (activity != null && activity instanceof BaseActivity)
		{
			return (BaseActivity) activity;
		}
		return null;
	}

	@Override
	public void onCLickLeft_SDTitleSimple(SDTitleItem v)
	{
		getActivity().finish();
	}

	@Override
	public void onCLickMiddle_SDTitleSimple(SDTitleItem v)
	{

	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{

	}
	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart(tag);
	}
	
	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd(tag);
	}

}
