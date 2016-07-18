package com.fanwe.constant;

/**
 * 网络地址配置类。如果非debug请直接改成DEBUG=false;
 * @author Administrator
 *
 */ 
public class  ServerUrl
{
	public static final boolean DEBUG = true;
	public static final String SERVER_API_TEST_URL="w2.mgxz.com";
//	public static final String SERVER_API_TEST_URL="114.55.58.178";
	public static final String SERVER_API_URL_MID = "www.mgxz.com";
	public static final String SERVER_API_URL_PRE = "http://";
	public static final String SERVER_API_URL_END = "/mapi/index.php";
	public static final String URL_PART_MOB = "/mob/main.html?from=app";
	public static final String URL_PART_WAP = "/wap/index.php";
	public static final String KEY_AES = "FANWE5LMUQC436IM";

	private static final String SERVER_API_URL = SERVER_API_URL_PRE + SERVER_API_URL_MID + SERVER_API_URL_END;
	
	
	public static String getServerApiUrl()
	{
		if (DEBUG)
		{
			return SERVER_API_URL_PRE + SERVER_API_TEST_URL + SERVER_API_URL_END;
		}else
		{
			return SERVER_API_URL;
		}
	} 
}
