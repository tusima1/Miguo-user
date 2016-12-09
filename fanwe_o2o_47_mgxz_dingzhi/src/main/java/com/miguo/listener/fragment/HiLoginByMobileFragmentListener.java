package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiLoginByMobileFragmentCategory;

/**
 * Created by Administrator on 2016/12/1.
 */

public class HiLoginByMobileFragmentListener extends FragmentListener{

    public HiLoginByMobileFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.act_login_tv_login:
                clickLogin();
                break;
        }
        super.onClick(v);
    }

    private void clickLogin(){
        getCategory().clickLogin();
    }

    @Override
    public HiLoginByMobileFragmentCategory getCategory() {
        return (HiLoginByMobileFragmentCategory) super.getCategory();
    }
}
