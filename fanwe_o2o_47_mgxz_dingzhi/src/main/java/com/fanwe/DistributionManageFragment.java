package com.fanwe;

import java.util.ArrayList;
import java.util.List;

import com.fanwe.adapter.MyDistritionPagerAdapter;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.fragment.BaseFragment;
import com.fanwe.fragment.DistributionMarketFragment;
import com.fanwe.fragment.DistributionRecommendFragment;
import com.fanwe.fragment.DistributionmarketingFragment;
import com.fanwe.fragment.MyDistributionFragment;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 会员中心-分销管理
 * 
 * @author Administrator
 * 
 */
public class DistributionManageFragment extends BaseFragment {

	/** 默认选中第几项,值为0-3(int) */
	public static final String EXTRA_SELECT_INDEX = "extra_select_index";

	@ViewInject(R.id.viewPager_distrion)
	private ViewPager viewPager_distrion;

	@ViewInject(R.id.act_main_ll_bottom_menu)
	private LinearLayout actBottom;

	private final static int requestCode = 888;
	private List<Fragment> totalList = new ArrayList<Fragment>();

	private MyDistributionFragment mFragMyDistribution = new MyDistributionFragment();
	private DistributionMarketFragment mFragDistributionMarket = new DistributionMarketFragment();
	private DistributionmarketingFragment mFragDistributionMarketing = new DistributionmarketingFragment();

	// 推荐
	private DistributionRecommendFragment mFragDistributionRecommend = new DistributionRecommendFragment();

	private int mSelectIndex;

	private TextView[] arrTextView;

	private boolean click = false;

	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.act_distribution_manage);
	}

	@Override
	protected void init() {
		totalList.add(mFragMyDistribution);
		//totalList.add(mFragDistributionMarket);
		//totalList.add(mFragDistributionMarketing);
		getIntentData();
		initTitle();
		initTabs();
		initViewPager();
	}

	private void getIntentData() {
		int index = getActivity().getIntent()
				.getIntExtra(EXTRA_SELECT_INDEX, 0);
		if (index < 0 || index > 1) {
			index = 0;
		}
		mSelectIndex = index;
		mFragDistributionMarket.setArguments(getActivity().getIntent()
				.getExtras());
	}

	private void initTabs() {
		String[] arrText = getResources().getStringArray(R.array.arrText);
		arrTextView = new TextView[arrText.length];
		for (int i = 0; i < arrText.length; i++) {
			TextView textView = (TextView) actBottom.getChildAt(i);
			textView.setText(" " + arrText[i]);
			textView.setTextSize(16);
			textView.setHeight(80);
			textView.setBackgroundResource(R.drawable.bg_distribution_tabs);
			textView.setTextColor(Color.BLACK);
			textView.setEnabled(true);
			textView.setTag(i);
			arrTextView[i] = textView;
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v)
				{
					viewPager_distrion.setCurrentItem((Integer) v.getTag());
				}
			});

		}
		arrTextView[0].setEnabled(false);
		arrTextView[0].setTextColor(getResources().getColor(R.color.main_color));
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
		startActivityForResult(intent, requestCode);
		click = true;
	}

	@SuppressWarnings("deprecation")
	private void initViewPager() {

		viewPager_distrion.setAdapter(new MyDistritionPagerAdapter(
				getActivity().getSupportFragmentManager(), totalList));
		viewPager_distrion.setOnPageChangeListener(new OnPageChangeListener() {

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

	private void setCurrentTab(int position) {
		for (int i = 0; i < arrTextView.length; i++) {
			arrTextView[i].setTextColor(Color.BLACK);
			arrTextView[i].setEnabled(true);
		}
		arrTextView[position].setTextColor(getResources().getColor(R.color.main_color));
		arrTextView[position].setEnabled(false);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}
