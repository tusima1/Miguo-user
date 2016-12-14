package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiWithdrawalConditionsCategory;

/**
 * Created by zlh on 2016/12/13.
 * 提现条件
 */
public class HiWithdrawalConditionsActivity extends HiBaseActivity {

    @Override
    protected Category initCategory() {
        return new HiWithdrawalConditionsCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_withdrawal_conditions);
    }
}
