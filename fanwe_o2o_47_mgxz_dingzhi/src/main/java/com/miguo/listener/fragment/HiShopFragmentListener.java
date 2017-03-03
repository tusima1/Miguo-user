package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiShopFragmentCategory;
import com.miguo.fragment.HiShopFragment;

/**
 * Created by Barry/狗蛋哥/zlh on 2017/3/2.
 */

public class HiShopFragmentListener extends FragmentListener {

    public HiShopFragmentListener(FragmentCategory category) {
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
    public HiShopFragmentCategory getCategory() {
        return (HiShopFragmentCategory) super.getCategory();
    }
}
