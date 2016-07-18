package com.fanwe.library.alipay;

import org.json.JSONObject;

public class ResultChecker
{
	public static final int RESULT_INVALID_PARAM = 0;
	public static final int RESULT_CHECK_SIGN_FAILED = 1;
	public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

	private String mContent;

	public ResultChecker(String content)
	{
		this.mContent = content;
	}

	public String getmContent()
	{
		return mContent;
	}

	public void setmContent(String mContent)
	{
		this.mContent = mContent;
	}

	public int checkSign()
	{
		int retVal = RESULT_CHECK_SIGN_SUCCEED;

		try
		{
			JSONObject objContent = string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);

			int iSignContentEnd = result.indexOf("&sign_type=");
			String signContent = result.substring(0, iSignContentEnd);

			JSONObject objResult = string2JSON(result, "&");
			String signType = objResult.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = objResult.getString("sign");
			sign = sign.replace("\"", "");

			if (signType.equalsIgnoreCase("RSA"))
			{
				if (!Rsa.doCheck(signContent, sign, PartnerConfig.RSA_ALIPAY_PUBLIC))
					retVal = RESULT_CHECK_SIGN_FAILED;
			}
		} catch (Exception e)
		{
			retVal = RESULT_INVALID_PARAM;
			e.printStackTrace();
		}

		return retVal;
	}

	public String getValue(String key)
	{
		String value = null;

		try
		{
			JSONObject objContent = string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);

			JSONObject objResult = string2JSON(result, "&");
			if (objResult.has(key))
			{
				value = objResult.getString(key);
				value = value.replace("\"", "");
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return value;
	}

	public String getResultStatus()
	{
		String result = null;

		try
		{
			JSONObject objContent = string2JSON(this.mContent, ";");
			result = objContent.getString("resultStatus");
			result = result.substring(1, result.length() - 1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public String getMemo()
	{
		String result = null;

		try
		{
			JSONObject objContent = string2JSON(this.mContent, ";");
			result = objContent.getString("memo");
			result = result.substring(1, result.length() - 1);
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return result;
	}

	public boolean isPayOk()
	{
		boolean isPayOk = false;

		String success = getValue("success");
		if (checkSign() == RESULT_CHECK_SIGN_SUCCEED && success.equalsIgnoreCase("true"))
			isPayOk = true;

		return isPayOk;
	}

	public JSONObject string2JSON(String str, String split)
	{
		JSONObject json = new JSONObject();
		try
		{
			String[] arrStr = str.split(split);
			for (int i = 0; i < arrStr.length; i++)
			{
				String[] arrKeyValue = arrStr[i].split("=");
				json.put(arrKeyValue[0], arrStr[i].substring(arrKeyValue[0].length() + 1));
			}
		}

		catch (Exception e)
		{
			e.printStackTrace();
		}

		return json;
	}
}