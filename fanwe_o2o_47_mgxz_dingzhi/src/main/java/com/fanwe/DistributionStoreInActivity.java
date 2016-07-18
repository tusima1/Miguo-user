package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.StoreInAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.StoreIn_Fx;
import com.fanwe.model.StoreIn_list;
import com.fanwe.o2o.miguo.R;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

public class DistributionStoreInActivity extends BaseActivity
{
	@ViewInject(R.id.iv_user_avatar)
	private CircularImageView mIv_avatar;
	
	@ViewInject(R.id.tv_name)
	private TextView mTv_name;
	
	@ViewInject(R.id.tv_number)
	private TextView mTv_number;
	
	@ViewInject(R.id.pull_scroll)
	private PullToRefreshGridView store_grid;
	
	private List<StoreIn_list>listModel = new ArrayList<StoreIn_list>();

	private StoreInAdapter mAdapter;
	private PageModel page =new PageModel();

	private int mId;

	protected StoreIn_Fx mActModel;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_in_store);
		init();
	}

	private void init()
	{
		initDefaultAdapter();
		initTitle();
		initPullToRefreshScrollView();
	}

	private void initPullToRefreshScrollView()
	{
		store_grid.setMode(Mode.BOTH);
		store_grid.setOnRefreshListener(new OnRefreshListener2<GridView>()
				{

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						page.resetPage();
						requestData(false);
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						if(page.increment())
						{
							requestData(true);
						}else 
						{
							store_grid.onRefreshComplete();
						}
						
					}
				});
		store_grid.setRefreshing();
	}

	private void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_fx");
		model.putAct("deal_fx_list");
		model.put("id", mId);
		model.putPage(page.getPage());
		SDRequestCallBack<StoreIn_Fx> handler = new SDRequestCallBack<StoreIn_Fx>()
		{
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if(actModel.getStatus() == 1)
				{
					mActModel =actModel;
					page.update(actModel.getPage());
					SDViewUtil.updateAdapterByList(listModel, actModel.getList(), mAdapter, isLoadMore);
				}
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				store_grid.onRefreshComplete();		
			}
		};
	InterfaceServer.getInstance().requestInterface(HttpMethod.POST, model, null, false, handler);
}

	private void initDefaultAdapter() 
	{
		mAdapter = new StoreInAdapter(listModel,this);
		store_grid.setAdapter(mAdapter);
	}

	private void initTitle()
	{
		Bundle bundle = getIntent().getExtras();
		mId =bundle.getInt("id");
		if(mId < 0 )
		{
			finish();
		}
		mTitle.setMiddleTextTop(bundle.getString("name"));
		mTitle.initRightItem(0);
		SDViewBinder.setImageView(bundle.getString("img"), mIv_avatar);
		SDViewBinder.setTextView(mTv_name, bundle.getString("name"));
		SDViewBinder.setTextView(mTv_number, String.valueOf(bundle.getInt("count")));
	}
}
