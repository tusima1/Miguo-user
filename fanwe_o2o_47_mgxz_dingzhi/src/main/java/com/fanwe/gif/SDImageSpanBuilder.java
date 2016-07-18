package com.fanwe.gif;

import android.view.View;

import com.fanwe.gif.SDPattern.MatcherInfo;

public class SDImageSpanBuilder extends SDBaseSpanBuilder<String>
{

	public SDImageSpanBuilder(View view)
	{
		super(view);
	}

	public void setImage(String url)
	{
		SDImageSpan span = new SDImageSpan(mView);
		span.setImage(url);
		putSpan(span);
	}

	@Override
	protected String getPatternString()
	{
		return "\\[([^\\[\\]]+)\\]";
	}

	@Override
	protected SDBaseSpan createSpanByFindKey(MatcherInfo matcherInfo, String value)
	{
		SDImageSpan span = new SDImageSpan(mView);
		span.setImage(value);
		return span;
	}

}
