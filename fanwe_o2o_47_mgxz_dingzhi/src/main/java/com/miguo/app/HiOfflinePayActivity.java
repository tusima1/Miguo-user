package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiOfflinePayCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/2/14.
 */
public class HiOfflinePayActivity extends HiBaseActivity {

    String shopId;

    @Override
    protected Category initCategory() {
        getIntentData();
        return new HiOfflinePayCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_offline_pay);
    }

    private void getIntentData(){
        if(null != getIntent()){
            shopId = getIntent().getStringExtra(IntentKey.OFFLINE_SHOP_ID);
        }
    }

    public String getShopId() {
        return shopId;
    }

}
