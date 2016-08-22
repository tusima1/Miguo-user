package com.fanwe.adapter;

import android.app.Activity;
import android.view.View;

import com.fanwe.common.model.getHomeClassifyList.ModelHomeClassifyList;
import com.fanwe.library.adapter.SDBasePagerAdapter;
import com.fanwe.library.customview.SDGridLinearLayout;

import java.util.List;

public class HomeIndexPageAdapter extends SDBasePagerAdapter<List<ModelHomeClassifyList>> {

    public HomeIndexPageAdapter(List<List<ModelHomeClassifyList>> listModel, Activity activity) {
        super(listModel, activity);
    }

    @Override
    public View getView(View container, int position) {
        final SDGridLinearLayout ll = new SDGridLinearLayout(mActivity);
        ll.setmColNumber(5);
        HomeIndexAdapter adapter = new HomeIndexAdapter(getItemModel(position), mActivity);
        ll.setAdapter(adapter);
        return ll;
    }

}
