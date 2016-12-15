package com.miguo.app;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiUpdateUserWithEnoughMoneyCategory;
import com.miguo.definition.IntentKey;

/**
 * Created by zlh on 2016/12/15.
 */
public class HiUpdateUserWithEnoughMoneyActivity extends HiBaseActivity {

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
        return new HiUpdateUserWithEnoughMoneyCategory(this);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_update_user_with_enough_money);
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
