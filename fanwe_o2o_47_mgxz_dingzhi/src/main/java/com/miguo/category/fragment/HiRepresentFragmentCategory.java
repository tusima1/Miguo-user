package com.miguo.category.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.miguo.fragment.HiBaseFragment;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentFragmentCategory extends FragmentCategory {

    public HiRepresentFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {

    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {

    }
}
