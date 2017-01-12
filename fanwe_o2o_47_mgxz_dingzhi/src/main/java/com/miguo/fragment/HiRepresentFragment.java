package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiRepresentFragmentCategory;

/**
 * Created by zlh on 2017/1/5.
 */

public class HiRepresentFragment extends HiBaseFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hihome_fragment_hirepresent, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiRepresentFragmentCategory(cacheView, this);
    }
}
