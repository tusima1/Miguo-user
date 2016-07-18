package com.fanwe.gif;

import java.util.Map;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

public class SDImageTextView extends TextView
{
	private SDImageSpanBuilder mSpanBuilder;

	public SDImageTextView(Context context)
	{
		this(context, null);
	}

	public SDImageTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
		mSpanBuilder = new SDImageSpanBuilder(this);
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

	public void setImage(String url)
	{
		mSpanBuilder.setImage(url);
		appendFinish();
	}

	public void appendFinish()
	{
		setText(mSpanBuilder.build());
		setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected void onDetachedFromWindow()
	{
		mSpanBuilder.reset();
		super.onDetachedFromWindow();
	}

}
