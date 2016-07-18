package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.fanwe.adapter.EventDetailAdapter;
import com.fanwe.baidumap.BaiduMapManager;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.library.dialog.SDDialogManager;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.model.Event_edtailModelList;
import com.fanwe.model.Event_indexActModel;
import com.fanwe.model.Event_infoModel;
import com.fanwe.model.PageModel;
import com.fanwe.model.RequestModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.service.HomeEventDetailReceiver;
import com.fanwe.umeng.UmengShareManager;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 活动详情
 * 
 * @author js02
 * 
 */
public class EventDetailActivity extends BaseActivity
{
	/** 活动id (int) */
	public static final String EXTRA_EVENT_ID = "extra_event_id";

	public static String KEY_EXTRAS ="extras";

	public static String NOTICE_TYPE ="activity";

	@ViewInject(R.id.act_event_lv)
	private PullToRefreshListView mPtrsvAll;
	
	@ViewInject(R.id.tv_time)
	private TextView mTv_time;
	
	@ViewInject(R.id.tv_timer_instance)
	private TextView mTv_instance;
	

	private Event_indexActModel mActModel;
	private List<Event_edtailModelList> mListActModel =new ArrayList<Event_edtailModelList>();

	private int mId;
	
	PageModel mPage = new PageModel();

	private EventDetailAdapter mAdapter;

	private double latitude;

	private double longitude;

	private long time_begin;

	private long time_end;

	private long time_request;

	private long time_local_request;

	private long time_diff;
	
	private long time = 0;

	public boolean isFlag = false;
	
	private CountDownTimer mCountDownTimer;

	private long now=0;

	private boolean isShow =true;

	private HomeEventDetailReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_event_detail);
		init();
	}

	private void init()
	{
		getIntentData();
		registerMessageReceiver();
		initTitle();
		locationCity();
		initPullToRefreshScrollView();
		
	}
