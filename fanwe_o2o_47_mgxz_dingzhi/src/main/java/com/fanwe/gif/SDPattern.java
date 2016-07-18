package com.fanwe.gif;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class SDPattern
{

	public static List<String> find(String strPattern, String strContent)
	{
		List<String> listKeys = new ArrayList<String>();

		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(strContent);

		while (matcher.find())
		{
			String key = matcher.group(0);
			listKeys.add(key);
		}
		return listKeys;
	}

	public static Matcher matcher(String strPattern, String strContent)
	{
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(strContent);
		return matcher;
	}

	public static List<Integer> findPosition(String content, String key)
	{
		List<Integer> listPosition = new ArrayList<Integer>();
		if (content != null && !TextUtils.isEmpty(key))
		{
			int searchIndex = 0;
			int index = content.indexOf(key, searchIndex);
			while (index >= 0)
			{
				listPosition.add(index);
				searchIndex = index + 1;
				index = content.indexOf(key, searchIndex);
			}
		}
		return listPosition;
	}

	public static LinkedBlockingQueue<Integer> findPositionQueue(String content, String key)
	{
		LinkedBlockingQueue<Integer> listPosition = new LinkedBlockingQueue<Integer>();
		if (content != null && !TextUtils.isEmpty(key))
		{
			int searchIndex = 0;
			int index = content.indexOf(key, searchIndex);
			while (index >= 0)
			{
				listPosition.add(index);
				searchIndex = index + 1;
				index = content.indexOf(key, searchIndex);
			}
		}
		return listPosition;
	}

	public static List<MatcherInfo> findMatcherInfo(String strPattern, String strContent)
	{
		List<MatcherInfo> listResult = new ArrayList<MatcherInfo>();
		List<String> listKey = find(strPattern, strContent);
		if (listKey != null)
		{
			Integer position = null;
			Map<String, LinkedBlockingQueue<Integer>> mapKeyPositions = new HashMap<String, LinkedBlockingQueue<Integer>>();
			for (String key : listKey)
			{
				LinkedBlockingQueue<Integer> queuePosition = null;
				if (!mapKeyPositions.containsKey(key))
				{
					queuePosition = findPositionQueue(strContent, key);
					mapKeyPositions.put(key, queuePosition);
				} else
				{
					queuePosition = mapKeyPositions.get(key);
				}

				if (queuePosition.size() > 0)
				{
					position = queuePosition.poll();
					if (position != null)
					{
						MatcherInfo info = new MatcherInfo();
						info.key = key;
						info.start = position;
						listResult.add(info);
					}
				}
			}
		}
		return listResult;
	}

	public static class MatcherInfo
	{
		public String key;
		public int start = -1;
	}
}
