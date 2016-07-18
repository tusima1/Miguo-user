package com.fanwe.library;

import android.app.Application;

import com.fanwe.library.config.SDConfig;
import com.fanwe.library.config.SDLibraryConfig;

public class SDLibrary
{

	private static SDLibrary mInstance;
	private Application mApplication;

	private SDLibraryConfig mConfig;

	public SDLibraryConfig getmConfig()
	{
		return mConfig;
	}

	public void initConfig(SDLibraryConfig mConfig)
	{
		this.mConfig = mConfig;
	}

	private SDLibrary()
	{
		mConfig = new SDLibraryConfig();
	}

	public Application getApplication()
	{
		return mApplication;
	}

	public void init(Application application)
	{
		this.mApplication = application;
		SDConfig.getConfig().init(application);
	}

	public static SDLibrary getInstance()
	{
		if (mInstance == null)
		{
			mInstance = new SDLibrary();
		}
		return mInstance;
	}

}
