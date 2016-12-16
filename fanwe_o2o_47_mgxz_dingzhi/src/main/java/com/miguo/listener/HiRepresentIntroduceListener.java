package com.miguo.listener;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.Category;
import com.miguo.category.HiRepresentIntroduceCategory;

/**
 * Created by zlh on 2016/12/13.
 */
public class HiRepresentIntroduceListener extends Listener {

    public HiRepresentIntroduceListener(Category category) {
        super(category);
    }

    @Override
    public void onClickThis(View v) {
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
    public HiRepresentIntroduceCategory getCategory() {
        return (HiRepresentIntroduceCategory)super.getCategory();
    }
}
