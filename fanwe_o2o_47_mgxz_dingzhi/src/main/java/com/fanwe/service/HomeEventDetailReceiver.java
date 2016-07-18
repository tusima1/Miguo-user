package com.fanwe.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.fanwe.DaiYanStoreWapActivity;
import com.fanwe.MainActivity;
import com.fanwe.MyMessageActivity;
import com.fanwe.StoreDetailActivity;
import com.fanwe.TuanDetailActivity;
import com.fanwe.config.AppConfig;
import com.fanwe.constant.JPushType;
import com.fanwe.constant.ServerUrl;
import com.fanwe.jpush.MessageHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class HomeEventDetailReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle bundle = intent.getExtras();

		String action = intent.getAction();
		if ("cn.jpush.android.intent.REGISTRATION".equalsIgnoreCase(action)
				|| "cn.jpush.android.intent.CONNECTION".equalsIgnoreCase(action)) {
			// Do Nothing
		} else {
			JPushType.hasMsg = true;
			MessageHelper.updateMessageCount();
		}
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
		

		if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {

			String notifitionId = bundle.getString(JPushInterface.ACTION_NOTIFICATION_RECEIVED);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {

			openNotification(context, bundle);

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.e("@@@", "是消息类型,而不是通知类型");
			bundle.getString(JPushInterface.EXTRA_NOTI_TYPE);
			processCustomMessage(context, bundle);
		}
	}

	private void openNotification(Context context, Bundle bundle) {
//		{"notice_type":"message","messsage_id":819,"object_id":6031,"messsage_type":3,"message_act":""}
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		String act = "";
		int object_id=-1;
		String notice_type = "";
		int messsage_id=-1;
		int messsage_type=-1;
		JSONObject obj;
		try {
			obj = new JSONObject(extras);
			object_id=obj.getInt("object_id");
			messsage_id=obj.getInt("messsage_id");
			messsage_type=obj.getInt("messsage_type");
			act=obj.getString("message_act");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 根据notifitionid,判断是那个通知
		// String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
		if (JPushType.TUAN_DEAL.equals(act)) {
			if (object_id == -1) {
				Log.e("Debug", "object_id没有值!");
				return;
			}
			bundle.putInt("extra_goods_id", object_id);
			startActivity(context, TuanDetailActivity.class, bundle);
		} else if (JPushType.SHOP.equals(act)) {
			// 门店详情
			if (object_id == -1) {
				Log.e("Debug", "object_id没有值!");
				return;
			}
			bundle.putInt("extra_merchant_id", object_id);
			startActivity(context, StoreDetailActivity.class, bundle);
		} else if (JPushType.LIMIT_SALE.equals(act)) {
			// 限时特卖

		} else if (JPushType.HTML5.equals(act)) {
			// h5页面
			openH5(object_id,context);
		} else {
			// 默认,去首页
			bundle.putInt("index", 3);
			startActivity(context, MainActivity.class, bundle);
		}
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
		String url;
		if (ServerUrl.DEBUG) {
			url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_TEST_URL + "/wap/index.php?ctl=activity&id="
					+ id + "&sess_id=" + session;
		}else{
			url = ServerUrl.SERVER_API_URL_PRE + ServerUrl.SERVER_API_URL_MID + "/wap/index.php?ctl=activity&id="
					+ id + "&sess_id=" + session;
		}
		bundle.putString("url", url);
		bundle.putInt("user_id", 1);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}

}
