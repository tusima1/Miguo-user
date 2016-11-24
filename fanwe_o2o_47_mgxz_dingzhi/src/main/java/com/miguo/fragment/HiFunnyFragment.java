package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiFunnyFragmentCategory;

/**
 * Created by zlh/Barry/狗蛋哥 on 2016/11/16.
 */

public class HiFunnyFragment extends HiBaseFragment {

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hifunny, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiFunnyFragmentCategory(cacheView, this);
    }
}
