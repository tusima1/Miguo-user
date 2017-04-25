package com.miguo.category;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.constant.ServerUrl;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiHomeActionWebActivity;
import com.miguo.app.HiWebPageActivity;
import com.miguo.dao.ShoppingCartDao;
import com.miguo.dao.impl.ShoppingCartDaoImpl;
import com.miguo.framework.WebActionJSHandler;
import com.miguo.listener.HiHomeActionWebListener;
import com.miguo.listener.HiWebPageListener;
import com.miguo.utils.BaseUtils;
import com.miguo.utils.SharedPreferencesUtils;
import com.miguo.view.ShoppingCartView;

import org.apache.http.util.EncodingUtils;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/3.
 */
public class HiHomeActionWebCategory extends Category implements ShoppingCartView {

    @ViewInject(R.id.webView)
    WebView webView;

    @ViewInject(R.id.title)
    TextView title;

    WebActionJSHandler handler;

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    @ViewInject(R.id.loading_fail)
    LinearLayout loadingFail;

    @ViewInject(R.id.refresh)
    TextView refresh;

    ShoppingCartDao shoppingCartDao;

    ShoppingCartInfo mShoppingCartInfo;

    @ViewInject(R.id.back)
    ImageView back;


    public HiHomeActionWebCategory(HiBaseActivity activity) {
        super(activity);
    }

    @Override
    protected void initFirst() {
        shoppingCartDao = new ShoppingCartDaoImpl(this);
        handler = new WebActionJSHandler(getActivity());
    }

    @Override
    protected void findCategoryViews() {
        ViewUtils.inject(this, getActivity());
    }

    @Override
    protected void initThisListener() {
        listener = new HiHomeActionWebListener(this);
    }

    public void clickBack(){
        if (webView!=null && webView.canGoBack()){
            webView.goBack();
        }else {
            BaseUtils.finishActivity(getActivity());
        }
    }

    @Override
    protected void setThisListener() {
        refresh.setOnClickListener(listener);
    }

    @Override
    protected void init() {
        initWebView();
        initTitle();
        update();
    }

    @Override
    protected void initViews() {
        setTitlePadding(titleLayout);
    }

    public void updateTitle( final String title) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                HiHomeActionWebCategory.this.title.setText(title);
            }
        });
    }

    private void initTitle(){
        if(!TextUtils.isEmpty(getActivity().getWebTitle())){
            title.setText(getActivity().getWebTitle());
        }
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
                start();
                super.onPageStarted(view, url, favicon);
            }

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return false;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                loaddingFail();
                super.onReceivedError(view, request, error);
            }
        });
        webView.addJavascriptInterface(handler, "mgxz");
    }

    public void update(){
        String name = SharedPreferencesUtils.getInstance().getUserName();
        String pwd = SharedPreferencesUtils.getInstance().getPassword();
        String url;
        //9月23日添加  &from=app
        url = getActivity().getUrl() + "?from/app";
        String postData ="name=" + name + "&pwd=" + pwd ;
        webView.postUrl(url, EncodingUtils.getBytes(postData, "base64"));
    }

    public void start() {
        webView.setVisibility(View.VISIBLE);
        loadingFail.setVisibility(View.GONE);
    }

    public void loaddingFail() {
        webView.setVisibility(View.GONE);
        webView.clearView();
        loadingFail.setVisibility(View.VISIBLE);
    }

    public void refresh() {
        initWebView();
    }

    public void onPause(){
        if(null != webView){
            webView.onPause();
        }
    }

    public void onDestory(){
        if(null != webView){
            webView.loadUrl("about:blank");
        }
    }


    @Override
    public HiHomeActionWebActivity getActivity() {
        return (HiHomeActionWebActivity) super.getActivity();
    }

    public void addToShoppingCart(String cart_type, String goods_id, String add_goods_num, String fx_user_id, String roomId) {
        if (!TextUtils.isEmpty(App.getInstance().getToken())) {
            shoppingCartDao.addToShoppingCart(roomId, fx_user_id, App.getInstance().getCurrentUser().getUser_id(), goods_id, cart_type, add_goods_num);
            return;
        }
        addLocalShoppingCart(cart_type, goods_id, add_goods_num, fx_user_id, roomId);

    }

    private void addLocalShoppingCart(String cart_type, String goods_id, String add_goods_num, String fx_user_id, String roomId) {
        mShoppingCartInfo = new ShoppingCartInfo();
        mShoppingCartInfo.setId(goods_id);
        mShoppingCartInfo.setFx_user_id(fx_user_id);//TODO fx_id 没登陆都是空
        mShoppingCartInfo.setNumber("1");
        mShoppingCartInfo.setImg("");
        mShoppingCartInfo.setLimit_num("");
        mShoppingCartInfo.setIs_first("");
        mShoppingCartInfo.setIs_first_price("");
        mShoppingCartInfo.setOrigin_price("");
        mShoppingCartInfo.setTuan_price("");
        mShoppingCartInfo.setTitle("");
        LocalShoppingcartDao.insertSingleNum(mShoppingCartInfo);
        addToShoppingCartSuccess();
    }

    @Override
    public void addToShoppingCartSuccess() {
        Intent intent = new Intent(getActivity(), ShopCartActivity.class);
        BaseUtils.jumpToNewActivity(getActivity(), intent);
    }

    @Override
    public void addToShoppingCartError() {
        showToast("添加购物车失败！");
    }
}
