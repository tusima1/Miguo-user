package com.fanwe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.fanwe.YouHuiDetailActivity;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.EnumEventTag;
import com.fanwe.library.customview.SDScaleImageView;
import com.fanwe.library.utils.SDTypeParseUtil;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.SDTimerDown;
import com.fanwe.utils.SDTimerDown.SDTimerDownListener;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.definition.ClassPath;
import com.miguo.factory.ClassNameFactory;
import com.sunday.eventbus.SDBaseEvent;

public class YouhuiDetailTopFragment extends YouhuiDetailBaseFragment
{

	@ViewInject(R.id.siv_image)
	private SDScaleImageView mSiv_image;

	@ViewInject(R.id.tv_name)
	private TextView mTv_name;

	@ViewInject(R.id.rb_star)
	private RatingBar mRb_star;

	@ViewInject(R.id.tv_star_number)
	private TextView mTv_star_number;

	@ViewInject(R.id.tv_download)
	private TextView mTv_download; // 领取

	@ViewInject(R.id.tv_total_num)
	private TextView mTv_total_num; // 优惠券总数

	@ViewInject(R.id.tv_user_limit)
	private TextView mTv_user_limit; // 每个会员每天领取数量限制

	@ViewInject(R.id.tv_left_time)
	private TextView mTv_left_time; // 剩余时间

	@ViewInject(R.id.tv_expire_day)
	private TextView mTv_expire_day; // 领取后有效期

	@ViewInject(R.id.tv_user_count)
	private TextView mTv_user_count; // 已领取数量

	@ViewInject(R.id.tv_score_limit)
	private TextView mTv_score_limit; // 消耗积分

	private SDTimerDown mCounter = new SDTimerDown();

	private int mId;

	private YouhuiDetailTopFragmentListener mListener;

	public void setListener(YouhuiDetailTopFragmentListener listener)
	{
		this.mListener = listener;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return setContentView(R.layout.frag_youhui_detail_top);
	}

	@Override
	protected void init()
	{
		initIntentData();
		initViewState();
		bindData();
		registeClick();
	}

	private void initIntentData()
	{
		Intent intent = getActivity().getIntent();
		mId = intent.getIntExtra(YouHuiDetailActivity.EXTRA_YOUHUI_ID, 0);
		if (mId <= 0)
		{
			hideFragmentView();
		}
	}

	private void initViewState()
	{
		if (!AppHelper.isLogin())
		{
			mTv_download.setText("登陆后领取");
		} else
		{
			mTv_download.setText("领取");
		}
	}

	private void bindData()
	{
		if (!toggleFragmentView(mInfoModel))
		{
			return;
		}

		SDViewBinder.setImageView(mSiv_image, mInfoModel.getIcon());
		SDViewBinder.setTextView(mTv_name, mInfoModel.getName());
		float ratingStar = SDTypeParseUtil.getFloat(mInfoModel.getAvg_point());
		SDViewBinder.setRatingBar(mRb_star, ratingStar);
		SDViewBinder.setTextView(mTv_star_number, String.valueOf(mInfoModel.getAvg_point()));

		SDViewBinder.setTextView(mTv_user_count, String.valueOf(mInfoModel.getUser_count()));
		SDViewBinder.setTextView(mTv_score_limit, String.valueOf(mInfoModel.getScore_limit()));
		SDViewBinder.setTextView(mTv_total_num, String.valueOf(mInfoModel.getTotal_num()));
		SDViewBinder.setTextView(mTv_user_limit, String.valueOf(mInfoModel.getUser_limit()));
		SDViewBinder.setTextView(mTv_expire_day, String.valueOf(mInfoModel.getExpire_day()));

		startCountDown(mInfoModel.getLast_time());
	}

	private void startCountDown(long timeSecond)
	{
		if (timeSecond > 0)
		{
			mCounter.startCount(mTv_left_time, timeSecond, new SDTimerDownListener()
			{

				@Override
				public void onTickFinish()
				{

				}

				@Override
				public void onTick()
				{

				}

				@Override
				public void onStart()
				{

				}
			});
		}
	}

	private void registeClick()
	{
		mTv_download.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.tv_download:
			clickBuyYouHui();
			break;
		default:
			break;
		}
	}

	private void clickBuyYouHui()
	{
		if (AppHelper.isLogin())
		{
			requestBuyYouHui();
		} else
		{
			Intent intent = new Intent(getActivity(), ClassNameFactory.getClass(ClassPath.LOGIN_ACTIVITY));
			startActivity(intent);
		}
	}

	/**
	 * 请求领取优惠券接口
	 */
	private void requestBuyYouHui()
	{
	}

	@Override
	public void onEventMainThread(SDBaseEvent event)
	{
		super.onEventMainThread(event);
		switch (EnumEventTag.valueOf(event.getTagInt()))
		{
		case LOGIN_SUCCESS:
			initViewState();
			break;

		default:
			break;
		}
	}

	public interface YouhuiDetailTopFragmentListener
	{
		void onRefresh();
	}

}