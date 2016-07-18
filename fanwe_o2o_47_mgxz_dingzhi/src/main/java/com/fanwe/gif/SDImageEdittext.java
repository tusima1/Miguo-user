package com.fanwe.gif;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.Spannable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

public class SDImageEdittext extends EditText
{

	private Map<Integer, SDBaseSpan> mMapSpan = new HashMap<Integer, SDBaseSpan>();

	public SDImageEdittext(Context context)
	{
		this(context, null);
	}

	public SDImageEdittext(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init();
	}

	private void init()
	{
	}

	public void insert(String text)
	{
		int index = getSelectionStart();
		getText().insert(index, text);
	}

	public void insertSpan(SDBaseSpan span)
	{
		if (span != null)
		{
			String key = span.getKey();
			if (!TextUtils.isEmpty(key))
			{
				span.setStart(getSelectionStart());
				insert(key);
				getText().setSpan(span, span.getStart(), span.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				mMapSpan.put(span.getEnd(), span);
			}
		}
	}

	public void deleteLastSpan()
	{
		int index = getSelectionStart();
		SDBaseSpan span = mMapSpan.get(index);
		if (span != null)
		{
			getText().delete(span.getStart(), span.getEnd());
			mMapSpan.remove(index);
		}
	}

}
