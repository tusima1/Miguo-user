package com.fanwe.library.fragment;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.library.R;
import com.fanwe.library.title.SDTitle;
import com.fanwe.library.title.SDTitle.SDTitleListener;
import com.fanwe.library.title.TitleItem;
import com.fanwe.library.title.TitleItemConfig;
import com.fanwe.library.utils.SDOtherUtil;
import com.fanwe.library.utils.SDViewUtil;

/**
 * WebView fragment
 * 
 * @author js02
 * 
 */
public class WebViewFragment extends SDBaseFragment implements SDTitleListener
{

	protected ProgressBar mPgbHorizontal;
	protected WebView mWeb;

	protected EnumProgressMode mProgressMode = EnumProgressMode.NONE;
	protected EnumWebviewHeightMode mWebviewHeightMode = EnumWebviewHeightMode.WRAP_CONTENT;

	protected String mStrUrl;
	protected String mStrHtmlContent;
	protected String mStrReferer;
	protected String mStrTitle;

	private boolean isFirstLoadFinish = true;
	protected boolean isShowTitle = false;
	protected boolean isScaleToShowAll = false;
	protected boolean isSupportZoom = true;
	protected boolean isSupportZoomControls = false;
	protected boolean resetWebViewHeightWhenFirstLoadFinish = false;
	protected WebViewFragmentListener mListener;
	protected SDTitle mTitle;
	protected TextView mTvClose;

	public void setmListener(WebViewFragmentListener mListener)
	{
		this.mListener = mListener;
	}

	public void setmProgressMode(EnumProgressMode mProgressMode)
	{
		this.mProgressMode = mProgressMode;
		changeProgressMode();
	}

	public void setmWebviewHeightMode(EnumWebviewHeightMode mWebviewHeightMode)
	{
		this.mWebviewHeightMode = mWebviewHeightMode;
		changeWebviewHeightMode();
	}

	protected void changeWebviewHeightMode()
	{
		if (mWeb != null)
		{
			ViewGroup.LayoutParams paramsOld = mWeb.getLayoutParams();
			if (paramsOld != null && paramsOld instanceof LinearLayout.LayoutParams)
			{
				LinearLayout.LayoutParams params = null;
				switch (mWebviewHeightMode)
				{
				case WRAP_CONTENT:
					params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					break;
				case MATCH_PARENT:
					params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
					break;

				default:
					break;
				}
				mWeb.setLayoutParams(params);
			}
		}
	}

	public WebView getWebView()
	{
		return this.mWeb;
	}

	public void setShowTitle(boolean isShowTitle)
	{
		this.isShowTitle = isShowTitle;
		changeTitleVisibility();
	}

	public void setResetWebViewHeightWhenFirstLoadFinish(boolean resetWebViewHeightWhenFirstLoadFinish)
	{
		this.resetWebViewHeightWhenFirstLoadFinish = resetWebViewHeightWhenFirstLoadFinish;
	}

	public void setSupportZoomControls(boolean isSupportZoomControls)
	{
		this.isSupportZoomControls = isSupportZoomControls;
	}

	public void setSupportZoom(boolean isSupportZoom)
	{
		this.isSupportZoom = isSupportZoom;
	}

	public void setScaleToShowAll(boolean isScaleToNormal)
	{
		this.isScaleToShowAll = isScaleToNormal;
	}

	public void setUrl(String url)
	{
		this.mStrUrl = url;
	}

	public void setTitle(String title)
	{
		this.mStrTitle = title;
		if (mTitle != null)
		{
			mTitle.setMiddleTextTop(title);
		}
	}

	public void setHtmlContent(String htmlContent)
	{
		this.mStrHtmlContent = htmlContent;
	}

	public void setReferer(String referer)
	{
		this.mStrReferer = referer;
	}

