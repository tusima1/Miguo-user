package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyMessageAdapter;
import com.fanwe.adapter.MyMessageAdapter.OnMessageReadListener;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.jpush.MessageHelper;
import com.fanwe.library.dialog.SDDialogManager;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Message;
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

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

public class MessageActivity extends BaseActivity {
	private PullToRefreshScrollView mPtr;
	private SDListViewInScroll mListview;
	private PageModel mPage = new PageModel();
	private List<Message> mListModel = new ArrayList<Message>();
	private MyMessageAdapter mAdapter;
	private View ll_empty;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setmTitleType(TitleType.TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_message_activity);
		init();
	}

	private void init() {
		initTitle();
		initView();
	}

	private void initView() {
		mPtr = (PullToRefreshScrollView) findViewById(R.id.ptr);
		mListview = (SDListViewInScroll) findViewById(R.id.lv_message);
		initPullToRefreshListView();
		mAdapter = new MyMessageAdapter(mListModel, MessageActivity.this);
		mListview.setAdapter(mAdapter);

		mAdapter.setOnMessageReadListener(new OnMessageReadListener() {

			@Override
			public void markHasRead(int messageType, Message hasReadMsg) {
				if (hasReadMsg != null) {

					int index = mListModel.indexOf(hasReadMsg);
					mListModel.get(index).setStatus("2");
					mAdapter.notifyDataSetChanged();
				}

			}
		});

		ll_empty = findViewById(R.id.ll_empty);
		((TextView) ll_empty.findViewById(R.id.empty_tv_text)).setText("暂无消息");
	}

	private void initPullToRefreshListView() {
		mPtr.setMode(Mode.BOTH);
		mPtr.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullRefresh();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				pullLoadMore();
			}
		});
		mPtr.setRefreshing();
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
			mPtr.onRefreshComplete();
		}
	}

	protected void requestData(final boolean isLoadMore) {
		// "type":0, //0全部消息列表, 5活动消息列表
		RequestModel model = new RequestModel();
		model.putCtl("uc_message");
		model.putAct("index");
		model.put("type", 5);
		model.putPage(mPage.getPage());
		SDRequestCallBack<Uc_message_indexActModel> handler = new SDRequestCallBack<Uc_message_indexActModel>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					List<Message> list = actModel.getList();
					if (mPage.getPage() == 1 && list == null) {
						ll_empty.setVisibility(View.VISIBLE);
					} else {
						if (ll_empty.getVisibility() == View.VISIBLE) {
							ll_empty.setVisibility(View.GONE);
						}
					}
					if (list != null) {
						mPage.update(actModel.getPage());
						SDViewUtil.updateAdapterByList(mListModel, list, mAdapter, isLoadMore);
					}

				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {

			}

			@Override
			public void onFinish() {
				SDDialogManager.dismissProgressDialog();
				mPtr.onRefreshComplete();
			}
		};

		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("活动消息");
		mTitle.initRightItem(1);
	}


	@Override
	protected void onRestart() {
		super.onRestart();
		MessageHelper.updateMessageCount();
	}

}
