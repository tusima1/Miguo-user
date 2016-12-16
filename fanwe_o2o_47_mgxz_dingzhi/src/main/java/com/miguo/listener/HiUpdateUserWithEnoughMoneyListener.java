package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiUpdateUserWithEnoughMoneyCategory;

/**
 * Created by zlh on 2016/12/15.
 */
public class HiUpdateUserWithEnoughMoneyListener extends Listener {

    public HiUpdateUserWithEnoughMoneyListener(Category category) {
        super(category);
    }

    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.update:
                clickUpdate();
                break;
        }
    }

    private void clickUpdate(){
        getCategory().clickUpdate();
    }

    @Override
    public HiUpdateUserWithEnoughMoneyCategory getCategory() {
        return (HiUpdateUserWithEnoughMoneyCategory) super.getCategory();
    }
}
