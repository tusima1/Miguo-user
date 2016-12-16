package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiWithdrawalConditionsCategory;

/**
 * Created by zlh on 2016/12/14.
 */

public class HiWithdrawalConditionsListener extends Listener {

    public HiWithdrawalConditionsListener(Category category) {
        super(category);
    }

    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.tv_update_introduce:
                clickUpdate();
                break;
        }
    }

    private void clickUpdate(){
        getCategory().clickUpdate();
    }

    @Override
    public HiWithdrawalConditionsCategory getCategory() {
        return (HiWithdrawalConditionsCategory) super.getCategory();
    }
}
