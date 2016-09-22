package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanwe.adapter.MyDistritionPagerAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.MyRedEnvelopeFragment;
import com.fanwe.fragment.MyRedEnvelopeInvalidFragment;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 会员中心-我的红包
 * 
 * @author Administrator
 * 
 */
public class MyRedEnvelopeActivity extends BaseActivity
{

	@ViewInject(R.id.ll_tabs)
	private LinearLayout ll_tabs;
	@ViewInject(R.id.viewPager_myred)
	private ViewPager viewPager_myred;
	
	private MyRedEnvelopeFragment myRedEnvelopeFragment =new MyRedEnvelopeFragment();
	
	private MyRedEnvelopeInvalidFragment myRedEnvelopeInvalidFragment=new MyRedEnvelopeInvalidFragment();
	
	private List<Fragment> listModel=new ArrayList<Fragment>();
	
	private TextView[] arrText;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_my_red_envelope);
		init();
	}

	private void init()
	{
		listModel.add(myRedEnvelopeFragment);
		listModel.add(myRedEnvelopeInvalidFragment);
		initTitle();
		initTabs();
		initViewPager();
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {
		viewPager_myred.setAdapter(new MyDistritionPagerAdapter(getSupportFragmentManager(),listModel));
		viewPager_myred.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				setCurrentTab(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("我的红包");
	}
	private void initTabs()
	{
		String []arr=getResources().getStringArray(R.array.my_arrText);
		arrText=new TextView [arr.length];
		for (int i = 0; i < ll_tabs.getChildCount(); i++) {
			
			TextView textView=(TextView) ll_tabs.getChildAt(i);
			textView.setText(""+arr[i]);
			textView.setGravity(Gravity.CENTER);
			textView.setEnabled(true);
			textView.setTextSize(16);
			textView.setTextColor(Color.parseColor("#595959"));
			textView.setBackgroundResource(R.drawable.bg_distribution_tabs);
			textView.setTag(i);
			arrText[i]=textView;
			textView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					viewPager_myred.setCurrentItem((Integer) v.getTag());
				}
			});
		}
		arrText[0].setEnabled(false);
		arrText[0].setTextColor(getResources().getColor(R.color.main_color));
	}
	private void setCurrentTab(int position) {
		for (int i = 0; i < arrText.length; i++) {
			arrText[i].setTextColor(Color.BLACK);
			arrText[i].setEnabled(true);
		}
		arrText[position].setTextColor(getResources().getColor(R.color.main_color));
		arrText[position].setEnabled(false);
	}
	/**
	 * 我的红包
	 */
	protected void clickRedEnvelope()
	{
		getSDFragmentManager().toggle(R.id.act_my_red_envelope_fl_content, null, MyRedEnvelopeFragment.class);
	}
	
	/**
	 * 已失效
	 */
	protected void clickInvalid()
	{
		getSDFragmentManager().toggle(R.id.act_my_red_envelope_fl_content, null, MyRedEnvelopeInvalidFragment.class);
	}
	/**
	 * 兑换红包
	 */
	/*
	protected void clickExchange()
	{
		getSDFragmentManager().toggle(R.id.act_my_red_envelope_fl_content, null, ExchangeRedEnvelopeFragment.class);
	}*/

}
