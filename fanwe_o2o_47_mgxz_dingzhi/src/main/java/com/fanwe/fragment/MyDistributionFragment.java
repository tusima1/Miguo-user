package com.fanwe.fragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fanwe.DistributionMyQRCodeActivity;
import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.DistributionWithdrawActivity;
import com.fanwe.MemberRankActivity;
import com.fanwe.MyCollectionActivity;
import com.fanwe.MyMessageActivity;
import com.fanwe.WithdrawLogActivity;
import com.fanwe.adapter.MyDistributionAdapter;
import com.fanwe.app.AppHelper;
import com.fanwe.common.ImageLoaderManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.DistributionGoodsModel;
import com.fanwe.model.MyDistributionUser_dataModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_fx_my_fxActModel;
import com.fanwe.model.User_center_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.fanwe.utils.SDInterfaceUtil;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
	/**
	 * 分销--我的
	 * @Author cxk
	 */
public class MyDistributionFragment extends BaseFragment
{
	
	private Uc_fx_my_fxActModel mActModel;
	private CircularImageView mSiv_image;
	
	@ViewInject(R.id.ll_userName)
	private LinearLayout mLl_userName;
	
	private TextView mTv_username;
	private List<DistributionGoodsModel> mListModel = new ArrayList<DistributionGoodsModel>();
	private MyDistributionAdapter mAdapter;
	
	@ViewInject(R.id.frag_my_refresh)
	private PullToRefreshScrollView frag_PullTo;
	
