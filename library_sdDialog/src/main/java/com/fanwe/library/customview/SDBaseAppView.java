package com.fanwe.library.customview;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.library.activity.SDBaseActivity;
import com.fanwe.library.activity.SDBaseActivity.SDBaseActivityLifeCircleListener;
import com.fanwe.library.utils.SDViewUtil;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;
import com.sunday.eventbus.SDEventObserver;

public class SDBaseAppView extends LinearLayout implements SDBaseActivityLifeCircleListener, SDEventObserver
{

	protected Activity mActivity;

	public SDBaseAppView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		baseInit();
	}

	public SDBaseAppView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		baseInit();
	}

	public SDBaseAppView(Context context)
	{
		super(context);
		baseInit();
	}

	protected void baseInit()
	{
		SDEventManager.register(this);
		if (getContext() instanceof Activity)
		{
			setActivity((Activity) getContext());
		}
		init();
	}

	protected void init()
	{

	}

	public void setActivity(Activity activity)
	{
		this.mActivity = activity;
		if (activity instanceof SDBaseActivity)
		{
			((SDBaseActivity) activity).setmListenerLifeCircle(this);
		}
	}

	@Override
	protected void onDetachedFromWindow()
	{
		SDEventManager.unregister(this);
		super.onDetachedFromWindow();
	}

	protected void setContentView(int layoutId)
	{
		removeAllViews();
		LayoutInflater.from(getContext()).inflate(layoutId, this, true);
	}

	protected void setContentView(View view)
	{
		removeAllViews();
		addView(view);
	}

	protected void setContentView(View view, ViewGroup.LayoutParams params)
	{
		removeAllViews();
		addView(view, params);
	}

	public void hideView()
	{
		SDViewUtil.hide(this);
	}

	public void showView()
	{
		SDViewUtil.show(this);
	}

	public void invisibleView()
	{
		SDViewUtil.invisible(this);
	}

	public boolean toggleView(List<?> list)
	{
		if (list != null && !list.isEmpty())
		{
			showView();
			return true;
		} else
		{
			hideView();
			return false;
		}
	}

	public boolean toggleView(Object obj)
	{
		if (obj != null)
		{
			showView();
			return true;
		} else
		{
			hideView();
			return false;
		}
	}

	public boolean toggleView(String content)
	{
		if (!TextUtils.isEmpty(content))
		{
			showView();
			return true;
		} else
		{
			hideView();
			return false;
		}
	}

	public boolean toggleView(int show)
	{
		if (show == 1)
		{
			showView();
			return true;
		} else
		{
			hideView();
			return false;
		}
	}

	public boolean toggleFragmentView(boolean show)
	{
		if (show)
		{
			showView();
			return true;
		} else
		{
			hideView();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public <V extends View> V find(int id)
	{
		return (V) findViewById(id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState, Activity activity)
	{

	}

	@Override
	public void onStart(Activity activity)
	{

	}

	@Override
	public void onRestart(Activity activity)
	{

	}

	@Override
	public void onResume(Activity activity)
	{

	}

	@Override
	public void onPause(Activity activity)
	{

	}

	@Override
	public void onStop(Activity activity)
	{

	}

	@Override
	public void onDestroy(Activity activity)
	{

	}

	@Override
	public void onEvent(SDBaseEvent event)
	{

	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{

	}

	@Override
	public void onEventBackgroundThread(SDBaseEvent event)
	{

	}

	@Override
	public void onEventAsync(SDBaseEvent event)
	{

	}
}
