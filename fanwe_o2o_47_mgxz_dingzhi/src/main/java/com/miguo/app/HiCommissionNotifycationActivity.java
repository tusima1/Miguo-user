package com.miguo.app;

import android.content.Intent;

import com.fanwe.InitAdvsMultiActivity;
import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiCommissionNotifycationCategory;
import com.miguo.definition.ClassPath;
import com.miguo.definition.CommissionFromType;
import com.miguo.definition.IntentKey;
import com.miguo.factory.ClassNameFactory;
import com.miguo.utils.AppStateUtil;
import com.miguo.utils.BaseUtils;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/16.
 * 佣金代言消息列表
 */
public class HiCommissionNotifycationActivity extends HiBaseActivity {

    String from;

    @Override
    protected Category initCategory() {
        initAppApi();
        return new HiCommissionNotifycationCategory(this);
    }

    @Override
    protected void setContentView() {
        setFrom(getIntent().getStringExtra(IntentKey.COMMISSION_NOTIFYCATION_FROM));
        setContentView(R.layout.activity_hicommission_notifycation);
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public void finishActivity() {
        if(null != from && from.equals(CommissionFromType.NOTIFYCATION)){
            Intent intent = new Intent(this, InitAdvsMultiActivity.class);
            BaseUtils.jumpToNewActivityWithFinish(this, intent);
            return;
        }
        super.finishActivity();
    }
}
