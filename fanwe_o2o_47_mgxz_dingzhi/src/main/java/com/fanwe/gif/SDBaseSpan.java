package com.fanwe.gif;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.view.View;

public abstract class SDBaseSpan extends DynamicDrawableSpan
{
	protected View mView;
	protected Context mContext;
	protected Resources mResources;
	protected String key;
	protected int start = -1;
	protected int end = -1;

	protected boolean enable = true;

	public void setStart(int start)
	{
		this.start = start;
		updateIndex();
	}

	public void setEnable(boolean enable)
	{
		this.enable = enable;
	}

	public boolean isEnable()
	{
		return enable;
	}

	public int getStart()
	{
		return start;
	}

	public int getEnd()
	{
		return end;
	}

	public void setKey(String key)
	{
		this.key = key;
		updateIndex();
	}

	private void updateIndex()
	{
		if (start >= 0 && !TextUtils.isEmpty(key))
		{
			end = start + key.length();
		}
	}

	public String getKey()
	{
		return key;
	}

	public SDBaseSpan(View view)
	{
		this.mView = view;
		this.mContext = mView.getContext();
		this.mResources = mContext.getResources();
	}

	public void updateTargetView()
	{
		mView.postInvalidate();
	}

	protected abstract int getDrawableDefaultResId();

	protected abstract Bitmap onGetBitmap();

	protected void beforeReturnDrawable(Drawable drawable)
	{

	}

	@Override
	public Drawable getDrawable()
	{
		Drawable drawable;

		Bitmap bitmap = onGetBitmap();
		if (bitmap != null)
		{
			drawable = new BitmapDrawable(mResources, bitmap);
		} else
		{
			drawable = new BitmapDrawable(mResources, mResources.openRawResource(getDrawableDefaultResId()));
		}

		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		drawable.setBounds(0, 0, width, height);
		beforeReturnDrawable(drawable);
		return drawable;
	}

	@Override
	public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint)
	{
		Drawable b = getDrawable();
		canvas.save();
		int transY = bottom - b.getBounds().bottom;
		if (mVerticalAlignment == ALIGN_BASELINE)
		{
			transY -= paint.getFontMetricsInt().descent;
		}
		canvas.translate(x, transY);
		b.draw(canvas);
		canvas.restore();
	}
}
