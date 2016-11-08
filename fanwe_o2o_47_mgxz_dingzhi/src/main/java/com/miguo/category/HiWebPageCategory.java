package com.miguo.category;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiWebPageActivity;
import com.miguo.framework.WebActionJSHandler;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/3.
 */
public class HiWebPageCategory extends Category{

    @ViewInject(R.id.webView)
    WebView webView;

    @ViewInject(R.id.title)
    TextView title;

    WebActionJSHandler handler;

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    public HiWebPageCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        handler = new WebActionJSHandler(getActivity());
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {

    }

    @Override
    protected void setThisListener() {

    }

    @Override
    protected void init() {
        initWebView();
    }

    @Override
    protected void initViews() {
        setTitlePadding(titleLayout);
    }

    private void updateTitle(){
        title.setText(getActivity().getWebTitle().equals("") ? "" : getActivity().getWebTitle());
    }

    private void initWebView(){
        WebSettings webSettings = webView.getSettings();
        //支持js
        webSettings.setJavaScriptEnabled(true);
        //支持对网页缩放
        webSettings.setSupportZoom(true);
        //支持android4.0
        webSettings.setBuiltInZoomControls(true);
        //默认缩放模式
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        webView.loadUrl(getActivity().getUrl());
        webView.addJavascriptInterface(handler, "mgxz");
    }

    @Override
    public HiWebPageActivity getActivity() {
        return (HiWebPageActivity) super.getActivity();
    }
}
