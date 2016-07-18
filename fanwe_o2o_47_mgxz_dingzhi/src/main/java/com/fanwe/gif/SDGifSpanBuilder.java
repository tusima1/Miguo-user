package com.fanwe.gif;

import android.view.View;

import com.fanwe.gif.SDHandlerLooper.SDHandlerLooperListener;
import com.fanwe.gif.SDPattern.MatcherInfo;

public class SDGifSpanBuilder extends SDBaseSpanBuilder<String>
{

	private SDHandlerLooper mHandlerLooper = new SDHandlerLooper();

	public SDGifSpanBuilder(View view)
	{
		super(view);
	}

	@Override
	protected SDBaseSpan createSpanByFindKey(MatcherInfo matcherInfo, String url)
	{
		SDGifSpan span = new SDGifSpan(mView);
		span.setGif(url);
		return span;
	}

	@Override
	protected String getPatternString()
	{
		return "\\[([^\\[\\]]+)\\]";
	}

	public void play()
	{
		mHandlerLooper.start(200, new SDHandlerLooperListener()
		{

			@Override
			public void run()
			{
				mView.postInvalidate();
			}
		});
	}

	public void stopPlay()
	{
		mHandlerLooper.stop();
	}

}
