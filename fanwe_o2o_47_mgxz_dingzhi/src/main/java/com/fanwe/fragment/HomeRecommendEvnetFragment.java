package com.fanwe.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.EventDetailActivity;
import com.fanwe.adapter.HomeRecommendEventAdapter;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.model.EventModel_List;
import com.fanwe.model.EventModel_Time;
import com.fanwe.model.Index_indexActModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 首页推荐活动
 * 
 * @author js02
 * 
 */
public class HomeRecommendEvnetFragment extends BaseFragment
{
	
	@ViewInject(R.id.tv_event_time)
	private TextView mTv_time;
	
	@ViewInject(R.id.iv_event_bt)
	private ImageView mIv_bt;
	
	
	@ViewInject(R.id.ll_container)
	private LinearLayout mLl_container;
	
	@ViewInject(R.id.tv_timer)
	private TextView mTv_timer;
	
	
	private List<EventModel_List> mListModel = new ArrayList<EventModel_List>();
	
	private Index_indexActModel mIndexModel;
	
	private EventModel_Time eventModel_Time;
	

	private long time_begin;

	private long time_end;

	private long time_request;

	private long time_local_request;

//	private long  time_local_current;
	
	private long time_diff;

	private long now;

	private long time = 0;

	private boolean isFlag = false;

	private CountDownTimer mCountDownTimer;

	public void setmIndexModel(Index_indexActModel indexModel)
	{
		this.mIndexModel = indexModel;
		this.mListModel = mIndexModel.getSpecial().getList();
		this.eventModel_Time = mIndexModel.getSpecial().getInfo();
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_home_recommend_event);
	}

	@Override
	protected void init()
	{
		super.init();
		bindData();
		registeClick();
	}

	private void bindData()
	{
		if(! toggleFragmentView(mListModel))
		{
			return;
		}
		time();
		mLl_container.removeAllViews();
		HomeRecommendEventAdapter adapter = new HomeRecommendEventAdapter(mListModel, getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT,1);
		int size = mListModel.size();
		for (int i = 0; i < size; i++)
		{
			View view = adapter.getView(i, null, null);
			mLl_container.addView(view, params);
		}
	}
	 
	 public void time()
	 {
		 	if(eventModel_Time == null)
		 	{
		 		return;
		 	}
		 	if(!TextUtils.isEmpty(eventModel_Time.getTime_begin()))
		 	{
		 		time_begin = Long.parseLong(eventModel_Time.getTime_begin());
		 	}
		 	if(!TextUtils.isEmpty(eventModel_Time.getTime_end()))
		 	{
		 		time_end = Long.parseLong(eventModel_Time.getTime_end());
		 	}
		 	if(!TextUtils.isEmpty(eventModel_Time.getTime_request()))
		 	{
		 		time_request = Long.parseLong(eventModel_Time.getTime_request());
		 	}
		 	if(!TextUtils.isEmpty(eventModel_Time.getTime_local_request()))
		 	{
		 		time_local_request = Long.parseLong(eventModel_Time.getTime_local_request());
		 	}
			time_diff = time_local_request - time_request;
			startThread();
	 }
	 public void startThread() {
			this.now = getLocalTime();
			 if( now < time_begin)//已结束
				{
				 mTv_timer.setText("距离活动开始");
				 	time  = time_begin;
				 	isFlag =false;
				 	long down = time - now;
				 	setCount(down);
				}else if(now < time_end )//正在开始
				{
					mTv_timer.setText("距离活动结束");
					time = time_end;
					isFlag =false;
					long down = time - now;
					setCount(down);
				}else if(now == time_begin)
				{
					mTv_timer.setText("距离活动结束");
					time = now;
					isFlag = false;
					long down =time_end - time_begin;
					setCount(down);
				}else if(now > time_end)//还未开始
				{
					mTv_timer.setText("该活动已结束");
					SDViewBinder.setTextView(mTv_time, "00:00:00");
					time = 0;
					isFlag = true;
				}
		}

	 public long getLocalTime()
		{
			long locatTime  = System.currentTimeMillis()/1000- time_diff;
			return locatTime;
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
	 
		  public void setCount(final long time)
		  {
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
	private void registeClick()
	{
		mIv_bt.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				clickSeeAllEvent();
			}
		});
	}

	private void clickSeeAllEvent()
	{
		startActivity(new Intent(getActivity(),EventDetailActivity.class));
	}
	@Override
	public void onDetach() {
		
		super.onDetach();
		if(mCountDownTimer != null)
		{
			mCountDownTimer.cancel();
			mCountDownTimer = null;
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}