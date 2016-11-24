package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiWebPageCategory;

/**
 * Created by Administrator on 2016/11/21.
 */

public class HiWebPageListener extends Listener {

    public HiWebPageListener(Category category) {
        super(category);
    }

    @Override
    protected void onClickThis(View v) {
        switch (v.getId()){
            case R.id.refresh:
                clickRefresh();
                break;
        }
    }

    private void clickRefresh(){
        getCategory().refresh();
    }

    @Override
    public HiWebPageCategory getCategory() {
        return (HiWebPageCategory) super.getCategory();
    }
}
