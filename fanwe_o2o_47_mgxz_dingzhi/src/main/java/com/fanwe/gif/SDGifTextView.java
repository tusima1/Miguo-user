package com.fanwe.gif;

import java.util.Map;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

public class SDGifTextView extends TextView
{
	private SDGifSpanBuilder mSpanBuilder;

	public SDGifTextView(Context context)
	{
		this(context, null);
	}

	public SDGifTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mSpanBuilder = new SDGifSpanBuilder(this);
	}

	/**
	 * 设置key和url的数据map
	 * 
	 * @param mapDictonary
	 */
	public void setDictonary(Map<String, String> mapDictonary)
	{
		mSpanBuilder.setDictonary(mapDictonary);
	}

	public void setTextContent(String content)
	{
		mSpanBuilder.setTextContent(content);
		appendFinish();
	}

	public void appendFinish()
	{
		setText(mSpanBuilder.build());
		setMovementMethod(LinkMovementMethod.getInstance());
		playGif();
	}

	public void playGif()
	{
		if (!mSpanBuilder.hasSpan())
		{
			return;
		}
		mSpanBuilder.play();
	}

	public void stopPlay()
	{
		mSpanBuilder.stopPlay();
	}

	@Override
	protected void onDetachedFromWindow()
	{
		stopPlay();
		mSpanBuilder.reset();
		super.onDetachedFromWindow();
	}

}
