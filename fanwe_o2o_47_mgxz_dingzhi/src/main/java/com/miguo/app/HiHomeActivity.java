package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiHomeCategory;

/**
 * Created by  zlh/Barry/狗蛋哥 on 2016/10/13.
 */
public class HiHomeActivity extends HiBaseActivity{

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hihome_activity);
    }

    @Override
    protected Category initCategory() {
        return new HiHomeCategory(this);
    }



}
