package com.fanwe.gif;

import java.io.File;

import android.text.TextUtils;

import com.fanwe.app.App;
import com.fanwe.common.HttpManagerX;
import com.fanwe.library.utils.MD5Util;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.util.OtherUtils;

public class SDUrlSourceLoader
{

	private static SDUrlSourceLoader mInstance;

	private String mGifFileDir;

	private SDUrlSourceLoader()
	{
		mGifFileDir = OtherUtils.getDiskCacheDir(App.getApplication(), "urlsource");
		File filePath = new File(mGifFileDir);
		if (filePath.exists())
		{
			if (filePath.isFile())
			{
				filePath.delete();
			}
		} else
		{
			filePath.mkdirs();
		}
		mGifFileDir = mGifFileDir + File.separator;
	}

	public static SDUrlSourceLoader getInstance()
	{
		if (mInstance == null)
		{
			syncInit();
		}
		return mInstance;
	}

	private static void syncInit()
	{
		mInstance = new SDUrlSourceLoader();
	}

	public synchronized void load(String url, final SDUrlSourceLoaderListener listener)
	{
		if (TextUtils.isEmpty(url))
		{
			return;
		}
		File cache = getCache(url);
		if (cache != null)
		{
			if (listener != null)
			{
				listener.onSuccess(null, cache);
			}
		} else
		{
			String path = getPath(url);
			HttpManagerX.getHttpUtils().download(url, path, new RequestCallBack<File>()
			{
				@Override
				public void onStart()
				{
					if (listener != null)
					{
						listener.onStart();
					}
				}

				@Override
				public void onFinish()
				{
					if (listener != null)
					{
						listener.onFinish();
					}
				}

				@Override
				public void onSuccess(ResponseInfo<File> responseInfo)
				{
					if (listener != null)
					{
						listener.onSuccess(responseInfo, responseInfo.result);
					}
				}

				@Override
				public void onFailure(HttpException error, String msg)
				{
					if (listener != null)
					{
						listener.onFailure(error, msg);
					}
				}
			});
		}

	}

	public static String createKey(String url)
	{
		return MD5Util.MD5(url);
	}

	private String getPath(String url)
	{
		return mGifFileDir + createKey(url);
	}

	public File getCache(String url)
	{
		String path = getPath(url);
		File file = new File(path);
		if (file.exists())
		{
			return file;
		} else
		{
			return null;
		}
	}

	public interface SDUrlSourceLoaderListener
	{
		void onStart();

		void onFinish();

		void onSuccess(ResponseInfo<File> responseInfo, File file);

		void onFailure(HttpException error, String msg);
	}

}
