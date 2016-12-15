package com.miguo.app;

import android.content.Intent;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiWithdrawalConditionsCategory;
import com.miguo.definition.RequestCode;
import com.miguo.definition.ResultCode;
import com.miguo.utils.BaseUtils;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == ResultCode.RESUTN_OK){
            switch (requestCode){
                case RequestCode.UPDATE_USER_GO_WITHDRAW_CONDITION:
                    BaseUtils.finishActivity(this);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
