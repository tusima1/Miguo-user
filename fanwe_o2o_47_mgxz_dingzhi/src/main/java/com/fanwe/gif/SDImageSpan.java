package com.fanwe.gif;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.fanwe.library.utils.MD5Util;
import com.fanwe.o2o.miguo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class SDImageSpan extends SDBaseSpan
{

	private String cacheKey;
	private String url;

	public SDImageSpan(View view)
	{
		super(view);
	}

	public SDImageSpan setImage(String url)
	{
		this.url = url;
		this.cacheKey = MD5Util.MD5(url);
		return this;
	}

	@Override
	protected int getDrawableDefaultResId()
	{
		return R.drawable.nopic_expression;
	}

	@Override
	protected void beforeReturnDrawable(Drawable drawable)
	{
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth() + 20, drawable.getIntrinsicHeight() + 20);
	}

	@Override
	protected Bitmap onGetBitmap()
	{
		Bitmap bitmap = SDBitmapCache.getInstance().getMemoryCache().get(cacheKey);
		if (bitmap == null)
		{
			ImageLoader.getInstance().loadImage(url, new ImageLoadingListener()
			{

				@Override
				public void onLoadingStarted(String imageUri, View view)
				{
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason)
				{
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage)
				{
					SDBitmapCache.getInstance().getMemoryCache().put(cacheKey, loadedImage);
					updateTargetView();
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view)
				{
				}
			});
		}
		return bitmap;
	}
}
