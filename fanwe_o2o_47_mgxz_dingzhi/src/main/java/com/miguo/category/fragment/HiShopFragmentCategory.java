package com.miguo.category.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.constant.ServerUrl;
import com.fanwe.jshandler.AppJsHandler;
import com.fanwe.library.utils.SDHandlerUtil;
import com.fanwe.o2o.miguo.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.listener.fragment.HiShopFragmentListener;
import com.miguo.ui.view.PtrFrameLayoutForViewPager;
import com.miguo.utils.SharedPreferencesUtils;

import org.apache.http.util.EncodingUtils;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/1.
 */

public class HiShopFragmentCategory extends FragmentCategory {

    @ViewInject(R.id.webView)
    WebView webView;

    @ViewInject(R.id.title)
    TextView title;

    @ViewInject(R.id.title_layout)
    RelativeLayout top;

    @ViewInject(R.id.back)
    ImageView back;

    @ViewInject(R.id.ptr_layout)
    PtrFrameLayoutForViewPager ptrFrameLayoutForViewPager;

    public HiShopFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiShopFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {
        back.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initWebView();
        setTitlePadding(top);
        initPtrLayout(ptrFrameLayoutForViewPager);
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return webView.getScrollY() == 0;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        update();
        super.onRefreshBegin(frame);
    }

    public void update(){
        String name = SharedPreferencesUtils.getInstance().getUserName();
        String pwd = SharedPreferencesUtils.getInstance().getPassword();
        String url;
        //9月23日添加  &from=app
        url = ServerUrl.getAppH5Url() + "user/applogin?from=app";
        String postData ="name=" + name + "&pwd=" + pwd ;
        webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
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
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if(ptrFrameLayoutForViewPager.isRefreshing()){
                    ptrFrameLayoutForViewPager.refreshComplete();
                }
                HiShopFragmentCategory.this.back.setVisibility(view.canGoBack() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                loaddingFail();
                super.onReceivedError(view, request, error);
            }
        });
        addJavascriptInterface();
    }

    @SuppressLint("AddJavascriptInterface")
    protected void addJavascriptInterface() {
        // 详情回调处理
        AppJsHandler detailHandler = new AppJsHandler(getActivity()) {
            @Override
            @JavascriptInterface
            public void setPageTitle(final String title) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        HiShopFragmentCategory.this.title.setText(title);
                    }
                });
            }

            @Override
            @JavascriptInterface
            public void promote(final String location, final String title, final String summary,
                                final String pic) {
                SDHandlerUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        setShareContent(location, title, summary, pic);
                    }
                });
            }
        };
        webView.addJavascriptInterface(detailHandler, "mgxz");
    }

    public void clickBack(){
        webView.goBack();
    }

}
