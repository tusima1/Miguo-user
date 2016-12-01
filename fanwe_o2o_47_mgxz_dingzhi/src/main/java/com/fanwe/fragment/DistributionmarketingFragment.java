package com.fanwe.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.fanwe.app.AppHelper;
import com.fanwe.http.InterfaceServer;
import com.fanwe.model.RequestModel;
import com.fanwe.model.ShareURL;
import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.JsonUtil;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 营销
 *
 * @author cxk
 */
public class DistributionmarketingFragment extends BaseFragment {
    @ViewInject(R.id.ll_short0)
    private LinearLayout mll_short0;

    @ViewInject(R.id.ll_short1)
    private LinearLayout mll_short1;

    @ViewInject(R.id.ll_short2)
    private LinearLayout mll_short2;

    @ViewInject(R.id.ll_short3)
    private LinearLayout mll_short3;

    @ViewInject(R.id.webView_dis)
    private WebView mWb_web;

    private HttpHandler<String> mHttpHandler;
    private String title;
    private String clickUrl;
    private String imageUrl;
    private String content;
    private String rule;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.flag_marketing);
    }

    @Override
    protected void init() {
        super.init();
        initGetShared_URL();
    }

    private void initGetShared_URL() {

        if (AppHelper.getLocalUser() == null) {
            return;
        }
        if (mHttpHandler != null) {
            mHttpHandler.cancel();
        }
        final RequestModel model = new RequestModel();
        model.putCtl("uc_red_packet");
        model.putAct("share_url");
        model.putUser();
        InterfaceServer.getInstance().requestInterface(model, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                ShareURL actModel = JsonUtil.json2Object(responseInfo.result, ShareURL.class);

                if (actModel.getStatus() == 1) {
                    clickUrl = actModel.getShare_url();
                    imageUrl = actModel.getShare_ico();
                    title = actModel.getShare_title();
                    content = actModel.getShare_info();
                    rule = actModel.getShare_rule();
                    WebSettings webSettings = mWb_web.getSettings();
                    webSettings.setSavePassword(false);
                    webSettings.setSaveFormData(true);
                    webSettings.setJavaScriptEnabled(true);
                    webSettings.setSupportZoom(false);
                    mWb_web.setBackgroundColor(Color.parseColor("#f4f4f4"));
                    mWb_web.loadDataWithBaseURL(null, rule, "text/html", "utf-8", null);
                }
            }

        });

    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }
}
