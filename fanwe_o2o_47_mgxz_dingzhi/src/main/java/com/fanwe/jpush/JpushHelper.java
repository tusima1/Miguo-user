package com.fanwe.jpush;

import android.text.TextUtils;
import android.util.Log;

import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.base.CommonHelper;
import com.fanwe.model.LocalUserModel;
import com.fanwe.network.MgCallback;
import com.fanwe.work.AppRuntimeWorker;

import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class JpushHelper {
	 public String alias ="";

	public static void registerAll() {
		initJPushConfig();
		initJpushRegister();
	}

	public static void initJPushConfig() {
		Set<String> tagSet = getTag();
		LocalUserModel userModel = AppHelper.getLocalUser();
		if(userModel==null){
			return ;
		}
		String alias =userModel.getUser_id()+"" ;
		if (TextUtils.isEmpty(alias)||"null".equals(alias) || tagSet == null) {
			Log.e("Jpush", "initJPushConfig: token为空");
			return;
		}
		/**
		 * 参数一:alias token 参数二:tag 城市id
		 */
		JPushInterface.setAliasAndTags(App.getApplication().getApplicationContext(), alias, tagSet,
				new TagAliasCallback() {

					@Override
					public void gotResult(int arg0, String arg1, Set<String> arg2) {
						Log.e("Jpush", "initJPushConfig注册Jpush服务商结果: status:" + arg0 + " alias:" + arg1 + " tags:" + arg2);
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
	 * 往服务器注册
	 */
	public static void initJpushRegister() {


		LocalUserModel userModel = AppHelper.getLocalUser();
		if(userModel==null){
			return ;
		}
		String alias =userModel.getUser_id()+"" ;
		if (TextUtils.isEmpty(alias)) {
			Log.e("Jpush", "initJpushRegister: token为空");
			return;
		}

		CommonHelper commonHelper = new CommonHelper(null,null);
		commonHelper.doRegisterJPushAlias(alias, new MgCallback(){

			@Override
			public void onSuccessResponse(String responseBody) {

			}

			@Override
			public void onErrorResponse(String message, String errorCode) {
				Log.e("Jpush", "Jpush 注册失败!");
			}
		});

	}


	private static Set<String> getTag() {
		Set<String> tagSet = new LinkedHashSet<String>();
		String city_id = AppRuntimeWorker.getCity_id();
		tagSet.add(city_id);

		return tagSet;
	}


}
