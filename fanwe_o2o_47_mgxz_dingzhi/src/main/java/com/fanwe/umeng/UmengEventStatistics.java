package com.fanwe.umeng;

import java.util.Map;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;

/**
 * @author didikee
 * @date 2016年7月1日 上午10:59:11
 * 
 * 友盟事件统计
 */
public class UmengEventStatistics {
	public static final String BUY="Buy";
	public static final String MAIN_1="Home";
	public static final String MAIN_2="Merchant";
	public static final String MAIN_3="Market";
	public static final String MAIN_4="Mine";
	
	/**发送事件id**/
	public static void sendEvent(Context context,String eventID) {
		MobclickAgent.onEvent(context, eventID);
	}
	
	/**发送事件id和附加key-values信息**/
	public static void sendEventWithKeyValues(Context context,String eventID,Map<String, String> keyValues) {
		MobclickAgent.onEvent(context, eventID, keyValues);
	}
}
