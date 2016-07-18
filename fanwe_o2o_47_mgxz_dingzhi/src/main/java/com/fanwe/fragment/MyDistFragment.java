package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyXiaoMiAdapter;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.Member;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_Xiaomi_ActModel;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MyDistFragment extends BaseFragment implements
		View.OnClickListener
{
	@ViewInject(R.id.frag_my_xiaomi)
	private PullToRefreshScrollView mPtr_ScrollView;

	@ViewInject(R.id.listView_xiaomi)
	private SDListViewInScroll mListView;

	@ViewInject(R.id.ll_empty)
	private LinearLayout mLl_empty;

	@ViewInject(R.id.ll_vip1)
	private LinearLayout mLl_vip1;

	@ViewInject(R.id.tv_textVip1)
	private TextView mTv_textVip1;

	@ViewInject(R.id.tv_vip1Number)
	private TextView mTv_vip1Number;

	@ViewInject(R.id.ll_vip2)
	private LinearLayout mLl_vip2;

	@ViewInject(R.id.tv_textVip2)
	private TextView mTv_textVip2;

	@ViewInject(R.id.tv_vip2Number)
	private TextView mTv_vip2Number;

	@ViewInject(R.id.ll_vip3)
	private LinearLayout mLl_vip3;

	@ViewInject(R.id.tv_textVip3)
	private TextView mTv_textVip3;

	@ViewInject(R.id.tv_vip3Number)
	private TextView mTv_vip3Number;

	private MyXiaoMiAdapter mAdapter;
	private PageModel mPage = new PageModel();
	private List<Member> listModel = new ArrayList<Member>();
	protected OnDialogData mListener;

	private int mType;
	private int mRank = 0;

	public void setmListener(OnDialogData listener) {
		this.mListener = listener;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.flag_dist);
	}

	@Override
	protected void init() {
		super.init();
		getIntentData();
		bindData();
		initClick();
		initPullToScrollView();
	}

	private void initClick() {
		mLl_vip1.setOnClickListener(this);
		mLl_vip2.setOnClickListener(this);
		mLl_vip3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		mPage.resetPage();
		switch (v.getId()) {
		case R.id.ll_vip1:
			clickVip1();
			break;
		case R.id.ll_vip2:
			clickVip2();
			break;
		case R.id.ll_vip3:
			clickVip3();
			break;
		default:
			break;
		}
	}
	private void clickVip3()
	{
		mRank = 3;
		mLl_vip1.setEnabled(true);
		mLl_vip2.setEnabled(true);
		mLl_vip3.setEnabled(false);
		mTv_textVip1
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		mTv_textVip2
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		mTv_textVip3.setTextColor(getResources().getColor(R.color.main_color));
		requestData(false);
	}

	private void clickVip2() {
		mRank = 2;
		mLl_vip1.setEnabled(true);
		mLl_vip2.setEnabled(false);
		mLl_vip3.setEnabled(true);
		mTv_textVip1
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		mTv_textVip3
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		mTv_textVip2.setTextColor(getResources().getColor(R.color.main_color));
		requestData(false);
	}

	private void clickVip1() {
		mRank = 1;
		mLl_vip1.setEnabled(false);
		mLl_vip2.setEnabled(true);
		mLl_vip3.setEnabled(true);
		mTv_textVip1.setTextColor(getResources().getColor(R.color.main_color));
		mTv_textVip2
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		mTv_textVip3
				.setTextColor(getResources().getColor(R.color.text_fenxiao));
		requestData(false);
	}

	private void getIntentData() {
		mType = getArguments().getInt("type");
	}

	private void initPullToScrollView() {
		mPtr_ScrollView.setMode(Mode.BOTH);
		mPtr_ScrollView
				.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						mPage.resetPage();
						mRank = 0;
						mLl_vip1.setEnabled(true);
						mLl_vip2.setEnabled(true);
						mLl_vip3.setEnabled(true);
						mTv_textVip1.setTextColor(getResources().getColor(
								R.color.text_fenxiao));
						mTv_textVip2.setTextColor(getResources().getColor(
								R.color.text_fenxiao));
						mTv_textVip3.setTextColor(getResources().getColor(
								R.color.text_fenxiao));
						pullRefresh();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						pullLoadMore();
					}
				});
		mPtr_ScrollView.setRefreshing();
	}

	private void pullRefresh() {
		mPage.resetPage();
		requestData(false);
	}

	private void requestData(final boolean isLoadMore) {
		RequestModel model = new RequestModel();
		model.putCtl("uc_fxinvite");
		model.putAct("sub_members");
		model.put("type", mType);
		model.put("rank", mRank);
		model.putPage(mPage.getPage());
		SDRequestCallBack<Uc_Xiaomi_ActModel> handler = new SDRequestCallBack<Uc_Xiaomi_ActModel>() {
			@Override
			public void onStart() {
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (actModel.getStatus() == 1) {
					mPage.update(actModel.getPage());
					bindVip(actModel);
//					SDViewUtil.updateAdapterByList(listModel,
//							actModel.getList(), mAdapter, isLoadMore);
					List<Member> newData = actModel.getList();
					if (isLoadMore) {
						if (newData==null || newData.isEmpty()) {
							SDToast.showToast("没有更多数据!");
							return;
						}
						listModel.addAll(newData);
						mAdapter.notifyDataSetChanged();
					}else {
						if (listModel!=null) {
							listModel.clear();
							if (newData==null || newData.isEmpty()) {
								SDToast.showToast("没有更多数据!");
								mAdapter.notifyDataSetChanged();
								return;
							}
							listModel.addAll(newData);
							mAdapter.notifyDataSetChanged();
						}
					}
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				mPtr_ScrollView.onRefreshComplete();
			}

			@Override
			public void onFinish() {
				mPtr_ScrollView.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}

	protected void bindVip(Uc_Xiaomi_ActModel actModel) {
		if (actModel == null) {
			return;
		}
		if (mListener != null) {
			mListener.setData(
					actModel.getLevel1() + actModel.getLevel2_1(),
					actModel.getLevel1(),
					actModel.getLevel2_1(),
					actModel.getLevel1() + actModel.getLevel2()
							+ actModel.getLevel3(),actModel.getUp_name(),
					actModel.getUp_id());
		}
		SDViewBinder.setTextView(mTv_vip1Number, "（" + actModel.getLevel1()
				+ "）");
		SDViewBinder.setTextView(mTv_vip2Number, "（" + actModel.getLevel2()
				+ "）");
		SDViewBinder.setTextView(mTv_vip3Number, "（" + actModel.getLevel3()
				+ "）");
	}

	private void pullLoadMore() {
		if (mPage.increment()) {
			requestData(true);
		} else {
			SDToast.showToast("没有更多数据了");
			mPtr_ScrollView.onRefreshComplete();
		}
	}

	private void bindData() {
		mAdapter = new MyXiaoMiAdapter(listModel, getActivity(), mType);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mLl_empty);
	}

	public interface OnDialogData {
		public void setData(int vip1, int num1, int num2, int total,String up_name,int up_id);
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
