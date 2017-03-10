package com.miguo.category;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiSystemMessageDetailActivity;
import com.miguo.framework.WebActionJSHandler;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/10.
 */

public class HiSystemMessageDetailCategory extends Category {

    @ViewInject(R.id.webView)
    WebView webView;

    WebActionJSHandler handler;


    public HiSystemMessageDetailCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initFirst() {
        handler = new WebActionJSHandler(getActivity());
    }

    @Override
    protected void initThisListener() {

    }

    @Override
    protected void setThisListener() {

    }

    @Override
    protected void init() {

    }

    @Override
    protected void initViews() {

    }

    public void update(){
        String url = getActivity().getUrl();
        if (TextUtils.isEmpty(url)) {
            return;
        }
        LocalUserModel userModel = AppHelper.getLocalUser();
        if (userModel != null) {
            String userid = userModel.getUser_mobile();
            String password = userModel.getUser_pwd();
            if (!TextUtils.isEmpty(App.getInstance().getToken()) && !TextUtils.isEmpty(userid) && !TextUtils.isEmpty(password)) {
                url = url.contains("mgxz.com") ? url + "?" + "name=" + userid + "&pwd=" + password + "&from=app" : url;
            } else {
                url = url + "?name=&pwd=&from=app";
            }
        } else {
            url = url + "?name=&pwd=&from=app";
        }
        webView.loadUrl(url);
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void initWebView() {
        WebSettings webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //支持对网页缩放
        webSettings.setSupportZoom(true);
        //支持android4.0
        webSettings.setBuiltInZoomControls(true);
        //默认缩放模式
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                start();
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                loaddingFail();
                super.onReceivedError(view, request, error);
            }
        });
        webView.addJavascriptInterface(handler, "mgxz");
        update();
    }

    @Override
    public HiSystemMessageDetailActivity getActivity() {
        return (HiSystemMessageDetailActivity)super.getActivity();
    }
}
