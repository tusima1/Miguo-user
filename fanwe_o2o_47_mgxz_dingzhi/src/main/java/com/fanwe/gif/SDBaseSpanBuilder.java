package com.fanwe.gif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.View;

import com.fanwe.gif.SDPattern.MatcherInfo;

public abstract class SDBaseSpanBuilder<T>
{
	public static final String KEY_DEFAULT = "KEY";

	protected View mView;
	protected Context mContext;
	protected List<MatcherInfo> mListKey = new ArrayList<MatcherInfo>();
	protected Map<String, T> mMapDictonary = new HashMap<String, T>();
	protected List<SDBaseSpan> mListSpan = new ArrayList<SDBaseSpan>();
	protected SpannableStringBuilder mSb = new SpannableStringBuilder();

	public SDBaseSpanBuilder(View view)
	{
		this.mView = view;
		mContext = view.getContext();
	}

	public boolean hasSpan()
	{
		return !mListSpan.isEmpty();
	}

	public void setTextContent(String content)
	{
		reset();
		if (!TextUtils.isEmpty(content))
		{
			findKeys(content);
			appendText(content);
			for (MatcherInfo matcherInfo : mListKey)
			{
				T value = getValue(matcherInfo.key);
				if (value != null)
				{
					SDBaseSpan span = createSpanByFindKey(matcherInfo, value);
					span.setKey(matcherInfo.key);
					span.setStart(matcherInfo.start);
					putSpan(span);
				}
			}
		}
	}

	protected abstract SDBaseSpan createSpanByFindKey(MatcherInfo matcherInfo, T url);

	public void setDictonary(Map<String, T> mapDictonary)
	{
		this.mMapDictonary = mapDictonary;
	}

	private T getValue(String key)
	{
		T value = null;
		if (mMapDictonary != null)
		{
			value = mMapDictonary.get(key);
		}
		return value;
	}

	private void findKeys(String text)
	{
		String patternString = getPatternString();
		if (!TextUtils.isEmpty(patternString))
		{
			mListKey.clear();
			List<MatcherInfo> listKey = SDPattern.findMatcherInfo(patternString, text);
			if (listKey != null && !listKey.isEmpty())
			{
				mListKey.addAll(listKey);
			}
		}
	}

	protected abstract String getPatternString();

	public void reset()
	{
		mListKey.clear();
		mListSpan.clear();
		mSb.clear();
		mSb.clearSpans();
	}

	public void appendText(String text)
	{
		mSb.append(text);
	}

	/**
	 * 最终添加都要调用这个唯一入口
	 * 
	 * @param key
	 * @param span
	 */
	protected void putSpan(SDBaseSpan span)
	{
		if (span == null)
		{
			return;
		}
		String key = span.getKey();
		int start = span.getStart();

		// 验证key
		if (TextUtils.isEmpty(key))
		{
			key = createDefaultKey();
			span.setKey(key);
		}
		if (!isContentContainsKey(key))
		{
			mSb.append(key);
		}

		// 验证position
		if (start < 0)
		{
			start = mSb.length() - key.length();
			span.setStart(start);
		}
		mListSpan.add(span);
	}

	public boolean isContentContainsKey(String key)
	{
		return mSb.toString().contains(key);
	}

	protected String createDefaultKey()
	{
		long keyEnd = System.currentTimeMillis();
		String key = "[" + KEY_DEFAULT + keyEnd + "]";
		if (isContentContainsKey(key))
		{
			key = createDefaultKey();
		}
		return key;
	}

	private void insertSpans()
	{
		for (SDBaseSpan span : mListSpan)
		{
			if (span.isEnable())
			{
				mSb.setSpan(span, span.getStart(), span.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
	}

	public SpannableStringBuilder build()
	{
		insertSpans();
		return mSb;
	}

}
