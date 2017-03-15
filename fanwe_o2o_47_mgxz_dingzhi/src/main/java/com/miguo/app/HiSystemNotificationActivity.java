package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiSystemNotificationCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/15.
 * 系统消息列表
 */
public class HiSystemNotificationActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiSystemNotificationCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_hisystem_notifycation);
    }
}
