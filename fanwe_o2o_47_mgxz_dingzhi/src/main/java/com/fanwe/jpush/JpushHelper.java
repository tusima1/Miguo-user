package com.fanwe.jpush;

import java.security.MessageDigest;
import java.util.LinkedHashSet;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.http.InterfaceServer;
import com.fanwe.model.LocalUserModel;
import com.fanwe.model.RequestModel;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushHelper {

	public static void registerAll() {
		initJPushConfig();
		initJpushRegister();
	}

	public static void initJPushConfig() {
		Set<String> tagSet = getTag();
		String token = getToken();
		if (TextUtils.isEmpty(token) || tagSet == null) {
			Log.d("Jpush", "initJPushConfig: token为空");
			return;
		}
		/**
		 * 参数一:alias token 参数二:tag 城市id
		 */
		JPushInterface.setAliasAndTags(App.getApplication().getApplicationContext(), token, tagSet,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						Log.d("Jpush", "initJPushConfig注册Jpush服务商结果: status:" + arg0 + " alias:" + arg1 + " tags:" + arg2);
					}
				});
	}

	/**
	 * 设置tag
	 * 
	 * @param city_ID
	 *            例如:"city_228";
	 * @return false表示参数错误 true 表示已经发起网络请求了
	 */
	public static boolean setTag(String city_ID) {
		if ("".equals(city_ID) || !city_ID.contains("_")) {
			return false;
		}
		Set<String> tagSet = new LinkedHashSet<String>();
		tagSet.add(city_ID);
		JPushInterface.setTags(App.getApplication(), tagSet, new TagAliasCallback() {

			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
				// TODO
			}
		});
		return true;
	}

	/**
	 * userID与RegistrationID的md5值
	 * 
	 * 任何时候都不推荐你单独设置唯一识别码alias
	 * 
	 * @param token
	 *            设备的唯一标示
	 * @return
	 * 
	 */
	public static boolean setAlias_Token(String token) {
		String token2 = getToken();
		return false;
	}

	/**
	 * 往服务器注册
	 */
	public static void initJpushRegister() {

		String token = getToken();
		if (TextUtils.isEmpty(token)) {
			Log.d("Jpush", "initJpushRegister: token为空");
			return;
		}
		RequestModel model = new RequestModel();
		model.putCtl("user_apns");
		model.putUser();
		model.put("dev_type", "android");
		model.put("device_token", token);

		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				int status = -1;
				try {
					JSONObject obj;
					obj = new JSONObject(responseInfo.result);
					status = obj.getInt("user_login_status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (status == 1) {
					Log.d("Jpush", "Jpush 注册后台成功!");
				}
			}

		});
	}

	private static String getToken() {
		String device_token = JPushInterface.getRegistrationID(App.getApplication());
		LocalUserModel user = AppHelper.getLocalUser();
		if (user == null) {
			return "";
		}
		int user_id = user.getUser_id();
		String token = MD5(device_token + user_id);
		return token;
	}

	private static Set<String> getTag() {
		Set<String> tagSet = new LinkedHashSet<String>();
		String sArray[] = { "city_" + AppRuntimeWorker.getCityIdByCityName(AppRuntimeWorker.getCity_name()) };
		for (String sTagItme : sArray) {
			if (TextUtils.isEmpty(sTagItme)) {
				return null;
			}
			tagSet.add(sTagItme);
		}
		return tagSet;
	}

	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}
}
