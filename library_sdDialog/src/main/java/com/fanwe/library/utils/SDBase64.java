package com.fanwe.library.utils;

import android.util.Base64;

public class SDBase64
{

	public static String encode(String content)
	{
		String result = null;
		try
		{
			result = Base64.encodeToString(content.getBytes("UTF-8"), Base64.DEFAULT);
		} catch (Exception e)
		{
			result = null;
		}
		return result;
	}

	public static String encode(byte[] content)
	{
		String result = null;
		try
		{
			result = Base64.encodeToString(content, Base64.DEFAULT);
		} catch (Exception e)
		{
			result = null;
		}
		return result;
	}

	public static String decode(String content)
	{
		String result = null;
		try
		{
			byte[] byteResult = Base64.decode(content, Base64.DEFAULT);
			result = new String(byteResult, "UTF-8");
		} catch (Exception e)
		{
			result = null;
		}
		return result;
	}

}
