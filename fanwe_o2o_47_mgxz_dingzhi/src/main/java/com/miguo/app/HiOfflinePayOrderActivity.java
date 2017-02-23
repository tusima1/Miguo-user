package com.miguo.app;

import android.content.Intent;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayOrderCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.IntentKey;
import com.miguo.definition.WechatPayStatus;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.BaseUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class HiOfflinePayOrderActivity extends HiBaseActivity {

    String shopId;
    String shopName;
    String amount;
    String orderSn;
    String orderId;
    /**
     * 用户余额
     */
    Double userAmount;
    /**
     * 需付金额
     */
    Double totalAmount;
    @Override
    protected Category initCategory() {
        getIntentData();
        return new HiOfflinePayOrderCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_offline_pay_order);
    }

    private void getIntentData(){
        if(null != getIntent()){
            setShopId(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_SHOP_ID));
            setShopName(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_SHOP_NAME));
            setAmount(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_AMOUNT));
            setOrderSn(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_SN));
            setOrderId(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_ID));
            setUserAmount(getIntent().getDoubleExtra(IntentKey.OFFLINE_PAY_USER_AMOUNT, 0));
            setTotalAmount(getIntent().getDoubleExtra(IntentKey.OFFLINE_PAY_TOTAL_AMOUNT, 0));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void doOnResume() {
        if(null != getCategory()){
            if(WechatPayStatus.isSuccess()){
                getCategory().paySuccessWechat();
                WechatPayStatus.reset();
            }
            if(WechatPayStatus.isERROR()){
                getCategory().payError("支付失败");
                WechatPayStatus.reset();
            }
        }
        getCategory().checkPaySuccessWithAlipay();
    }

    @Override
    protected void finishActivity() {
        clickBack();
    }

    public void clickBack(){
        Intent intent = new Intent(this, ClassNameFactory.getClass(ClassPath.OFFLINE_PAY));
        intent.putExtra(IntentKey.OFFLINE_SHOP_ID, getShopId());
        finish();
        BaseUtils.jumpToNewActivityWithBackway(this, intent);
    }

    @Override
    public HiOfflinePayOrderCategory getCategory() {
        return (HiOfflinePayOrderCategory)super.getCategory();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderSn() {
        return "订单号:" + orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserAmountString() {
        return "用户余额：" + userAmount + "元";
    }

    public Double getUserAmount(){
        return userAmount;
    }

    public void setUserAmount(Double userAmount) {
        this.userAmount = userAmount;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
