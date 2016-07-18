package com.fanwe;

import com.fanwe.constant.Constant.TitleType;
import com.fanwe.http.InterfaceServer;
import com.fanwe.http.listener.SDRequestCallBack;
import com.fanwe.model.RequestModel;
import com.fanwe.model.Uc_DistModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class RegisterAgreementActivity extends BaseActivity
{
	@ViewInject(R.id.webView_agreement)
	private WebView mWebView;
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
	}

	private void requestData() 
	{
		RequestModel model = new RequestModel();
		model.putCtl("page");
		model.putAct("agreement");
		InterfaceServer.getInstance().requestInterface(model, new SDRequestCallBack<Uc_DistModel>()
			{

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				if (actModel.getStatus() == 1)
				{
					bindResult(actModel);
				}
			}

			@Override
			public void onFinish()
			{
						
			}
		});
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