	private PageModel mPage = new PageModel();
	private TextView mTv_totalMomey;//总佣金
	private TextView mTv_tixian;//提现现金
	private HttpHandler<String> mHttpHandler;
	private TextView mTv_place;
	/*private LinearLayout mll_hongbao;
	private TextView mTv_hongbao;*/
	private TextView mTv_cang;
	private TextView mTv_xiaomi;
	private TextView mTv_Predict;
	private boolean click = false;
	protected User_center_indexActModel userActModel;

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.frag_my_distribution);
	}

	@Override
	protected void init()
	{
		super.init();
		initTitle();
		addTopView();
		initPullToRefreshListView();
	}
	
	public void initTitle() {
		mTitle.setMiddleTextTop("全民营销");
		mTitle.setLeftImageLeft(0);
		mTitle.initRightItem(1);
		if (click) {
			mTitle.getItemRight(0).setImageRight(R.drawable.my_message_bot);
		} else {
			mTitle.getItemRight(0).setImageRight(
					R.drawable.my_distribution_message);
		}
	}
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index) {

		super.onCLickRight_SDTitleSimple(v, index);
		Intent intent = (new Intent(getActivity(), MyMessageActivity.class));
		startActivity(intent);
		click = true;
	}
	/**
	 * 请求我的账户接口
	 */
	public void requestMyAccount()
	{
		if (AppHelper.getLocalUser() == null)
		{
			return;
		}
		
		if (mHttpHandler != null)
		{
			mHttpHandler.cancel();
		}

		RequestModel model = new RequestModel();
		model.putCtl("user_center");
		model.putUser();
		SDRequestCallBack<User_center_indexActModel> handler = new SDRequestCallBack<User_center_indexActModel>()
		{
			
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					userActModel = actModel;
					bindData(actModel);
				}
			}

			@Override
			public void onFinish()
			{
				frag_PullTo.onRefreshComplete();
			}
		};
		mHttpHandler = InterfaceServer.getInstance().requestInterface(model, handler);
	}
	
	protected void bindData(User_center_indexActModel actModel)
	{
		if(actModel == null)
		{
			return;
		}
		SDViewBinder.setImageView(actModel.getUser_avatar(), mSiv_image, ImageLoaderManager.getOptionsNoCacheNoResetViewBeforeLoading());
		SDViewBinder.setTextView(mTv_username, actModel.getUser_name(), "未找到");
	}
	
	private void addTopView()
	{
		mSiv_image = (CircularImageView)findViewById(R.id.iv_user_avatar);
		mTv_username = (TextView)findViewById(R.id.tv_username);
		mTv_place=(TextView)findViewById(R.id.frag_my_tv_place);
		mTv_totalMomey = (TextView)findViewById(R.id.frag_total_momey);
		LinearLayout mLl_Predict = (LinearLayout) findViewById(R.id.ll_tixian_Predict);
		mTv_Predict = (TextView)findViewById(R.id.frag_my_account_tv_user_money);
		LinearLayout mll_tixian=(LinearLayout)findViewById(R.id.ll_tixian_Actual);
		mTv_tixian=(TextView)findViewById(R.id.frag_my_account_tv_user_score);
		LinearLayout mll_dianpu=(LinearLayout)findViewById(R.id.ll_look_dianpu);
		LinearLayout mll_xiaomi=(LinearLayout)findViewById(R.id.ll_my_xiaomi);
		/*mll_hongbao=(LinearLayout)findViewById(R.id.ll_my_hongbao);
		mTv_hongbao=(TextView) findViewById(R.id.tv_my_hongbao);*/
		mTv_xiaomi = (TextView) findViewById(R.id.tv_my_xiaomi);
		LinearLayout mll_erweima=(LinearLayout)findViewById(R.id.ll_erweima);
		LinearLayout mll_collect=(LinearLayout)findViewById(R.id.ll_my_shoucang);
		mTv_cang=(TextView) findViewById(R.id.tv_my_shoucang);
		LinearLayout mll_short=(LinearLayout)findViewById(R.id.ll_yijian_short);
		
		/**
		 * 总佣金
		 */
		mTv_totalMomey.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(getActivity(),WithdrawLogActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 实际提现
		 */
		mll_tixian.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				if(mActModel.getLevel_id() < 2)
				{
					Intent intent = new Intent(getActivity(),MemberRankActivity.class);
					startActivity(intent);
					SDToast.showToast("您还没有提现权限");
				}else
				{
					Intent intent = new Intent(getActivity(),DistributionWithdrawActivity.class);
					intent.putExtra("money", mActModel.getUser_data().getFx_money()+"");
					startActivity(intent);
				}
			}
		});
		/**
		 * 预计提现
		 */
		mLl_Predict.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent intent  = new Intent(getActivity(),DistributionMyXiaoMiActivity.class);
				intent.putExtra("yes", true);
				intent.putExtra("money", mActModel.getYuji());
				intent.putExtra("up_name", userActModel.getUp_name());
				intent.putExtra("up_id", userActModel.getUp_id());
				startActivity(intent);
			}
		});
		/**
		 * 查看店铺
		 */
		mll_dianpu.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent= new Intent(getActivity(),DistributionStoreWapActivity.class);
				startActivity(intent);
			}
		});
		
		/**
		 * 我的小米
		 */
		mll_xiaomi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				Intent intent =new Intent(getActivity(),DistributionMyXiaoMiActivity.class);
				intent.putExtra("up_name", userActModel.getUp_name());
				intent.putExtra("up_id", userActModel.getUp_id());
				startActivity(intent);
			}
		});
		
		/**
		 * 我的红包
		 *//*
		mll_hongbao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MyRedEnvelopeActivity.class));
			}
		});*/
		/**
		 * 二维码
		 */
		mll_erweima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				Intent intent =new Intent(getActivity(),DistributionMyQRCodeActivity.class);
				Bundle bundle = new Bundle();
				if (mActModel == null)
				{
					return;
				}
				MyDistributionUser_dataModel userData = mActModel.getUser_data();
				if (userData == null)
				{
					return;
				}
				String share_card = userData.getShare_mall_card();
				String user_avatar = userData.getUser_avatar();
				if("".equals(share_card)||"".equals(user_avatar)){
					return;
				}
				bundle.putString("card",share_card);
				bundle.putString("photo", user_avatar);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		/**
		 * 我的收藏
		 */
		mll_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), MyCollectionActivity.class));
			}
		});

		/**
		 * 我的会员
		 */
		mLl_userName.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(getActivity(),MemberRankActivity.class);
				startActivity(intent);
			}
		});
		/**
		 * 推广我的小店
		 */
		mll_short.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}
	 //只有下拉刷新，没有上啦加载更多
	 
	private void initPullToRefreshListView()
	{
		frag_PullTo.setMode(Mode.PULL_FROM_START);
		frag_PullTo.setOnRefreshListener(new OnRefreshListener2<ScrollView>()
		{

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				
				pullRefresh();
				requestMyAccount();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView)
			{
				pullLoadMore();
			}
		});
		frag_PullTo.setRefreshing();
	}
	
	public void requestData(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		model.putCtl("uc_fx");
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
						bindData();
						SDViewUtil.updateAdapterByList(mListModel, actModel.getItem(), mAdapter, isLoadMore);
					}
				}
			}

			@Override
			public void onFinish()
			{
				frag_PullTo.onRefreshComplete();
			}
		});
	}
	
	protected void bindData()
	{
		if (mActModel == null)
		{
			return;
		}
		MyDistributionUser_dataModel userData = mActModel.getUser_data();
		if (userData == null)
		{
			return;
		}
		if(mActModel.getLevel_id() == 1){
			SDViewBinder.setTextView(mTv_place, "青铜", "未找到");
		}else if(mActModel.getLevel_id() == 2){
			SDViewBinder.setTextView(mTv_place, "白金", "未找到");
		}else if(mActModel.getLevel_id() == 3){
			SDViewBinder.setTextView(mTv_place, "钻石", "未找到");
		}
		
		BigDecimal bd1 = new BigDecimal(userData.getFx_money());
		bd1 = bd1.setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal bd2 = new BigDecimal(userData.getFx_total_balance());
		bd2 = bd2.setScale(2,BigDecimal.ROUND_HALF_UP);
		BigDecimal bd3 = new BigDecimal(mActModel.getYuji());
		bd3 = bd3.setScale(2,BigDecimal.ROUND_HALF_UP);
		SDViewBinder.setTextView(mTv_tixian, "￥"+bd1 +"");
		SDViewBinder.setTextView(mTv_totalMomey,"￥"+bd2+"");
		SDViewBinder.setTextView(mTv_cang, mActModel.getCollection_count()+"");
		//SDViewBinder.setTextView(mTv_hongbao, mActModel.getRed_packet_count()+"");
		SDViewBinder.setTextView(mTv_xiaomi, mActModel.getUser_num()+"");
		SDViewBinder.setTextView(mTv_Predict,"￥"+bd3,"￥0.00");
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
			frag_PullTo.onRefreshComplete();
		}
	}
	@Override
	public void onHiddenChanged(boolean hidden)
	{
		refreshMyAccountFragment();
	}

	@Override
	public void onResume()
	{
		refreshMyAccountFragment();
		super.onResume();
	}

	private void refreshMyAccountFragment()
	{
		if (!this.isHidden())
		{
			requestMyAccount();
			requestData(false);
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
