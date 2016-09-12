package com.fanwe.fragment;

import com.fanwe.jshandler.AppJsHandler;
import com.fanwe.library.fragment.WebViewFragment;
import com.fanwe.library.title.TitleItemConfig;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.library.utils.SDViewUtil;
import com.fanwe.o2o.miguo.R;
import com.fanwe.umeng.UmengShareManager;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class AppWebViewFragment extends WebViewFragment
{
	
	private String mContent;
	private String id;
	private String title="米果小站";
	private String url;
	private String mSummary;
	private String imageUrl;
	@Override
	public void setUrl(String url)
	{
		this.mStrUrl = url;
	}
	
	
	public void setUserId(String id)
	{
		this.id= id;
	}
	
	public void setShareContent(String location,String title,String summary,String pic){
		url = location;
		mContent = title;
		mSummary = summary;
		imageUrl = pic;
	}
	@Override
	protected void addJavascriptInterface()
	{
		// 详情回调处理
		AppJsHandler detailHandler = new AppJsHandler(getActivity())
		{
			@Override
			@JavascriptInterface
			public void setPageTitle(final String title)
			{
				SDHandlerUtil.runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						setTitle(title);
					}
				});
			}
			
		@Override
		@JavascriptInterface
		public void promote(final String location, final String title, final String summary,
				final String pic) {
			SDHandlerUtil.runOnUiThread(new Runnable()
			{
				@Override
				public void run()
				{
					setShareContent(location,title,summary,pic);
				}
			});
		}
		};
		mWeb.addJavascriptInterface(detailHandler,"mgxz");
	}
	@Override
	protected View onCreateContentView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		int resId = getContentViewResId();
		if (resId == 0)
		{
			resId = R.layout.frag_webview;
		}
		return setContentView(resId);
	}
	
	@Override
	protected WebView findWebView()
	{
		return super.findWebView();
	}
	
	@Override
	protected ProgressBar findProgressBar() 
	{
		return super.findProgressBar();
	}
	@Override
	protected View onCreateTitleView()
	{
		return super.onCreateTitleView();
	}

	
	@Override
	protected void init() 
	{
		super.init();
		initTitle(title);
		
	}
	@Override
	protected void initWebView() 
	{
		
		initSetting();
		//交汇起冲突
		addJavascriptInterface();
		mWeb.setWebViewClient(getWebViewClient());
		mWeb.setWebChromeClient(getWebChromeClient());
	}
	
	private void initSetting()
	{
		WebSettings webSettings = mWeb.getSettings(); 
		webSettings.setSavePassword(false); 
		webSettings.setSaveFormData(true); 
		webSettings.setJavaScriptEnabled(true); 
		webSettings.setSupportZoom(false);
		mWeb.setBackgroundColor(Color.parseColor("#f4f4f4"));
	}
	
	@Override
	protected void onProgressChanged_WebChromeClient(WebView view, int newProgress)
	{
		if (mProgressMode != null)
		{
			switch (mProgressMode)
			{
			case HORIZONTAL:
				if (newProgress == 100)
				{
					SDViewUtil.hide(mPgbHorizontal);
				} else
				{
					SDViewUtil.show(mPgbHorizontal);
					mPgbHorizontal.setProgress(newProgress);
				}
				break;
			case NONE:

				break;

			default:
				break;
			}
		}
	}

	private void initTitle(String title)
	{
		mTitle.setMiddleTextTop(title);
		if(TextUtils.isEmpty(id))
		{
			mTitle.removeAllRightItems();
			mTitle.addItemRight_TEXT("推广");
		}else{
			mTitle.removeAllRightItems();
		}
	}
	
	
	@Override
	public void onRightClick_SDTitleListener(TitleItemConfig config, int index, View view)
	{
		UmengShareManager.share(getActivity(), mContent, mSummary, url, UmengShareManager.getUMImage(getActivity(), imageUrl), null);
	}
		
	@Override
	public void setTitle(String title)
	{
		super.setTitle(title);
	}
}
