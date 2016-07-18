package com.fanwe.gif;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class SDBitmapCache
{
	private static final int DEFAULT_MEMORY_CACHE_SIZE = 1 * 1024 * 1024;

	private static SDBitmapCache mInstance;

	private LruCache<String, Bitmap> mCache;

	private SDBitmapCache()
	{
		mCache = new LruCache<String, Bitmap>(DEFAULT_MEMORY_CACHE_SIZE);
	}

	public static SDBitmapCache getInstance()
	{
		if (mInstance == null)
		{
			syncInit();
		}
		return mInstance;
	}

	private static void syncInit()
	{
		mInstance = new SDBitmapCache();
	}

	public LruCache<String, Bitmap> getMemoryCache()
	{
		return mCache;
	}

}
