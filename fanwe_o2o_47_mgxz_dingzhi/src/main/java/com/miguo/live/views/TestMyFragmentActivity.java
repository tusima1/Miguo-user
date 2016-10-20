package com.miguo.live.views;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.fanwe.DistributionMyXiaoMiActivity;
import com.fanwe.DistributionStoreWapActivity;
import com.fanwe.ShopCartActivity;
import com.fanwe.o2o.miguo.R;
import com.fanwe.user.view.MyCouponListActivity;
import com.fanwe.user.view.customviews.RedDotView;
import com.miguo.live.views.customviews.MGToast;

public class TestMyFragmentActivity extends Activity implements RedDotView
        .OnRedDotViewClickListener, View.OnClickListener {

    private RedDotView mRDV_orderNotPay;//待付款订单
    private RedDotView mRDV_orderNotUse;//待使用
    private RedDotView mRDV_orderNotComment;//待评价
    private RedDotView mRDV_orderNotRefund;//退款
    private View mShoppingCart;
    private View mShop;
    private View mWallet;
    private View mFriends;
    private View mQuan;
    private View mSuggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_mine);
        initView();


    }

    private void initView() {
        initMyOrders();
        initGridLayout();
    }

    private void initGridLayout() {
        mShoppingCart = findViewById(R.id.ll_shopping_cart);
        mShop = findViewById(R.id.ll_shop);
        mWallet = findViewById(R.id.ll_wallet);
        mFriends = findViewById(R.id.ll_friends);
        mQuan = findViewById(R.id.ll_quan);
        mSuggestion = findViewById(R.id.ll_suggestion);

        mShoppingCart.setOnClickListener(this);
        mShop.setOnClickListener(this);
        mWallet.setOnClickListener(this);
        mFriends.setOnClickListener(this);
        mQuan.setOnClickListener(this);
        mSuggestion.setOnClickListener(this);
    }

    private void initMyOrders() {

        mRDV_orderNotPay = ((RedDotView) findViewById(R.id.rdv_order_not_paid));
        mRDV_orderNotUse = ((RedDotView) findViewById(R.id.rdv_order_not_used));
        mRDV_orderNotComment = ((RedDotView) findViewById(R.id.rdv_order_not_commented));
        mRDV_orderNotRefund = ((RedDotView) findViewById(R.id.rdv_order_to_refund));

        int color = Color.parseColor("#999999");

        mRDV_orderNotPay.setAllParams("待付款", R.drawable.ic_pay, 0, color);
        mRDV_orderNotUse.setAllParams("待使用", R.drawable.ic_use, 0, color);
        mRDV_orderNotComment.setAllParams("待评价", R.drawable.ic_talk, 0, color);
        mRDV_orderNotRefund.setAllParams("退款", R.drawable.ic_refund, 0, color);

        mRDV_orderNotPay.setOnRedDotViewClickListener(this);
        mRDV_orderNotUse.setOnRedDotViewClickListener(this);
        mRDV_orderNotComment.setOnRedDotViewClickListener(this);
        mRDV_orderNotRefund.setOnRedDotViewClickListener(this);

    }


    @Override
    public void onRedDotViewClick(View v) {
        //TODO 全部订单
//        clickMyOrderView("all");
        if (v == mRDV_orderNotPay) {
            clickMyOrderView("pay_wait");
        } else if (v == mRDV_orderNotUse) {
            clickMyOrderView("use_wait");
        } else if (v == mRDV_orderNotComment) {
            clickMyOrderView("comment_wait");
        } else if (v == mRDV_orderNotRefund) {
            clickMyOrderView("refund");
        }
    }

    /*点击订单*/
    private void clickMyOrderView(String key) {
        MGToast.showToast(key);
//        Intent intent = new Intent(getActivity(), MyOrderListActivity.class);
//        intent.putExtra(MyOrderListActivity.EXTRA_ORDER_STATUS, key);
//        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == mShoppingCart) {
            /*购物车*/
            startActivity(ShopCartActivity.class);
        } else if (v == mShop) {
            /*我的小店*/
            startActivity(DistributionStoreWapActivity.class);
        } else if (v == mFriends) {
            /*分销战队*/
            startActivity(DistributionMyXiaoMiActivity.class);
        } else if (v == mQuan) {
            /*消费券*/
            startActivity(MyCouponListActivity.class);
        } else if (v == mWallet) {

        } else if (v == mSuggestion) {

        }
    }

    /**
     * goto 新Activity
     *
     * @param clazz 类
     */
    public void startActivity(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
