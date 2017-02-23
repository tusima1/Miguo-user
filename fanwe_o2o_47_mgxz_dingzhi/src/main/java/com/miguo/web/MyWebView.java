package com.miguo.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by didik 
 * Created time 2016/12/19
 * Description: 
 */

public class MyWebView extends WebView {
    protected WebListener webListener;

    private Object jsObj;
    private String requestUrl="";

    public MyWebView(Context context) {
        super(context);
    }

    public MyWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initWebViewParams();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void initWebViewParams() {
        WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        this.setWebViewClient(setMyWebViewClient());
        this.setWebChromeClient(new WebChromeClient());
    }

    @SuppressLint({"JavascriptInterface", "AddJavascriptInterface"})
    public void startLoadUrl(){
        this.loadUrl(requestUrl);
        this.addJavascriptInterface(jsObj, "mgxz");
    }

    protected WebViewClient setMyWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webStart();
                if (webListener!=null){
                    webListener.onPageStarted(view,url,favicon);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                loadUrl(request.getUrl().toString());
                return true;
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                webError();
                if (webListener != null) {
                    webListener.onReceivedError(view, request, error);
                }
            }
        };
    }

    public void webError() {
        this.setVisibility(GONE);
        this.loadUrl("about:blank");
    }

    public void webStart() {
        this.setVisibility(VISIBLE);
    }

    public void setWebViewParams(Object js,String url){
        this.jsObj=js;
        this.requestUrl=url;
    }

    public interface WebListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);
    }

    public void setWebListener(WebListener webListener) {
        this.webListener = webListener;
    }
}
