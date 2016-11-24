package com.fanwe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fanwe.app.AppConfig;
import com.fanwe.constant.Constant.TitleType;
import com.fanwe.event.EnumEventTag;
import com.fanwe.library.title.SDTitleItem;
import com.fanwe.o2o.miguo.R;
import com.miguo.app.HiHomeActivity;
import com.sunday.eventbus.SDEventManager;

public class GuideActivity extends BaseActivity {
	private LinearLayout mHome;
	private LinearLayout mSeller;
	private LinearLayout mFx;
	private LinearLayout mMine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_guide);
		init();
	}

	private void init() {
		initTitle();
		initView();
	}

	private void initView() 
	{
		mHome = (LinearLayout) findViewById(R.id.home);
		mSeller = (LinearLayout) findViewById(R.id.seller);
		mFx = (LinearLayout) findViewById(R.id.fx);
		mMine = (LinearLayout) findViewById(R.id.mine);

		mHome.setOnClickListener(this);
		mSeller.setOnClickListener(this);
		mFx.setOnClickListener(this);
		mMine.setOnClickListener(this);
	}

	private void initTitle() {
		mTitle.setLeftImageLeft(0);
		mTitle.initRightItem(1);
		mTitle.setMiddleTextTop("米果小站");
		mTitle.getItemRight(0).setImageRight(R.drawable.bg_saoyisao);
	}
	
	@Override
	public void onCLickRight_SDTitleSimple(SDTitleItem v, int index)
	{
		super.onCLickRight_SDTitleSimple(v, index);
		SDEventManager.post(GuideActivity.class, EnumEventTag.START_SCAN_QRCODE.ordinal());
	}
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.home:
			startFragment(0);
			break;
		case R.id.seller:
			startFragment(1);
			break;
		case R.id.fx:
			startFragment(2);
			break;
		case R.id.mine:
			startFragment(3);
			break;

		default:
			break;
		}
	}

	private void startFragment(int index) {

		Intent intent = new Intent(GuideActivity.this,
				HiHomeActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("index", index);
		intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 100) {
			if (resultCode == MyCaptureActivity.RESULT_CODE_SCAN_SUCCESS) {
				String result = data
						.getStringExtra("extra_result_success_string");
				if (result.contains("offline")) {
					Intent intentStore = new Intent(this,
							StoreConfirmOrderActivity.class);
					intentStore.putExtra("mID", Integer.parseInt(result
							.split("\\/")[result.split("\\/").length - 1]));
					startActivity(intentStore);
					
				}else if(result.contains(".html#") && !result.contains("offline"))
				{
					String session = AppConfig.getSessionId();
					result.replace(".html#",".html?from=app&sess_id="+session+"#");
					Intent intentWeb = new Intent(this,CaptureResultWebActivity.class);
				}else if(result.contains(".html#") && !result.contains("offline"))
				{
					result.replace(".html#",".html?from=app#");
					Intent intentWeb = new Intent(this,CaptureResultWebActivity.class);

				} else if (result.contains(".html#")
						&& !result.contains("offline")) {
					result.replace(".html#", ".html?from=app#");
					Intent intentWeb = new Intent(this,
							CaptureResultWebActivity.class);
					intentWeb.putExtra("url", result);
					startActivity(intentWeb);
				} else if (result.contains("/#") && !result.contains("offline")) {
					String session = AppConfig.getSessionId();
					result.replace("/#", "/?from=app&sess_id="+session+"#");
					Intent intentWeb = new Intent(this,
							CaptureResultWebActivity.class);
					intentWeb.putExtra("url", result);
					startActivity(intentWeb);
				}
			}
		}
	}
}
