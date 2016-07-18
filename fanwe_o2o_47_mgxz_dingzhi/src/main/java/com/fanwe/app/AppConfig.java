package com.fanwe.app;

import com.fanwe.library.config.SDConfig;
import com.fanwe.o2o.miguo.R;

public class AppConfig
{

	//
	public static int getRegionConfVersion()
	{
		return SDConfig.getConfig().getInt(R.string.config_region_version, 0);
	}

	public static void setRegionConfVersion(int value)
	{
		SDConfig.getConfig().setInt(R.string.config_region_version, value);
	}

	//
	public static String getSessionId()
	{
		return SDConfig.getConfig().getString(R.string.config_session_id, "");
	}

	public static void setSessionId(String value)
	{
		SDConfig.getConfig().setString(R.string.config_session_id, value);
	}

	//
	public static String getUserName()
	{
		return SDConfig.getConfig().getString(R.string.config_user_name, "");
	}

	public static void setUserName(String userName)
	{
		SDConfig.getConfig().setString(R.string.config_user_name, userName);
	}

	//
	public static String getRefId()
	{
		return SDConfig.getConfig().getString(R.string.config_ref_id, "");
	}

	public static void setRefId(String value)
	{
		SDConfig.getConfig().setString(R.string.config_ref_id, value);
	}

	//
	public static String getImageCode()
	{
		return SDConfig.getConfig().getString(R.string.config_image_code, null);
	}

	public static void setImageCode(String value)
	{
		SDConfig.getConfig().setString(R.string.config_image_code, value);
	}

}
