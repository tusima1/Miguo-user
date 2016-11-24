package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HomeBannerFragmentCategory;

/**
 * Created by Administrator on 2016/11/2.
 */
public class HomeBannerFragmentListener extends FragmentListener{

    public HomeBannerFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image:
                clickImage();
                break;
        }
    }

    private void clickImage(){
        getCategory().clickImage();
    }

    @Override
    public HomeBannerFragmentCategory getCategory() {
        return (HomeBannerFragmentCategory) super.getCategory();
    }
}
