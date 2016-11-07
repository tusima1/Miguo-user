package com.fanwe.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;

/**
 * 有趣页
 */
public class FunnytFragment extends BaseFragment {

    private FunnyTitleFragment funnyTitleFragment;

    @Override
    protected View onCreateContentView(LayoutInflater inflater,
                                       ViewGroup container, Bundle savedInstanceState) {
        return setContentView(R.layout.frag_funny);
    }

    @Override
    protected void init() {
        super.init();
        initFragment();
    }

    private void initFragment() {
        funnyTitleFragment = new FunnyTitleFragment();
        getSDFragmentManager().replace(R.id.frag_funny_titleBar, funnyTitleFragment);
    }

    @Override
    protected String setUmengAnalyticsTag() {
        return this.getClass().getName().toString();
    }

}
