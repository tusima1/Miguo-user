package com.fanwe;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.dao.barry.UserAgreementDao;
import com.fanwe.dao.barry.impl.UserAgreementDaoImpl;
import com.fanwe.dao.barry.view.UserAgreementView;
import com.fanwe.model.Uc_DistModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterAgreementActivity extends BaseActivity
{
	@ViewInject(R.id.webView_agreement)
	private WebView mWebView;

	UserAgreementDao userAgreementDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setmTitleType(TitleType.TITLE);
		setContentView(R.layout.act_agreement);
		init();
	}

	private void init()
	{
		
		iniTitle();
		requestData();
		initWebView();
		initData();
	}


	private void initData(){
		userAgreementDao = new UserAgreementDaoImpl(new UserAgreementView() {
			@Override
			public void getUserAgreementSuccess(final String html) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
					}
				});
			}

			@Override
			public void getUserAgreementError(String msg) {

			}
		});
		userAgreementDao.getUserAgreement("3");
	}

	private void requestData() 
	{
	}

	protected void bindResult(Uc_DistModel actModel) 
	{
		mWebView.loadDataWithBaseURL(null, actModel.getBody(), "text/html", "utf-8", null);
	}

	private void iniTitle()
	{
		mTitle.setMiddleTextTop("注册协议");
	}

	private void initWebView()
	{
		WebSettings webSettings = mWebView.getSettings(); 
		webSettings.setSavePassword(false); 
		webSettings.setSaveFormData(true); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setSupportZoom(false);
		mWebView.setBackgroundColor(Color.parseColor("#ffffff"));
	}
}
