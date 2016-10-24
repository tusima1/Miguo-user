package com.fanwe.library.common;

import android.app.Activity;

import java.util.Stack;

public class SDActivityManager
{
	private static Stack<Activity> mStackActivity;
	private static SDActivityManager mInstance;

	private SDActivityManager()
	{
		mStackActivity = new Stack<Activity>();
	}

	public static SDActivityManager getInstance()
	{
		if (mInstance == null)
		{
			syncInit();
		}
		return mInstance;
	}

	private synchronized static void syncInit()
	{
		if (mInstance == null)
		{
			mInstance = new SDActivityManager();
		}
	}

	// ----------------------------activity life method

	public void onCreate(Activity activity)
	{
		addActivity(activity);
	}

	public void onResume(Activity activity)
	{
		addActivity(activity);
	}

	public void onDestroy(Activity activity)
	{
		removeActivity(activity);
	}

	public Activity getLastActivity()
	{
		Activity activity =null;
		if (mStackActivity!=null && mStackActivity.size()>0){
			activity = mStackActivity.lastElement();
		}
		return activity;
	}

	public boolean isLastActivity(Activity activity)
	{
		if (activity != null)
		{
			return getLastActivity() == activity;
		} else
		{
			return false;
		}
	}

	public boolean isEmpty()
	{
		return mStackActivity.isEmpty();
	}

	public void addActivity(Activity activity)
	{
		if (!mStackActivity.contains(activity))
		{
			mStackActivity.add(activity);
		}
	}

	/**
	 * 移除指定的Activity
	 */
	public void removeActivity(Activity activity)
	{
		if (activity != null)
		{
			mStackActivity.remove(activity);
		}
	}

	public void finishLastActivity()
	{
		Activity activity = getLastActivity();
		finishActivity(activity);
	}

	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity)
	{
		if (activity != null)
		{
			mStackActivity.remove(activity);
			activity.finish();
		}
	}

	/**
	 * 结束指定类名的Activity
	 */
	public void finishActivity(Class<?> cls)
	{
		for (Activity activity : mStackActivity)
		{
			if (activity.getClass().equals(cls))
			{
				finishActivity(activity);
			}
		}
	}

	public void finishAllActivity()
	{
		for (Activity activity : mStackActivity)
		{
			if (activity != null)
			{
				activity.finish();
			}
		}
		mStackActivity.clear();
	}

	public void finishAllActivityExcept(Class<?> cls)
	{
		for (int i = mStackActivity.size() - 1; i >= 0; i--)
		{
			Activity act = mStackActivity.get(i);
			if (act != null)
			{
				if (act.getClass().equals(cls))
				{
					continue;
				} else
				{
					finishActivity(act);
				}
			}
		}
	}

	public void finishAllActivityExcept(Activity activity)
	{
		for (int i = mStackActivity.size() - 1; i >= 0; i--)
		{
			Activity act = mStackActivity.get(i);
			if (act != null)
			{
				if (act == activity)
				{
					continue;
				} else
				{
					finishActivity(act);
				}
			}
		}
	}

}