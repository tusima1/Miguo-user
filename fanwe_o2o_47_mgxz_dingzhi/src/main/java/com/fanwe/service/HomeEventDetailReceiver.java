package com.fanwe.service;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.fanwe.DaiYanStoreWapActivity;
import com.fanwe.InitAdvsMultiActivity;
import com.fanwe.MyMessageActivity;
import com.fanwe.app.AppConfig;
import com.fanwe.constant.JPushType;
import com.fanwe.constant.ServerUrl;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.miguo.app.HiHomeActivity;
import com.miguo.app.HiShopDetailActivity;
import com.miguo.entity.JpushMessageBean;
import com.miguo.factory.MessageTypeFactory;
import com.miguo.ui.view.notify.NotifyMessage;
import com.miguo.utils.AppStateUtil;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.NotificationsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class HomeEventDetailReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		String action = intent.getAction();
		if ("cn.jpush.android.intent.REGISTRATION".equalsIgnoreCase(action) || "cn.jpush.android.intent.CONNECTION".equalsIgnoreCase(action)) {
			return;
		}

		JPushType.hasMsg = true;
		MessageHelper.updateMessageCount();
//		sendShortcut2Launcher(context, 1);
		handNotifications(bundle, intent, context);
	}

	private void processCustomMessage(Context context, Bundle bundle) {
		if (MyMessageActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_NOTI_TYPE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent("com.fanwe.service.HomeEventDetailReceiver");

			msgIntent.putExtra(MyMessageActivity.NOTICE_TYPE, message);
			if (!TextUtils.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MyMessageActivity.NOTICE_TYPE, extras);
					}
				} catch (JSONException e) {
				}
			}
			context.sendBroadcast(msgIntent);
		}
	}

	private void handNotifications(Bundle bundle, Intent intent, Context context) {
		// object_id: 数据ID
		// messsage_type: 消息类型 1红包 2下线 3佣金 4升级提醒 5促销活动
		// messsage_act: 消息动作, 一般是表明object_id是什么样的类型, 空的话表明它消息表中的ID.
		// bundle:{"notice_type":"message","object_id":324,"messsage_type":3}


		/**
		 * 收到了推送消息
		 */
		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			JpushMessageBean bean = getMessage(bundle.getString(JPushInterface.EXTRA_EXTRA));
			if(bean != null && !NotificationsUtils.isNotificationEnabled(context)){
				bean.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
				new NotifyMessage(context).show(bean);
			}
			return;
		}
		/**
		 * 推送被打开
		 */
		if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			openNotification(context, bundle);
			return;
		}
		/**
		 * 自定义消息收到了
		 */
		if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.e("@@@", "是消息类型,而不是通知类型");
			bundle.getString(JPushInterface.EXTRA_NOTI_TYPE);
			processCustomMessage(context, bundle);
			return;
		}
	}

	private JpushMessageBean getMessage(String extra){
		JpushMessageBean bean = new JpushMessageBean();
		try{
			JSONObject object = new JSONObject(extra);
			int messageType = 0;
			if(object.has("message_type")){
				messageType = toInt(object.getString("message_type"));
			}
			String push_jump_target = "";
			if(object.has("push_jump_target")){
				push_jump_target = object.getString("push_jump_target");
			}
			String push_jump_paramater = "";
			if(object.has("push_jump_paramater")){
				push_jump_paramater = object.getString("push_jump_paramater");
			}
			String system_message_id = "";
			if(object.has("system_message_id")){
				system_message_id = object.getString("system_message_id");
			}

			String title = "";
			if(object.has("title")){
				title = object.getString("title");
			}
			long create_time = 0;
			if(object.has("create_time")){
				create_time = toLong(object.getString("create_time"));
			}
			bean.setMessage_type(messageType);
			bean.setPush_jump_target(push_jump_target);
			bean.setPush_jump_paramater(push_jump_paramater);
			bean.setSystem_message_id(system_message_id);
			bean.setTitle(title);
			bean.setCreate_time(create_time);
			return bean;
		}catch (JSONException e){
			return bean;
		}
	}

	private int toInt(String var){
		try {
			return Integer.parseInt(var);
		}catch (Exception e){
			return 0;
		}
	}

	private long toLong(String var){
		try{
			return Long.parseLong(var);
		}catch (Exception e){
			return 0;
		}
	}
	/**
	 * 判断App是否在前台工作
	 * 如果在前台工作，并且没有推送权限，就要推送应用内弹窗
	 * @param context
	 * @return
     */
	private boolean isAppRunning(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
			}
		}
		return false;
	}

	private void openNotification(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		JpushMessageBean bean = getMessage(extras);
		bean.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
		if(bean == null){
			return;
		}

		if(!AppStateUtil.isAppRunning(context)){
			Intent intent = new Intent(context, InitAdvsMultiActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("type", bean.getPush_jump_target());
			intent.putExtra("value", bean.getPush_jump_paramater());
			intent.putExtra("system_message_id", bean.getSystem_message_id());
			BaseUtils.jumpToNewActivity(context, intent);
			return;
		}
		MessageTypeFactory.jump(context, bean);


	}

	/** 启动页面 **/
	private void startActivity(Context context, Class<?> cls, Bundle bundle) {
		Intent mIntent = new Intent(context, cls);
		mIntent.putExtras(bundle);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(mIntent);
	}

	private void openH5(int id,Context context) {
		Intent intent = new Intent(context, DaiYanStoreWapActivity.class);
		Bundle bundle = new Bundle();
		String session = AppConfig.getSessionId();
		String url = ServerUrl.getAppServerApiUrl()+ "/wap/index.php?ctl=activity&id="
				+ id + "&sess_id=" + session;
		bundle.putString("url", url);
		bundle.putInt("user_id", 1);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

}
