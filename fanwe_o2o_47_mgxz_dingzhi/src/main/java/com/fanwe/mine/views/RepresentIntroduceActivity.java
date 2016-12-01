package com.fanwe.mine.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.fanwe.ConfirmTopUpActivity;
import com.fanwe.app.App;
import com.fanwe.base.CallbackView2;
import com.fanwe.dao.barry.MemberDetailDao;
import com.fanwe.dao.barry.impl.MemberDetailDaoImpl;
import com.fanwe.dao.barry.view.MemberDetailView;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.UserConstants;
import com.fanwe.user.presents.UserHttpHelper;
import com.fanwe.utils.DataFormat;
import com.fanwe.utils.StringTool;
import com.miguo.live.views.customviews.MGToast;
import com.miguo.utils.MGUIUtil;

import java.util.HashMap;
import java.util.List;

/**
 * 代言殿堂
 * Created by qiang.chen on 2016/10/27.
 */
public class RepresentIntroduceActivity extends Activity implements CallbackView2 {
    private TextView tvUpdate;
    private WebView mWebView;
    private MemberDetailDao memberDetailDao;
    private UserHttpHelper userHttpHelper;
    String fxLevel = App.getInstance().getCurrentUser().getFx_level();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_represent_introduce);
        initTitle();
        preWidget();
        initWeb();
        preData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(userHttpHelper==null){
            userHttpHelper = new UserHttpHelper(this, this);
        }
        userHttpHelper.getUserLevel();
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

    @Override
    public void onSuccess(String responseBody) {

    }


    public void setUserLevel(List<HashMap<String, String>> datas) {
        if (datas != null && datas.size() < 1) {
            return;
        } else {
            String level = datas.get(0).get("fx_level");
            if (!TextUtils.isEmpty(level)) {
                App.getInstance().getCurrentUser().setFx_level(level);
                fxLevel = level;
            }
        }
    }

    @Override
    public void onSuccess(String method, final List datas) {
        if (UserConstants.USER_DISTRIBUTION_LEVEL.equals(method)) {
            MGUIUtil.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setUserLevel(datas);
                }
            });
        }
    }

    @Override
    public void onFailue(String responseBody) {

    }

    @Override
    public void onFinish(String method) {

    }
}
