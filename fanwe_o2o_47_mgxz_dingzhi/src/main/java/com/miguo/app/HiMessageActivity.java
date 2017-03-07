package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiMessageCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/6.
 */
public class HiMessageActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiMessageCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_himessage);
    }
}
