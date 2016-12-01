package com.miguo.category.fragment;

import android.view.View;

import com.lidroid.xutils.ViewUtils;
import com.miguo.fragment.HiBaseFragment;
import com.miguo.listener.fragment.HiLoginQuickByMobileFragmentListener;

/**
 * Created by zlh on 2016/12/1.
 */

public class HiLoginQuickByMobileFragmentCategory extends FragmentCategory {

    public HiLoginQuickByMobileFragmentCategory(View view, HiBaseFragment fragment) {
        super(view, fragment);
    }

    @Override
    protected void findFragmentViews() {
        ViewUtils.inject(this, view);
    }

    @Override
    protected void initFragmentListener() {
        listener = new HiLoginQuickByMobileFragmentListener(this);
    }

    @Override
    protected void setFragmentListener() {

    }

    @Override
    protected void init() {

    }
}
