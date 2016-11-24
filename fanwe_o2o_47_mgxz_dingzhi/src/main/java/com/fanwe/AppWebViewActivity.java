package com.fanwe;

import android.content.Intent;

import com.fanwe.event.EnumEventTag;
import com.fanwe.library.activity.WebViewActivity;
import com.fanwe.library.fragment.WebViewFragment;
import com.fanwe.library.fragment.WebViewFragment.EnumProgressMode;
import com.fanwe.library.fragment.WebViewFragment.EnumWebviewHeightMode;
import com.sunday.eventbus.SDBaseEvent;

/**
 * webview界面
 * 
 * @author js02
 * 
 */
public class AppWebViewActivity extends WebViewActivity
{

	private boolean mIsStartByAdvs = false;

	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	/*@Override
	protected void getIntentData()
	{
		mIsStartByAdvs = getIntent().getBooleanExtra(BaseActivity.EXTRA_IS_ADVS, false);
		super.getIntentData();
	}
*/
	@Override
	protected WebViewFragment createFragment()
	{
		WebViewFragment fragment = new WebViewFragment();
		fragment.setmProgressMode(EnumProgressMode.NONE);
		fragment.setmWebviewHeightMode(EnumWebviewHeightMode.MATCH_PARENT);
		return fragment;
	}


	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case LOGIN_SUCCESS:
			if (mFragWebview != null)
			{
				mFragWebview.startLoadData();
			}
			break;
		default:
			break;
		}
		super.onEventMainThread(event);
	}

}