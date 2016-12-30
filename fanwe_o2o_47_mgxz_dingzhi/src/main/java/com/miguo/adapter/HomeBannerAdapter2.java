package com.miguo.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.miguo.definition.IntentKey;
import com.miguo.entity.AdspaceListBean;
import com.miguo.fragment.HomeBannerFragmet;

import java.util.List;

/**
 * Created by Administrator on 2016/12/30.
 */

public class HomeBannerAdapter2 extends FragmentPagerAdapter {

    private List<AdspaceListBean.Result.Body> bodys;
    private FragmentManager manager;
    private long systemTime;
    private int position;

    public HomeBannerAdapter2(FragmentManager fm) {
        super(fm);
    }

    public HomeBannerAdapter2(FragmentManager fm, List<AdspaceListBean.Result.Body> bodys) {
        super(fm);
        this.bodys = bodys;
        manager=fm;
    }

    @Override
    public Fragment getItem(int position) {
        this.position=position;
        HomeBannerFragmet fragmet = new HomeBannerFragmet();
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentKey.HOME_BANNER_IMAGE, bodys.get(position));
        fragmet.setArguments(bundle);
        return fragmet;
    }

    /**
     * 注意重载方法  要选择有ViewGroup 的
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        HomeBannerFragmet itemFragment=(HomeBannerFragmet) super.instantiateItem(container, position);
        itemFragment.setBanner(bodys.get(position));
        return itemFragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return bodys.size();
    }

    public void notifyDataSetChanged(List<AdspaceListBean.Result.Body> list){
        bodys = list;
        notifyDataSetChanged();
    }

}
