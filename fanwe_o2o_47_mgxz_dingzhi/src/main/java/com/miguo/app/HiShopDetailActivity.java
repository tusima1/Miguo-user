package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiShopDetailCategory;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/19.
 */
public class HiShopDetailActivity extends HiBaseActivity{

    public static final String EXTRA_MERCHANT_ID = "extra_merchant_id";
    public static final String EXTRA_SHOP_ID = "extra_shop_id";

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hishaop_detail);
    }



    @Override
    protected Category initCategory() {
        return new HiShopDetailCategory(this);
    }
}
