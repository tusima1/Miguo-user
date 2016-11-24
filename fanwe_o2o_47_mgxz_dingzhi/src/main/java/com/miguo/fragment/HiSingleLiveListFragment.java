package com.miguo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.o2o.miguo.R;
import com.miguo.category.fragment.HiSingleLiveListFragmentCategory;

/**
 * Created by Administrator on 2016/11/22.
 */

public class HiSingleLiveListFragment extends HiBaseFragment {

    String tab_id;

    @Override
    protected View craetView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hisignele_live_list, container, false);
    }

    @Override
    protected void initFragmentCategory() {
        category = new HiSingleLiveListFragmentCategory(cacheView, this);
    }

    public String getTab_id() {
        return tab_id;
    }

    public void setTab_id(String tab_id) {
        this.tab_id = tab_id;
    }
}
