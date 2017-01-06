package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiLoginQuickByMobileFragmentCategory;

/**
 * Created by zlh on 2016/12/1.
 * 快速登录
 */
public class HiLoginQuickByMobileFragment extends HiBaseFragment {

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_phone_login, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiLoginQuickByMobileFragmentCategory(cacheView, this);
    }
}
