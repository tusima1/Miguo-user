package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.constant.Constant;
import com.fanwe.o2o.miguo.R;

/**
 * Created by didik on 2016/9/13.
 */
public class MyFragment2 extends BaseFragment {

    @Override
    protected View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setmTitleType(Constant.TitleType.TITLE_NONE);
        return setContentView(R.layout.frag_my);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return null;
    }
}
