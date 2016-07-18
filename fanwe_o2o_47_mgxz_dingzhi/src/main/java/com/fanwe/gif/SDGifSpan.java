package com.fanwe.gif;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.gif.SDUrlSourceLoader.SDUrlSourceLoaderListener;
import com.fanwe.library.command.SDCommand;
import com.fanwe.library.command.SDCommandManager;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public class SDGifSpan extends SDBaseSpan
{

	private int mCurrentFrameIndex;
	private int mTotalFrame;
	private GifDecoder mDecoder;
	private SDUrlSourceLoader mGifLoader = SDUrlSourceLoader.getInstance();

	private String cacheKeyPrefix;
	private InputStream inputStream;
	private boolean isGifReady = false;

	public SDGifSpan(View view)
	{
		super(view);
	}

	public SDGifSpan setGif(final InputStream is)
	{
		inputStream = is;
		return this;
	}

	public boolean isGifReady()
	{
		return isGifReady;
	}

	public void read()
	{
		if (inputStream != null && mDecoder == null)
		{
			mCurrentFrameIndex = 0;
			mDecoder = new GifDecoder();
			mDecoder.read(inputStream);
			mDecoder.complete();
			mTotalFrame = mDecoder.getFrameCount();
			isGifReady = true;
		}
	}

	public SDGifSpan setGif(File file)
	{
		try
		{
			cacheKeyPrefix = MD5Util.MD5(file.getAbsolutePath());
			setGif(new FileInputStream(file));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return this;
	}

	public SDGifSpan setGif(int resId)
	{
		cacheKeyPrefix = MD5Util.MD5(String.valueOf(resId));
		setGif(mContext.getResources().openRawResource(resId));
		return this;
	}

	public SDGifSpan setGif(String url)
	{
		cacheKeyPrefix = MD5Util.MD5(String.valueOf(url));
		mGifLoader.load(url, new SDUrlSourceLoaderListener()
		{

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo, File file)
			{
				try
				{
					setGif(new FileInputStream(file));
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}

			@Override
			public void onStart()
			{
			}

			@Override
			public void onFinish()
			{
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
			}
		});
		return this;
	}

	private String getCacheKey()
	{
		return cacheKeyPrefix + mCurrentFrameIndex;
	}

	private void next()
	{
		if (isGifReady && mTotalFrame > 0)
		{
			mCurrentFrameIndex = (mCurrentFrameIndex + 1) % mTotalFrame;
		}
	}

	@Override
	protected int getDrawableDefaultResId()
	{
		return R.drawable.nopic_expression;
	}

	@Override
	protected void beforeReturnDrawable(Drawable drawable)
	{
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth() + 15, drawable.getIntrinsicHeight() + 15);
	}

	@Override
	protected Bitmap onGetBitmap()
	{
		Bitmap bitmap = null;
		if (isGifReady)
		{
			bitmap = mDecoder.getFrame(mCurrentFrameIndex);
			next();
		} else
		{
			SDCommandManager.getInstance().add(new SDCommand()
			{

				@Override
				public void onRun()
				{
					read();
				}
			});
		}
		return bitmap;
	}
}
