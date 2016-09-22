package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyDistributionAdapter;
import com.fanwe.app.AppHelper;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.SDListViewInScroll;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.miguo.live.views.customviews.MGToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.DistributionGoodsModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_fx_my_fxActModel;
import com.fanwe.model.User_center_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.fanwe.utils.SDInterfaceUtil;
import com.fanwe.work.AppRuntimeWorker;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.socialize.utils.Log;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
/*
 * 商品管理
 * @Autor cxk
 */
public class DistributionCommodityManagementActivity extends BaseActivity {
	@ViewInject(R.id.act_manage_refresh)
	private PullToRefreshScrollView mTo_refresh;
	@ViewInject(R.id.lv_goods_edit)
	private SDListViewInScroll listView_distribution;
	
	@ViewInject(R.id.ll_empty)
	private LinearLayout mLl_empty;
	
	@ViewInject(R.id.tv_empty)
	private TextView mTv_empty;
	
	private PageModel mPage = new PageModel();
	private HttpHandler<String> mHttpHandler;
	private List<DistributionGoodsModel> mListModel = new ArrayList<DistributionGoodsModel>();
	private Uc_fx_my_fxActModel mActModel;
	private MyDistributionAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_manage);
		init();
	}

	private void init() {
		initTitle();
		initDefaultData();
		initPullToRefreshScrollView();
	}

	private void initDefaultData() {
		mAdapter = new MyDistributionAdapter(mListModel, this);
		listView_distribution.setAdapter(mAdapter);
		mTv_empty.setText("您还没有分销商品哦！！");
		listView_distribution.setEmptyView(mLl_empty);
	}

	private void initTitle() {
		mTitle.setMiddleTextTop("商品管理");
		mTitle.initRightItem(1);
	}
	private void initPullToRefreshScrollView()
	{
		mTo_refresh.setMode(Mode.PULL_FROM_START);
		mTo_refresh.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				pullRefresh();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				pullLoadMore();
			}
		});
		mTo_refresh.setRefreshing();
	}
	protected void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_fx");
		model.putAct("my_fx");
		model.putPage(mPage.getPage());

		InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>()
		{
			@Override
			public void onStart()
			{
				
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				Uc_fx_my_fxActModel actModel = JsonUtil.json2Object(responseInfo.result, Uc_fx_my_fxActModel.class);
				if (!SDInterfaceUtil.isActModelNull(actModel))
				{
					if (actModel.getStatus() == 1)
					{
						mActModel = actModel;
						mPage.update(actModel.getPage());
						SDViewUtil.updateAdapterByList(mListModel, actModel.getItem(), mAdapter, isLoadMore);
					}
				}
			}
			@Override
			public void onFinish()
			{
				mTo_refresh.onRefreshComplete();
			}
		});
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
			MGToast.showToast("没有更多数据了");
			mTo_refresh.onRefreshComplete();
		}
	}
}
