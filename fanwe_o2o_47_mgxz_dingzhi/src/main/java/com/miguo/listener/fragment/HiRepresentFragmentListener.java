package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiRepresentFragmentCategory;

/**
 * Created by Administrator on 2017/1/12.
 */

public class HiRepresentFragmentListener extends FragmentListener {

    public HiRepresentFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.menu:
                clickMenu();
                break;
        }
    }

    private void clickMenu(){
        getCategory().clickMenu();
    }

    @Override
    public HiRepresentFragmentCategory getCategory() {
        return (HiRepresentFragmentCategory)super.getCategory();
    }
}
