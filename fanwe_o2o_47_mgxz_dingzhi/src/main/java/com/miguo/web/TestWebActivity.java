package com.miguo.web;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanwe.jshandler.AppJsHandler;
import com.fanwe.o2o.miguo.R;
import com.miguo.definition.IntentKey;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

public class TestWebActivity extends AppCompatActivity {

    private MyWebView webView;
    private ImageView iv_back;
    private ImageView iv_right;
    private TextView tv_title;
    private JsHandler jsHandler1;
    private AppJsHandler jsHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web);

        webView = ((MyWebView) findViewById(R.id.webView));
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_right = ((ImageView) findViewById(R.id.iv_right));
        tv_title = ((TextView) findViewById(R.id.tv_title));


        jsHandler = new AppJsHandler(this){
            @Override
            public void setTitle(final String title) {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_title.setText(title);
                    }
                });
            }
        };

        jsHandler1 = new JsHandler(){
            @Override
            public void setTitle(final String title) {
                MGUIUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_title.setText(title);
                    }
                });
            }
        };

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }
            }
        });

        iv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MGToast.showToast("iv_right");
            }
        });
    }
    private String url="";
    @Override
    protected void onResume() {
        super.onResume();
//        webView.setWebViewParams(jsHandler1,getIntent().getStringExtra(IntentKey.HOME_BANNER_WEB_PAGE));
        url=getIntent().getStringExtra(IntentKey.HOME_BANNER_WEB_PAGE);
        initWebViewParams();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface","JavascriptInterface"})
    public void initWebViewParams() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        webView.setWebViewClient(setMyWebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
        webView.addJavascriptInterface(jsHandler1, "mgxz");
    }

    protected WebViewClient setMyWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.webStart();
//                if (webListener!=null){
//                    webListener.onPageStarted(view,url,favicon);
//                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                webView.loadUrl(request.getUrl().toString());
                return true;
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                webView.webError();
//                if (webListener != null) {
//                    webListener.onReceivedError(view, request, error);
//                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        };
    }

}
