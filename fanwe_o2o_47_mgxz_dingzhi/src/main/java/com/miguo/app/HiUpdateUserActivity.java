package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.fanwe.utils.DataFormat;
import com.miguo.category.Category;
import com.miguo.category.HiUpdateUserCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by zlh on 2016/12/14.
 * 余额不足：升级代言人
 */
public class HiUpdateUserActivity extends HiBaseActivity {

    /**
     * 用户余额
     */
    double userAccount;
    /**
     * 升级费用
     */
    double updateAccount;

    @Override
    protected Category initCategory() {
        getIntentData();
        return new HiUpdateUserCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_update_user);
    }

    private void getIntentData(){
        setUserAccount(getIntent().getDoubleExtra(IntentKey.USER_ACCOUNT, 0));
        setUpdateAccount(getIntent().getDoubleExtra(IntentKey.UPDATE_ACCOUNT, -1));
    }

    public double getUpdateAccount() {
        return updateAccount;
    }

    public void setUpdateAccount(double updateAccount) {
        this.updateAccount = updateAccount;
    }

    public double getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(double userAccount) {
        this.userAccount = userAccount;
    }
}
