package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyMessageAdapter;
import com.fanwe.adapter.MyMessageAdapter.OnMessageReadListener;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.BadgeView;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.library.dialog.SDDialogManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Message;
import com.fanwe.model.Message_Activty;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_message_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/*
 * 消息
 */
public class MyMessageActivity extends BaseActivity {

	public static String KEY_MESSAGE = "message";

	public static String NOTICE_TYPE = "extras";

	public static Intent MESSAGE_RECEIVED_ACTION;

	public static boolean isForeground = true;

	@ViewInject(R.id.frag_my_message)
	private PullToRefreshScrollView mTo_message;

	@ViewInject(R.id.lv_my_message)
	private SDListViewInScroll lv_message;

	private PageModel mPage = new PageModel();

	private List<Message> mListModel = new ArrayList<Message>();

	private MyMessageAdapter mAdapter;

	private ImageView iv_icon1;
	private BadgeView redDotBadge1;
	private TextView redDotCount1;
	private ImageView red_small_big1;
	private ImageView red_more1;
	private TextView tv_title1;
	private TextView tv_message1;
	private TextView tv_time1;
	private View re_dot;

	@ViewInject(R.id.ll_top11)
	private LinearLayout ll_Top1;

	// private ImageView iv_icon2;
	// private BadgeView redDotBadge2;
	// private TextView redDotCount2;
	// private ImageView red_small_big2;
	// private ImageView red_more2;
	// private TextView tv_title2;
	// private TextView tv_message2;
	// private TextView tv_time2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setmTitleType(TitleType.TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_my_message);
		init();
	}

	private void init() {
		initTitle();
		initTopItem();
		initPullToRefreshListView();
		initView();
		bindDefaultData();
	}

	private void initView() {
		item1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MyMessageActivity.this, MessageActivity.class));
			}
		});
	}

	private void initTopItem() {
		item1 = findViewById(R.id.item_1);
		re_dot = item1.findViewById(R.id.re_dot);
		iv_icon1 = (ImageView) item1.findViewById(R.id.iv_iamge);
		redDotBadge1 = (BadgeView) item1.findViewById(R.id.badge_red_dot);
		redDotCount1 = (TextView) item1.findViewById(R.id.tv_red_count);
		red_small_big1 = (ImageView) item1.findViewById(R.id.iv_red_big_small);
		red_more1 = (ImageView) item1.findViewById(R.id.iv_red_more);
		tv_title1 = (TextView) item1.findViewById(R.id.tv_title);
		tv_message1 = (TextView) item1.findViewById(R.id.tv_message);
		tv_time1 = (TextView) item1.findViewById(R.id.tv_time);

	}


	private void initTitle() {
		mTitle.setMiddleTextTop("消息");
		mTitle.initRightItem(1);

	}

	private void initPullToRefreshListView() {
		mTo_message.setMode(Mode.BOTH);
		mTo_message.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullRefresh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullLoadMore();
			}
		});
		mTo_message.setRefreshing();
	}

	private void pullRefresh() {
		mPage.resetPage();
		requestData(false);
	}

	private void pullLoadMore() {
		if (mPage.increment()) {
			requestData(true);
		} else {
			MGToast.showToast("没有更多数据了");
			mTo_message.onRefreshComplete();
		}
	}

	protected void requestData(final boolean isLoadMore) {
		// "type":0, //0全部消息列表, 5活动消息列表
		RequestModel model = new RequestModel();
		model.putCtl("uc_message");
		model.putAct("index");
		model.put("type", 0);
		model.putPage(mPage.getPage());
		SDRequestCallBack<Uc_message_indexActModel> handler = new SDRequestCallBack<Uc_message_indexActModel>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				MessageHelper.updateMessageCount();
				if (actModel.getStatus() == 1) {
					if (actModel.getList() != null) {

						mPage.update(actModel.getPage());
						SDViewUtil.updateAdapterByList(mListModel, actModel.getList(), mAdapter, isLoadMore);

					}

					if (actModel.getActivity() != null) {
						bindActivityData(actModel.getActivity());
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mTo_message.onRefreshComplete();
			}
		};

		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	// private ImageView iv_icon1;
	// private BadgeView redDotBadge1;
	// private TextView redDotCount1;
	// private ImageView red_small_big1;
	// private ImageView red_more1;
	// private TextView tv_title1;
	// private TextView tv_message1;
	// private TextView tv_time1;

	// 'id': '2',
	// 'type': '5',
	// 'rel_id': '855', //关联数据ID
	// 'act': 'deal', //动作
	// 'content': '消息测试',
	// 'status': '1', //1未读 2已读
	// 'create_time': '2016-06-13 08:00',
	// 'num': 10 //未读数量
	protected void bindActivityData(Message_Activty activitiy) {
		iv_icon1.setBackgroundResource(R.drawable.my_activities_photo);
		if (activitiy == null) {
			MGToast.showToast("活动消息的数据不存在!服务器没有传递或者解析错误!");
		} else {

			int activity_msg_count = activitiy.getNum();
			if (activity_msg_count > 0) {
				redDotBadge1.setCount(activity_msg_count);
				redDotCount1.setText("" + activity_msg_count);
			} else {
				re_dot.setVisibility(View.GONE);
			}
			tv_message1.setText(activitiy.getContent());
			tv_time1.setText(activitiy.getCreate_time());
		}
		if (MessageHelper.msg_Activity > 0) {
			redDotBadge1.setCount(MessageHelper.msg_Activity);
			redDotCount1.setText("" + MessageHelper.msg_Activity);
		}
		tv_time1.setText("活动消息");

	}

	private void bindDefaultData() {
		mAdapter = new MyMessageAdapter(mListModel, this);
		lv_message.setAdapter(mAdapter);
		mAdapter.setOnMessageReadListener(new OnMessageReadListener() {

			@Override
			public void markHasRead(int messageType, Message hasReadMsg) {
				if (hasReadMsg != null) {
					int index = mListModel.indexOf(hasReadMsg);
					mListModel.get(index).setStatus("2");
					hasChange = true;
				}
			}
		});
	}

	private boolean hasChange = false;

	private View item1;

	@Override
	protected void onRestart() {
		super.onRestart();
		if (hasChange) {
			mAdapter.notifyDataSetChanged();
			hasChange = false;
		}

		if (MessageHelper.msg_Activity > 0) {
			redDotBadge1.setCount(MessageHelper.msg_Activity);
			redDotCount1.setText("" + MessageHelper.msg_Activity);
		} else {
			redDotBadge1.setCount(0);
			redDotCount1.setText("");
			re_dot.setVisibility(View.GONE);
		}
	}

}
