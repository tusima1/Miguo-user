package com.fanwe.fragment;

import java.io.File;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.MainActivity;
import com.fanwe.NoticeDetailActivity;
import com.fanwe.NoticeListActivity;
import com.fanwe.app.App;
import com.fanwe.constant.Constant.LoadImageType;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.SettingModelDao;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.utils.SDActivityUtil;
import com.fanwe.library.utils.SDFileUtil;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDIntentUtil;
import com.fanwe.library.utils.SDPackageUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.library.utils.SDViewBinder;
import com.fanwe.o2o.miguo.R;
import com.fanwe.service.AppUpgradeService;
import com.fanwe.work.AppRuntimeWorker;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sunday.eventbus.SDEventManager;

/**
 * 更多fragment
 * 
 * @author js02
 * 
 */
public class MoreFragment extends BaseFragment
{

	@ViewInject(R.id.rl_notice_list)
	private RelativeLayout mRl_notice_list;

	@ViewInject(R.id.rl_about_us)
	private RelativeLayout mRl_about_us;

	@ViewInject(R.id.rl_scan)
	private RelativeLayout mRl_scan;

	@ViewInject(R.id.rl_clear_cache)
	private RelativeLayout mRl_clear_cache;

	@ViewInject(R.id.tv_cache_size)
	private TextView mTv_cache_size;

	@ViewInject(R.id.tv_version)
	private TextView mTv_version;

	@ViewInject(R.id.rl_upgrade)
	private RelativeLayout mRlUpgrade;

	@ViewInject(R.id.tv_kf_phone)
	private TextView mTv_kf_phone;

	@ViewInject(R.id.rl_kf_phone)
	private RelativeLayout mRl_kf_phone;

	@ViewInject(R.id.tv_kf_email)
	private TextView mTv_kf_email;

	@ViewInject(R.id.rl_kf_email)
	private RelativeLayout mRl_kf_email;

	@ViewInject(R.id.cb_load_image_in_mobile_net)
	private CheckBox mCb_load_image_in_mobile_net;

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setmTitleType(TitleType.TITLE);
		return setContentView(R.layout.frag_more);
	}

	@Override
	protected void init()
	{
		super.init();
		initTitle();
		bindData();
		showCacheSize();
		registeClick();

	}

	private void initTitle()
	{
		mTitle.setMiddleTextTop("更多");
	}
	private void bindData()
	{
		int loadImageInMobileNet = SettingModelDao.getLoadImageType();
		if (loadImageInMobileNet == LoadImageType.ALL)
		{
			mCb_load_image_in_mobile_net.setChecked(true);
			mCb_load_image_in_mobile_net.setText("是");
		} else
		{
			mCb_load_image_in_mobile_net.setChecked(false);
			mCb_load_image_in_mobile_net.setText("否");
		}
		mCb_load_image_in_mobile_net.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{

				if (isChecked)
				{
					SettingModelDao.updateLoadImageType(LoadImageType.ALL);
					mCb_load_image_in_mobile_net.setText("是");
				} else
				{
					SettingModelDao.updateLoadImageType(LoadImageType.ONLY_WIFI);
					mCb_load_image_in_mobile_net.setText("否");
				}
			}
		});

		PackageInfo pi = SDPackageUtil.getCurrentPackageInfo();
		mTv_version.setText(String.valueOf(pi.versionName));

		String kfPhone = AppRuntimeWorker.getKf_phone();
		SDViewBinder.setTextView(mTv_kf_phone, kfPhone);

		String kfEmail = AppRuntimeWorker.getKf_email();
		SDViewBinder.setTextView(mTv_kf_email, kfEmail);

	}

	private void showCacheSize()
	{
		File cacheDir = ImageLoader.getInstance().getDiskCache().getDirectory();
		if (cacheDir != null)
		{
			long cacheSize = SDFileUtil.getFileSize(cacheDir);
			mTv_cache_size.setText(SDFileUtil.formatFileSize(cacheSize));
		}
	}

	private void registeClick()
	{
		mRl_notice_list.setOnClickListener(this);
		mRl_about_us.setOnClickListener(this);
		mRl_scan.setOnClickListener(this);
		mRl_clear_cache.setOnClickListener(this);
		mRl_kf_phone.setOnClickListener(this);
		mRl_kf_email.setOnClickListener(this);
		mRlUpgrade.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.rl_notice_list:
			clickAnnoucement();
			break;
		case R.id.rl_about_us:
			clickAbout();
			break;
		case R.id.rl_kf_phone:
			clickKfPhone();
			break;
		case R.id.rl_kf_email:
			clickKfEmail();
			break;
		case R.id.rl_scan:
			clickQrcode();
			break;
		case R.id.rl_clear_cache:
			clickClearCache();
			break;
		case R.id.rl_upgrade:
			clickTestUpgrade();
			break;

		default:
			break;
		}
	}

	/**
	 * 客服邮箱
	 */
	private void clickKfEmail()
	{

	}

	/**
	 * 客服电话
	 */
	private void clickKfPhone()
	{
		String kfPhone = AppRuntimeWorker.getKf_phone();
		if (!TextUtils.isEmpty(kfPhone))
		{
			Intent intent = SDIntentUtil.getIntentCallPhone(kfPhone);
			SDActivityUtil.startActivity(this, intent);
		} else
		{
			SDToast.showToast("未找到客服电话");
		}
	}

	/**
	 * 扫一扫
	 */
	private void clickQrcode()
	{
		SDEventManager.post(MainActivity.class, EnumEventTag.START_SCAN_QRCODE.ordinal());
	}

	/**
	 * 关于
	 */
	private void clickAbout()
	{
		int noticeId = AppRuntimeWorker.getAbout_info();
		if (noticeId > 0)
		{
			Intent intent = new Intent(getActivity(), NoticeDetailActivity.class);
			intent.putExtra(NoticeDetailActivity.EXTRA_NOTICE_ID, noticeId);
			startActivity(intent);
		} else
		{
			SDToast.showToast("未找到关于我们ID");
		}
	}

	/**
	 * 公告列表
	 */
	private void clickAnnoucement()
	{
		startActivity(new Intent(App.getApplication(), NoticeListActivity.class));
	}

	private void clickTestUpgrade()
	{
		Intent intent = new Intent(App.getApplication(), AppUpgradeService.class);
		intent.putExtra(AppUpgradeService.EXTRA_SERVICE_START_TYPE, 1);
		getActivity().startService(intent);
	}
	/**
	 * 清除缓存
	 */
	private void clickClearCache()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				ImageLoader.getInstance().clearDiskCache();
				SDHandlerUtil.runOnUiThread(new Runnable()
				{
					public void run()
					{
						mTv_cache_size.setText("0.00B");
						SDToast.showToast("清除完毕");
					}
				});
			}
		}).start();
	}

	@Override
	public void onHiddenChanged(boolean hidden)
	{
		if (!hidden)
		{
			showCacheSize();
		}
		super.onHiddenChanged(hidden);
	}
	@Override
	protected String setUmengAnalyticsTag() {
		return this.getClass().getName().toString();
	}
}