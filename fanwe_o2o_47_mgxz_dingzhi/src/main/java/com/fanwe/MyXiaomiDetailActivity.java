package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.common.SDActivityManager;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Discover_indexActModel;
import com.fanwe.model.Member;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_Xiaomi_ActModel;
import com.fanwe.model.Uc_Xiaomi_DetailActModel;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fanwe.adapter.MyXiaoMiDetailAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.o2o.miguo.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 二级下线
 * 
 * @author cxk
 *
 */
public class MyXiaomiDetailActivity extends BaseActivity
{

	@ViewInject(R.id.frag_my_detail)
	private PullToRefreshScrollView mPtrsvAll;

	@ViewInject(R.id.listView_detail)
	private SDListViewInScroll mListView;

	@ViewInject(R.id.ll_empty)
	private LinearLayout mLl_empty;
	
	@ViewInject(R.id.tv_userName)
	private TextView mTv_user;
	 
	@ViewInject(R.id.tv_userNumber)
 	private TextView mTv_number;

	private List<Member> listMember = new ArrayList<Member>();
	private int user_id;
	private String user_name;
	private PageModel mPage = new PageModel();	

	private MyXiaoMiDetailAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_xiaomi_detail);
		init();
	}
	
	private void init()
	{
		initIntent();
		initTitle();
		initDefaultData();
		initPullToScrollView();
	}

	private void initDefaultData()
	{
		mAdapter = new MyXiaoMiDetailAdapter(listMember,this);
		mListView.setAdapter(mAdapter);
		mListView.setEmptyView(mLl_empty);
	}

	private void initIntent() 
	{
		  Bundle bundle =getIntent().getExtras();
		  user_id =  bundle.getInt("id",-1);
		  if(user_id  < 0)
		  {
			  finish();
		  }
		  user_name = bundle.getString("user");
		  mTv_user.setText(user_name+"（");
		  int user_num = bundle.getInt("number"); 
		  mTv_number.setText(String.valueOf(user_num));
	}

	private void initTitle() 
	{
		mTitle.setMiddleTextTop("我的小米");
		mTitle.initRightItem(1);
		mTitle.getItemRight(0).setTextTop("关闭");
	}

	private void initPullToScrollView() 
	{
		mPtrsvAll.setMode(Mode.BOTH);
		mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ScrollView> refreshView)
			{
				mPage.resetPage();
				pullRefresh();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ScrollView> refreshView) {
				pullLoadMore();
			}
		});
		mPtrsvAll.setRefreshing();
	}

	/**
	 * 请求接口
	 */
	private void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_fxinvite");
		model.putAct("sub_members");
		model.put("user_id",user_id);
		model.putPage(mPage.getPage());
		InterfaceServer.getInstance().requestInterface(model,
				new SDRequestCallBack<Uc_Xiaomi_DetailActModel>() {
					@Override
					public void onStart() 
					{
						
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (actModel.getStatus() == 1) 
						{
							mPage.update(actModel.getPage());
							SDViewUtil.updateAdapterByList(listMember,
									actModel.getList(),mAdapter, isLoadMore);
						}
					}
					@Override
					public void onFinish() 
					{
						mPtrsvAll.onRefreshComplete();
					}
				});
	}

	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		super.onCLickRight_SDTitleSimple(v, index);
		SDActivityManager.getInstance().finishActivity(
				DistributionMyXiaoMiActivity.class);
		finish();
	}
	private void pullRefresh()
	{
		mPage.resetPage();
		requestData(false);
	}
	private void pullLoadMore()
	{
		if (mPage.increment())
		{
			requestData(true);
		} else
		{
			SDToast.showToast("没有更多数据了");
			mPtrsvAll.onRefreshComplete();
		}
	}
}
