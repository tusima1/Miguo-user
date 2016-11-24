package com.miguo.listener.fragment;

import android.view.View;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.FragmentCategory;
import com.miguo.category.fragment.HiFunnyFragmentCategory;

/**
 * Created by Administrator on 2016/11/22.
 */

public class HiFunnyFragmentListener extends FragmentListener {

    public HiFunnyFragmentListener(FragmentCategory category) {
        super(category);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.live:
            case R.id.live2:
                clickLive();
                break;
            case R.id.guide:
            case R.id.guide2:
                clickGuide();
                break;
        }
    }

    private void clickLive(){
        getCategory().clickLive();
    }

    private void clickGuide(){
        getCategory().clickGuide();
    }

    @Override
    public HiFunnyFragmentCategory getCategory(){
        return (HiFunnyFragmentCategory)category;
    }

}
