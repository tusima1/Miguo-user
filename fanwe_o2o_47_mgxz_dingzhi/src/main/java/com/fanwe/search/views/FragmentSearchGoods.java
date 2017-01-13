package com.fanwe.search.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fanwe.o2o.miguo.R;
import com.miguo.groupon.listener.IDataInterface;
import com.miguo.groupon.views.FragmentFeaturedGroupon;
import com.miguo.groupon.views.FragmentGoodsList;

/**
 * 搜索商品
 * Created by qiang.chen on 2017/1/6.
 */

public class FragmentSearchGoods extends Fragment implements IDataInterface {
    private LinearLayout layoutEmpty;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_search_goods, container, false);
        layoutEmpty = (LinearLayout) view.findViewById(R.id.layout_empty_frag_search);
        initFragment();
        setListener();
        return view;
    }

    public void search(String keyword) {
        if (fragmentGoods != null) {
            fragmentGoods.keyword = keyword;
            fragmentGoods.refresh();
        }
    }

    private void setListener() {
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentGoods != null) {
                    fragmentGoods.refresh();
                }
            }
        });
    }

    private FragmentGoodsList fragmentGoods;
    private FragmentFeaturedGroupon fragmentFeatured;
    private android.support.v4.app.FragmentManager fm;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentGoods == null) {
            fragmentGoods = new FragmentGoodsList();
            fragmentGoods.setIDataInterface(this);
            fragmentGoods.refresh();
            ft.add(R.id.content_frag_search, fragmentGoods);
        } else {
            ft.show(fragmentGoods);
        }
        ft.commit();
    }

    @Override
    public void verifyData(boolean isValid) {
        if (isValid) {
            layoutEmpty.setVisibility(View.GONE);
            addGoodsFragment();
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            addFeaturedFragment();
        }
    }

    /**
     * 商品列表
     */
    private void addGoodsFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentFeatured != null && !fragmentFeatured.isHidden()) {
            ft.hide(fragmentFeatured);
        }
        if (fragmentGoods.isHidden()) {
            ft.show(fragmentGoods);
        }
        ft.commit();
    }

    /**
     * 为你推荐
     */
    private void addFeaturedFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentGoods != null && !fragmentGoods.isHidden()) {
            ft.hide(fragmentGoods);
        }
        fragmentFeatured = new FragmentFeaturedGroupon();
        fragmentFeatured.refresh();
        ft.add(R.id.content_frag_search, fragmentFeatured);
        ft.commit();
    }
}