	@Override
	protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		int resId = getContentViewResId();
		if (resId == 0)
		{
			resId = R.layout.frag_webview;
		}
		return setContentView(resId);
	}

	/**
	 * 可以被重写返回fragment布局ID，如果重写此方法，需要重写findWebView()和findProgressBar()
	 * 
	 * @return
	 */
	protected int getContentViewResId()
	{
		return 0;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		findViews();
		init();
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	protected View onCreateTitleView()
	{
		mTitle = new SDTitle(getActivity());
		mTitle.setmListener(this);
		mTitle.setLeftImageLeft(R.drawable.ic_arrow_left_white);
		return mTitle;
	}

	private void findViews()
	{
		mWeb = findWebView();
		mPgbHorizontal = findProgressBar();
	}

	protected WebView findWebView()
	{
		return (WebView) findViewById(R.id.wv_webview);
	}

	protected ProgressBar findProgressBar()
	{
		return (ProgressBar) findViewById(R.id.pgb_horizontal);
	}

	protected void init()
	{
		initTitle(mStrTitle);
		changeWebviewHeightMode();
		initWebView();
		initFinish();
		changeProgressMode();
		startLoadData();
	}

	public void startLoadData()
	{
		if (mStrHtmlContent != null)
		{
			loadHtmlContent(mStrHtmlContent);
			return;
		}

		if (mStrUrl != null)
		{
			loadUrl(mStrUrl, mWeb);
			return;
		}

	}

	protected void changeProgressMode()
	{
		if (mProgressMode != null && mPgbHorizontal != null)
		{
			switch (mProgressMode)
			{
			case HORIZONTAL:
				SDViewUtil.show(mPgbHorizontal);
				break;
			case NONE:
				SDViewUtil.hide(mPgbHorizontal);
				break;

			default:
				break;
			}
		}
	}

	protected void initWebView()
	{
		initSetting();
		addJavascriptInterface();
		mWeb.setWebViewClient(getWebViewClient());
		mWeb.setWebChromeClient(getWebChromeClient());

	}

	// WebViewClient
	protected WebViewClient getWebViewClient()
	{
		return new WebViewClient()
		{
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				return shouldOverrideUrlLoading_WebViewClient(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url)
			{
				onPageFinished_WebViewClient(view, url);
			}
		};
	}

	protected boolean shouldOverrideUrlLoading_WebViewClient(WebView view, String url)
	{
		loadUrl(url, view);
		return true;
	}

	protected void onPageFinished_WebViewClient(WebView view, String url)
	{
		if (isFirstLoadFinish)
		{
			isFirstLoadFinish = false;
			if (resetWebViewHeightWhenFirstLoadFinish)
			{
				SDViewUtil.resetViewHeight(view);
			}
		}
	}

	// WebChromeClient
	protected WebChromeClient getWebChromeClient()
	{
		return new WebChromeClient()
		{
			@Override
			public void onProgressChanged(WebView view, int newProgress)
			{
				onProgressChanged_WebChromeClient(view, newProgress);
			}
		};
	}

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

	protected void addJavascriptInterface()
	{

	}

	@SuppressLint({ "SetJavaScriptEnabled", "NewApi" })
	private void initSetting()
	{
		WebSettings settings = mWeb.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSupportZoom(isSupportZoom);
		settings.setBuiltInZoomControls(true);
		settings.setPluginState(PluginState.ON);
		settings.setDomStorageEnabled(true);
		if (SDOtherUtil.getBuildVersion() >= 11)
		{
			settings.setDisplayZoomControls(false);
		}
		SDViewUtil.setWebviewZoomControlVisibility(mWeb, View.GONE);
		// 设置自适应屏幕
		if (isScaleToShowAll)
		{
			settings.setUseWideViewPort(true);
			settings.setLoadWithOverviewMode(true);
		}
	}

	protected void initFinish()
	{
		if (mListener != null)
		{
			mListener.onInitFinish(mWeb);
		}
	}

	protected void loadHtmlContent(String htmlContent)
	{
		if (htmlContent != null)
		{
			mWeb.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);
		}
	}

	protected void loadUrl(String url, WebView webView)
	{
		boolean shouldLoad = true;
		if (mListener != null)
		{
			if (mListener.onLoadUrl(webView, url)) // 事件被消费
			{
				shouldLoad = false;
			}
		}

		if (shouldLoad)
		{
			if (!TextUtils.isEmpty(url))
			{
				if (!TextUtils.isEmpty(mStrReferer))
				{
					Map<String, String> mapHeader = new HashMap<String, String>();
					mapHeader.put("Referer", mStrReferer);
					webView.loadUrl(url, mapHeader);
				} else
				{
					webView.loadUrl(url);
				}
			}
		}
	}

	public void goBack()
	{
		if (mWeb != null && mWeb.canGoBack())
		{
			mWeb.goBack();
		}
	}

	@Override
	public void onPause()
	{
		mWeb.onPause();
		super.onPause();
	}

	@Override
	public void onResume()
	{
		mWeb.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy()
	{
		if (mWeb != null)
		{
			mWeb.destroy();
			mWeb = null;
		}
		super.onDestroy();
	}

	private void initTitle(String title)
	{

		mTitle.setMiddleTextTop(title);

		// addCloseButton();

		mTitle.removeAllRightItems();
		mTitle.addItemRight_TEXT("关闭");

		changeTitleVisibility();
	}

	private void changeTitleVisibility()
	{
		if (mTitle != null)
		{
			if (isShowTitle)
			{
				SDViewUtil.show(mTitle);
			} else
			{
				SDViewUtil.hide(mTitle);
			}
		}
	}

	protected void addCloseButton()
	{
		if (mTvClose != null)
		{
			mTitle.mLlLeft.removeView(mTvClose);
		}
		LinearLayout.LayoutParams paramsClose = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		// paramsClose.leftMargin = SDViewUtil.dp2px(15);

		mTvClose = new TextView(getActivity());
		mTvClose.setText("关闭");
		SDViewUtil.setTextSizeSp(mTvClose, 17);
		mTvClose.setGravity(Gravity.CENTER);
		mTvClose.setTextColor(Color.parseColor("#ffffff"));
		mTvClose.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				getActivity().finish();
			}
		});
		mTitle.mLlLeft.addView(mTvClose, paramsClose);
	}

	@Override
	public void onLeftClick_SDTitleListener(TitleItem item)
	{
		if (mWeb.canGoBack())
		{
			mWeb.goBack();
		} else
		{
			getActivity().finish();
		}
	}

	@Override
	public void onMiddleClick_SDTitleListener(TitleItem item)
	{

	}

	@Override
	public void onRightClick_SDTitleListener(TitleItemConfig config, int index, View view)
	{
		getActivity().finish();
	}

	public interface WebViewFragmentListener
	{
		public void onInitFinish(WebView webView);

		public boolean onLoadUrl(WebView webView, String url);
	}

	public enum EnumProgressMode
	{
		HORIZONTAL, NONE
	}

	public enum EnumWebviewHeightMode
	{
		WRAP_CONTENT, MATCH_PARENT
	}
}