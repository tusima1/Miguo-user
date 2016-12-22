package com.miguo.web;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_web);

        webView = ((MyWebView) findViewById(R.id.webView));
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_right = ((ImageView) findViewById(R.id.iv_right));
        tv_title = ((TextView) findViewById(R.id.tv_title));



        jsHandler1 = new JsHandler(){
            @JavascriptInterface
            @Override
            public void setTitle(final String title) {
                super.setTitle(title);
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
        url=getIntent().getStringExtra(IntentKey.HOME_BANNER_WEB_PAGE);
        webView.setWebViewParams(jsHandler1,url);
        webView.startLoadUrl();
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            finish();
        }
    }
}
