package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiLoginByMobileFragmentCategory;

/**
 * Created by zlh on 2016/12/1.
 */

public class HiLoginByMobileFragment extends HiBaseFragment {

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_login, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiLoginByMobileFragmentCategory(cacheView, this);
    }
}