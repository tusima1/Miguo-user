package com.fanwe.user.adapters;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.fanwe.user.view.customviews.WelcomeView;

import java.util.List;

/**
 * Created by didik on 2016/9/29.
 */

public class WelcomePagerAdapter extends PagerAdapter {

    private List<WelcomeView> mWelList;

    public void setData(List<WelcomeView> mWelList){
        this.mWelList=mWelList;
    }

    @Override
    public int getCount() {
        return mWelList==null ? 0:mWelList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mWelList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        WelcomeView view = mWelList.get(position);
        container.addView(view);
        return view;
    }
}
