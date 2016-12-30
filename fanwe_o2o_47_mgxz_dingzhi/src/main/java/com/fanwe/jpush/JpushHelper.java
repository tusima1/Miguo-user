package com.fanwe.jpush;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.base.CommonHelper;
import com.fanwe.constant.ServerUrl;
import com.fanwe.library.utils.MD5Util;
import com.fanwe.network.MgCallback;
import com.fanwe.work.AppRuntimeWorker;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushHelper {

	public static void registerAll() {
		initJPushConfig();
		initJPushJavaRegister();
	}

	private static void initJPushConfig() {
		Set<String> tagSet = getTag();
		String alias =getAlias();
		if (TextUtils.isEmpty(alias)|| tagSet == null) {
			return;
		}
		/**
		 * 参数一:alias token 参数二:tag 城市id
		 */
		JPushInterface.setAliasAndTags(App.getApplication().getApplicationContext(), alias, tagSet,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						/*Code	描述	详细解释
						6001	无效的设置，tag/alias 不应参数都为 null
						6002	设置超时	建议重试
						6003	alias 字符串不合法	有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥
						6004	alias超长。最多 40个字节	中文 UTF-8 是 3 个字节
						6005	某一个 tag 字符串不合法	有效的别名、标签组成：字母（区分大小写）、数字、下划线、汉字、特殊字符(v2.1.6支持)@!#$&*+=.|￥
						6006	某一个 tag 超长。一个 tag 最多 40个字节	中文 UTF-8 是 3 个字节
						6007	tags 数量超出限制。最多 1000个	这是一台设备的限制。一个应用全局的标签数量无限制。
						6008	tag 超出总长度限制	总长度最多 7K 字节
						6009	未知错误	由于权限问题，导致的PushService启动异常。
						6011	10s内设置tag或alias大于10次	短时间内操作过于频繁*/
						Log.e("Jpush", "Jpush服务器,0是成功[method:initJPushConfig()]: status:" + arg0 + " alias:" + arg1 + " tags:" + arg2);
					}
				});
	}

	/**
	 * 设置tag
	 *
	 * @param originalCityId
	 *            例如:"228";
	 * @return false表示参数错误 true 表示已经发起网络请求了
	 */
	public static boolean setTag(String originalCityId) {
		if (TextUtils.isEmpty(originalCityId)) {
			return false;
		}
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.add(doCityId(originalCityId));
		JPushInterface.setTags(App.getApplication(), tagSet, new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				Log.e("Jpush", "Jpush服务器,0是成功[method:setTag()]: status:" + arg0 + " alias:" + arg1 + " tags:" + arg2);
			}
		});
		return true;
	}

	/**
	 * 正式环境 "city_" + MD5("product" + 城市id)
	 * 测试环境 "city_" + MD5("test" + 城市id)
	 * 其他环境 "city_" + MD5("dev" + 城市id)
	 * @return String
	 */
	private static String doCityId(String originalCity){
		String platform;
		if (ServerUrl.DEBUG){
			platform = ServerUrl.TEST ? "test" : "dev";
		}else {
			platform="product";
		}
		return "city_"+MD5Util.MD5(platform + originalCity);
	}
	/**
	 * 往服务器注册
	 */
	private static void initJPushJavaRegister() {
		String user_id =getAlias();
		if (TextUtils.isEmpty(user_id)) {
			return;
		}
		CommonHelper commonHelper = new CommonHelper(null,null);
		commonHelper.doRegisterJPushAlias(user_id, new MgCallback(){

			@Override
			public void onSuccessResponse(String responseBody) {
				Log.e("Jpush", "JpushMG "+responseBody);
			}

			@Override
			public void onErrorResponse(String message, String errorCode) {
				Log.e("Jpush", "JpushMG 后台 注册失败!"+"\n"+message+errorCode);
			}
		});

	}

	public static void unRegisterAll(){
		JPushInterface.setAlias(App.getApplication(), "", new TagAliasCallback() {
			@Override
			public void gotResult(int i, String s, Set<String> set) {
				if (i==0){
					Log.e("JPush","极光用户退出成功!");
				}else {
					Log.e("JPush","极光用户退出失败!");
				}
			}
		});
		CommonHelper commonHelper = new CommonHelper(null,null);
		commonHelper.doRegisterJPushAlias("", new MgCallback(){

			@Override
			public void onSuccessResponse(String responseBody) {
			}

			@Override
			public void onErrorResponse(String message, String errorCode) {
				Log.e("Jpush", "JpushMG 后台注销失败!"+"\n"+message+errorCode);
			}
		});

	}

	private static Set<String> getTag() {

		Set<String> tagSet = new LinkedHashSet<String>();
		String city_id = AppRuntimeWorker.getCity_id();
		tagSet.add(doCityId(city_id));
		return tagSet;
	}

	private static String getAlias(){
		String alias="";
		String imei="";
		try {
			alias = App.getInstance().getCurrentUser().getUser_id();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			imei = App.getInstance().getImei();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MD5Util.MD5(alias + imei);
	}


}
