package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiHomeActionWebCategory;

/**
 * Created by Barry on 2017/4/17.
 */

public class HiHomeActionWebListener extends Listener {

    public HiHomeActionWebListener(Category category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh:
                clickRefresh();
                break;
            case R.id.back:
                clickBack();
                break;
        }
    }

    private void clickBack(){
        getCategory().clickBack();
    }

    private void clickRefresh(){
        getCategory().refresh();
    }

    @Override
    public HiHomeActionWebCategory getCategory() {
        return (HiHomeActionWebCategory) super.getCategory();
    }

}
