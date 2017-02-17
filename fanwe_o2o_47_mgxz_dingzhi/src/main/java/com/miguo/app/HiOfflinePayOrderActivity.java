package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayOrderCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/17.
 */

public class HiOfflinePayOrderActivity extends HiBaseActivity {

    String shopName;
    String amount;
    String orderSn;
    String orderId;
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
         setShopName(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_SHOP_NAME));
         setAmount(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_AMOUNT));
         setOrderSn(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_SN));
         setOrderId(getIntent().getStringExtra(IntentKey.OFFLINE_PAY_ORDER_ID));
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

}
