package com.fanwe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.GPSFragment;
import com.fanwe.fragment.GPSFragment.OnGPSLintener;
import com.fanwe.fragment.HoltelSearchFragment;
import com.fanwe.fragment.HoltelSearchFragment.OnHoltelSearchListener;
import com.fanwe.fragment.TimeFragment;
import com.fanwe.fragment.TimeFragment.OnTimeListener;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class HoltelListActivity extends BaseActivity
{

	/** 分类id(int) */
	public static final String EXTRA_CATE_ID = "extra_cate_id";

	/** 品牌id(int) */
	public static final String EXTRA_BID = "extra_bid";
	
	@ViewInject(R.id.gps_searchcity)
	private FrameLayout mFra_Gps;
	
	@ViewInject(R.id.select_time_holtel)
	private FrameLayout mFra_Time;
	
	@ViewInject(R.id.search_nearby)
	private FrameLayout mFra_Search;
	
	@ViewInject(R.id.act_confirm_order_btn_search)
	private Button mBtn_confirm;
	
	protected String mCity;
	
	protected String time_begin;
	
	protected String time_end;
	
	protected String mKeyword = "";

	private GPSFragment mFraGps;

	private TimeFragment mFraTime;
	
	private HoltelSearchFragment mFraSearch;

	protected int mQid =0;

	private String mSearch ="";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_holtel_lay);
		init();
	}

	private void init()
	{
		initTitle();
		initFragment();
		registeClick();
	}


	private void registeClick() 
	{
		mBtn_confirm.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.act_confirm_order_btn_search:
			clickBtn();
			break;
			
		default:
			break;
		}
	}

	private void clickBtn()
	{
		Intent intent =new Intent(this,HoltelSearchListActivity.class);
		Bundle bundle =new Bundle();
		bundle.putString("city_name", mCity);
		bundle.putString("time_begin", getTime(time_begin+"-00-00-00"));
		bundle.putString("time_end", getTime(time_end+"-00-00-00"));
		bundle.putString("keyword", mKeyword);
		bundle.putString("search", mSearch);
		bundle.putInt("qid", mQid);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public static String getTime(String user_time)
	{
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss",
                Locale.CHINA);
		Date d;
		try {
			
		d = sdf.parse(user_time);
		long l = d.getTime();
		String str = String.valueOf(l);
		re_time = str.substring(0, 10);

		} catch (ParseException e) {
		
		e.printStackTrace();
		}
		return re_time;
		}
	public void initFragment()
	{
		//定位
		 mFraGps = new GPSFragment();
		 mFraGps.setOnGPSListener(new OnGPSLintener()
		 {
			
			@Override
			public void setOnGPSLintener(String city) {
				mCity = city;
				Bundle bundle = new Bundle();
				bundle.putString("city", mCity);
				mFraSearch.setArguments(bundle);
			}
		});
		 getSDFragmentManager().replace(R.id.gps_searchcity, mFraGps);
		//日期选择
		 mFraTime = new TimeFragment();
		 mFraTime.setOnTimeListener(new OnTimeListener()
		 {
			
			@Override
			public void onTimeListener(String begin, String end) 
			{
				time_begin = begin;
				time_end = end;
			}
		});
		 getSDFragmentManager().replace(R.id.select_time_holtel, mFraTime);
		 //关键词搜索
		 mFraSearch = new HoltelSearchFragment();
		 mFraSearch.setOnHoltelSearchListener(new OnHoltelSearchListener() {
			
			public void onBusinessSearchListener(String search, int qid, int id) {
				
				if(id == 0)
				{
					mSearch = search;
					mQid= qid;
					mKeyword = "";
				}else
				{
					mKeyword = search;
					mQid = qid;
					mSearch = "";
				}
			}
		});
		 getSDFragmentManager().replace(R.id.search_nearby, mFraSearch);
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("酒店");
	}
}
