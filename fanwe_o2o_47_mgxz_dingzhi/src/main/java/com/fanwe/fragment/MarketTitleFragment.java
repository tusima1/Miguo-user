package com.fanwe.fragment;

import com.fanwe.MarketCityListActivity;
import com.fanwe.event.EnumEventTag;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MarketTitleFragment extends BaseFragment{

	@ViewInject(R.id.frag_home_title_bar_tv_app_name)
	private TextView mTvAppName;

	@ViewInject(R.id.frag_home_title_bar_ll_earn)
	private LinearLayout mLlEarn;

	@ViewInject(R.id.frag_home_title_bar_tv_earn)
	private TextView mTvCurrentCity;


	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return setContentView(R.layout.frag_market_title_bar);
	}

	@Override
	protected void init() {
		super.init();
		bindTitlebarCityNameData();
		registeClick();
	}
	private void registeClick() {
		mLlEarn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clickEarn(v);
			}
		});
	}

	/**
	 * 设置城市
	 * @param city
	 */
	public void setShowCity(String city) {
		if ("".equals(city)) {
			return;
		}
		mTvCurrentCity.setText(city);
	}

	private void bindTitlebarCityNameData()
	{
		// 设置当前默认城市
		SDViewBinder.setTextView(mTvCurrentCity, AppRuntimeWorker.getCity_name(), "杭州");
		JpushHelper.setTag("city_"+AppRuntimeWorker.getCity_id());
	}

	/**
	 * 点击区域
	 */
	private void clickEarn(View v)
	{
		Intent intent = new Intent(getActivity(), MarketCityListActivity.class);
		startActivityForResult(intent, 200);
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case RETRY_INIT_SUCCESS:
			bindTitlebarCityNameData();
			break;
		case CITY_CHANGE:
			bindTitlebarCityNameData();
			break;
		default:
			break;
		}
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}


}
