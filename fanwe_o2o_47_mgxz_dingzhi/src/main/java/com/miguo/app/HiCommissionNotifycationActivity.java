package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiCommissionNotifycationCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/16.
 * 佣金代言消息列表
 */
public class HiCommissionNotifycationActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiCommissionNotifycationCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hicommission_notifycation);
    }
}
