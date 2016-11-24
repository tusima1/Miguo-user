package com.fanwe.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.fanwe.MyMessageActivity;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Bundle bundle = intent.getExtras();
		if(JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction()))
		{
			//打开通知
			Intent in=new Intent(context, ClassNameFactory.getClass(ClassPath.HOME_ACTIVITY));
			bundle.putInt("index", 3);
			in.putExtras(bundle);
        	in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(in);
		}else if(JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction()))
		{
			bundle.getString(JPushInterface.EXTRA_MESSAGE);
			processCustomMessage(context, bundle);
		}
		
	}
	   private void processCustomMessage(Context context, Bundle bundle)
	   {
	        if (MyMessageActivity.isForeground) 
	        {
	            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
	            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
	            Log.d("Jpush", "message:" +message+ " extras :"+extras);
	            Intent msgIntent = new Intent("com.fanwe.service.MyReceiver");
	            msgIntent.putExtra(MyMessageActivity.KEY_MESSAGE, message);
	            if (! TextUtils.isEmpty(extras)) {
	                try {
	                    JSONObject extraJson = new JSONObject(extras);
	                    if (null != extraJson && extraJson.length() > 0) {
	                        msgIntent.putExtra(MyMessageActivity.NOTICE_TYPE, extras);
	                    }
	                } catch (JSONException e)
	                {

	                }
	            }
	            context.sendBroadcast(msgIntent);
	        }
	    }
}