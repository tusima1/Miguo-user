package com.fanwe.jpush;

import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.utils.SDToast;
import com.fanwe.model.MessageCount;
import com.fanwe.model.RequestModel;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;

public class MessageHelper {

	public static int msg_All = 0;
	public static int msg_Activity = 0;

	public static void updateMessageCount() {
		RequestModel model = new RequestModel();
		model.putCtl("uc_message");
		model.putAct("msg_num");
		SDRequestCallBack<MessageCount> handler = new SDRequestCallBack<MessageCount>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel != null) {
					msg_Activity = actModel.getMsg_activity();
					int msg_normal = actModel.getMsg_normal();
					msg_All = msg_Activity + msg_normal;
				} else {
					msg_Activity = 0;
					msg_All = 0;
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				SDToast.showToast("获取消息数量失败!");
			}

			@Override
			public void onFinish() {
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	public static void markAsRead(int msgId) {
		// "ctl":"uc_message","act":"set_reader","id":123
		RequestModel model = new RequestModel();
		model.putCtl("uc_message");
		model.putAct("set_reader");
		model.put("id", msgId);
		SDRequestCallBack<MessageCount> handler = new SDRequestCallBack<MessageCount>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
			}

			@Override
			public void onFailure(HttpException error, String msg) {
			}

			@Override
			public void onFinish() {
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	

}
