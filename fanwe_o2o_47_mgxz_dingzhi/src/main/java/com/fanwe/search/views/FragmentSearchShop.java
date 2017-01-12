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
import com.fanwe.seller.views.fragment.FragmentShopList;
import com.miguo.groupon.listener.IDataInterface;
import com.miguo.groupon.views.FragmentFeaturedGroupon;

/**
 * 搜索商家
 * Created by qiang.chen on 2017/1/6.
 */

public class FragmentSearchShop extends Fragment implements IDataInterface {
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
        if (fragmentShop != null) {
            fragmentShop.keyword = keyword;
            fragmentShop.refresh();
        }
    }

    private void setListener() {
        layoutEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragmentShop != null) {
                    fragmentShop.refresh();
                }
            }
        });
    }

    private FragmentShopList fragmentShop;
    private FragmentFeaturedGroupon fragmentFeatured;
    private android.support.v4.app.FragmentManager fm;

    /**
     * Fragment初始化
     */
    private void initFragment() {
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentShop == null) {
            fragmentShop = new FragmentShopList();
            fragmentShop.setIDataInterface(this);
            fragmentShop.refresh();
            ft.add(R.id.content_frag_search, fragmentShop);
        } else {
            ft.show(fragmentShop);
        }
        ft.commit();
    }

    @Override
    public void verifyData(boolean isValid) {
        if (isValid) {
            layoutEmpty.setVisibility(View.GONE);
            addShopFragment();
        } else {
            layoutEmpty.setVisibility(View.VISIBLE);
            addFeaturedFragment();
        }
    }


    /**
     * 商家列表
     */
    private void addShopFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentFeatured != null && !fragmentFeatured.isHidden()) {
            ft.hide(fragmentFeatured);
        }
        if (fragmentShop.isHidden()) {
            ft.show(fragmentShop);
        }
        ft.commit();
    }

    /**
     * 为你推荐
     */
    private void addFeaturedFragment() {
        FragmentTransaction ft = fm.beginTransaction();
        if (fragmentShop != null && !fragmentShop.isHidden()) {
            ft.hide(fragmentShop);
        }
        fragmentFeatured = new FragmentFeaturedGroupon();
        fragmentFeatured.refresh();
        ft.add(R.id.content_frag_search, fragmentFeatured);
        ft.commit();
    }

}