private void registerMessageReceiver() {
		receiver = new HomeEventDetailReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		intentFilter.addAction("com.fanwe.service.HomeEventDetailReceiver");
		registerReceiver(receiver, intentFilter);
	}
	private void initPullToRefreshScrollView()
	{
		mPtrsvAll.setMode(Mode.BOTH);
		
		mPtrsvAll.setOnRefreshListener(new OnRefreshListener2<ListView>()
		{
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mPage.resetPage();
				requestDetail(false);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				
				if(mPage.increment())
				{
					requestDetail(true);
				}else
				{
					SDToast.showToast("没有更多数据了");
					mPtrsvAll.onRefreshComplete();
				}
			}
		});
		mPtrsvAll.setRefreshing();
	}
	public void scrollToTop()
	{
		if ( mPtrsvAll != null && mAdapter != null && mAdapter.getCount() > 0)
		{
			mPtrsvAll.getRefreshableView().setSelection(0);
		}
	}


	private void getIntentData()
	{
		Intent intent = getIntent();
		mId = intent.getIntExtra(EXTRA_EVENT_ID, -1);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		setIntent(intent);
		init();
		super.onNewIntent(intent);
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("限时特卖");

	}
	
	public long getLocalTime()
	{
		long locatTime  = System.currentTimeMillis()/1000- time_diff;
		return locatTime;
	}
	
	 public void startThread() {
		this.now = getLocalTime();
		 if( now < time_begin)//已结束
			{
			 	mTv_instance.setText("距离活动开始");
			 	time = time_begin;
			 	isShow = false;
			 	isFlag =false;
			 	long down = time - now;
			 	setCount(down);
			}else if(now < time_end )//正在开始
			{
				mTv_instance.setText("距离活动结束");
				time = time_end;
				isShow = true;
				isFlag = false;
				long down = time - now;
				setCount(down);
			}else if(now == time_begin)
			{
				mTv_instance.setText("距离活动结束");
				time = now;
				isFlag = false;
				isShow =true;
				long down =time_end -time_begin;
				setCount(down);
			}else//还未开始
			{
				SDViewUtil.hide(mTv_time);
				mTv_instance.setText("当前活动已结束，尽情期待");
				time = 0;
				isShow = false;
				isFlag = true;
			}
		 	mAdapter = new EventDetailAdapter(mListActModel,mActivity,isShow);
			mPtrsvAll.setAdapter(mAdapter);
	}
	private void initData(Event_indexActModel actModel)
	 {
		if(actModel == null)
		 {
			 return;
		 }
		 	
			time_begin = Long.parseLong(actModel.getSpecial().getTime_begin());
			time_end = Long.parseLong(actModel.getSpecial().getTime_end());
			time_request = Long.parseLong(actModel.getSpecial().getTime_request());
			time_local_request = Long.parseLong(actModel.getSpecial().getTime_local_request());
			time_diff = time_local_request - time_request;
			startThread();
		  
	 }
   
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		clickShare();
	}

	/**
	 * 分享
	 */
	private void clickShare()
	{
		if (mActModel == null)
		{
			SDToast.showToast("未找到可分享内容");
			return;
		}

		Event_infoModel infoModel = mActModel.getEvent_info();
		if (infoModel == null)
		{
			SDToast.showToast("未找到可分享内容");
			return;
		}
		String content = infoModel.getName() + infoModel.getShare_url();
		String imageUrl = infoModel.getIcon();
		String clickUrl = infoModel.getShare_url();

		UmengShareManager.share(this, "", content, clickUrl, UmengShareManager.getUMImage(this, imageUrl), null);
	}
	private void locationCity()
	{
		BaiduMapManager.getInstance().startLocation(new BDLocationListener()
		{

			@Override
			public void onReceiveLocation(BDLocation location)
			{
				
				if (location != null)
				{
					dealLocationSuccess();
				}
				BaiduMapManager.getInstance().stopLocation();
			}

		});
	}

	protected void dealLocationSuccess() {
		//维度
		latitude = BaiduMapManager.getInstance().getLatitude();
		//经度
		longitude = BaiduMapManager.getInstance().getLongitude();
		
	}
	
	/**
	 * 请求活动详情接口
	 */
	public void requestDetail(final boolean isLoadMore)
	{
		RequestModel model = new RequestModel();
		long timecurrentTimeMillis = System.currentTimeMillis()/1000;
		model.putCtl("special");
		model.putAct("index");
		model.put("time_local_request", timecurrentTimeMillis);
		model.putPage(mPage.getPage());
		model.put("m_latitude", latitude);
		model.put("m_longitude", longitude);
		
		SDRequestCallBack<Event_indexActModel> handler = new SDRequestCallBack<Event_indexActModel>()
		{

			@Override
			public void onStart()
			{
				SDDialogManager.showProgressDialog("请稍候...");
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				
				if (actModel.getStatus() == 1)
				{
					
					mPage.update(actModel.getPage());
					SDViewUtil.updateAdapterByList(mListActModel, actModel.getList(), mAdapter, isLoadMore);
					initData(actModel);
					
				}
			}

			@Override
			public void onFailure(HttpException error, String msg)
			{
				
			}

			@Override
			public void onFinish()
			{
				SDDialogManager.dismissProgressDialog();
				mPtrsvAll.onRefreshComplete();
			}
		};
		InterfaceServer.getInstance().requestInterface(model, handler);
	}
	 
	@SuppressLint("DefaultLocale")
	private void setTime(long time) {
        if (isFlag) {
            return;
        }
        int hour = (int) (time / 3600);
        int minute = (int) ((time % 3600) / 60);
        int second = (int) (time % 60);
        String s = String.format("%02d:%02d:%02d", hour, minute, second);
        SDViewBinder.setTextView(mTv_time, s);
    }
	  public void setCount(final long time) {
	        if (mCountDownTimer != null) {
	            mCountDownTimer.cancel();
	        }
	        mCountDownTimer = new CountDownTimer(time * 1000, 1000) {
	            @Override
	            public void onTick(long millisUntilFinished) {
	                setTime((int) (millisUntilFinished / 1000));
	            }
	            
	            @Override
	            public void onFinish() {
	            		
	            	startThread();
	            }
	        };
	        mCountDownTimer.start();
	    }
	  

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case COMMENT_SUCCESS:
			setmIsNeedRefreshOnResume(true);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		if(mCountDownTimer != null)
		{
			mCountDownTimer.cancel();
			mCountDownTimer = null;
		}
		if(receiver != null)
		{
			unregisterReceiver(receiver);
			receiver = null;
		}
	}
	
	@Override
	protected void onNeedRefreshOnResume()
	{
		requestDetail(false);
		super.onNeedRefreshOnResume();
	}

}