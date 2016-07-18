package com.fanwe.fragment;

import com.fanwe.CityListActivity;
import com.fanwe.HomeSearchActivity;
import com.fanwe.MainActivity;
import com.fanwe.event.EnumEventTag;
import com.fanwe.jpush.JpushHelper;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.sunday.eventbus.SDBaseEvent;
import com.sunday.eventbus.SDEventManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 首页标题栏fragment
 * 
 * @author js02
 * 
 */
public class HomeTitleBarFragment extends BaseFragment
{
	
	@ViewInject(R.id.frag_home_title_bar_tv_app_name)
	private ImageView mTvAppName;

	@ViewInject(R.id.frag_home_title_bar_ll_earn)
	private LinearLayout mLlEarn;

	@ViewInject(R.id.frag_home_title_bar_tv_earn)
	private TextView mTvCurrentCity;

	@ViewInject(R.id.frag_home_title_bar_iv_earn_arrow)
	private ImageView mIvEarnArrow;

	@ViewInject(R.id.frag_home_title_bar_ll_search)
	private LinearLayout mLlSearch;
	
	@ViewInject(R.id.frag_home_title_bar_ll_saoyisao)
	private LinearLayout mLlSao;

	
	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_home_title_bar);
	}

	@Override
	protected void init()
	{
		super.init();
		//initTitle();
		bindTitlebarCityNameData();
		registeClick();
	}

	/*private void initTitle()
	{
		Init_indexActModel initModel = AppRuntimeWorker.getInitActModel();
		if (initModel != null)
		{
			String title = initModel.getProgram_title();
			if (!TextUtils.isEmpty(title))
			{
				mTvAppName.setText(title);
			} else
			{
				mTvAppName.setText(getString(R.string.app_name));
			}
		}
	}*/

	private void bindTitlebarCityNameData()
	{
		// 设置当前默认城市
		SDViewBinder.setTextView(mTvCurrentCity, AppRuntimeWorker.getCity_name(), "未找到");
		JpushHelper.setTag("city_"+AppRuntimeWorker.getCity_id());
	}

	private void registeClick()
	{
		mLlEarn.setOnClickListener(this);
		mLlSearch.setOnClickListener(this);
		mLlSao.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.frag_home_title_bar_ll_earn:
			clickEarn(v);
			break;

		case R.id.frag_home_title_bar_ll_search:
			clickSearch();
			break;
		case R.id.frag_home_title_bar_ll_saoyisao:
			clickSao();
			break;
		default:
			break;
		}
	}
	/**
	 * 扫一扫
	 */
	private void clickSao()
	{
		SDEventManager.post(MainActivity.class, EnumEventTag.START_SCAN_QRCODE.ordinal());
	}

	/**
	 * 点击区域
	 */
	private void clickEarn(View v)
	{
		Intent intent = new Intent(getActivity(), CityListActivity.class);
		getParentFragment().startActivityForResult(intent,100);
	}

	/**
	 * 点击搜索
	 */
	private void clickSearch()
	{
		Intent intent = new Intent(getActivity(), HomeSearchActivity.class);
		startActivity(intent);
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