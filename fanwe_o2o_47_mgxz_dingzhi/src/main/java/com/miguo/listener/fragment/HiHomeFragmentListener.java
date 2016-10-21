package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiHomeFragmentCategory;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/10/20.
 */
public class HiHomeFragmentListener extends FragmentListener{

    public HiHomeFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.frag_home_title_bar_ll_msg:
                clickMessage();
                break;
        }
    }

    private void clickMessage(){
        getCategory().clickMessage();
    }

    @Override
    public HiHomeFragmentCategory getCategory() {
        return (HiHomeFragmentCategory) super.getCategory();
    }
}
