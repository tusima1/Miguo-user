package com.miguo.category;

import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanwe.ShopCartActivity;
import com.fanwe.app.App;
import com.fanwe.app.AppHelper;
import com.fanwe.model.LocalUserModel;
import com.fanwe.o2o.miguo.R;
import com.fanwe.shoppingcart.model.LocalShoppingcartDao;
import com.fanwe.shoppingcart.model.ShoppingCartInfo;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.miguo.app.HiBaseActivity;
import com.miguo.app.HiWebPageActivity;
import com.miguo.dao.ShoppingCartDao;
import com.miguo.dao.impl.ShoppingCartDaoImpl;
import com.miguo.framework.WebActionJSHandler;
import com.miguo.live.views.utils.BaseUtils;
import com.miguo.view.ShoppingCartView;

import java.util.List;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/3.
 */
public class HiWebPageCategory extends Category implements ShoppingCartView{

    @ViewInject(R.id.webView)
    WebView webView;

    @ViewInject(R.id.title)
    TextView title;

    WebActionJSHandler handler;

    @ViewInject(R.id.title_layout)
    RelativeLayout titleLayout;

    ShoppingCartDao shoppingCartDao;

    ShoppingCartInfo mShoppingCartInfo;


    public HiWebPageCategory(HiBaseActivity activity) {
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

    public void updateTitle(String title){
        this.title.setText(title);
    }

    private void initWebView(){
        String url = getActivity().getUrl();
        LocalUserModel userModel = AppHelper.getLocalUser();
        if(userModel == null){
            return;
        }
        String userid = userModel.getUser_mobile();
        String password = userModel.getUser_pwd();
        if(!TextUtils.isEmpty(App.getInstance().getToken()) && !TextUtils.isEmpty(userid) && !TextUtils.isEmpty(password)){
            url = url.contains("mgxz.com") ? url + "?" + "name=" + userid + "&pwd=" + password : url;
        }

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
        webView.loadUrl(url);
        webView.addJavascriptInterface(handler, "mgxz");
    }

    @Override
    public HiWebPageActivity getActivity() {
        return (HiWebPageActivity) super.getActivity();
    }

    public void addToShoppingCart(String cart_type,String goods_id,String add_goods_num,String fx_user_id,String roomId){
        if(!TextUtils.isEmpty(App.getInstance().getToken())){
            shoppingCartDao.addToShoppingCart(roomId, fx_user_id, App.getInstance().getmUserCurrentInfo().getUserInfoNew().getUser_id(), goods_id, cart_type, add_goods_num);
            return;
        }
        addLocalShoppingCart(cart_type, goods_id, add_goods_num, fx_user_id, roomId);

    }

    private void addLocalShoppingCart(String cart_type,String goods_id,String add_goods_num,String fx_user_id,String roomId){
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
        LocalShoppingcartDao.insertModel(mShoppingCartInfo);
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
