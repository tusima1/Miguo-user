package com.fanwe.mine.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.fanwe.ConfirmTopUpActivity;
import com.fanwe.app.App;
import com.fanwe.dao.barry.MemberDetailDao;
import com.fanwe.dao.barry.impl.MemberDetailDaoImpl;
import com.fanwe.dao.barry.view.MemberDetailView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.miguo.live.views.customviews.MGToast;

/**
 * 代言殿堂
 * Created by qiang.chen on 2016/10/27.
 */
public class RepresentIntroduceActivity extends Activity {
    private TextView tvUpdate;
    private WebView mWebView;
    private MemberDetailDao memberDetailDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_represent_introduce);
        initTitle();
        preWidget();
        initWeb();
        preData();
    }

    private void initWeb() {
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSavePassword(false);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
//        mWebView.setBackgroundColor(Color.parseColor("#ffffff"));
    }

    private void preData() {
        memberDetailDao = new MemberDetailDaoImpl(new MemberDetailView() {
            @Override
            public void getMemberDetailSuccess(final String html) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                    }
                });
            }

            @Override
            public void getMemberDetailError(String msg) {

            }
        });
        memberDetailDao.getMerberDetail("1");
    }

    private void preWidget() {
        tvUpdate = (TextView) findViewById(R.id.tv_update_introduce);
        mWebView = (WebView) findViewById(R.id.webView_introduce);
        tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
    }

    private void update() {
        String fxLevel = App.getInstance().getmUserCurrentInfo().getUserInfoNew().getFx_level();
        if (DataFormat.toInt(fxLevel) >= 2) {
            MGToast.showToast("您还没有达到升级要求!");
            return;
        }
        Intent intent = new Intent(this, ConfirmTopUpActivity.class);
        startActivity(intent);
    }

    private void initTitle() {
        findViewById(R.id.iv_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.tv_middle)).setText("代言殿堂");
    }
}
