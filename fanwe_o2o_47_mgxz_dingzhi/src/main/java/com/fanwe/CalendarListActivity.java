package com.fanwe;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.customview.MyCalendar;
import com.fanwe.customview.MyCalendar.OnDaySelectListener;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CalendarListActivity extends BaseActivity implements OnDaySelectListener
{
	public static int result = 679;
	
	public static int Result_CODE = 898;
	
	public static int RESULT_CANCELED =234;
	@ViewInject(R.id.ll)
	private LinearLayout mLl_calendar;
	
	 private MyCalendar c1;
	 private  Date date;
	 private String nowday;
	 private long nd = 1000*24L*60L*60L;//一天的毫秒数
	 private SimpleDateFormat simpleDateFormat,sd1,sd2;
	 private  SharedPreferences sp;
	 private  Editor editor;
	 private String sp_inday;
	 private String sp_outday;
	 private String outday;
	 private String inday;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_calendar);
		sp=getSharedPreferences("date",Context.MODE_PRIVATE);
        //本地保存
        sp_inday=sp.getString("dateIn", "");
		sp_outday=sp.getString("dateOut", "");
        editor=sp.edit();
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        nowday=simpleDateFormat.format(new Date());
		sd1=new SimpleDateFormat("yyyy");
		sd2=new SimpleDateFormat("dd");
		init();
	}

	private void init()
	{
		initTitle();
		initCalendar();
	}

	private void initCalendar()
	{
		List<String> listDate=getDateList();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        for(int i=0;i<listDate.size();i++)
        {
        	c1 = new MyCalendar(this);
            c1.setLayoutParams(params);
            Date date=null;
            try {
    		    date = simpleDateFormat.parse(listDate.get(i));
    		} catch (ParseException e) 
            {
    			e.printStackTrace();
    		}
            if(!"".equals(sp_inday))
            {
            	c1.setInDay(sp_inday);
            }
            if(!"".equals(sp_outday))
            {
            	c1.setOutDay(sp_outday);
            }
            c1.setTheDay(date);
            c1.setOnDaySelectListener(this);
            mLl_calendar.addView(c1);
        }
	}

	private List<String> getDateList()
	{
		List<String> list=new ArrayList<String>();
		Date date=new Date();
		int nowMon=date.getMonth()+1;
		String yyyy = sd1.format(date);
		String dd = sd2.format(date);
		if(nowMon==9){
			list.add(simpleDateFormat.format(date));
			list.add(yyyy+"-10-"+dd);
			list.add(yyyy+"-11-"+dd);
			if(!dd.equals("01")){
				list.add(yyyy+"-12-"+dd);
			}
		}else if(nowMon==10){
			list.add(yyyy+"-10-"+dd);
			list.add(yyyy+"-11-"+dd);
			list.add(yyyy+"-12-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			}
		}else if(nowMon==11){
			list.add(yyyy+"-11-"+dd);
			list.add(yyyy+"-12-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-02-"+dd);
			}
		}else if(nowMon==12){
			list.add(yyyy+"-12-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-01-"+dd);
			list.add((Integer.parseInt(yyyy)+1)+"-02-"+dd);
			if(!dd.equals("01")){
				list.add((Integer.parseInt(yyyy)+1)+"-03-"+dd);
			}
		}else{
			list.add(yyyy+"-"+getMon(nowMon)+"-"+dd);
			list.add(yyyy+"-"+getMon((nowMon+1))+"-"+dd);
			list.add(yyyy+"-"+getMon((nowMon+2))+"-"+dd);
			if(!dd.equals("01")){
				list.add(yyyy+"-"+getMon((nowMon+3))+"-"+dd);
			}
		}
		return list;
	}

	private String getMon(int nowMon) 
	{
		String month;
		if(nowMon < 10){
			month="0"+nowMon;
		}else{
			month=""+nowMon;
		}
		return month;
	}

	private void initTitle() 
	{
		mTitle.setMiddleTextTop("选择日期");
	}

	@Override
	public void onDaySelectListener(View view, String date) {
		try {
			if(simpleDateFormat.parse(date).getTime()<simpleDateFormat.parse(nowday).getTime()){
				return;
			 }
			long dayxc=(simpleDateFormat.parse(date).getTime()-simpleDateFormat.parse(nowday).getTime())/nd;
			if(dayxc>60){
				return;
			}
		} catch (ParseException e)
		{
			e.printStackTrace();
		}
		//若以前已经选择了日期，则在进入日历后会显示以选择的日期，该部分作用则是重新点击日历时，清空以前选择的数据（包括背景图案）
		  if(!"".equals(sp_inday))
		  {
          	MyCalendar.viewIn.setBackgroundColor(Color.WHITE);
          	((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#898989"));
          	((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setGravity(Gravity.CENTER);
          	MyCalendar.viewIn.findViewById(R.id.ll_container).setBackgroundResource(R.color.bg_activity_white);
          	 SDViewUtil.hide(MyCalendar.viewIn.findViewById(R.id.tv_calendar));
          	 try {
				if(simpleDateFormat.parse(sp_inday).getTime() == simpleDateFormat.parse(nowday).getTime())
				 {
					((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setText("今天");
					((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#FF6600"));
				 }
				if((simpleDateFormat.parse(sp_inday).getTime() - simpleDateFormat.parse(nowday).getTime())/nd == 1)
				 {
					 ((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setText("明天");
					 ((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#FF6600"));
				 }
				if((simpleDateFormat.parse(sp_inday).getTime() - simpleDateFormat.parse(nowday).getTime())/nd == 2)
				 {
					 ((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setText("后天");
					 ((TextView) MyCalendar.viewIn.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#FF6600"));
				 }
			} catch (ParseException e) 
          	 {
				e.printStackTrace();
			}
          }
          if(!"".equals(sp_outday))
          {
        	 MyCalendar.viewOut.setBackgroundColor(Color.WHITE);
        	 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#898989"));
        	 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setGravity(Gravity.CENTER);
        	 MyCalendar.viewOut.findViewById(R.id.ll_container).setBackgroundResource(R.color.bg_activity_white);
            SDViewUtil.hide(MyCalendar.viewOut.findViewById(R.id.tv_calendar));
            try {
				if((simpleDateFormat.parse(sp_outday).getTime() - simpleDateFormat.parse(nowday).getTime())/nd == 1)
				 {
					 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setText("明天");
					 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#FF6600"));
				 }
				if((simpleDateFormat.parse(sp_outday).getTime() - simpleDateFormat.parse(nowday).getTime())/nd == 2)
				 {
					 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setText("后天");
					 ((TextView) MyCalendar.viewOut.findViewById(R.id.tv_calendar_day)).setTextColor(Color.parseColor("#FF6600"));
				 }
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
          }
          
          String dateDay=date.split("-")[2];
          if(Integer.parseInt(dateDay)<10)
          {
        	  dateDay=date.split("-")[2].replace("0", "");
          }
    	TextView textDayView=(TextView) view.findViewById(R.id.tv_calendar_day);
    	TextView textView=(TextView) view.findViewById(R.id.tv_calendar);
    	LinearLayout container=(LinearLayout) view.findViewById(R.id.ll_container);
    	container.setBackgroundResource(R.drawable.bg_circle);
        textDayView.setTextColor(Color.WHITE);
         if(null==inday||inday.equals(""))
         {
             textDayView.setText(dateDay);
             SDViewUtil.show(textView);
             textView.setText("入住");
             inday = date;
         }else{
        	 SDViewUtil.hide(textView);
        	 textDayView.setGravity(Gravity.CENTER);
        	 if(inday.equals(date))
        	 {
        		 view.setBackgroundColor(Color.WHITE);
        		 textDayView.setText(dateDay);
            	 SDViewUtil.hide(textView);
            	 container.setBackgroundResource(R.color.bg_activity_white);
            	 textDayView.setGravity(Gravity.CENTER);
            	 textDayView.setTextColor(Color.parseColor("#898989"));
                 inday="";
        	 }else{
     			try {
					if(simpleDateFormat.parse(date).getTime()<simpleDateFormat.parse(inday).getTime()){
						 view.setBackgroundColor(Color.WHITE);
						 textDayView.setTextColor(Color.parseColor("#898989"));
						 SDViewUtil.hide(textView);
						 container.setBackgroundResource(R.color.bg_activity_white);
						 textDayView.setGravity(Gravity.CENTER);
						return;
					 }
				} catch (ParseException e)
     			{
					e.printStackTrace();
				}
            	 textDayView.setText(dateDay);
            	 SDViewUtil.show(textView);
                 textView.setText("离开");
                 outday=date;
                 editor.putString("dateIn", inday);
                 editor.putString("dateOut", outday);
                 editor.commit();
                 Intent intent = new Intent();
                 intent.putExtra("success", 1);
                 if(getIntent().getIntExtra("id", 0)==1)
                 {
                	 setResult(result, intent);
                 }
                 if(getIntent().getIntExtra("id", 0)==2)
                 {
                	 setResult(RESULT_CANCELED, intent);
                 }
                 if(getIntent().getIntExtra("id", 0) == 3)
                 {
                	 setResult(Result_CODE, intent);
                 }
                 finish();
        	 }
         }
	}
}
