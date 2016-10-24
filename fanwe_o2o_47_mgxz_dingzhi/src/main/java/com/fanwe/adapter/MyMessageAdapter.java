package com.fanwe.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.DaiYanStoreWapActivity;
import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.MainActivity;
import com.fanwe.MemberRankActivity;
import com.fanwe.MyRedEnvelopeActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.app.AppConfig;
import com.fanwe.constant.JPushType;
import com.fanwe.constant.ServerUrl;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.library.adapter.SDBaseAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.ViewHolder;
import com.fanwe.model.Message;
import com.fanwe.model.MessageCount;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.seller.views.GoodsDetailActivity;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.miguo.app.HiShopDetailActivity;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MyMessageAdapter extends SDBaseAdapter<Message> {

	private OnMessageReadListener listener;

	public MyMessageAdapter(List<Message> listModel, Activity activity) {
		super(listModel, activity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.act_mymessage, null);
		}
		final ImageView iv_iamge = ViewHolder.get(convertView, R.id.iv_iamge);
		TextView tv_title = ViewHolder.get(convertView, R.id.tv_title);
		TextView tv_message = ViewHolder.get(convertView, R.id.tv_message);
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		ImageView iv_red = ViewHolder.get(R.id.iv_red, convertView);
		final Message model = getItem(position);
		if (model != null) {

			SDViewBinder.setTextView(tv_message, model.getContent());
			SDViewBinder.setTextView(tv_time, model.getCreate_time());

			if ("1".equals(model.getType())) {

				iv_iamge.setBackgroundResource(R.drawable.my_hongbao_photo);
				SDViewBinder.setTextView(tv_title, "您收到一个新的红包");

			} else if ("2".equals(model.getType())) {

				iv_iamge.setBackgroundResource(R.drawable.my_friend_photo);
				SDViewBinder.setTextView(tv_title, "您有一个新的成员加入");

			} else if ("3".equals(model.getType())) {

				iv_iamge.setBackgroundResource(R.drawable.my_yongjin_photo);
				SDViewBinder.setTextView(tv_title, "您有一笔新的佣金收入");

			} else if ("4".equals(model.getType())) {

				iv_iamge.setBackgroundResource(R.drawable.my_remind_upgrage);
				SDViewBinder.setTextView(tv_title, "您有一条新的升级提醒");

			} else if ("5".equals(model.getType())) {

				iv_iamge.setBackgroundResource(R.drawable.my_activities_photo);
				// SDViewBinder.setTextView(tv_title, "活动消息");
				SDViewBinder.setTextView(tv_title, model.getTitle());
			}

			if ("1".equals(model.getStatus())) {

				iv_red.setVisibility(View.VISIBLE);
			} else if ("2".equals(model.getStatus())) {

				iv_red.setVisibility(View.GONE);
			}
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if ("5".equals(model.getType())) {
						
						if ("1".equals(model.getStatus())) {
							if (listener != null) {
								MessageHelper.markAsRead(Integer.valueOf(model.getId()));
								listener.markHasRead(5, model);
							}
							startActivityWithUpdateMessage(model.getAct(), Integer.valueOf(model.getId()));
						} else {
							StartActivityByAct(model.getAct(), Integer.valueOf(model.getRel_id()));
						}
						return;

					} else if ("2".equals(model.getType())) {

						mActivity.startActivity(new Intent(mActivity, DistributionMyXiaoMiActivity.class));

					} else if ("3".equals(model.getType())) {

						mActivity.startActivity(new Intent(mActivity, WithdrawLogActivity.class));

					} else if ("4".equals(model.getType())) {

						mActivity.startActivity(new Intent(mActivity, MemberRankActivity.class));

					} else if ("1".equals(model.getType())) {
						mActivity.startActivity(new Intent(mActivity, MyRedEnvelopeActivity.class));
					}
					if ("1".equals(model.getStatus())) {
						if (listener != null) {
							listener.markHasRead(Integer.valueOf(model.getType()), model);
						}
					}
					removeNotificationFromStatuBar(model.getFollow_id());
				}
			});
		}
		return convertView;
	}

	protected void removeNotificationFromStatuBar(String notificationID) {
		if ("0".equals(notificationID)) {
			return;
		}
		int msgID = 0;
		try {
			msgID = Long.valueOf(notificationID).intValue();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (msgID != 0) {
			JPushInterface.clearNotificationById(mActivity, msgID);
		}
	}

	protected void startActivityWithUpdateMessage(final String act, final int object_id) {
		RequestModel model = new RequestModel();
		model.putCtl("uc_message");
		model.putAct("msg_num");
		SDRequestCallBack<MessageCount> handler = new SDRequestCallBack<MessageCount>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel != null) {
					MessageHelper.msg_Activity = actModel.getMsg_activity();
					int msg_normal = actModel.getMsg_normal();
					MessageHelper.msg_All = MessageHelper.msg_Activity + msg_normal;
				} else {
					MessageHelper.msg_Activity = 0;
					MessageHelper.msg_All = 0;
				}
				// 保证消息在首页是有变化的
				StartActivityByAct(act, object_id);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
//				MGToast.showToast("获取消息数量失败!");
			}

			@Override
			public void onFinish() {
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);

	}

	private void StartActivityByAct(String act, int object_id) {
		final Bundle bundle = new Bundle();
		if (JPushType.TUAN_DEAL.equals(act)) {

			bundle.putInt("extra_goods_id", object_id);
			startActivity(mActivity, GoodsDetailActivity.class, bundle);

		} else if (JPushType.SHOP.equals(act)) {
			// 门店详情
			bundle.putInt("extra_merchant_id", object_id);
			startActivity(mActivity, HiShopDetailActivity.class, bundle);

		} else if (JPushType.LIMIT_SALE.equals(act)) {
			// 限时特卖

		} else if (JPushType.HTML5.equals(act)) {
			// h5页面
			openH5(object_id);
		} else {
			// 默认,去首页
			bundle.putInt("index", 3);
			startActivity(mActivity, MainActivity.class, bundle);
		}
	}

	private void openH5(int id) {
		Intent intent = new Intent(mActivity, DaiYanStoreWapActivity.class);
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
		mActivity.startActivity(intent);
	}

	private void startActivity(Context context, Class<?> cls, Bundle bundle) {
		Intent mIntent = new Intent(context, cls);
		mIntent.putExtras(bundle);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(mIntent);
	}

	@Override
	public int getCount() {

		return super.getCount();
	}

	public interface OnMessageReadListener {
		void markHasRead(int messageType, Message hasReadMsg);
	}

	public void setOnMessageReadListener(OnMessageReadListener listener) {
		this.listener = listener;
	}
}
