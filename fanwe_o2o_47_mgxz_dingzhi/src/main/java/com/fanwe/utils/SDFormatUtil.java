package com.fanwe.utils;

import java.text.NumberFormat;

import android.text.TextUtils;

import com.fanwe.library.utils.SDTypeParseUtil;

public class SDFormatUtil
{

	public static String formatMoneyChina(String money)
	{
		if (!TextUtils.isEmpty(money))
		{
			String moneyRound = String.valueOf(SDNumberUtil.round(SDTypeParseUtil.getDouble(money), 2));
			money = moneyRound;
			if (money.contains("."))
			{
				int decimalIndex = money.indexOf(".");
				String decimalPart = money.substring(decimalIndex + 1);
				if ("0".equals(decimalPart))
				{
					money = money.substring(0, decimalIndex);
				}
			}
			return "￥" + money;
		} else
		{
			return "￥0";
		}
	}

	public static String formatMoneyChina(double money)
	{
		return formatMoneyChina(String.valueOf(money));
	}
	public static float stringToFloat(String value){
		if(TextUtils.isEmpty(value)){
			return 0.00f;
		}else{
			return Float.valueOf(value);
		}
	}


	public static int  stringToInteger(String number){
		if(TextUtils.isEmpty(number)){
			return 0;
		}
		return Integer.valueOf(number);
	}

	public static String formatNumberString(String formatString, int number)
	{
		if (formatString != null && formatString.length() > 0)
		{
			NumberFormat format = NumberFormat.getNumberInstance();
			format.setMaximumFractionDigits(number);
			try
			{
				return format.format(Double.valueOf(formatString));
			} catch (Exception e)
			{
				return null;
			}

		} else
		{
			return formatString;
		}
	}

	public static String formatNumberDouble(double formatDouble, int number)
	{
		return formatNumberString(String.valueOf(formatDouble), number);
	}

	public static String formatDuring(long mss)
	{
		long days = getDuringDay(mss);
		long hours = getDuringHours(mss);
		long minutes = getDuringMinutes(mss);
		long seconds = getDuringSeconds(mss);
		StringBuilder sb = new StringBuilder();
		if (days > 0)
		{
			sb.append(days + "天");
		}
		if (hours > 0)
		{
			sb.append(hours + "小时");
		}
		if (minutes > 0)
		{
			sb.append(minutes + "分钟");
		}
		return sb.toString();
	}

	public static long getDuringDay(long mss)
	{
		return mss / getDaysMilliseconds();
	}

	public static long getDuringHours(long mss)
	{
		return (mss % (getDaysMilliseconds())) / (getHoursMilliseconds());
	}

	public static long getDuringMinutes(long mss)
	{
		return (mss % (getHoursMilliseconds())) / (getMinutesMilliseconds());
	}

	public static long getDuringSeconds(long mss)
	{
		return (mss % (getMinutesMilliseconds())) / getSecondsMilliseconds();
	}

	public static long getDaysMilliseconds()
	{
		return (1000 * 60 * 60 * 24);
	}

	public static long getHoursMilliseconds()
	{
		return (1000 * 60 * 60);
	}

	public static long getMinutesMilliseconds()
	{
		return (1000 * 60);
	}

	public static long getSecondsMilliseconds()
	{
		return (1000);
	}
}
