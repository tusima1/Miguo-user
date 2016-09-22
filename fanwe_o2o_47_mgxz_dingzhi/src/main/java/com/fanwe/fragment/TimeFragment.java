package com.fanwe.fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.CalendarListActivity;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
	 * 酒店日期选择
	 * @author cxk
	 *
	 */
public class TimeFragment extends BaseFragment
{
	
	@ViewInject(R.id.tv_in_data)
	private TextView mTv_inData;
	
	@ViewInject(R.id.tv_in_time)
	private TextView mTv_inTime;
	
	@ViewInject(R.id.tv_out_data)
	private TextView mTv_outData;
	
	@ViewInject(R.id.tv_out_time)
	private TextView mTv_outTime;
	
	@ViewInject(R.id.tv_days)
	private TextView mTv_days;
	
	@ViewInject(R.id.ll_time)
	private LinearLayout mLl_time;
	
	protected OnTimeListener mListener;

	private SharedPreferences sp;

	private String inday;

	private String outday;

	private SimpleDateFormat formatTime,sd,sd1;

	private static String nowday= new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	private Editor editor;

	private static  int request_Code = 679;
	
	 private long nd = 1000*24L*60L*60L;

	private String sp_inday;

	private String sp_outday;

	public void setOnTimeListener(OnTimeListener listener)
	{
		this.mListener = listener;
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_time);
	}
	
	
	@Override
	protected void init()
	{
		super.init();
		sp= getActivity().getSharedPreferences("date",Context.MODE_PRIVATE);
		initDefaultDate();
		registeClick();
		
	}

	private void initDefaultDate() 
	{
		Date date = new Date();
		sd = new SimpleDateFormat("dd");
		sd1 = new SimpleDateFormat("yyyy");
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		Date date1=calendar.getTime();
		int nowMonth = date.getMonth()+1;
		mTv_inData.setText(getMonth(nowMonth)+"-"+sd.format(date));
		int month = date1.getMonth()+1;
		mTv_outData.setText(getMonth(month)+"-"+sd.format(date1));
		outday = sd1.format(date1)+"-"+mTv_outData.getText();
		SDViewUtil.show(mTv_inTime);
		SDViewUtil.show(mTv_outTime);
		mTv_inTime.setText("今天");
		mTv_outTime.setText("明天");
		mTv_days.setText("共"+1+"晚");
		editor = sp.edit();
		inday = sd1.format(date)+"-"+mTv_inData.getText();
		editor.putString("dateIn",inday);
		editor.putString("dateOut",outday);
		editor.commit();
		if(mListener != null)
		{
			mListener.onTimeListener(inday, outday);
		}
	}
	
	private String getMonth(int month) {
    	String mon="";
    	if(month<10)
    	{
    		mon="0"+month;
    	}else
    	{
    		mon=""+month;
    	}
    	return mon;
    }
	
	private void registeClick()
	{
		mLl_time.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_time:
			clickTime();
			break;
		default:
			break;
		}
	}

	private void clickTime()
	{
		Intent intent = new Intent(getActivity(),CalendarListActivity.class);
		intent.putExtra("id", 1);
		startActivityForResult(intent, request_Code);
	}
	
	@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data)
	{
			
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == request_Code)
			{
				if(resultCode == CalendarListActivity.result)
				{
					if(data.getIntExtra("success", -1) == 1)
					{
						bindData();
					}
				}
			}
		}
	
	private void bindData()
	{
		String[] indate = null;
		String[] outdate = null;
		sp_inday = sp.getString("dateIn", "");
		sp_outday =sp.getString("dateOut", "");
		formatTime = new SimpleDateFormat("MM-dd");
		
		SimpleDateFormat inputFormatter1 = new SimpleDateFormat("yyyy-MM-dd");
		if(!"".equals(sp_inday))
		{
			Date date1;
			try {
				date1 = inputFormatter1.parse(sp_inday);
				indate = formatTime.format(date1).split("-");
				mTv_inData.setText(formatTime.format(date1));
					if(indate[1].equals(sd.format(inputFormatter1.parse(nowday)))/*(date1.getTime()-inputFormatter1.parse(nowday).getTime())/nd == 0*/)
					{
						SDViewUtil.show(mTv_inTime);
						mTv_inTime.setText("今天");
					}else 
					{
						SDViewUtil.hide(mTv_inTime);
					}
					 if(Integer.parseInt(indate[1])-Integer.parseInt(sd.format(inputFormatter1.parse(nowday))) == 1/*(date1.getTime()-inputFormatter1.parse(nowday).getTime())/nd == 1*/)
					{
						SDViewUtil.show(mTv_inTime);
						mTv_inTime.setText("明天");
					}else 
					{
						SDViewUtil.hide(mTv_inTime);
					}
					if(Integer.parseInt(indate[1])-Integer.parseInt(sd.format(inputFormatter1.parse(nowday))) == 2/*(date1.getTime()-inputFormatter1.parse(nowday).getTime())/nd == 2*/)
					{
						SDViewUtil.show(mTv_inTime);
						mTv_inTime.setText("后天");
					}else 
					{
						SDViewUtil.hide(mTv_inTime);
					}
					

			} catch (ParseException e) 
			{
				e.printStackTrace();
			}
		}
		if(!"".equals(sp_outday))
		{
			Date date1;
			try {
				date1 = inputFormatter1.parse(sp_outday);
				outdate = formatTime.format(date1).split("-");
				mTv_outData.setText(formatTime.format(date1));
					if((date1.getTime()-inputFormatter1.parse(nowday).getTime())/nd == 1)
					{
						SDViewUtil.show(mTv_outTime);
						mTv_outTime.setText("明天");
					}else 
					{
						SDViewUtil.hide(mTv_outTime);
					}
					if((date1.getTime()-inputFormatter1.parse(nowday).getTime())/nd == 2)
					{
						SDViewUtil.show(mTv_outTime);
						mTv_outTime.setText("后天");
					}else 
					{
						SDViewUtil.hide(mTv_outTime);
					}
			} catch (ParseException e) {
				
				e.printStackTrace();
			}
		}
		if(mListener != null)
		{
			mListener.onTimeListener(sp_inday,sp_outday);
		}
		if(outdate[0].equals(indate[0]))
		{
			mTv_days.setText("共"+(Integer.parseInt(outdate[1])-Integer.parseInt(indate[1]))+"晚");
		}else
		{
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(inputFormatter1.parse(sp_inday));
				int sumDay = cal.getActualMaximum(Calendar.DATE);
				mTv_days.setText("共"+(sumDay -Integer.parseInt(indate[1])+Integer.parseInt(outdate[1]))+"晚");
			} catch (ParseException e)     
			{
				e.printStackTrace();
			}
		}
		
	}
	public interface OnTimeListener
	{
		public void onTimeListener(String begin,String end);
	}
	
	@Override
	public void onDetach() 
	{
		super.onDetach();
		if(sp != null)
		{
			sp.edit().remove("dateIn");
			sp.edit().remove("dateOut");
			sp.edit().commit();
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
