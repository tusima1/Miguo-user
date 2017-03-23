package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiSystemMessageDetailCategory;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/23.
 */

public class HiSystemMessageDetailListener extends Listener {

    public HiSystemMessageDetailListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                clickBack();
                break;
        }
    }

    private void clickBack(){
        getCategory().clickBack();
    }

    @Override
    public HiSystemMessageDetailCategory getCategory() {
        return (HiSystemMessageDetailCategory)super.getCategory();
    }
}
